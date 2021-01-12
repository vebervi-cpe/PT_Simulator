package model;

import utils.Coord;

public class Station {

	private int id;
	private Coord position;
	
	public Station(int id, Coord position) {
		this.id = id;
		this.position = position;
	}
	
	public int getId() {
		return this.id;
	}
	
	public Coord getPosition() {
		return this.position;
	}
	
	public String toString() {
		return "Station #" + this.getId() +
				"\n  Position : " + this.getPosition() + "\n";
	}
	
}
