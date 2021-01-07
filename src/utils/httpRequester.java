package utils;

import java.util.ArrayList;
import java.util.List;

import model.Fire;
import model.Truck;

public final class httpRequester {
	
	private static Boolean localTest = true;

	private static String get(String endpoint) {
		return "TODO";
	}
	
	private static String post(String endpoint, String data) {
		return "TODO";
	}
	
	public static List<Truck> getAllTrucks(String endpoint) {
		List<Truck> trucks = new ArrayList<Truck>();
		
		if(localTest) {
			trucks = factory_getTrucks();
		} else {
			String results = get(endpoint);
			// TODO
		}
		
		return trucks;
	}
	
	public static List<Fire> getAllFires(String endpoint) {
		List<Fire> fires = new ArrayList<Fire>();

		if(localTest) {
			fires = factory_getFires();
		} else {
			String results = get(endpoint);
			// TODO
		}

		return fires;
	}
	
	public static void updateDBSimulator(String endpoint, List<Truck> trucks, List<Fire> fires) {
		// TODO
		for(Fire fire : fires) {
			if(fire.getId() == -1) {
				// TODO - insertion d'un nouveau feu.
			}
			if(fire.getIntensity() == 0) {
				// TODO - requête HTTP DELETE.
			}
		}
	}
	
	public static void updateDBEmergency(String endpoint, List<Truck> trucks) {
		// TODO
	}
	
	/*
	 * 	Fonctions "factory" utilisées quand localTest == true.
	 *  Permet de générer des Trucks / Fires aléatoires.
	 *  C'est pour pouvoir tester que les camions se rapprochent bien de leur destination à chaque tour.
	 */
	
	private static List<Truck> factory_getTrucks() {
		List<Truck> trucks = new ArrayList<Truck>();
		for(int i = 0; i < 5; i++) {
			trucks.add(new Truck(-1, new Coord((int) (Math.random() * 10), (int) (Math.random() * 10)), new Coord((int) (Math.random() * 10), (int) (Math.random() * 10))));
		}
		return trucks;
	}
	
	private static List<Fire> factory_getFires() {
		List<Fire> fires = new ArrayList<Fire>();
		for(int i = 0; i < 5; i++) {
			fires.add(new Fire(-1, new Coord((int) (Math.random() * 10), (int) (Math.random() * 10)), (int) (Math.random() * 10 + 1)));
		}
		return fires;
	}
	
}
