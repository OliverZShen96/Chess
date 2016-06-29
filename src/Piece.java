

public class Piece {
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
}
