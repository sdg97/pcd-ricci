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
import model.Master;
import utilities.Tuple2;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.concurrent.ExecutionException;

import com.brunomnsilva.smartgraph.graph.*;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;

public class GraphVisualizer extends Application {
	
	private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private static Master master;
	
	private Graph<String, String> createGraph() {
		Graph<String, String> g = new GraphEdgeList<String, String>();

		g.insertVertex("A");
		g.insertVertex("B");
		g.insertEdge("A", "B", "1");

		return g;
	}
	
	public void updateGraph(utilities.Graph g) {
		Graph<String, String> graph = new GraphEdgeList<String, String>();

		System.out.println("UpdateGraph "+ g.getNodes());
		
		for(String s : g.getNodes()){
			graph.insertVertex(s);
			System.out.println("Insert " + s);
		}
		
		System.out.println("UpdateGraph "+ g.getEdges());

		for(Tuple2<String, String> t : g.getEdges()) {
			System.out.println("I want to insert edge " + t.getFirst() + " to " + t.getSecond() );
			System.out.println("Vetex: " + graph.vertices());
			graph.insertEdge(t.getSecond(), t.getFirst(),  t.getSecond()+t.getFirst());
		}
		
	}
	
	public void update() {
		System.out.println("CIAO");
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
		SmartGraphPanel<String, String> graphView = new SmartGraphPanel<>(createGraph(), strategy);
				
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
                String link = field.getText();
                int level = spinner.getValue();
                Tuple2<String, Integer> t = new Tuple2<>(link, 0);
                try {
                	if(master != null)
                		master.compute(t, level);
                } catch (InterruptedException e1) {
					e1.printStackTrace();
				} catch (ExecutionException e1) {
					e1.printStackTrace();
				}
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
	
	public void setMaster(Master m) {
		master = m;
		System.out.println("Master: " + master);
	}
	
	public void play(String[] args) {
		launch(args);
	}
}



