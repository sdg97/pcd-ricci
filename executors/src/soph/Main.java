package soph;

import java.util.ArrayList;


public class Main {

	public static void main(String[] args) throws InterruptedException {

		int poolSize = Runtime.getRuntime().availableProcessors() + 1 ;

		Master master = new Master(poolSize);
		int numNodes = master.compute(null);

		System.exit(0);
	}

}
