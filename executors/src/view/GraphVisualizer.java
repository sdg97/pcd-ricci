package view;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.brunomnsilva.smartgraph.graph.*;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;

public class GraphVisualizer extends Application{
	
	private Graph<String, String> createGraph() {
		Graph<String, String> g = new GraphEdgeList<String, String>();

		g.insertVertex("A");
		g.insertVertex("B");
		g.insertVertex("C");
		g.insertVertex("D");
		g.insertVertex("E");
		g.insertVertex("F");
		g.insertVertex("G");

		g.insertEdge("A", "B", "1");
		g.insertEdge("A", "C", "2");
		g.insertEdge("A", "D", "3");
		g.insertEdge("A", "E", "4");
		g.insertEdge("A", "F", "5");
		g.insertEdge("A", "G", "6");

		g.insertVertex("H");
		g.insertVertex("I");
		g.insertVertex("J");
		g.insertVertex("K");
		g.insertVertex("L");
		g.insertVertex("M");
		g.insertVertex("N");

		g.insertEdge("H", "I", "7");
		g.insertEdge("H", "J", "8");
		g.insertEdge("H", "K", "9");
		g.insertEdge("H", "L", "10");
		g.insertEdge("H", "M", "11");
		g.insertEdge("H", "N", "12");

		g.insertEdge("A", "H", "0");
		return g;
	}
	
	public static void update() {
		System.out.println("CIAO");
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
		SmartGraphPanel<String, String> graphView = new SmartGraphPanel<>(createGraph(), strategy);
		
		Scene s = new Scene(graphView,200, 300);
		
		primaryStage.setTitle("WikiRef");
		primaryStage.setScene(s);
		primaryStage.show();
		
		graphView.init();
	}

}

