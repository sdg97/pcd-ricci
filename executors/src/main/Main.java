package main;


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
			
			while(true) {

				if(System.currentTimeMillis() - time == 200) {
					//System.out.println("update view");
					graphView.update(); 
					time = System.currentTimeMillis();
				}
			}
		}).start();

		graphView.play(args);
				
	}

}