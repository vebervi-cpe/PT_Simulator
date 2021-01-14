package model;

import java.util.ArrayList;
import java.util.List;

import utils.Coord;
import utils.httpRequester;

public class Simulator {

	private List<Truck> trucks;
	private List<Fire> fires;
	private List<Station> stations;
	
	private List<Truck> trucksToUpdate = new ArrayList<Truck>();
	private List<Fire> firesToUpdate = new ArrayList<Fire>();
	
	// Plus la valeur est grande, plus le risque de générer un feu à chaque tour est grand.
	private double randomFireThreshold = 0.5;
	
	private String serverDBSimulator;
	private String serverDBEmergency;
	
	public Simulator(String serverSimulator, String serverEmergency) {
		this.serverDBSimulator = serverSimulator;
		this.serverDBEmergency = serverEmergency;
	}
	
	public void run() {
		Boolean getStationsOnce = true;
		while(true) {
			System.out.print("<====> START OF THE LOOP <====>\n");
			
			// Etape 1 : récupérer les feux, les camions et les casernes de la BDD Emergency.
			this.trucks = httpRequester.getAllTrucks(this.serverDBEmergency);
			this.fires = httpRequester.getAllFires(this.serverDBEmergency);
			if(getStationsOnce) {
				this.stations = httpRequester.getAllStations(this.serverDBEmergency);
				getStationsOnce = false;
			}

			System.out.print("<====> TRUCKS GET <====>\n" + this.trucks + "\n");
			System.out.print("<====> FIRES GET <====>\n" + this.fires + "\n");
			System.out.print("<====> STATIONS GET <====>\n" + this.stations + "\n");

			// Etape 2 : déplacer les camions.
			this.step2_moveTrucks();

			// Etape 3 : calculer les nouvelles intensités des feux.
			this.step3_computeFires();

			// Etape 4 : générer aléatoirement des feux.
			this.step4_generateRandomFire();
			
			System.out.print("<====> TRUCKS TO UPDATE <====>\n" + this.trucksToUpdate + "\n");
			System.out.print("<====> FIRES TO UPDATE <====>\n" + this.firesToUpdate + "\n\n");
			
			System.out.print("<====> HTTP REQUESTS <====>\n");

			// Etape 5 : mettre à jour la BDD Simulator (feux).
			httpRequester.updateDBSimulator(this.serverDBSimulator, this.firesToUpdate);

			// Etape 6 : mettre à jour la BDD Emergency (camions).
			httpRequester.updateDBEmergency(this.serverDBEmergency, this.trucksToUpdate);
			
			System.out.print("<====> END OF THE LOOP <====>\n\n");
			
			this.clearLists();
			
			// Etape 0 : pause de 10 secondes.
			this.step0_wait(10);
		}
	}
	
	private void step0_wait(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void step2_moveTrucks() {
		for(Truck truck : this.trucks) {
			Boolean hasFire = false;
			// On instancie notre destination à une valeur quelconque pour éviter les quelques rares NullPointerException.
			Coord destination = new Coord(0, 0);

			// On parcours tous les feux...
			for(Fire fire : this.fires) {
				// ...et si notre camion est lié à un feu que l'on a récupéré, c'est sa destination.
				if(truck.getIdFire() == fire.getId()) {
					hasFire = true;
					destination = fire.getPosition();
				}
			}

			// Sinon, c'est que le camion n'est lié à aucun feu...
			if(!hasFire) {
				// ...alors on récupère les coordonnées de sa caserne pour en faire la destination.
				for(Station station : this.stations) {
					if(station.getId() == truck.getIdStation()) {
						destination = station.getPosition();
					}
				}
			}
			
			// S'il le camion n'est pas déjà sur sa destination, on va devoir le faire avancer.
			if(!truck.getPosition().equals(destination)) {
				Coord nextPosition = truck.getPosition();
				float travelDistance = (float) 0.05;
				
				// On décale de X en vertical et en horizontal selon où se trouve la destination (à améliorer ? oui).
				if(nextPosition.getX() < destination.getX())
					nextPosition.setX(nextPosition.getX() + travelDistance);
				if(nextPosition.getX() > destination.getX())
					nextPosition.setX(nextPosition.getX() - travelDistance);

				if(nextPosition.getY() < destination.getY())
					nextPosition.setY(nextPosition.getY() + travelDistance);
				if(nextPosition.getY() > destination.getY())
					nextPosition.setY(nextPosition.getY() - travelDistance);
				
				truck.move(nextPosition);
				trucksToUpdate.add(truck);
			}
		}
	}
	
	private void step3_computeFires() {
		for(Fire fire : this.fires) {
			// Si un camion se trouve sur le feu.
			Truck truck = fire.getTruck(this.trucks);
			if(truck != null) {
				fire.setIntensity(fire.getIntensity() - truck.getCapacity());
				// Si notre feu a une intensité inférieure à 0, on la met directement à 0.
				if(fire.getIntensity() < 0) {
					fire.setIntensity(0);
				}
				firesToUpdate.add(fire);
			}
		}
	}
	
	private void step4_generateRandomFire() {
		// Si il n'y a aucun feu actif, le risque de générer un feu est plus grand que d'habitude.
		double randomBoost = 0;
		if(this.fires.isEmpty()) {
			randomBoost = 0.5;
		}

		if(Math.random() < this.randomFireThreshold + randomBoost) {
			// Les valeurs en dur représentent Lyon et ses alentours.
			float minX = (float) 45.7079, maxX = (float) 45.7985;
			float minY = (float) 4.7703, maxY = (float) 4.9401;
			float randomCoordX = (float) (minX + Math.random() * (maxX - minX));
			float randomCoordY = (float) (minY + Math.random() * (maxY - minY));
			
			Coord randomCoord = new Coord(randomCoordX, randomCoordY);
			int randomIntensity = (int) (Math.random() * 9 + 1);
			this.firesToUpdate.add(new Fire(-1, randomCoord, randomIntensity));
		}
	}
	
	private void clearLists() {
		this.trucks.clear();
		this.fires.clear();
		this.trucksToUpdate.clear();
		this.firesToUpdate.clear();
	}
	
}
