package utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import model.Fire;
import model.Station;
import model.Truck;

public final class httpRequester {

	private static String get(String serverEndpoint) {
		System.out.print("HTTP GET : " + serverEndpoint + "\n");
		String content = new String();
		try {
			URL url = new URL(serverEndpoint);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setConnectTimeout(20000);

			int status = con.getResponseCode();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
	
			while ((inputLine = in.readLine()) != null) {
			    content += inputLine;
			}

			in.close();
			con.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return content;
	}
	
	private static void put(String serverEndpoint, String data) {
		System.out.print("HTTP PUT : " + serverEndpoint + " | " + data + "\n");
		try {
			URL url = new URL(serverEndpoint);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("PUT");
			con.setConnectTimeout(20000);
			con.setDoOutput(true);

			DataOutputStream out = new DataOutputStream(con.getOutputStream());
			out.writeBytes(data);
			out.flush();
			out.close();
			int status = con.getResponseCode();
			
			// Lecture de la réponse.
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
			    content.append(inputLine);
			}
			System.out.print("Request response (" + status + ") : " + content + "\n");

			in.close();
			con.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void delete(String serverEndpoint, String data) {
		System.out.print("HTTP DELETE : " + serverEndpoint + " | " + data + "\n");
		try {
			URL url = new URL(serverEndpoint);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("DELETE");
			con.setConnectTimeout(20000);
			con.setDoOutput(true);

			DataOutputStream out = new DataOutputStream(con.getOutputStream());
			out.writeBytes(data);
			out.flush();
			out.close();
			int status = con.getResponseCode();
			
			// Lecture de la réponse.
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
			    content.append(inputLine);
			}
			System.out.print("Request response (" + status  + "): " + content + "\n");

			in.close();
			con.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static List<Truck> getAllTrucks(String server) {
		List<Truck> trucks = new ArrayList<Truck>();
		String endpoint = "/camions/lire.php";
		String results = get(server + endpoint);
		JSONParser parser = new JSONParser();

		try {
			JSONObject jsonObject = (JSONObject) parser.parse(results);
			JSONArray camions = (JSONArray) jsonObject.get("camions");
            Iterator i = camions.iterator();
            while (i.hasNext()) {
                JSONObject innerObj = (JSONObject) i.next();
                int id_camion = Integer.parseInt((String) innerObj.get("id_camion"));
                Coord position = new Coord(Float.parseFloat((String) innerObj.get("positionX_camion")), Float.parseFloat((String) innerObj.get("positionY_camion")));
                int id_caserne = Integer.parseInt((String) innerObj.get("id_caserne"));
                int id_feu = Integer.parseInt((String) innerObj.get("id_feu"));
                int capacite = Integer.parseInt((String) innerObj.get("capacite"));
                trucks.add(new Truck(id_camion, position, id_caserne, id_feu, capacite));
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return trucks;
	}
	
	public static List<Fire> getAllFires(String server) {
		List<Fire> fires = new ArrayList<Fire>();
		String endpoint = "/feux/lire2.php";
		String results = get(server + endpoint);
		JSONParser parser = new JSONParser();
			
		try {
			JSONObject jsonObject = (JSONObject) parser.parse(results);
			JSONArray feux = (JSONArray) jsonObject.get("feux");
            Iterator i = feux.iterator();
            while (i.hasNext()) {
                JSONObject innerObj = (JSONObject) i.next();
                int id_feu = Integer.parseInt((String) innerObj.get("id_feu"));
                Coord position = new Coord(Float.parseFloat((String) innerObj.get("positionX")), Float.parseFloat((String) innerObj.get("positionY")));
                int intensite = Integer.parseInt((String) innerObj.get("intensite"));
                if(intensite != 0)  {
                	fires.add(new Fire(id_feu, position, intensite));
                }
            }
		} catch (Exception e) {
			e.printStackTrace();
		}

		return fires;
	}
	
	public static List<Station> getAllStations(String server) {
		List<Station> stations = new ArrayList<Station>();
		String endpoint = "/casernes/lire.php";
		String results = get(server + endpoint);
		JSONParser parser = new JSONParser();

		try {
			JSONObject jsonObject = (JSONObject) parser.parse(results);
			JSONArray casernes = (JSONArray) jsonObject.get("casernes");
            Iterator i = casernes.iterator();
            while (i.hasNext()) {
                JSONObject innerObj = (JSONObject) i.next();
                int id_caserne = Integer.parseInt((String) innerObj.get("id_caserne"));
                Coord position = new Coord(Float.parseFloat((String) innerObj.get("positionX_caserne")), Float.parseFloat((String) innerObj.get("positionY_caserne")));
                stations.add(new Station(id_caserne, position));
            }
		} catch (Exception e) {
			e.printStackTrace();
		}

		return stations;
	}
	
	public static void updateDBSimulator(String server, List<Fire> fires) {
		for(Fire fire : fires) {
			// Si c'est un feu nouvellement généré.
			if(fire.getId() == -1) {
				try {
					String endpoint = "/feux/creer.php";

					// On fabrique notre objet JSON qui va contenir le feu à ajouter.
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("intensite", String.valueOf(fire.getIntensity()));
					jsonObject.put("posX", String.valueOf(fire.getPosition().getX()));
					jsonObject.put("posY", String.valueOf(fire.getPosition().getY()));
					jsonObject.put("date_debut", String.valueOf(LocalDate.now()));
					
					StringWriter output = new StringWriter();
					jsonObject.writeJSONString(output);
					String jsonText = output.toString();
					
					put(server + endpoint, jsonText);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				continue;
			}
			
			// Si c'est un feu qui a été éteint.
			if(fire.getIntensity() == 0) {
				try {
					String endpoint = "/feux/fin.php";

					// On fabrique notre objet JSON qui va contenir le feu à supprimer.
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("id_feu", fire.getId());

					StringWriter output = new StringWriter();
					jsonObject.writeJSONString(output);
					String jsonText = output.toString();
					
					delete(server + endpoint, jsonText);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				continue;
			}
			
			// Autrement, c'est un feu qui a seulement vu son intensité changer.
			try {
				String endpoint = "/feux/modifier_intensite.php";

				// On fabrique notre objet JSON qui va contenir le feu à ajouter.
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("id_feu", fire.getId());
				jsonObject.put("intensite", fire.getIntensity());

				StringWriter output = new StringWriter();
				jsonObject.writeJSONString(output);
				String jsonText = output.toString();
				
				put(server + endpoint, jsonText);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void updateDBEmergency(String server, List<Truck> trucks) {
		String endpoint = "/camions/modifier_position.php";
		for(Truck truck : trucks) {
			try {
				// On fabrique notre objet JSON qui va contenir le camion à mettre à jour.
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("id_camion", truck.getId());
				jsonObject.put("posX", truck.getPosition().getX());
				jsonObject.put("posY", truck.getPosition().getY());
				
				StringWriter output = new StringWriter();
				jsonObject.writeJSONString(output);
				String jsonText = output.toString();
				
				put(server + endpoint, jsonText);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
