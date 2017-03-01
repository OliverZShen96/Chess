import java.io.Serializable;

public class Piece implements Serializable{
	private Player color;
	private int x;
	private int y;
	private String type;
	private boolean hasMoved;
	
	public Piece(int x, int y, String type, Player color) {
		this.color = color;
		this.x = x;
		this.y = y;
		this.type = type;	
		this.hasMoved = false;
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
	
	public void moveTo(int x, int y) {
		setX(x);
		setY(y);
		this.hasMoved = true;
	}
	

	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}

	public void promote() {
		this.type = "Queen";
	}
	
	public boolean hasMoved() {
		return this.hasMoved;
	}
}
