import java.io.Serializable;

public class Piece implements Serializable{
	private boolean isWhite;
	private int x;
	private int y;
	private String type;
	
	public Piece(int x, int y, String type, boolean isWhite) {
		this.isWhite = isWhite;
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
	
	public boolean isWhite() {
		return isWhite;
	}

	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
}
