package Compiler.IRVisitor;

import Compiler.IR.Operand.Register;

import java.util.Objects;

/**
 * Directed Edge:
 * 		can be considered as edges between basic blocks
 */

public class Edge {
	Object u, v;
	public Edge(Object u, Object v){
		this.u = u;
		this.v = v;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Edge edge = (Edge) o;
		return u == edge.u && v == edge.v;
	}

	@Override
	public int hashCode() {
		return Objects.hash(u, v);
	}
}
