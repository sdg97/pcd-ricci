package utilities;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Graph {
	
	private Set<String> nodes;
	private Set<Tuple2<String, String>> edges;
	
	public Graph() {
		this.nodes = new HashSet<>();
		this.edges = new HashSet<Tuple2<String,String>>();	
	}
	
	public void addNodes(Collection c) {
		this.nodes.addAll(c);
	}
	
	public void addEdges(Collection c) {
		this.edges.addAll(c);
	}
	
	public void addEdge(Tuple2<String, String> e) {
		this.edges.add(e);
	}
	
	
	
	

}
