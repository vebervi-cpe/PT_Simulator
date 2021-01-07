package model;

import utils.Coord;

public class Truck {

	private int id;
	private Coord position;
	private Coord destination;
	
	public Truck(int id, Coord position, Coord destination) {
		this.id = id;
		this.position = position;
		this.destination = destination;
	}
	
	public int getId() {
		return this.id;
	}
	
	public Coord getPosition() {
		return this.position;
	}
	
	public Coord getDestination() {
		return this.destination;
	}
	
	void move(Coord position) {
		this.position = position;
	}
	
	public String toString() {
		return "Truck #" + this.getId() +
				"\n  Position : " + this.getPosition() +
				"\n  Destination : " + this.getDestination() + "\n";
	}
	
}
