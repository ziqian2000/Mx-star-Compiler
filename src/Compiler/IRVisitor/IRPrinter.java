package Compiler.IRVisitor;

import Compiler.IR.BasicBlock;
import Compiler.IR.Function;
import Compiler.IR.IR;
import Compiler.IR.Instr.*;
import Compiler.IR.Operand.Operand;
import Compiler.IR.Operand.VirtualRegister;
import Compiler.Utils.FuckingException;

import java.util.List;
import java.util.Set;

public class IRPrinter implements IRVisitor{

	Set<BasicBlock> BBVisit;

	int unnamedRegCnt = 0;
	int unnamedBBCnt = 0;

	String output;

	public void run(IR ir){
		output = "";
		for(Function function : ir.getFunctionList())
			visit(function);
		System.out.println(output);
	}

	// utility method

	public void print(String str){
		output += str;
	}

	public void println(String str){
		print(str + "\n");
	}

	public String opr2Str(Operand opr){
		String str = opr.getIdentifier();
		return str == null ? String.valueOf(unnamedRegCnt++) : str;
	}

	public String BB2Str(BasicBlock BB){
		String str = BB.getIdentifier();
		return str == null ? String.valueOf(unnamedBBCnt++) : str;
	}

	// visit function & basic block

	public void visit(Function function){
		print(function.getIdentifier() + " ( ");
		for (VirtualRegister opr : function.getParaList()){
			print(opr2Str(opr) + " , ");
		}
		println(" ):");
		visit(function.getEntryBB());
	}

	public void visit(BasicBlock basicBlock){
		if(BBVisit.contains(basicBlock)) return;

		List<IRIns> irInsList = basicBlock.getInstList();
		println("\t" + BB2Str(basicBlock) + ":");
		for(IRIns irIns : irInsList){
			print("\t\t");
			irIns.accept(this);
		}

		if(!basicBlock.isCompleted())
			throw new FuckingException("Basic block \"" + BB2Str(basicBlock) + "\" is not completed.");

		BBVisit.add(basicBlock);
		IRIns irIns = irInsList.get(irInsList.size() - 1);
		if(irIns instanceof Jump) visit(((Jump) irIns).getBB());
		else if(irIns instanceof Branch) {visit(((Branch) irIns).getThenBB()); visit(((Branch) irIns).getElseBB());}
		else if(!(irIns instanceof Return)) throw new FuckingException("Basic block \"" + BB2Str(basicBlock) + "\" ends with \"" + irIns.toString() + "\"");
	}

	// visit instruction

	public void visit(Alloc ins){
		println(opr2Str(ins.getPtr()) + " = alloc " + opr2Str(ins.getSize()));
	}

	public void visit(Binary ins){
		println(opr2Str(ins.getDst()) + " = " + String.valueOf(ins.getOp()).toLowerCase() + " " + opr2Str(ins.getLhs()) + " " + opr2Str(ins.getRhs()));
	}

	public void visit(Branch ins){
		println("br " + opr2Str(ins.getCond()) + " : " + BB2Str(ins.getThenBB()) + " , " + BB2Str(ins.getElseBB()));
	}

	public void visit(Call ins){
		print(opr2Str(ins.getDst()) + " = " + ins.getFunction().getIdentifier());
		if(ins.getObjPtr() != null)
			print(" [ " + opr2Str(ins.getObjPtr()) + " ] ");
		print(" ( ");
		for(Operand opr : ins.getParaList())
			print(opr2Str(opr) + " , ");
		println(" ) ");
	}

	public void visit(Jump ins){
		println("jump " + BB2Str(ins.getBB()));
	}

	public void visit(Load ins){
		println("load " + opr2Str(ins.getDst()) + " <- " + opr2Str(ins.getPtr()));
	}

	public void visit(Move ins){
		println(opr2Str(ins.getDst()) + " = " + opr2Str(ins.getSrc()));
	}

	public void visit(Return ins){
		if(ins.getRetValue() == null) println("ret void");
		else println("ret " + opr2Str(ins.getRetValue()));
	}

	public void visit(Store ins){
		println("store " + opr2Str(ins.getSrc()) + " -> " + opr2Str(ins.getPtr()));
	}

	public void visit(Unary ins){
		println(opr2Str(ins.getDst()) + " = " + String.valueOf(ins.getOp()).toLowerCase() + " " + opr2Str(ins.getOpr()));
	}


}
