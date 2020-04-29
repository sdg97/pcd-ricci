package soph;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.GraphEdgeList;

public class Master extends Thread {

	private ExecutorService executor;
	private int depthLevel;

	public Master (int poolSize){		
		this.executor = Executors.newFixedThreadPool(poolSize);
		this.depthLevel = 5;
	}

	public int compute(Tuple2 initTuple) throws InterruptedException { 
		
		if (initTuple == null)
			initTuple = new Tuple2("Macchina", 0);
		
		ArrayList<Tuple2> resultSet = new ArrayList<>();
		ArrayList<Tuple2> tuples;
		try {
			Future<ArrayList<Tuple2>> res = executor.submit(new Tasks(initTuple));
			tuples = res.get();
			for(int i = 0; i < tuples.size(); i++)
				resultSet.add(tuples.get(i));
		} catch (Exception e) {
			e.printStackTrace();
		}

		int j = 0;
		for (int k = 0; k < resultSet.size() /*&& k < 100*/; k++) {	
			try {
					try {
						Future<ArrayList<Tuple2>> res = executor.submit(new Tasks(resultSet.get(k)));
						tuples = res.get();
						for(int i = 0; i < tuples.size(); i++)
							if(tuples.get(i).getLevel() < this.depthLevel)
								resultSet.add(tuples.get(i));
					} catch (Exception e) {
						e.printStackTrace();
					}
				log("Consume future " + j);
				j++;
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
		System.out.println("Number of elem in resultSet" + resultSet.size());
		executor.shutdown();
		return j;
	}


	private void log(String msg){
		System.out.println("[SERVICE] "+msg);
	}

}


