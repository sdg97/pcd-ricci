package soph;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.GraphEdgeList;

public class Master {

	private ExecutorService executor;
	private Result result;
	private int c = 0;
	private Graph<String, String> graph;

	public Master (Tuple2 tuple, int poolSize){		
		executor = Executors.newFixedThreadPool(poolSize);
		this.result = new Result(10);
		this.result.add(tuple);
		this.graph = new GraphEdgeList<String, String>();
	}

	public void compute() throws InterruptedException {
		
		while(c < 100 /*true*/) {
			try {
				executor.execute(new Tasks(result, c));
				log("MASTER PRINT");
				c++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("Num of nodes is " + c);
	}

	public ArrayList<Tuple2> getRes() throws InterruptedException{
		ArrayList<Tuple2> res = this.result.getResult();
		return res;
	}

	@SuppressWarnings("unused")
	private void log(String msg){
		System.out.println("[MASTER] "+msg);
	}
	

}



//public class QuadratureService extends Thread {
//
//	private int numTasks;
//	private ExecutorService executor;
//	
//	public QuadratureService (int numTasks, int poolSize){		
//		this.numTasks = numTasks;
//		executor = Executors.newFixedThreadPool(poolSize);
//	}
//	
//	public double compute(IFunction mf, double a, double b) throws InterruptedException { 
//
//		double x0 = a;
//		double step = (b-a)/numTasks;		
//	    ArrayList<Future<Double>> resultSet = new ArrayList<Future<Double>>();
////		for (int i = 0; i < numTasks; i++) {
////		}				
//		try {
//			Future<Double> res = executor.submit(new ComputeAreaTask(x0, x0 + step, mf));
//			resultSet.add(res);
//			log("submitted task " + x0 + " " + (x0+step));
//			x0 += step;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	    double sum = 0;
//	    int j = 0;
//	    //ArrayList<Future<Double>> copyRes = new ArrayList<Future<Double>>(resultSet);
//	    //for (Future<Double> future: resultSet) {
//	    for (int k = 0; k < resultSet.size() && k < 100; k++) {	
//	    try {
//	    		sum += resultSet.get(k).get();
//	    		for (int i = 0; i < numTasks; i++) {
//	    			try {
//	    				Future<Double> res = executor.submit(new ComputeAreaTask(x0, x0 + step, mf));
//	    				resultSet.add(res);
//	    				log("****submitted task in FOR" + x0 + " " + (x0+step));
//	    				x0 += step;
//	    			} catch (Exception e) {
//	    				e.printStackTrace();
//	    			}
//	    		}
//	    		log("Consume future " + j);
//	    		j++;
//	    	} catch (Exception ex){
//	    		ex.printStackTrace();
//	    	}
//	    }
//	    System.out.printf("The result is %s\n", sum);
//	    executor.shutdown();
//		return sum;
//	}
//	
//	
//	private void log(String msg){
//		System.out.println("[SERVICE] "+msg);
//	}
//}
