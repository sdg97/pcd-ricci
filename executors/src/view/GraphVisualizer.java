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
import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.brunomnsilva.smartgraph.graph.*;
import com.brunomnsilva.smartgraph.graphview.SmartRandomPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphProperties;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;

public class GraphVisualizer extends Application {
	
	private volatile boolean updateCheck = false;
	private final SmartGraphProperties a = new SmartGraphProperties();
	private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private static Master master;
	private Set<String> nodes = new HashSet<String>();
	private Set<Tuple2<String, String>> edges = new HashSet();
	private static Graph<String, String> g = new GraphEdgeList<String, String>();
	private static SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
	private final static SmartGraphPanel<String, String> graphView =  new SmartGraphPanel<>(g, strategy);;
	public void updateGraph(utilities.Graph graph) {
		//Graph<String, String> graph = new GraphEdgeList<String, String>();
		//RESET GRAPH
		updateCheck = true;
		System.out.println("UpdateGraph "+ graph.getNodes());
		
		for(String s : graph.getNodes()){
			if(!nodes.contains(s)) {
				nodes.add(s);
				g.insertVertex(s);				
			}
			//System.out.println("Insert " + s);
		}
		

		for(Tuple2<String, String> t : graph.getEdges()) {
			System.out.println(t + "" +!edges.stream().anyMatch(i -> i.getFirst().equals(t.getFirst()) && i.getSecond().equals(t.getSecond())));

			if(!edges.stream().anyMatch(i -> i.getFirst().equals(t.getFirst()) && i.getSecond().equals(t.getSecond()))) {
				g.insertEdge(t.getSecond(), t.getFirst(),  t.getSecond()+t.getFirst());
				edges.add(t);
			}
			//System.out.println("I want to insert edge " + t.getFirst() + " to " + t.getSecond() );
			//System.out.println("Vetex: " + g.vertices());
		}
		graphView.update();

	}
	
	public void update() {
		if(updateCheck) {
			graphView.applyCss();
			updateCheck = false;
		}

	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
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
        System.out.println(graphView.getScene());

        root.add(playButton, 7, 7);
        Scene scene = new Scene(root, screenSize.getWidth()*0.7, screenSize.getWidth()*0.4);
        System.out.println(graphView.getScene());
        graphView.automaticLayoutProperty();
        graphView.setAutomaticLayout(true);
        scene.getStylesheets().add(getClass().getResource( File.separator + "smartgraph.css").toExternalForm());

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



