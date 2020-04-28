package soph;

public class Tuple2 {
	private String name;
	private String link;
	
	public Tuple2(String name, String link) {
		this.name = name;
		this.link = link;
	}

	public String getName() {
		return name;
	}

	public String getLink() {
		return link;
	}
	
	@Override
	public String toString() {
		return "(" + name + "," + link + ")";
	}

}
