package model;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import utilities.Tuple2;

/**
 * Dato un link prende i link correlati
 */
public class Tasks implements Callable<Tuple2<String, ArrayList<Tuple2<String, Integer>>>> {

	private Tuple2<String, Integer> myTuple;
	private ArrayList<Tuple2<String, Integer>> nodes;
	
	public Tasks(Tuple2<String, Integer> t) {
		this.myTuple = t;
		this.nodes = new ArrayList<>();
	}

	@Override
	public Tuple2<String, ArrayList<Tuple2<String, Integer>>> call() throws Exception {
		for(int i = 0; i < 3; i++)
			this.nodes.add(new Tuple2<String, Integer>("ciao", myTuple.getSecond()+1));
		log("#nodes = " + this.nodes.size());
		return new Tuple2<String, ArrayList<Tuple2<String, Integer>>>(this.myTuple.getFirst(), this.nodes);
	}
	
	private void log(String msg) {
		System.out.println("[TASK]" + msg);
	}


}
