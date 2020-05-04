package main;

import java.io.FileNotFoundException;
import java.util.concurrent.ExecutionException;
import model.Master;
import model.MasterImpl;
import simulator.MasterSimulator;
import view.GraphVisualizer;

public class Main {
	
	public static void main(String[] args) throws InterruptedException {

		int poolSize = Runtime.getRuntime().availableProcessors() + 1 ;
		GraphVisualizer graphView = new GraphVisualizer();
		Master master = new MasterImpl(poolSize, graphView);
		MasterSimulator masterSimulator = new MasterSimulator(graphView);
		graphView.setMasters(master, masterSimulator);
		
		new Thread(() ->  {
			long time = System.currentTimeMillis();
			long firstTime = time;
			while(true) {

				if(System.currentTimeMillis() - time >= 400) {
					try {
						graphView.update();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} 
					time = System.currentTimeMillis();
				}
				
				/*Check if master simulator is active*/
				if(masterSimulator.getIteration() > 0) {
					if(System.currentTimeMillis() - firstTime >= 20000 && masterSimulator.getIteration() < masterSimulator.iterationsSize()) {
						try {
							graphView.refreshSimulator();
						} catch (InterruptedException | ExecutionException e) {
							e.printStackTrace();
						}
						firstTime = System.currentTimeMillis();
					}
				}
			}
		}).start();

		graphView.play(args);				
	}

}
