package algorithm;

import loader.Precinct;

public class ConflictedEdge {
	private Precinct a, b;

	public ConflictedEdge(Precinct a, Precinct b) {
		this.a = a;
		this.b = b;
	}

	public Precinct getA() {
		return a;
	}

	public void setA(Precinct a) {
		this.a = a;
	}

	public Precinct getB() {
		return b;
	}

	public void setB(Precinct b) {
		this.b = b;
	}

	@Override
	public String toString() {
		return "ConflictedEdge [a=" + a + ", b=" + b + "]";
	}
}
