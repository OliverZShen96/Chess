import java.io.Serializable;

public class Square implements Serializable{
	private int x;
	private int y;
	
	public Piece piece;
	
	public Square(int x, int y) {
		this.x = x;
		this.y = y;
		this.piece = null;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public String getPieceType() {
		if (piece != null) return piece.getType();
		return null;
	}
	
	public Player getColor() {
		if (piece != null) return piece.getColor();
		return null;
	}
	
	public void placePiece(Piece piece) {
		this.piece = piece;
		piece.setX(x);
		piece.setY(y);
	}
	
	public void clearSquare() {
		this.piece = null;
	}
	
	public boolean hasPiece() {
		if (piece == null) return false;
		return true;
	}
	
	public Piece getPiece() {
		return piece;
	}
}
