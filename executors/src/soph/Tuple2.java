package soph;

public class Tuple2 {
	private String link;
	private int level;
	
	public Tuple2(String link, int level) {
		this.link = link;
		this.level = level;
	}

	public int getLevel() {
		return this.level;
	}

	public String getLink() {
		return this.link;
	}
	
	@Override
	public String toString() {
		return "(" + link + "," + level + ")";
	}

}
