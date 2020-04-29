package view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.awt.Dimension;
import java.awt.Toolkit;

import com.brunomnsilva.smartgraph.graph.*;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;

public class GraphVisualizer extends Application{
	
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
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
		
		
//		GridPane grid = new GridPane();
//		grid.setAlignment(Pos.TOP_CENTER);
//
//				
//		Label address = new Label("Add Link:");
//		grid.add(address, 0, 1);
//
//		TextField userTextField = new TextField();
//		grid.add(userTextField, 1, 1);
//
//		grid.add(graphView, 1, 100);
		
		
		
		GridPane root = new GridPane();
        root.setHgap(8);
        root.setVgap(8);
        root.setPadding(new Insets(5));
        
        ColumnConstraints cons1 = new ColumnConstraints();
        cons1.setHgrow(Priority.NEVER);
        root.getColumnConstraints().add(cons1);

        ColumnConstraints cons2 = new ColumnConstraints();
        cons2.setHgrow(Priority.ALWAYS);
        
        root.getColumnConstraints().addAll(cons1, cons2);
        
        RowConstraints rcons1 = new RowConstraints();
        rcons1.setVgrow(Priority.NEVER);
        
        RowConstraints rcons2 = new RowConstraints();
        rcons2.setVgrow(Priority.ALWAYS);  
        
        root.getRowConstraints().addAll(rcons1, rcons2);
        
        Label labelAddress = new Label("Insert address:");
        TextField field = new TextField();

        Label labelLev = new Label("Depth level: ");
        Spinner<Integer> spinner = new Spinner<Integer>();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 15, 1);
        spinner.setValueFactory(valueFactory);
        
        Button playButton = new Button("Play");
        playButton.setFont(new Font(22));
        playButton.setOnAction(new EventHandler<ActionEvent>() {
        	
            @Override public void handle(ActionEvent e) {
                System.out.println("Press Play");
                //TODO
            }
        });
        
        root.add(labelAddress, 0, 0);
        root.add(field, 1, 0, 5, 1);
        root.add(labelLev, 6, 0);
        root.add(spinner, 7, 0, 1, 1);
        
        root.add(graphView, 0, 1, 6, 6);
        
        root.add(playButton, 7, 7);
        
        
        Scene scene = new Scene(root, screenSize.getWidth()*0.7, screenSize.getWidth()*0.4);
  
        primaryStage.setTitle("WikiRef");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
          @Override
          public void handle(WindowEvent t) {
              Platform.exit();
              System.exit(0);
          }
      });
        
        graphView.init();
		
		
	}

}



