package soph;

import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 * Dato un link prende i link correlati
 */
public class Tasks implements Callable<ArrayList<Tuple2>> {

	private Tuple2 myTuple;
	private ArrayList<Tuple2> nodes;
	
	public Tasks(Tuple2 t) {
		this.myTuple = t;
		this.nodes = new ArrayList<>();
	}

	@Override
	public ArrayList<Tuple2> call() throws Exception {
		for(int i = 0; i < 3; i++)
			this.nodes.add(new Tuple2("ciao", myTuple.getLevel()+1));
		log("#nodes = " + this.nodes.size());
		return this.nodes;
	}
	
	private void log(String msg) {
		System.out.println("[TASK]" + msg);
	}


}
