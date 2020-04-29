package utilities;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Graph {
	
	private Set<Tuple2<String, Integer>> nodes;
	private Set<Tuple2<String, String>> edges;
	
	public Graph() {
		this.nodes = new HashSet<>();
		this.edges = new HashSet<>();	
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addNodes(Collection c) {
		this.nodes.addAll(c);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addEdges(Collection c) {
		this.edges.addAll(c);
	}
	
	public void addEdge(Tuple2<String, String> e) {
		this.edges.add(e);
	}
	
	public Set<String> getNodes(){
		Set<String> s = new HashSet<String>();
		for(Tuple2<String, Integer> t : nodes)
			s.add(t.getFirst());
		return s;
	}
	
	public Set<Tuple2<String, String>> getEdges(){
		return this.edges;
	}

}
