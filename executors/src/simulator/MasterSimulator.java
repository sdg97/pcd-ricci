package simulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import model.Master;
import utilities.Graph;
import utilities.Tuple2;
import view.GraphVisualizer;

public class MasterSimulator implements Master{


	private Graph graph;
	private GraphVisualizer gv;
	private int iteration;
	private List<List<Tuple2<String, List<String>>>> iterations;

	public MasterSimulator(GraphVisualizer gv) {	
		this.gv = gv;
		this.iteration = 0;
		this.iterations = new ArrayList<List<Tuple2<String,List<String>>>>();

		this.iterations.add(Arrays.asList(new Tuple2<String, List<String>>("Macchina", Arrays.asList("Automobile", "Macchina elettrica", "Lavoro (fisica)", "Fluido", "Macchina semplice")),
							new Tuple2<String, List<String>>("Fluido", Arrays.asList("Materiale", "Liquido", "Miscela (chimica)", "Stato della materia", "Sostanza pura", "Sforzo di taglio", "Plasticità (fisica)", "Plasma (fisica)", "Aeriforme", "Deformazione", "Grandezza fisica")),
							new Tuple2<String, List<String>>("Macchina semplice", Arrays.asList("Sistema Internazionale","Forza", "Gaspard Gustave de Coriolis", "Francia", "Fisica", "Campo vettoriale conservativo", "Max Jammer", "Energia", "Joule", "Energia cinetica", "Distanza", "ISBN", "Struttura dissipativa", "Campo (fisica)", "1826", "Energia potenziale"))));
		
		this.iterations.add(Arrays.asList(new Tuple2<String, List<String>>("Macchina", Arrays.asList("Automobile", "Macchina elettrica", "Lavoro (fisica)", "Forza", "Fluido", "Macchina semplice")),
									new Tuple2<String, List<String>>("Fluido", Arrays.asList("Materiale", "Liquido", "Miscela (chimica)", "Stato della materia", "Sostanza pura", "Sforzo di taglio", "Plasticità (fisica)", "Plasma (fisica)", "Aeriforme", "Deformazione", "Grandezza fisica")),
									new Tuple2<String, List<String>>("Macchina semplice", Arrays.asList("Sistema Internazionale","Forza", "Gaspard Gustave de Coriolis", "Francia", "Fisica", "Campo vettoriale conservativo", "Max Jammer", "Energia", "Joule", "Energia cinetica", "Distanza", "ISBN", "Struttura dissipativa", "Campo (fisica)", "1826", "Energia potenziale")),
									new Tuple2<String, List<String>>("Forza", Arrays.asList("Stato macroscopico", "Forza (disambigua)", "Principi della dinamica", "Grandezza vettoriale", "Vettore (matematica)", "Corpo (fisica)", "Grandezza fisica", "Tempo", "Stato di quiete", "Quantità di moto", "Derivata", "Moto (fisica)", "Particella elementare"))));
		
		this.iterations.add(Arrays.asList(new Tuple2<String, List<String>>("Macchina", Arrays.asList("Automobile", "Macchina elettrica", "Lavoro (fisica)", "Fluido", "Macchina semplice")),
							new Tuple2<String, List<String>>("Fluido", Arrays.asList("Materiale", "Liquido", "Miscela (chimica)", "Stato della materia", "Sostanza pura", "Sforzo di taglio", "Plasticità (fisica)", "Plasma (fisica)", "Aeriforme", "Deformazione", "Grandezza fisica"))));	
		
		this.iterations.add(Arrays.asList(new Tuple2<String, List<String>>("Macchina", Arrays.asList("Forza")),
				new Tuple2<String, List<String>>("Forza", Arrays.asList("Stato macroscopico", "Forza (disambigua)", "Principi della dinamica", "Grandezza vettoriale", "Vettore (matematica)", "Corpo (fisica)",  "Tempo", "Stato di quiete", "Quantità di moto", "Derivata", "Moto (fisica)", "Particella elementare"))));


				
		this.iterations.add(Arrays.asList(new Tuple2<String, List<String>>("Macchina", Arrays.asList("Fluido", "Forza" )),
				new Tuple2<String, List<String>>("Fluido", Arrays.asList("Materiale", "Liquido", "Miscela (chimica)", "Stato della materia", "Sostanza pura", "Sforzo di taglio", "Plasticità (fisica)", "Plasma (fisica)", "Aeriforme", "Deformazione", "Grandezza fisica")),
				new Tuple2<String, List<String>>("Forza", Arrays.asList("Stato macroscopico", "Forza (disambigua)", "Principi della dinamica", "Grandezza vettoriale", "Vettore (matematica)", "Corpo (fisica)",  "Tempo", "Stato di quiete", "Quantità di moto", "Derivata", "Moto (fisica)", "Particella elementare"))));	

	}

	@Override
	public int compute(Tuple2<String, Integer> initTuple, int dl) throws InterruptedException, ExecutionException {
		this.iterations.get(this.iteration).forEach(t -> {
			graph = new Graph();
			graph.addNodes(Arrays.asList(t.getFirst(), t.getSecond()));
			t.getSecond().forEach(e -> {
				graph.addEdge(new Tuple2<String, String>(e, t.getFirst()));				
			});
			gv.updateGraph(graph);
		});
		this.iteration++;
		return 0;
	}
	
	public int getIteration() {
		return this.iteration;
	}
	
	public int iterationsSize() {
		return this.iterations.size();
	}

}


