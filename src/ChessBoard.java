import java.util.ArrayList;

public class ChessBoard {
	private ArrayList<Piece> white;
	private ArrayList<Piece> black;
	private Square[][] squares;
	
	public ChessBoard() {
		this.squares = new Square[8][8];
		for(int x = 0; x <= 7; x++) {
			for(int y = 0; y <= 7; y++) {
				this.squares[x][y] = new Square(x,y);
			}
		}
		this.white = new ArrayList<Piece>();
		this.black = new ArrayList<Piece>();
		setup();
	}
	
	public void clear() {
		// clear all squares
		for(int x = 0; x <= 7; x++) {
			for(int y = 0; x <= 7; x++) {
				this.squares[x][y].clearSquare();
			}
		}
		// remove all pieces from white and black
		white.clear();
		black.clear();
	}
	
	public void setup() {
		this.clear();
		int x, y;
		
		y = 7;
		//white backline
		for (x = 0; x <= 7; x++) {
			Piece p = null;
			if (x == 0 || x == 7) {
				p = new Piece(x, y, "Castle", true);
			} else if (x == 1 || x == 6) {
				p = new Piece(x, y, "Knight", true);
			} else if (x == 2 || x == 5) {
				p = new Piece(x, y, "Bishop", true);
			} else if (x == 3) {
				p = new Piece(x, y, "Queen", true);
			} else if (x == 4) {
				p = new Piece(x, y, "King", true);
			}
			this.white.add(p);
			this.squares[x][y].placePiece(p);
		}
		
		y = 6;
		//white pawns
		for (x = 0; x <= 7; x++) {
			Piece p = new Piece(x,y, "Pawn", true);
			this.white.add(p);
			this.squares[x][y].placePiece(p);
		}
		
		y = 0;
		//black backline
		for (x = 0; x <= 7; x++) {
			Piece p = null;
			if (x == 0 || x == 7) {
				p = new Piece(x, y, "Castle", false);
			} else if (x == 1 || x == 6) {
				p = new Piece(x, y, "Knight", false);
			} else if (x == 2 || x == 5) {
				p = new Piece(x, y, "Bishop", false);
			} else if (x == 3) {
				p = new Piece(x, y, "Queen", false);
			} else if (x == 4) {
				p = new Piece(x, y, "King", false);
			}
			this.black.add(p);
			this.squares[x][y].placePiece(p);
		}
		
		y = 1;
		//black pawns
		for (x = 0; x <= 7; x++) {
			Piece p = new Piece(x,y, "Pawn", false);
			this.black.add(p);
			this.squares[x][y].placePiece(p);
		}
	}
	
	public ArrayList<Piece> getWhitePieces() {
		return white;
	}
	public ArrayList<Piece> getBlackPieces() {
		return black;
	}
}
