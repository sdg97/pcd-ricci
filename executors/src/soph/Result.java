package soph;

import java.util.ArrayList;

public class Result {
	
	private ArrayList<Tuple2> list;
	private int nTotalResultsToWait;
	private int nResultsArrived;
	
	public Result(int nResults){
		nTotalResultsToWait = nResults;
		nResultsArrived = 0;
		this.list = new ArrayList<Tuple2>();
	}
	
	public synchronized void add(Tuple2 elem){
		this.list.add(elem);
		//Da pensare!!
		nResultsArrived++;
		if (nResultsArrived >= nTotalResultsToWait){
			notifyAll();
		}
	}
	
	public synchronized Tuple2 getFirst() {
		return this.list.size() > 0 ? this.list.remove(0) : null;
	}

	public synchronized ArrayList<Tuple2> getResult() throws InterruptedException {
		while (nResultsArrived < nTotalResultsToWait){
			wait();
		}
		return this.list;
	}

}
