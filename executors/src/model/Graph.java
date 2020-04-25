package model;

import javafx.application.Application;
import view.GraphVisualizer;

public class Graph {
	
	
	public static void main(String args[]) {
		
		
		
		new Thread(() ->  {
			long time = System.currentTimeMillis();
			
			while(true) {
				if(System.currentTimeMillis() - time == 5000) {
					GraphVisualizer.update();
					time = System.currentTimeMillis();
				}
			}
		}).start();
		
		
		Application.launch(GraphVisualizer.class, args);
	}

}
