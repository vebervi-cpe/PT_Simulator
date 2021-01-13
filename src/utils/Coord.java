package utils;

public class Coord {

	private float x;
	private float y;
	
	public Coord(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public float getX() {
		return this.x;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public float getY() {
		return this.y;
	}
	
	public void setY(float y) {
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
        // Comparer à 0.05 permet d'occulter certains chiffres après la virgule.
        return (Math.abs(this.getX() - coord.getX()) < 0.05) && (Math.abs(this.getY() - coord.getY()) < 0.05);
    }
	
}
