package Compiler.Optimization;

import Compiler.IR.BasicBlock;
import Compiler.IR.Function;
import Compiler.IR.IR;
import Compiler.IR.Instr.*;
import Compiler.IR.Operand.Immediate;
import Compiler.IR.Operand.Operand;
import Compiler.IR.Operand.StaticStrConst;
import Compiler.IR.Operand.VirtualRegister;
import Compiler.IRVisitor.Edge;
import Compiler.IRVisitor.IRVisitor;

import java.util.*;

public class SparseConditionalConstantPropagation extends Pass implements IRVisitor {

	boolean changed;
	private IR ir;

	private enum ValueState{
		UNDEFINED, CONST, OVER_DEFINED
	}

	private static class State {
		ValueState valueState;
		Integer value;

		public State(ValueState valueState, Integer value){
			this.valueState = valueState;
			this.value = value;
		}
	}

	Queue<BasicBlock> BBWorklist = new LinkedList<>();
	Queue<VirtualRegister> virRegWorklist = new LinkedList<>();
	Map<VirtualRegister, State> stateMap = new LinkedHashMap<>();
	Set<BasicBlock> executableBB = new LinkedHashSet<>();
	Set<Edge> executableEdge = new LinkedHashSet<>();

	public SparseConditionalConstantPropagation(IR ir){
		this.ir = ir;
	}

	public boolean run() {
		changed = false;
		for(var func : ir.getFunctionList()) if(!func.getIsBuiltin()) {
			func.makePreOrderBBList();
			computeDefUseChain(func);
			clean();
			init(func);
			work(func);
		}
		return changed;
	}

	private void clean(){
		BBWorklist.clear();
		virRegWorklist.clear();
		stateMap.clear();
		executableBB.clear();
		executableEdge.clear();
	}

	private void init(Function func){
		// set entry block to be executable
		BBWorklist.add(func.getEntryBB());
		executableBB.add(func.getEntryBB());
		// set parameters to be over-defined
		if(func.getObj() != null){
			stateMap.put(func.getObj(), new State(ValueState.OVER_DEFINED, null));
		}
		for(var parameters : func.getParaList()){
			stateMap.put(parameters, new State(ValueState.OVER_DEFINED, null));
		}
	}

	private void work(Function func){
		while(!BBWorklist.isEmpty() || !virRegWorklist.isEmpty()){
			if(!BBWorklist.isEmpty()) visit(BBWorklist.poll());
			if(!virRegWorklist.isEmpty()) virRegWorklist.poll().uses.forEach(useIns -> useIns.accept(this));
		}

		for(var BB : func.getPreOrderBBList()) if(executableBB.contains(BB)) {
			IRIns nextIns;
			for(var ins = BB.getHeadIns(); ins != null; ins = nextIns){
				nextIns =  ins.getNextIns();
				if(ins.getDefRegister() == null) continue;

				var defReg = ins.getDefRegister();
				var defState = getState(defReg);

				if(defState.valueState == ValueState.CONST){

					for (IRIns useIns : defReg.uses) {
						useIns.replaceUseOpr(defReg, new Immediate(defState.value));
					}
					ins.removeFromList();
					changed = true;
				}
			}
		}
	}

	public void visit(BasicBlock BB){
		for(var ins = BB.getHeadIns(); ins != null; ins = ins.getNextIns())
			ins.accept(this);
	}

	@Override
	public void visit(Alloc instr) {
		makeOverDefined(instr.getPtr());
	}

	@Override
	public void visit(Move instr) {
		State srcState = getState(instr.getSrc());
		if(srcState.valueState == ValueState.CONST){
			makeConst(instr.getDst(), srcState.value);
		}
		else if(srcState.valueState == ValueState.OVER_DEFINED){
			makeOverDefined(instr.getDst());
		}
	}

	@Override
	public void visit(Binary instr) {
		State lhsState = getState(instr.getLhs()), rhsState = getState(instr.getRhs());
		if(lhsState.valueState == ValueState.CONST && rhsState.valueState == ValueState.CONST){
			makeConst(instr.getDst(), binaryComputing(lhsState.value, rhsState.value, instr.getOp()));
		}
		else if(lhsState.valueState == ValueState.OVER_DEFINED || rhsState.valueState == ValueState.OVER_DEFINED){
			makeOverDefined(instr.getDst());
		}
	}

	@Override
	public void visit(Call instr) {
		if(instr.getDst() != null) makeOverDefined(instr.getDst());
	}

	@Override
	public void visit(Branch instr) {
		State condState = getState(instr.getCond());
		if(condState.valueState == ValueState.CONST){
			if(condState.value > 0) makeExecutable(instr.getBelongBB(), instr.getThenBB());
			else makeExecutable(instr.getBelongBB(), instr.getElseBB());
		}
		else if(condState.valueState == ValueState.OVER_DEFINED){
			makeExecutable(instr.getBelongBB(), instr.getThenBB());
			makeExecutable(instr.getBelongBB(), instr.getElseBB());
		}
	}

	@Override
	public void visit(Jump instr) {
		makeExecutable(instr.getBelongBB(), instr.getBB());
	}

	@Override
	public void visit(Load instr) {
		makeOverDefined(instr.getDst());
	}

	@Override
	public void visit(Return instr) {
		// nothing to do
	}

	@Override
	public void visit(Store instr) {
		// nothing to do
	}

	@Override
	public void visit(Unary instr) {
		State srcState = getState(instr.getOpr());
		if(srcState.valueState == ValueState.CONST)
			makeConst(instr.getDst(), unaryComputing(srcState.value, instr.getOp()));
		else if(srcState.valueState == ValueState.OVER_DEFINED)
			makeOverDefined(instr.getDst());
	}

	@Override
	public void visit(Phi instr) {
		Integer constValue = null;
		boolean overDefined = false;
		for(var entry : instr.getPath().entrySet()){
			BasicBlock BB = entry.getKey();
			Operand opr = entry.getValue();

			if(opr == null) continue;
			if(!executableEdge.contains(new Edge(instr.getBelongBB(), BB))) continue;

			State state = getState(opr);
			if(state.valueState == ValueState.CONST){
				if(constValue == null) constValue = state.value;
				else if(!constValue.equals(state.value)) {
					overDefined = true;
					break;
				}
			}
			else if(state.valueState == ValueState.OVER_DEFINED){
				overDefined = true;
				break;
			}
		}
		if(!overDefined && constValue != null) makeConst(instr.getDst(), constValue);
		else makeOverDefined(instr.getDst());
	}

	// utility method

	void makeOverDefined(Operand opr){
		assert opr instanceof VirtualRegister;
		if(getState(opr).valueState != ValueState.OVER_DEFINED){
			stateMap.put((VirtualRegister) opr, new State(ValueState.OVER_DEFINED, null));
			virRegWorklist.add((VirtualRegister) opr);
		}
	}

	void makeConst(Operand opr, Integer value){
		assert opr instanceof VirtualRegister;
		if(getState(opr).valueState != ValueState.CONST){
			stateMap.put((VirtualRegister) opr, new State(ValueState.CONST, value));
			virRegWorklist.add((VirtualRegister) opr);
		}
	}

	void makeExecutable(BasicBlock fromBB, BasicBlock toBB){
		var edge = new Edge(fromBB, toBB);
		executableEdge.add(edge);

		if(!executableBB.contains(toBB)) {
			executableBB.add(toBB);
			BBWorklist.add(toBB);
		}
		else{
			for(var phiIns = toBB.getHeadIns(); phiIns instanceof Phi; phiIns = phiIns.getNextIns()){
				phiIns.accept(this);
			}
		}
	}

	State getState(Operand opr){
		if(opr instanceof Immediate) return new State(ValueState.CONST, ((Immediate) opr).getValue());
		else if(opr instanceof VirtualRegister) {
			if(!stateMap.containsKey(opr)) stateMap.put((VirtualRegister) opr, new State(ValueState.UNDEFINED,null));
			return stateMap.get(opr);
		}
		else if(opr instanceof StaticStrConst){
			return new State(ValueState.OVER_DEFINED, null);
		}
		else assert false;
		return null;
	}

	int binaryComputing(int lhs, int rhs, Binary.Op op) {
		int ans = -2333;
		switch (op) {
			case ADD:
				ans = lhs + rhs;
				break;
			case SUB:
				ans = lhs - rhs;
				break;
			case MUL:
				ans = lhs * rhs;
				break;
			case DIV:
				if(rhs != 0) ans = lhs / rhs;
				break;
			case MOD:
				if(rhs != 0) ans = lhs % rhs;
				break;
			case SHL:
				ans = lhs << rhs;
				break;
			case SHR:
				ans = lhs >> rhs;
				break;
			case LT:
				ans = lhs < rhs ? 1 : 0;
				break;
			case LE:
				ans = lhs <= rhs ? 1 : 0;
				break;
			case GT:
				ans = lhs > rhs ? 1 : 0;
				break;
			case GE:
				ans = lhs >= rhs ? 1 : 0;
				break;
			case EQ:
				ans = lhs == rhs ? 1 : 0;
				break;
			case NEQ:
				ans = lhs != rhs ? 1 : 0;
				break;
			case AND:
				ans = lhs & rhs;
				break;
			case OR:
				ans = lhs | rhs;
				break;
			case XOR:
				ans = lhs ^ rhs;
				break;
		}
		return ans;
	}

	int unaryComputing(int src, Unary.Op op){
		int ans = 0;
		switch (op) {
			case NEG: ans = -src;
				break;
			case COM: ans = ~src;
				break;
		}
		return ans;
	}
}