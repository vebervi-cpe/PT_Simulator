package utils;

public class Coord {

	private int x;
	private int y;
	
	public Coord(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return this.x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public String toString() {
		return "(" + this.getX() + ";" + this.getY() + ")";
	}
	
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Coord coord = (Coord) obj;
        return this.getX() == coord.getX() && this.getY() == coord.getY();
    }
	
}
