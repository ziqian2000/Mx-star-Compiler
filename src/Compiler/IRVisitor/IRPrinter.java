package Compiler.IRVisitor;

import Compiler.IR.BasicBlock;
import Compiler.IR.Function;
import Compiler.IR.IR;
import Compiler.IR.Instr.*;
import Compiler.IR.Operand.*;
import Compiler.Utils.FuckingException;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IRPrinter implements IRVisitor{

	Set<BasicBlock> BBVisit = new HashSet<>();

	int unnamedCnt = 0;

	PrintStream output;

	public void run(IR ir, PrintStream printStream){
		output = printStream;
		for(StaticStrConst str : ir.getStaticStrConstList())
			visit(str);
		for(Function function : ir.getFunctionList())
			visit(function);
	}

	// utility method

	public void print(String str){
		output.print(str);
	}

	public void println(String str){
		output.print(str + "\n");
	}

	public String opr2Str(Operand opr){
		// immediate
		if(opr instanceof Immediate) return opr.getIdentifier();

		// not immediate, must be in storage : string, value, pointer
		if(!(opr instanceof Storage)) throw new FuckingException("how can opr be not of Storage type");
		Storage o = (Storage) opr;

		// if no name, assign one
		if(o.getName() == null) o.setName((o.getIdentifier() == null ? "t" : o.getIdentifier()) + "_" + unnamedCnt++);

		// return the name
		if(o instanceof Register){
			if(((Register) o).getGlobal()) return "@" + o.getName();
			else return "%" + o.getName();
		}
		else return "@" + o.getName(); // string
	}

	public String BB2Str(BasicBlock BB){
		if(BB.getIdentifier() == null) BB.setIdentifier(String.valueOf(unnamedCnt++));
		return BB.getIdentifier();
	}

	// visit staticString, function & basic block

	public void visit(StaticStrConst str){
		println(opr2Str(str) + " = " + str.getValue());
	}

	public void visit(Function function){
		if(function.getEntryBB() == null) return; // builtin function

		print("func " + function.getIdentifier() + " ");
		if(function.getObj() != null)
			print(opr2Str(function.getObj()) + " ");
		for (Register opr : function.getParaList()){
			print(opr2Str(opr) + " ");
		}
		println("{");
		visit(function.getEntryBB());
		println("}"); println("");
	}

	public void visit(BasicBlock basicBlock){

		if(BBVisit.contains(basicBlock)) return;

		List<IRIns> irInsList = basicBlock.getInstList();
		println(BB2Str(basicBlock) + ":");
		for(IRIns irIns : irInsList){
			print("\t");
			irIns.accept(this);
		}

		BBVisit.add(basicBlock);

		if(!irInsList.isEmpty()){
			IRIns irIns = irInsList.get(irInsList.size() - 1);
			if(irIns instanceof Jump) visit(((Jump) irIns).getBB());
			else if(irIns instanceof Branch) {visit(((Branch) irIns).getThenBB()); visit(((Branch) irIns).getElseBB());}
			else if(!(irIns instanceof Return)) throw new FuckingException("what fucking instruction at the end of the basic block");
		}

	}

	// visit instruction

	public void visit(Alloc ins){
		println(opr2Str(ins.getPtr()) + " = alloc " + opr2Str(ins.getSize()));
	}

	public void visit(Binary ins){
		println(opr2Str(ins.getDst()) + " = " + String.valueOf(ins.getOp()).toLowerCase() + " " + opr2Str(ins.getLhs()) + " " + opr2Str(ins.getRhs()));
	}

	public void visit(Branch ins){
		println("br " + opr2Str(ins.getCond()) + " " + BB2Str(ins.getThenBB()) + " " + BB2Str(ins.getElseBB()));
	}

	public void visit(Call ins){
		print((ins.getDst() == null ? "" : opr2Str(ins.getDst()) + " = ") + "call " + ins.getFunction().getIdentifier());
		if(ins.getObj() != null) print(" " + opr2Str(ins.getObj()));
		for(Operand opr : ins.getParaList())
			print(" " + opr2Str(opr));
		println("");
	}

	public void visit(Jump ins){
		println("jump " + BB2Str(ins.getBB()));
	}

	public void visit(Load ins){
		println(opr2Str(ins.getDst()) + " = load " + opr2Str(ins.getPtr()));
	}

	public void visit(Move ins){
		println(opr2Str(ins.getDst()) + " = move " + opr2Str(ins.getSrc()));
	}

	public void visit(Return ins){
		if(ins.getRetValue() == null) println("ret");
		else println("ret " + opr2Str(ins.getRetValue()));
	}

	public void visit(Store ins){
		println("store " + opr2Str(ins.getSrc()) + " " + opr2Str(ins.getPtr()));
	}

	public void visit(Unary ins){
		println(opr2Str(ins.getDst()) + " = " + String.valueOf(ins.getOp()).toLowerCase() + " " + opr2Str(ins.getOpr()));
	}


}