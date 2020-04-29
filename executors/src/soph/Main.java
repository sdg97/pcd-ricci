package soph;

import java.util.ArrayList;


public class Main {

	public static void main(String[] args) throws InterruptedException {

		int poolSize = Runtime.getRuntime().availableProcessors() + 1 ;

		Master master = new Master(new Tuple2("Macchina", 0), poolSize);
		master.compute();

		ArrayList<Tuple2> result = master.getRes();
		System.out.println("Result: "+result);

		System.exit(0);
	}

}
