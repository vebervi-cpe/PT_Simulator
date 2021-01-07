package model;

import java.util.ArrayList;
import java.util.List;

import utils.Coord;
import utils.httpRequester;

public class Simulator {

	private List<Truck> trucks;
	private List<Fire> fires;
	
	private List<Truck> trucksToUpdate = new ArrayList<Truck>();
	private List<Fire> firesToUpdate = new ArrayList<Fire>();
	
	// Plus la valeur est grande, plus le risque de générer un feu à chaque tour est grand.
	private double randomFireThreshold = 0.5; 
	
	private String endpointDBSimulator;
	private String endpointDBEmergency;
	
	public Simulator(String endpointSimulator, String endpointEmergency) {
		this.endpointDBSimulator = endpointSimulator;
		this.endpointDBEmergency = endpointEmergency;
	}
	
	public void run() {
		while(true) {
			// Etape 0 : pause de 5 secondes.
			this.step0_wait(5);
			
			System.out.print("<====> START OF THE LOOP <====>\n");
			
			// Etape 1 : récupérer les feux et les camions de la BDD Emergency.
			this.trucks = httpRequester.getAllTrucks(this.endpointDBEmergency);
			this.fires = httpRequester.getAllFires(this.endpointDBEmergency);

			System.out.print("<====> TRUCKS GET <====>\n" + this.trucks + "\n");
			System.out.print("<====> FIRES GET <====>\n" + this.fires + "\n");

			// Etape 2 : déplacer les camions.
			this.step2_moveTrucks();

			// Etape 3 : calculer les nouvelles intensités des feux.
			this.step3_computeFires();

			// Etape 4 : générer aléatoirement des feux.
			this.step4_generateRandomFire();
			
			System.out.print("<====> TRUCKS TO UPDATE <====>\n" + this.trucksToUpdate + "\n");
			System.out.print("<====> FIRES TO UPDATE <====>\n" + this.firesToUpdate + "\n");

			// Etape 5 : mettre à jour la BDD Simulator (camions et feux).
			httpRequester.updateDBSimulator(this.endpointDBSimulator, this.trucksToUpdate, this.firesToUpdate);

			// Etape 6 : mettre à jour la BDD Emergency (camions).
			httpRequester.updateDBEmergency(this.endpointDBEmergency, this.trucksToUpdate);
			
			System.out.print("<====> END OF THE LOOP <====>\n\n");
			
			this.clearLists();

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
			if(truck.getPosition() != truck.getDestination()) {
				Coord nextPosition = truck.getPosition();
				Coord destination = truck.getDestination();
				
				// On décale de 1 en vertical et en horizontal selon où se trouve la destination (à améliorer ?).
				if(nextPosition.getX() < destination.getX())
					nextPosition.setX(nextPosition.getX() + 1);
				if(nextPosition.getX() > destination.getX())
					nextPosition.setX(nextPosition.getX() - 1);

				if(nextPosition.getY() < destination.getY())
					nextPosition.setY(nextPosition.getY() + 1);
				if(nextPosition.getY() > destination.getY())
					nextPosition.setY(nextPosition.getY() - 1);
				
				truck.move(nextPosition);
				trucksToUpdate.add(truck);
			}
		}
	}
	
	private void step3_computeFires() {
		for(Fire fire : this.fires) {
			if(fire.hasTruck(this.trucks)) {
				fire.setIntensity(fire.getIntensity() - 1);
				firesToUpdate.add(fire);
			}
		}
	}
	
	private void step4_generateRandomFire() {
		if(Math.random() < this.randomFireThreshold) {
			Coord randomCoord = new Coord((int) (Math.random() * 10), (int) (Math.random() * 10));
			int randomIntensity = (int) (Math.random() * 10 + 1);
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
