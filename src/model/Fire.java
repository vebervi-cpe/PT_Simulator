package model;

import java.util.List;

import utils.Coord;

public class Fire {
	
	private int id;
	private Coord position;
	private int intensity;
	
	public Fire(int id, Coord position, int intensity) {
		this.id = id;
		this.position = position;
		this.intensity = intensity;
	}
	
	public int getId() {
		return this.id;
	}
	
	public Coord getPosition() {
		return this.position;
	}
	
	public int getIntensity() {
		return this.intensity;
	}
	
	public void setIntensity(int intensity) {
		this.intensity = intensity;
	}

	public Boolean hasTruck(List<Truck> trucks) {
		for(Truck truck : trucks) {
			if(this.position.equals(truck.getPosition())) {
				System.out.print("Je suis fire " + this.getId() + this.getPosition() + " mon truck" + truck.getId() + truck.getPosition());
				return true;
			}
		}
		return false;
	}
	
	public String toString() {
		return "Fire #" + this.getId() +
				"\n  Position : " + this.getPosition() +
				"\n  Intensity : " + this.getIntensity() + "\n";
	}
	
}
