
public class Square {
	private int x;
	private int y;
	
	public Piece piece;
	
	public Square(int x, int y) {
		this.x = x;
		this.y = y;
		this.piece = null;
	}
	
	public void placePiece(Piece piece) {
		this.piece = piece;
	}
	
	public void clearSquare(){
		this.piece = null;
	}
}
