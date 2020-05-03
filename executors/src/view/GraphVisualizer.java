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
import java.util.Collection;
import java.util.HashSet;
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
	private static Master masterSimulator;

	private static Set<Tuple2<String, String>> edgesToAdd = new HashSet<Tuple2<String, String>>();
	private static Set<Tuple2<String, String>> edgesToRemove = new HashSet<Tuple2<String, String>>();
	private static Set<Tuple2<String, Set<String>>> nodes = new HashSet<Tuple2<String, Set<String>>>();	
	private static Graph<String, String> g = new GraphEdgeList<String, String>();
	private static SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
	private final static SmartGraphPanel<String, String> graphView =  new SmartGraphPanel<>(g, strategy);

	private static volatile int nodeCount = 0;
	private static Label labelNum;
	private static int depthLevel = 0;
	private static String link = null;

	public void updateGraph(utilities.Graph graph, int depthLevel) {

		if(depthLevel == 0) {
			if (!this.getGraphVertex(graph.getListNode().get(0).getFirst()).isPresent()){			
				Tuple2<String, Integer> n = graph.getListNode().get(0);
				g.insertVertex(n.getFirst());
				nodeCount++;
				nodes.add(new Tuple2<>(n.getFirst(), new HashSet<String>()));
			}
		} else {
			System.out.println(graph.getEdges());
			if(graph.getEdges().isEmpty())
				return;
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
					//	System.out.println(fatherName + " ADD NEW CHILD " + childNode.getFirst());
						fatherNode.getSecond().add(childNode.getFirst());
						edgesToAdd.add(childNode);					
					}
				}

			//	System.out.println(edgesToRemove);
			}

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
			//System.out.println("ADD VERTEX "  + father);
			g.insertVertex(father);
			nodeCount++;
		}
		if(!getGraphVertex(child).isPresent()) {
			//System.out.println("ADD VERTEX "  + child);
			g.insertVertex(child);
			nodeCount++;
		}
		//System.out.println("ADD EDGE ("  + father + ", " + child + ")");
		g.insertEdge(father, child,  father+child);
	}

	public void update() throws FileNotFoundException {
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
		field.setPrefColumnCount(70);
		field.setText("https://it.wikipedia.org/wiki/Macchina");

		Label labelLev = new Label("Depth level: ");
		Spinner<Integer> spinner = new Spinner<Integer>();
		SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5, 2);
		spinner.setValueFactory(valueFactory);

		Button playButton = new Button("Play");
		playButton.setFont(new Font(18));
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

		Button playSimulatorButton = new Button("Play Simulator");
		playSimulatorButton.setFont(new Font(18));
		playSimulatorButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				System.out.println("Play simulator");
				link = field.getText();
				depthLevel = spinner.getValue();
				try {
					masterSimulator.compute(new Tuple2<String, Integer>(link, 1), depthLevel);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				} catch (ExecutionException e1) {
					e1.printStackTrace();
				}
			}
		});
		Button clearButton = new Button("Clear");
		clearButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.out.println("Clear");
				reset();
			}
		});

		Label labelNodes = new Label("The number of node is: ");
		labelNum = new Label("0");

		root.add(labelAddress, 0, 0);
		root.add(field, 1, 0);
		root.add(labelLev, 6, 0);
		root.add(spinner, 7, 0);
		root.add(graphView, 0, 1, 7, 6);

		root.add(labelNodes, 7, 2);
		root.add(labelNum, 7, 3);
		root.add(clearButton, 7, 4);
		root.add(playSimulatorButton, 7, 5);
		root.add(playButton, 7, 6);
		Scene scene = new Scene(root, screenSize.getWidth()*0.7, screenSize.getWidth()*0.4);
		System.out.println(graphView.getScene());
		graphView.setAutomaticLayout(true);
		scene.getStylesheets().add(getClass().getResource(File.separator + "smartgraph.css").toExternalForm());

		primaryStage.setTitle("Wikipedia Graph");
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

	public void refreshSimulator() throws InterruptedException, ExecutionException {
		if(link == null) {
			System.out.println("Not yet initialized");
			return;
		}
		Tuple2<String, Integer> t = new Tuple2<>(link, 0);
		if(masterSimulator != null) {
			masterSimulator.compute(t, depthLevel);
		}
	}


	public void setMasters(Master m, Master ms) {
		master = m;
		masterSimulator = ms;
		//System.out.println("Master: " + master);
	}

	private void reset() {
		Collection<Vertex<String>> copy = new HashSet<Vertex<String>>(g.vertices());
		for (Vertex<String> v : copy) {
			g.removeVertex(v);
		}
		Collection<Edge<String, String>> copyEdge = new HashSet<>(g.edges());
		for (Edge<String, String> e : copyEdge) {
			g.removeEdge(e);
		}
		nodeCount = 0;
		edgesToAdd.clear();
		edgesToRemove.clear();
		nodes.clear();
		Platform.runLater(() -> { graphView.update(); System.out.println("PLR - update");}) ;
	}

	public void play(String[] args) {
		launch(args);
	}
}



