package simulator;


import java.io.FileNotFoundException;
import java.util.concurrent.ExecutionException;
import view.GraphVisualizer;


public class MainSimulator {

	
	public static void main(String[] args) throws InterruptedException {


		GraphVisualizer graphView = new GraphVisualizer();
		MasterSimulator master = new MasterSimulator(graphView);
		graphView.setMaster(master);
		new Thread(() ->  {
			long time = System.currentTimeMillis();
			long firstTime = time;
			while(true) {
				if(System.currentTimeMillis() - time >= 400) {
					try {
						graphView.update();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					time = System.currentTimeMillis();
				}
				if(System.currentTimeMillis() - firstTime >= 20000 && master.getIteration() < master.iterationsSize()) {
					try {
						graphView.refresh();
					} catch (InterruptedException | ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					firstTime = System.currentTimeMillis();
				}
			}
		
		}).start();

		graphView.play(args);
				
	}

}
