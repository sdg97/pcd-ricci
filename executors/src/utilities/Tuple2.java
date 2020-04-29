package utilities;

public class Tuple2<A, B> {
	private A first;
	private B second;
	
	public Tuple2(A f, B s) {
		this.first = f;
		this.second = s;
	}

	public B getSecond() {
		return this.second;
	}

	public A getFirst() {
		return this.first;
	}
	
	@Override
	public String toString() {
		return "(" + first + "," + second + ")";
	}

}
