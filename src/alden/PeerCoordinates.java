package alden;
import java.io.*;

public class PeerCoordinates implements Serializable {
	private static final long serialVersionUID = 3667108226485766929L;
	public static final int MIN_X = 0;
	public static final int MAX_X = 99;
	public static final int MIN_Y = 0;
	public static final int MAX_Y = 99;
	
	private int x;
	private int y;

	public PeerCoordinates() {
		setToRandomCoordinate();
	}
	
	public PeerCoordinates(int x, int y) {
		if (x < MIN_X || x > MAX_X || y < MIN_Y || y > MAX_Y)
			throw new IllegalArgumentException();
		
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	public double distanceTo(PeerCoordinates other) {
		return Math.sqrt((x - other.x) * (x - other.x) + (y - other.y) * (y - other.y));
	}

	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	public boolean equals(Object other) {
		if (!(other instanceof PeerCoordinates))
			return false;
		
		return x == ((PeerCoordinates)other).x && y == ((PeerCoordinates)other).y;
	}

	public void setToRandomCoordinate() {
		x = MIN_X + (int)((MAX_X - MIN_X + 1) * Math.random());
		y = MIN_Y + (int)((MAX_Y - MIN_Y + 1) * Math.random());
	}
}
