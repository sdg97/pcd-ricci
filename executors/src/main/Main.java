package main;


import java.io.FileNotFoundException;
import java.util.concurrent.ExecutionException;

import model.Master;
import view.GraphVisualizer;


public class Main {

	
	public static void main(String[] args) throws InterruptedException {

		int poolSize = Runtime.getRuntime().availableProcessors() + 1 ;

		GraphVisualizer graphView = new GraphVisualizer();
		Master master = new Master(poolSize, graphView);
		graphView.setMaster(master);
		new Thread(() ->  {
			long time = System.currentTimeMillis();
			long firstTime = time;
			while(true) {

				if(System.currentTimeMillis() - time >= 400) {
					System.out.println("update view");
					try {
						graphView.update();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
//					try {
//						//graphView.refresh();
//					} catch (InterruptedException | ExecutionException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					time = System.currentTimeMillis();
					
				}
				if(System.currentTimeMillis() - firstTime >= 10000) {
					try {
						graphView.setMaster(new Master(poolSize, graphView));
						graphView.refresh();
						firstTime = System.currentTimeMillis();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();

		graphView.play(args);
				
	}

}
