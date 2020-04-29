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

public class Master extends Thread {

	private ExecutorService executor;
	private int depthLevel;
	private Graph graph;

	public Master (int poolSize){		
		this.executor = Executors.newFixedThreadPool(poolSize);
		this.depthLevel = 1;
		this.graph = new Graph();
	}

	public int compute(Tuple2<String, Integer> initTuple, int dl) throws InterruptedException, ExecutionException { 
		
		if (initTuple == null)
			initTuple = new Tuple2<String, Integer>("Macchina", 0);
		
		this.depthLevel = dl;

	    Set<Future<Tuple2<String, ArrayList<Tuple2<String, Integer>>>>> resultSet = new HashSet<>();

		ArrayList<Tuple2<String, Integer>> tuples = new ArrayList<>();
		ArrayList<Tuple2<String, Integer>> tmp = new ArrayList<>();
		Tuple2<String, ArrayList<Tuple2<String, Integer>>> t;
		
		tuples.add(initTuple);
		
		while(this.depthLevel > 0) {
			log("Depth level is " + this.depthLevel);
			for(int i = 0; i < tuples.size(); i++) {
				Future<Tuple2<String, ArrayList<Tuple2<String, Integer>>>> res = executor.submit(new Tasks(tuples.get(i)));
				resultSet.add(res);
				log(i + "******");
			}
			for(Future<Tuple2<String, ArrayList<Tuple2<String, Integer>>>> f : resultSet) {
				t = f.get();
				tmp.addAll(t.getSecond());
				for(Tuple2<String, Integer> n : f.get().getSecond())
					graph.addEdge(new Tuple2<String, String>(t.getFirst(), n.getFirst()));
			}
			tuples = tmp;
			graph.addNodes(tuples);
			this.depthLevel--;
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


