package soph;

import java.util.ArrayList;


public class Main {

	public static void main(String[] args) throws InterruptedException {
		int nTasks = 10;
		int poolSize = Runtime.getRuntime().availableProcessors() + 1 ;

		Master master = new Master("CIAO", poolSize);
		@SuppressWarnings("unused")
		Viewer viewer = new Viewer(1000, 500, master);

		System.out.println("GO Viewer");
		Thread.sleep(5000);

		ArrayList<Tuple2> result = master.getRes();
		System.out.println("Result: "+result);

		//System.exit(0);
	}

}
