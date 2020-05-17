package Compiler.Codegen;

import Compiler.Assembly.AsmBasicBlock;
import Compiler.Assembly.AsmFunction;
import Compiler.Assembly.Assembly;
import Compiler.Assembly.Instr.*;
import Compiler.Assembly.Operand.PhysicalRegister;
import Compiler.IR.BasicBlock;
import Compiler.IR.Function;
import Compiler.IR.IR;
import Compiler.IR.Instr.*;
import Compiler.IR.Operand.*;
import Compiler.IR.StackPointerOffset;
import Compiler.IRVisitor.IRAssistant;
import Compiler.IRVisitor.IRVisitor;
import Compiler.Utils.Config;
import Compiler.Utils.FuckingException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 	This pass will transform our IR into RISC-V assembly language
 * 		supposing we still have infinite virtual registers together with 32 RISC-V real physical registers.
 * 	What to do:
 * 		[x] Save callee-save registers on entry of a function.
 * 		[x] Save ra on entry of a function.
 * 		[x] Pass arguments in a0 ~ a7 as well as in stack, and save them on entry of callee
 * 		[x] Store return address in ra beforehand
 *		[x] Implement memory allocation by calling "malloc" lib function.
 */

public class InstructionSelector implements IRVisitor {

	final int MAX_IMM = (1 << 11) - 1;
	final int MIN_IMM = -(1 << 11);

	private IR ir;
	private Assembly asm = new Assembly();

	private AsmBasicBlock curBB;
	private AsmFunction curFunc;

	private Map<BasicBlock, AsmBasicBlock> BB2AsmBB = new HashMap<>();
	private Map<Function, AsmFunction> func2AsmFunc = new HashMap<>();
	private Map<String, VirtualRegister> saveMap = new HashMap<>();

	public InstructionSelector(IR ir){
		this.ir = ir;
	}

	public Assembly getAsm() {
		return asm;
	}

	public PhysicalRegister r(String name){
		return asm.getPhyReg(name);
	}

	public void run(){
		// global variables & string
		asm.globalVarList = new ArrayList<>(ir.getGlobalVarList());
		asm.stringList = new ArrayList<>(ir.getStaticStrConstList());

		// function mapping
		for(Function func : ir.getFunctionList()) {
			AsmFunction asmFunc = new AsmFunction(func);
			func2AsmFunc.put(func, asmFunc);
		}
		// basic blocks mapping
		for(Function func : ir.getFunctionList()) if(!func.getIsBuiltin()){
			func.makePreOrderBBList();
			func.getPreOrderBBList().forEach(BB -> BB2AsmBB.put(BB, new AsmBasicBlock(BB.getIdentifier())));
		}

		for(Function func : ir.getFunctionList()) if(!func.getIsBuiltin()) {
			visit(func);
		}
	}

	public void visit(Function func){
		curBB = BB2AsmBB.get(func.getEntryBB());
		AsmFunction asmFunc = func2AsmFunc.get(func);
		curFunc = asmFunc;
		asmFunc.setEntryBB(curBB);
		asm.addFunction(asmFunc);

		// save callee-save registers
		// todo: need not to save registers not used in callee
		saveMap.clear();
		for(String calleeSaveRegName : asm.getCalleeSaveRegisterName()){
			I32Value backup = new I32Value("backup_" + calleeSaveRegName);
			curBB.appendInst(new AsmMove(r(calleeSaveRegName), backup));
			saveMap.put(calleeSaveRegName, backup);
		}
		I32Value backup_ra = new I32Value("backup_ra");
		curBB.appendInst(new AsmMove(r("ra"), backup_ra));
		saveMap.put("ra", backup_ra);

		// save arguments
		var paraList = new ArrayList<Operand>();
		if(func.getObj() != null) paraList.add(func.getObj());
		paraList.addAll(func.getParaList());
		for(int i = 0; i < Integer.min(8, paraList.size()); i++)
			curBB.appendInst(new AsmMove(r("a" + i), (Register) paraList.get(i)));
		for(int i = 8; i < paraList.size(); i++)
			curBB.appendInst(new AsmLoad(new StackPointerOffset(Config.SIZE * (i - 8), true, asmFunc, r("sp")),
										(Register) paraList.get(i), Config.SIZE));

		// ending
		func.getPreOrderBBList().forEach(this::visit);
		asmFunc.setExitBB(BB2AsmBB.get(func.getExitBB()));
		asmFunc.makeBBList();
	}

	public void visit(BasicBlock BB){
		curBB = BB2AsmBB.get(BB);
		for(IRIns ins = BB.getHeadIns(); ins != null; ins = ins.getNextIns()){
			ins.accept(this);
		}
	}

	public void visit(Move instr){
		curBB.appendInst(new AsmMove(immAndStr2Reg(instr.getSrc()), (Register) instr.getDst()));
	}

	public void visit(Binary instr){
		if(instr.getLhs() instanceof Immediate && instr.getRhs() instanceof Immediate) {
//			assert false; // this should be done in SCCP, not here !
			curBB.appendInst(new AsmLI((Register) instr.getDst(), new Immediate(IRAssistant.calculation(instr.getOp(), ((Immediate) instr.getLhs()).getValue(), ((Immediate) instr.getRhs()).getValue()))));
		}
		else{
			boolean RType = instr.getOp() == Binary.Op.MUL || instr.getOp() == Binary.Op.DIV || instr.getOp() == Binary.Op.MOD || instr.getOp() == Binary.Op.SUB
					|| (instr.getLhs() instanceof Register && instr.getRhs() instanceof Register
					|| (instr.getLhs() instanceof Immediate && (instr.getOp() == Binary.Op.SHL || instr.getOp() == Binary.Op.SHR))
					|| (instr.getLhs() instanceof Immediate && !checkBound((Immediate) instr.getLhs()))
					|| (instr.getRhs() instanceof Immediate && !checkBound((Immediate) instr.getRhs())));
			// todo : SUB can be optimized in IR processing by replacing it with ADD ....
			if(RType){
				Register rs1 = immAndStr2Reg(instr.getLhs());
				Register rs2 = immAndStr2Reg(instr.getRhs());
				Register rd = (Register) instr.getDst();
				switch (instr.getOp()){
					case ADD: curBB.appendInst(new AsmRTypeIns(rs1, rs2, rd, AsmRTypeIns.Op.ADD));
						break;
					case SUB: curBB.appendInst(new AsmRTypeIns(rs1, rs2, rd, AsmRTypeIns.Op.SUB));
						break;
					case MUL: curBB.appendInst(new AsmRTypeIns(rs1, rs2, rd, AsmRTypeIns.Op.MUL));
						break;
					case DIV: curBB.appendInst(new AsmRTypeIns(rs1, rs2, rd, AsmRTypeIns.Op.DIV));
						break;
					case MOD: curBB.appendInst(new AsmRTypeIns(rs1, rs2, rd, AsmRTypeIns.Op.REM));
						break;
					case SHL: curBB.appendInst(new AsmRTypeIns(rs1, rs2, rd, AsmRTypeIns.Op.SLL));
						break;
					case SHR: curBB.appendInst(new AsmRTypeIns(rs1, rs2, rd, AsmRTypeIns.Op.SRA));
						break;
					case LT: curBB.appendInst(new AsmRTypeIns(rs1, rs2, rd, AsmRTypeIns.Op.SLT));
						break;
					case LE: I32Value tmpLE = new I32Value("tmpOfLE"); // todo: may be optimized, processed in IR? so that it can be optimized!
						curBB.appendInst(new AsmRTypeIns(rs2, rs1, tmpLE, AsmRTypeIns.Op.SLT));
						curBB.appendInst(new AsmITypeIns(tmpLE, new Immediate(1), rd, AsmITypeIns.Op.XORI));
						break;
					case GT: curBB.appendInst(new AsmRTypeIns(rs2, rs1, rd, AsmRTypeIns.Op.SLT));
						break;
					case GE: I32Value tmpGE = new I32Value("tmpOfGE"); // todo: may be optimized
						curBB.appendInst(new AsmRTypeIns(rs1, rs2, tmpGE, AsmRTypeIns.Op.SLT));
						curBB.appendInst(new AsmITypeIns(tmpGE, new Immediate(1), rd, AsmITypeIns.Op.XORI));
						break;
					case EQ: I32Value tmpEQ = new I32Value("tmpOfEQ"); // todo: may be optimized
						curBB.appendInst(new AsmRTypeIns(rs1, rs2, tmpEQ, AsmRTypeIns.Op.SUB));
						curBB.appendInst(new AsmITypeIns(tmpEQ, new Immediate(1), rd, AsmITypeIns.Op.SLTIU));
						break;
					case NEQ: I32Value tmpNEQ = new I32Value("tmpOfNEQ"); // todo: may be optimized
						curBB.appendInst(new AsmRTypeIns(rs1, rs2, tmpNEQ, AsmRTypeIns.Op.SUB));
						curBB.appendInst(new AsmRTypeIns(r("zero"), tmpNEQ, rd, AsmRTypeIns.Op.SLTU));
						break;
					case AND: curBB.appendInst(new AsmRTypeIns(rs1, rs2, rd, AsmRTypeIns.Op.AND));
						break;
					case OR: curBB.appendInst(new AsmRTypeIns(rs1, rs2, rd, AsmRTypeIns.Op.OR));
						break;
					case XOR: curBB.appendInst(new AsmRTypeIns(rs1, rs2, rd, AsmRTypeIns.Op.XOR));
						break;
					default:
						throw new FuckingException("FUCK YOU");
				}
			}
			else { // IType
				Register rd = (Register) instr.getDst();
				boolean swapped = instr.getLhs() instanceof Immediate; // if LHS and RHS has been swapped
				Register rs1 = swapped ? (Register)instr.getRhs() : (Register)instr.getLhs();
				Immediate imm = swapped ? (Immediate)instr.getLhs() : (Immediate)instr.getRhs();
				if(swapped){
					if(instr.getOp() == Binary.Op.LT) instr.setOp(Binary.Op.GT);
					else if(instr.getOp() == Binary.Op.GT) instr.setOp(Binary.Op.LT);
					else if(instr.getOp() == Binary.Op.LE) instr.setOp(Binary.Op.GE);
					else if(instr.getOp() == Binary.Op.GE) instr.setOp(Binary.Op.LE);
				}

				switch (instr.getOp()) {

					case ADD: curBB.appendInst(new AsmITypeIns(rs1, imm, rd, AsmITypeIns.Op.ADDI));
						break;

					case SUB:
					case MUL:
					case DIV:
					case MOD: throw new FuckingException("No implementation.");

					case SHL: curBB.appendInst(new AsmITypeIns(rs1, imm, rd, AsmITypeIns.Op.SLLI));
						break;
					case SHR: curBB.appendInst(new AsmITypeIns(rs1, imm, rd, AsmITypeIns.Op.SRAI));
						break;
					case LT: curBB.appendInst(new AsmITypeIns(rs1, imm, rd, AsmITypeIns.Op.SLTI));
						break;
					case LE: I32Value tmpLE = new I32Value("tmpOfLE"); // todo: may be optimized, processed in IR? so that it can be optimized!
						curBB.appendInst(new AsmRTypeIns(immAndStr2Reg(imm), rs1, tmpLE, AsmRTypeIns.Op.SLT));
						curBB.appendInst(new AsmITypeIns(tmpLE, new Immediate(1), rd, AsmITypeIns.Op.XORI));
						break;
					case GT: curBB.appendInst(new AsmRTypeIns(immAndStr2Reg(imm), rs1, rd, AsmRTypeIns.Op.SLT));
						break;
					case GE: I32Value tmpGE = new I32Value("tmpOfGE"); // todo: may be optimized
						curBB.appendInst(new AsmITypeIns(rs1, imm, tmpGE, AsmITypeIns.Op.SLTI));
						curBB.appendInst(new AsmITypeIns(tmpGE, new Immediate(1), rd, AsmITypeIns.Op.XORI));
						break;
					case EQ: I32Value tmpEQ = new I32Value("tmpOfEQ"); // todo: may be optimized
						curBB.appendInst(new AsmRTypeIns(rs1, immAndStr2Reg(imm), tmpEQ, AsmRTypeIns.Op.SUB));
						curBB.appendInst(new AsmITypeIns(tmpEQ, new Immediate(1), rd, AsmITypeIns.Op.SLTIU));
						break;
					case NEQ: I32Value tmpNEQ = new I32Value("tmpOfNEQ"); // todo: may be optimized
						curBB.appendInst(new AsmRTypeIns(rs1, immAndStr2Reg(imm), tmpNEQ, AsmRTypeIns.Op.SUB));
						curBB.appendInst(new AsmRTypeIns(r("zero"), tmpNEQ, rd, AsmRTypeIns.Op.SLTU));
						break;
					case AND: curBB.appendInst(new AsmITypeIns(rs1, imm, rd, AsmITypeIns.Op.ANDI));
						break;
					case OR: curBB.appendInst(new AsmITypeIns(rs1, imm, rd, AsmITypeIns.Op.ORI));
						break;
					case XOR: curBB.appendInst(new AsmITypeIns(rs1, imm, rd, AsmITypeIns.Op.XORI));
						break;
					default:
						throw new FuckingException("FUCK YOU");
				}
			}
		}
	}

	public void visit(Alloc instr){
		// call "malloc"
		curBB.appendInst(new AsmMove(immAndStr2Reg(instr.getSize()), r("a0")));
		curBB.appendInst(new AsmCall(asm.mallocAsmFunc));
		if(instr.getPtr() != null)
			curBB.appendInst(new AsmMove(r("a0"), (Register) instr.getPtr()));
	}

	public void visit(Call instr){
		var paraList = new ArrayList<Operand>();
		if(instr.getObj() != null) paraList.add(instr.getObj());
		paraList.addAll(instr.getParaList());

		for(int i = 0; i < Integer.min(8, paraList.size()); i++){
			Register para = immAndStr2Reg(paraList.get(i));
			curBB.appendInst(new AsmMove(para, r("a" + i)));
		}
		for(int i = 8; i < paraList.size(); i++){
			Register para = immAndStr2Reg(paraList.get(i));
			curBB.appendInst(new AsmStore(new StackPointerOffset(Config.SIZE * (i - 8), false, curFunc, r("sp")),
											para, null, Config.SIZE));
		}

		curFunc.setStackSizeFromTopMax(Config.SIZE * Integer.max(0, paraList.size() - 8));

		curBB.appendInst(new AsmCall(func2AsmFunc.get(instr.getFunction())));

		if(instr.getDst() != null)
			curBB.appendInst(new AsmMove(r("a0"), (Register) instr.getDst()));
	}
	
	public void visit(Branch instr){
		// todo: merge cmp into branch
		curBB.appendInst(new AsmBranch(immAndStr2Reg(instr.getCond()), r("zero"), AsmBranch.Op.BNE, BB2AsmBB.get(instr.getThenBB())));
		curBB.appendInst(new AsmJump(BB2AsmBB.get(instr.getElseBB()))); // todo : this jump can be eliminated
	}
	
	public void visit(Jump instr){
		curBB.appendInst(new AsmJump(BB2AsmBB.get(instr.getBB())));
	}
	
	public void visit(Load instr){
		assert !(instr.getPtr() instanceof StaticStrConst);
		curBB.appendInst(new AsmLoad((Register) instr.getPtr(), (Register) instr.getDst(), Config.SIZE));
	}
	
	public void visit(Return instr){
		if(instr.getRetValue() != null){
			curBB.appendInst(new AsmMove(immAndStr2Reg(instr.getRetValue()), r("a0")));
		}
		for(var entry : saveMap.entrySet()){
			curBB.appendInst(new AsmMove(entry.getValue(), r(entry.getKey()))); // including ra
		}
		curBB.appendInst(new AsmReturn());
	}
	
	public void visit(Store instr){
		assert !(instr.getPtr() instanceof StaticStrConst);

		if(((VirtualRegister) instr.getPtr()).isGlobal())
			curBB.appendInst(new AsmStore((Register)instr.getPtr(), immAndStr2Reg(instr.getSrc()), new I32Value("store_tmp"), Config.SIZE));
		else
			curBB.appendInst(new AsmStore((Register)instr.getPtr(), immAndStr2Reg(instr.getSrc()), null, Config.SIZE));
	}
	
	public void visit(Unary instr){
		Register rs1 = immAndStr2Reg(instr.getOpr());
		Register rd = (Register)instr.getDst();
		switch (instr.getOp()) {
			case NEG: curBB.appendInst(new AsmRTypeIns(r("zero"), rs1, rd, AsmRTypeIns.Op.SUB));
				break;
			case COM: curBB.appendInst(new AsmITypeIns(rs1, new Immediate(-1), rd, AsmITypeIns.Op.XORI));
				break;
		}
	}
	
	public void visit(Phi phi) {
		throw new FuckingException("Do you forget to destruct SSA form, idiot?");
	}

	// utility method

	public Operand boundImm(Operand opr){
		if(!(opr instanceof Immediate)) return opr;
		if(checkBound((Immediate) opr)) return opr;

		I32Value tmp = new I32Value("imm_tmp");
		curBB.appendInst(new AsmLI(tmp, (Immediate) opr));
		return tmp;
	}

	public boolean checkBound(Immediate imm){
		return MIN_IMM <= imm.getValue() && imm.getValue() <= MAX_IMM;
	}

	public Register immAndStr2Reg(Operand opr){
		if(opr instanceof Register) return (Register)opr;
		else if(opr instanceof Immediate){
			I32Value tmp = new I32Value("imm" + ((Immediate) opr).getValue());
			curBB.appendInst(new AsmLI(tmp, (Immediate) opr));
			return tmp;
		}
		else if(opr instanceof StaticStrConst){
			I32Value tmp = new I32Value("str");
			curBB.appendInst(new AsmLA(tmp, (StaticStrConst) opr));
			return tmp;

		}
		throw new FuckingException("Unexpected type in imm2Reg.");
	}

}
