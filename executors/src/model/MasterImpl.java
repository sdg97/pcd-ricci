package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import utilities.Graph;
import utilities.Tuple2;
import view.GraphVisualizer;

public class MasterImpl extends Thread implements Master{

	private ExecutorService executor;
	private int depthLevel;
	private Graph graph;
	private GraphVisualizer gv;
	private int poolSize;

	public MasterImpl (int poolSize, GraphVisualizer gv) {	
		//this.executor = Executors.newFixedThreadPool(poolSize);
		this.poolSize = poolSize;
		this.depthLevel = 0;
		this.graph = new Graph();
		this.gv = gv;
	}

	public int compute(Tuple2<String, Integer> initTuple, int dl) throws InterruptedException, ExecutionException { 
		if (initTuple == null)
			return -1;
		this.executor = Executors.newFixedThreadPool(poolSize);
		this.depthLevel = 0;
		this.graph = new Graph();
	    Set<Future<Tuple2<String, ArrayList<Tuple2<String, Integer>>>>> resultSet = new HashSet<>();
		ArrayList<Tuple2<String, Integer>> tuples = new ArrayList<>();
		ArrayList<Tuple2<String, Integer>> tmp = new ArrayList<>();
		Tuple2<String, ArrayList<Tuple2<String, Integer>>> t;
		
		tuples.add(new Tuple2<String, Integer>(initTuple.getFirst().substring(30), initTuple.getSecond()));
		graph.addNodes(tuples);
		gv.updateGraph(graph, depthLevel);
		this.depthLevel++;
		while(this.depthLevel <= dl) {
			log("Depth level is " + this.depthLevel);
			for(int i = 0; i < tuples.size(); i++) {
				Future<Tuple2<String, ArrayList<Tuple2<String, Integer>>>> res = executor.submit(new Tasks(tuples.get(i)));
				resultSet.add(res);
			}

			for(Future<Tuple2<String, ArrayList<Tuple2<String, Integer>>>> f : resultSet) {
				t = f.get();
				Graph tempGraph = new Graph();
				tempGraph.addNode(new Tuple2<String, Integer>(t.getFirst(), this.depthLevel) );
				for (Tuple2<String, Integer> tuple2 : t.getSecond()) {
					tmp.add(new Tuple2<String, Integer>(tuple2.getFirst(), tuple2.getSecond()));
				}
				
				for(Tuple2<String, Integer> n : t.getSecond()) {
					Tuple2<String, String> tempTuple = new Tuple2<String, String>(n.getFirst(), t.getFirst());
					graph.addEdge(tempTuple);
					tempGraph.addEdge(tempTuple);
				}
				tempGraph.addNodes(tuples);
				if(gv != null) gv.updateGraph(tempGraph, depthLevel);
			}
			tuples = tmp;
			graph.addNodes(tuples);
			this.depthLevel++;
			resultSet.clear();
		}


		//System.out.println("Number of elem in resultSet" + result.size());
		executor.shutdown();
		return 0;
	}


	@SuppressWarnings("unused")
	private void log(String msg){
		System.out.println("[SERVICE] "+msg);
	}

}


