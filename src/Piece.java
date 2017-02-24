import java.io.Serializable;

public class Piece implements Serializable{
	private Player color;
	private int x;
	private int y;
	private String type;
	
	public Piece(int x, int y, String type, Player color) {
		this.color = color;
		this.x = x;
		this.y = y;
		this.type = type;	
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	public String getType() {
		return type;
	}
	
	public Player getColor() {
		return color;
	}

	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
}
