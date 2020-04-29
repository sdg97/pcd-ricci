package main;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;


import sun.net.www.http.HttpClient;

public class MainPerProve {

	private static final String GET_URL = "https://it.wikipedia.org/w/api.php?action=parse&page=Macchina&format=json&section=0&prop=links"; 

	
	public static void main(String[] args) throws IOException, JSONException {
		
		     URL obj2 = new URL(GET_URL);
		     HttpURLConnection conn = (HttpURLConnection) obj2.openConnection();
		     conn.setRequestMethod("GET");
		     int responseC = conn.getResponseCode();
		     System.out.println("\nSending 'GET' request to URL : " + GET_URL);
		     System.out.println("Response Code : " + responseC);
		     BufferedReader in = new BufferedReader(
		             new InputStreamReader(conn.getInputStream()));
		     String inputLine;
		     StringBuffer response = new StringBuffer();
		     while ((inputLine = in.readLine()) != null) {
		     	response.append(inputLine);
		     }
		     in.close();
		     //print in String
		     System.out.println(response.toString());
		     //Read JSON response and print
		     JSONObject myResponse = new JSONObject(response.toString());
		     System.out.println("result after Reading JSON Response");
		     JSONArray job = (JSONArray)((JSONObject) myResponse.get("parse")).get("links");
		     for(int i = 0; i < job.length(); i++)
		    	 System.out.println("*- "+ job.getJSONObject(i).get("*"));
		 
		   }
			
			
			
			
//		} else {
//			System.out.println("GET request not worked");
//		}

	} 
