package model;

import utils.Coord;

public class Truck {

	private int id;
	private Coord position;
	private int idStation;
	private int idFire;
	private int capacity;
	
	public Truck(int id, Coord position, int idStation, int idFire, int capacity) {
		this.id = id;
		this.position = position;
		this.idStation = idStation;
		this.idFire = idFire;
		this.capacity = capacity;
	}
	
	public int getId() {
		return this.id;
	}
	
	public Coord getPosition() {
		return this.position;
	}
	
	public int getIdStation() {
		return this.idStation;
	}
	
	public int getIdFire() {
		return this.idFire;
	}
	
	public int getCapacity() {
		return this.capacity;
	}
	
	void move(Coord position) {
		this.position = position;
	}
	
	public String toString() {
		return "Truck #" + this.getId() +
				"\n  Position : " + this.getPosition() +
				"\n  IDStation : " + this.getIdStation() +
				"\n  IDFire : " + this.getIdFire() + 
				"\n  Capacity : " + this.getCapacity() + "\n";
	}
	
}
