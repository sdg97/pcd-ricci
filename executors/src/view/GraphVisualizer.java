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
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.brunomnsilva.smartgraph.graph.*;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphProperties;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import com.sun.javafx.geom.Edge;
import com.sun.org.apache.xerces.internal.dom.DeepNodeListImpl;

public class GraphVisualizer extends Application {

	private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private static Master master;
	//private static SmartGraphProperties properties = new SmartGraphProperties();
	private List<String> edges = new ArrayList<String>();
	private List<Tuple2<String, String>> edgesToAdd = new ArrayList<Tuple2<String, String>>();
	private static Graph<String, String> g = new GraphEdgeList<String, String>();
	private static SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
	private final static SmartGraphPanel<String, String> graphView =  new SmartGraphPanel<>(g, strategy);
	
	private int nodeCount = 0;
	//private boolean resized = false;
	private static Label labelNum;
	private static int depthLevel = 0;
	private static String link = null;

	public void updateGraph(utilities.Graph graph) {
		for(Tuple2<String, String> t : graph.getEdges()) {
			if(!edges.contains(t.getSecond() + t.getFirst())) {
				edges.add(t.getSecond() + t.getFirst());
				edgesToAdd.add(t);
			}
		}
	}

	public void update() throws FileNotFoundException {
		/*if(nodeCount > 3 && resized == false) {
			resized = true;
			System.out.println(graphView.getProperties());
			//System.out.println(File.separator + "smartgraph2.properties");
			System.out.println(properties.getVertexRadius());
			File newFile = new File(System.getProperty("user.dir") + File.separator + "smargraph2.properties");
			properties = new SmartGraphProperties(new FileInputStream(newFile));
			System.out.println(properties.getVertexRadius());
		}*/

		if(edgesToAdd.size() != 0) {
			Tuple2<String, String> edge = edgesToAdd.get(0);
			Set<String> vertices = g.vertices().stream().map(v -> v.element()).collect(Collectors.toSet());
			if(!vertices.contains(edge.getFirst())) {
				System.out.println("ADD VERTEX "  + edge.getFirst());
				g.insertVertex(edge.getFirst());
				nodeCount++;
			}
			if(!vertices.contains(edge.getSecond())) {
				System.out.println("ADD VERTEX "  + edge.getSecond());
				g.insertVertex(edge.getSecond());
				nodeCount++;
			}
			System.out.println("ADD EDGE ("  + edge.getFirst() + ", " + edge.getSecond() + ")");
			g.insertEdge(edge.getSecond(), edge.getFirst(),  edge.getSecond()+edge.getFirst());
			edgesToAdd.remove(0);
			graphView.update();
			System.out.println(nodeCount);
			Platform.runLater(() -> labelNum.setText("" + nodeCount)) ;

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
				link = field.getText();
				depthLevel = spinner.getValue();
				try {
					master.compute(new Tuple2<String, Integer>(link, 1), depthLevel);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				} catch (ExecutionException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		Label labelNodes = new Label("The number of node is: ");
		labelNum = new Label("0");
		
		root.add(labelAddress, 0, 0);
		root.add(field, 1, 0, 5, 1);
		root.add(labelLev, 6, 0);
		root.add(spinner, 7, 0, 1, 1);
		root.add(graphView, 0, 1, 6, 6);
		System.out.println(graphView.getScene());

		root.add(labelNodes, 4, 7);
		root.add(labelNum, 6, 7);
		root.add(playButton, 7, 7);
		Scene scene = new Scene(root, screenSize.getWidth()*0.7, screenSize.getWidth()*0.4);
		System.out.println(graphView.getScene());
		graphView.automaticLayoutProperty();
		graphView.setAutomaticLayout(true);
		scene.getStylesheets().add(getClass().getResource(File.separator + "smartgraph.css").toExternalForm());

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
	
	public void refresh() throws InterruptedException, ExecutionException, FileNotFoundException {
		if(link == null || depthLevel == 0) {
			System.out.println("Not yet initialized");
			return;
		}
		Tuple2<String, Integer> t = new Tuple2<>(link, 1);
		if(master != null) {
			Vertex<String> v1 = g.vertices().stream().collect(Collectors.toList()).get(0);
			Vertex<String> v2 = g.vertices().stream().collect(Collectors.toList()).get(1);			System.out.println("" + v1.element() + v2.element());
			String edgeToRemove = (v2.element() + v1.element());
			System.out.println(edges.contains(v2.element() + v1.element()));
			edges.remove(edgeToRemove);
			graphView.update();
			master.compute(t, depthLevel);
	
		}
	}


	public void setMaster(Master m) {
		master = m;
		System.out.println("Master: " + master);
	}

	public void play(String[] args) {
		launch(args);
	}
}



