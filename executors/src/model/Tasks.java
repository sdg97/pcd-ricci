package model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import org.json.JSONArray;
import org.json.JSONObject;

import utilities.Tuple2;

/**
 * Dato un link prende i link correlati
 */
public class Tasks implements Callable<Tuple2<String, ArrayList<Tuple2<String, Integer>>>> {

	private static final String GET_URL_INIT = "https://it.wikipedia.org/w/api.php?action=parse&page=";
	private static final String GET_URL_END = "&format=json&section=0&prop=links"; 


	private Tuple2<String, Integer> myTuple;
	private ArrayList<Tuple2<String, Integer>> nodes;

	public Tasks(Tuple2<String, Integer> t) {
		this.myTuple = t;
		this.nodes = new ArrayList<>();
	}

	@Override
	public Tuple2<String, ArrayList<Tuple2<String, Integer>>> call() throws Exception {
		String url = GET_URL_INIT + myTuple.getFirst().replaceAll(" ", "_") + GET_URL_END;
		URL obj2 = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) obj2.openConnection();
		conn.setRequestMethod("GET");

		if(conn.getResponseCode() == 200) {
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			JSONObject myResponse;
			JSONArray job;
			try {
				myResponse = new JSONObject(response.toString());	
				job = (JSONArray)((JSONObject) myResponse.get("parse")).get("links");
				for(int i = 0; i < job.length(); i++) {
					if(job.getJSONObject(i).get("ns").toString().equals("0") ) {
						this.nodes.add(new Tuple2<String, Integer>(job.getJSONObject(i).get("*").toString(), myTuple.getSecond()+1));
						//log(job.getJSONObject(i).get("*").toString());				
					}
				}
			} catch (Exception e) {
				//System.out.println(url);			
			}
		} else {
			log("Some error");
		}

		return new Tuple2<String, ArrayList<Tuple2<String, Integer>>>(this.myTuple.getFirst(), this.nodes);
	}

	private void log(String msg) {
		//System.out.println("[TASK]" + msg);
	}


}
