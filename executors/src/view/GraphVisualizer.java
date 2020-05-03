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
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import com.brunomnsilva.smartgraph.graph.*;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;

public class GraphVisualizer extends Application {

	private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private static Master master;
	//private static SmartGraphProperties properties = new SmartGraphProperties();
	private Set<Tuple2<String, String>> edgesToAdd = new HashSet<Tuple2<String, String>>();
	private Set<Tuple2<String, String>> edgesToRemove = new HashSet<Tuple2<String, String>>();
	private Set<Tuple2<String, Set<String>>> nodes = new HashSet<Tuple2<String, Set<String>>>();	
	private static Graph<String, String> g = new GraphEdgeList<String, String>();
	private static SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
	private final static SmartGraphPanel<String, String> graphView =  new SmartGraphPanel<>(g, strategy);
	
	private int nodeCount = 0;
	//private boolean resized = false;
	private static Label labelNum;
	private static int depthLevel = 0;
	private static String link = null;

	public void updateGraph(utilities.Graph graph) {
		System.out.println(graph.getEdges());
		String fatherName = graph.getEdges().stream().findFirst().get().getSecond();
		if(!nodes.stream().map(n -> n.getFirst()).collect(Collectors.toSet()).contains(fatherName)) {
			Tuple2<String, Set<String>> fatherNode = new Tuple2<String, Set<String>>(fatherName, new HashSet<String>());
			nodes.add(fatherNode); 
			for (Tuple2<String, String> childNode : graph.getEdges()) {
				fatherNode.getSecond().add(childNode.getFirst());
				edgesToAdd.add(childNode);
			}
		} else {
			Set<String> tempChild = new HashSet<String>();
			Tuple2<String, Set<String>> fatherNode = getNodeFromString(fatherName).get();
			fatherNode.getSecond().forEach(c -> {
				if(graph.getEdges().stream().map(e -> e.getFirst()).collect(Collectors.toSet()).contains(c)) {
					tempChild.add(c);
				} else {
					edgesToRemove.add(new Tuple2<String, String>(c, fatherName));
				}
			});
			fatherNode.getSecond().clear();
			fatherNode.getSecond().addAll(tempChild);
			for (Tuple2<String, String> childNode : graph.getEdges()) {
				if(!fatherNode.getSecond().contains(childNode.getFirst())) {
					System.out.println(fatherName + " ADD NEW CHILD " + childNode.getFirst());
					fatherNode.getSecond().add(childNode.getFirst());
					edgesToAdd.add(childNode);					
				}
			}
			
			System.out.println(edgesToRemove);
		}
	}
	
	private Optional<Tuple2<String, Set<String>>> getNodeFromString(String node) {
		return nodes.stream().filter(n -> n.getFirst().equals(node)).findFirst();
	}
	
	private Optional<com.brunomnsilva.smartgraph.graph.Edge<String, String>> getGraphEdge(String father, String child) {
		return g.edges().stream().filter(e -> e.element().equals(father + child)).findFirst();
	}
	
	private Optional<Vertex<String>> getGraphVertex(String node) {
		return g.vertices().stream().filter(e -> e.element().equals(node)).findFirst();
	}
	
	private boolean haveAnotherFather(String currentFather, String child) {
		for (Tuple2<String, Set<String>> fatherNode : nodes) {
			if(!fatherNode.getFirst().equals(currentFather)) {
				if(fatherNode.getSecond().contains(child)) {
					System.out.println(child + " HA COME ALTRO PADRE " + fatherNode.getFirst());
					return true;					
				}
				System.out.println(child + " NON HA ALTRI PADRI");
			} 
		}
		return false;
	}
	

	
	private void removeEdge(String father, String child) {
		Optional<Tuple2<String, Set<String>>> edgeToRemove = getNodeFromString(child);
		if(edgeToRemove.isPresent()) {
			System.out.println( "CHILDREN TO REMOVE " + edgeToRemove.get().getSecond());
			if(haveAnotherFather(father, child) ) {
				if(getGraphEdge(father, child).isPresent()) {
					System.out.println("REMOVE EDGE FROM " + father + " TO " + child);
					g.removeEdge(getGraphEdge(father, child).get());										
				}
			}else {
				for (String subChild : edgeToRemove.get().getSecond()) {
					removeEdge(child, subChild);
				}
				Tuple2<String, Set<String>> fatherNode = getNodeFromString(father).get();
				System.out.println("REMOVE VERTEX " + child);
				fatherNode.getSecond().remove(child);
				Set<Tuple2<String, Set<String>>> copyNodes = new HashSet<Tuple2<String,Set<String>>>(nodes);
				for (Tuple2<String, Set<String>> tuple2 : copyNodes) {
					if(tuple2.getFirst().equals(child)) nodes.remove(tuple2);
				}
				g.removeVertex(getGraphVertex(child).get());
				nodeCount--;
			}
		} else {

			if(haveAnotherFather(father, child) ) {
				if(getGraphEdge(father, child).isPresent()) {
					System.out.println("REMOVE EDGE FROM " + father + " TO " + child);
					g.removeEdge(getGraphEdge(father, child).get());										
				}
			} else {
				nodeCount--;
				System.out.println("REMOVE VERTEX " + child);
				if(getGraphVertex(child).isPresent()) {
					g.removeVertex(getGraphVertex(child).get());															
				}
			}
		}
	}
	
	private void addEdge(String father, String child) {
		if(!getGraphVertex(father).isPresent()) {
			System.out.println("ADD VERTEX "  + father);
			g.insertVertex(father);
			nodeCount++;
		}
		if(!getGraphVertex(child).isPresent()) {
			System.out.println("ADD VERTEX "  + child);
			g.insertVertex(child);
			nodeCount++;
		}
		System.out.println("ADD EDGE ("  + father + ", " + child + ")");
		g.insertEdge(father, child,  father+child);
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
			Optional<Tuple2<String, String>> edgeToAdd = edgesToAdd.stream().findFirst();
			addEdge(edgeToAdd.get().getSecond(), edgeToAdd.get().getFirst());
			edgesToAdd.remove(edgeToAdd.get());
		} else if(edgesToRemove.size() != 0) {
			Optional<Tuple2<String, String>> edgeToRemove = edgesToRemove.stream().findFirst();
			removeEdge(edgeToRemove.get().getSecond(), edgeToRemove.get().getFirst());
			edgesToRemove.remove(edgeToRemove.get());
		}
		
		Platform.runLater(() -> {labelNum.setText("" + nodeCount); graphView.update();}) ;
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
	
	public void refresh() throws InterruptedException, ExecutionException {
		if(link == null || depthLevel == 0) {
			System.out.println("Not yet initialized");
			return;
		}
		Tuple2<String, Integer> t = new Tuple2<>(link, 1);
		if(master != null) {
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



