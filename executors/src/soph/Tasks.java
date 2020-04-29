package soph;

import java.util.ArrayList;
import java.util.Random;

/**
 * Dato un link prende i link correlati
 */
public class Tasks implements Runnable {

	private Result result;
	private int id;
	private ArrayList<String> nodes;
	
	public Tasks(Result result, int id) {
		this.result = result;
		this.id = id;
		this.nodes = new ArrayList<>();
	}
	
	@Override
	public void run() {
		//TODO ricerca 
		this.nodes.add("Ciao");
		
		log("I'm "+ id + " execute");
		//Tuple2 myTuple = result.getTuple(this.id);
//	    try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		if(myTuple.getLevel() < 10)
		//	result.add(new Tuple2("fluido", myTuple.getLevel()+1));
		int rand = new Random().nextInt(10);
		Tuple2 t = result.getFirst();
		if(t != null && t.getLevel() < 2)
			for(int i = 0; i < rand; i++ )
				result.add(new Tuple2("ciao", t.getLevel()+1));

	}
	
	private void log(String msg) {
		System.out.println(msg);
	}

}
