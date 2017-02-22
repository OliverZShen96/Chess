import java.util.ArrayList;
import java.util.Collection;

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
	
	public Square getSquare(int x, int y) {
		return squares[x][y];
	}

	public ArrayList<Square> findPossiblemoves(Square squareFrom) {
		ArrayList<Square> possibleSquaresTo = new ArrayList<Square>();
		int x = squareFrom.getX();
		int y = squareFrom.getY();
		boolean isWhite = squareFrom.hasWhitePiece();
		
		if (squareFrom.getPieceType().equals("Pawn")) {
			possibleSquaresTo.addAll(findPawnMoves(x, y, isWhite));
		} else if (squareFrom.getPieceType().equals("Castle")) {
			possibleSquaresTo.addAll(findCastleMoves(x, y, isWhite));
		} else if (squareFrom.getPieceType().equals("Knight")) {
			possibleSquaresTo.addAll(findKnightMoves(x, y, isWhite));
		} else if (squareFrom.getPieceType().equals("Bishop")) {
			possibleSquaresTo.addAll(findBishopMoves(x, y, isWhite));
		} else if (squareFrom.getPieceType().equals("Queen")) {
			possibleSquaresTo.addAll(findQueenMoves(x, y, isWhite));
		} else if (squareFrom.getPieceType().equals("King")) {
			possibleSquaresTo.addAll(findKingMoves(x, y, isWhite));
		}
		return possibleSquaresTo;
	}

	private ArrayList<Square> findPawnMoves(int x, int y, boolean isWhite) {
		ArrayList<Square> moves = new ArrayList<Square>();
		int yDir = 1;
		if (isWhite) yDir = -1;
		// first space in front
		if (!squares[x][y+yDir].hasPiece()) {
			moves.add(squares[x][y+yDir]);
			
			// extra space if first move
			if ((isWhite && y == 6 || !isWhite && y == 1)) {
				if (!squares[x][y+yDir*2].hasPiece()) moves.add(squares[x][y+yDir*2]);
			}
		}	
		// capturing
		if (x < 7) {
			if (squares[x+1][y+yDir].hasPiece() && squares[x+1][y+yDir].hasWhitePiece() != isWhite) {
				moves.add(squares[x+1][y+yDir]);
			}
		}
		if (x > 0) {
			if (squares[x-1][y+yDir].hasPiece() && squares[x-1][y+yDir].hasWhitePiece() != isWhite) {
				moves.add(squares[x-1][y+yDir]);
			}
		}
		return moves;
	}
	private ArrayList<Square> findCastleMoves(int x, int y, boolean isWhite) {
		ArrayList<Square> moves = new ArrayList<Square>();
		int xVal, yVal;
		for (xVal = x+1, yVal = y; xVal <= 7; xVal++) {
			if (squares[xVal][yVal].hasPiece()) {
				if (squares[xVal][yVal].hasWhitePiece() == isWhite) break;
			}
			moves.add(squares[xVal][yVal]);
			if (squares[xVal][yVal].hasPiece()) break;
		}
		for (xVal = x-1, yVal = y; xVal >= 0; xVal--) {
			if (squares[xVal][yVal].hasPiece()) {
				if (squares[xVal][yVal].hasWhitePiece() == isWhite) break;
			}
			moves.add(squares[xVal][yVal]);
			if (squares[xVal][yVal].hasPiece()) break;
		}
		for (xVal = x, yVal = y+1; yVal <= 7; yVal++) {
			if (squares[xVal][yVal].hasPiece()) {
				if (squares[xVal][yVal].hasWhitePiece() == isWhite) break;
			}
			moves.add(squares[xVal][yVal]);
			if (squares[xVal][yVal].hasPiece()) break;
		}
		for (xVal = x, yVal = y-1; yVal >= 0; yVal--) {
			if (squares[xVal][yVal].hasPiece()) {
				if (squares[xVal][yVal].hasWhitePiece() == isWhite) break;
			}
			moves.add(squares[xVal][yVal]);
			if (squares[xVal][yVal].hasPiece()) break;
		}
		return moves;
	}
	private ArrayList<Square> findKnightMoves(int x, int y, boolean isWhite) {
		ArrayList<Square> moves = new ArrayList<Square>();
		int xBuff;
		int yBuff;
		for (xBuff = -2; xBuff <= 2; xBuff++) {
			if (xBuff == 0) continue;
			yBuff = 2;
			if (xBuff%2 == 0) yBuff = 1;
			
			if (xBuff + x >= 0 && xBuff + x <= 7) {
				if (y + yBuff >= 0 && y + yBuff <= 7) {
					if (isValidMove(xBuff+x, y+yBuff, isWhite)) moves.add(squares[xBuff+x][y+yBuff]);
				}
				if (y - yBuff >= 0 && y - yBuff <= 7) {
					if (isValidMove(xBuff+x, y-yBuff, isWhite)) moves.add(squares[xBuff+x][y-yBuff]);
				}
			}
		}
		return moves;
	}
	private ArrayList<Square> findBishopMoves(int x, int y, boolean isWhite) {
		ArrayList<Square> moves = new ArrayList<Square>();
		int xVal, yVal;
		for (xVal = x+1, yVal = y+1; xVal <= 7 && yVal <= 7; xVal++, yVal++) {
			if (squares[xVal][yVal].hasPiece()) {
				if (squares[xVal][yVal].hasWhitePiece() == isWhite) break;
			}
			moves.add(squares[xVal][yVal]);
			if (squares[xVal][yVal].hasPiece()) break;
		}
		for (xVal = x+1, yVal = y-1; xVal <= 7 && yVal >= 0; xVal++, yVal--) {
			if (squares[xVal][yVal].hasPiece()) {
				if (squares[xVal][yVal].hasWhitePiece() == isWhite) break;
			}
			moves.add(squares[xVal][yVal]);
			if (squares[xVal][yVal].hasPiece()) break;
		}
		for (xVal = x-1, yVal = y-1; xVal >= 0 && yVal >= 0; xVal--, yVal--) {
			if (squares[xVal][yVal].hasPiece()) {
				if (squares[xVal][yVal].hasWhitePiece() == isWhite) break;
			}
			moves.add(squares[xVal][yVal]);
			if (squares[xVal][yVal].hasPiece()) break;
		}
		for (xVal = x-1, yVal = y+1; xVal >= 0 && yVal <= 7; xVal--, yVal++) {
			if (squares[xVal][yVal].hasPiece()) {
				if (squares[xVal][yVal].hasWhitePiece() == isWhite) break;
			}
			moves.add(squares[xVal][yVal]);
			if (squares[xVal][yVal].hasPiece()) break;
		}
		return moves;
	}
	private ArrayList<Square> findQueenMoves(int x, int y, boolean isWhite) {
		ArrayList<Square> moves = new ArrayList<Square>();
		moves.addAll(findCastleMoves(x, y, isWhite));
		moves.addAll(findBishopMoves(x, y, isWhite));
		return moves;
	}
	private ArrayList<Square> findKingMoves(int x, int y, boolean isWhite) {
		ArrayList<Square> moves = new ArrayList<Square>();
		for (int xVal = x-1; xVal <= x+1; xVal++) {
			for (int yVal = y-1; yVal <= y+1; yVal++) {
				if (xVal == x && yVal == y) continue;
				if (xVal > 7 || xVal < 0 || yVal > 7 || yVal < 0) continue;
				if (!squares[xVal][yVal].hasPiece() || squares[xVal][yVal].hasWhitePiece() != isWhite) {
					moves.add(squares[xVal][yVal]);
				}
				System.out.println(xVal + " " + yVal);
			}
		}	
		return moves;
	}
	
	public boolean isChecked(boolean isWhite) {
		if (isWhite) {
			for (Piece p : white) {
				if (p.getType() == "King" && p.isWhite() == true) {
					if (findKingMoves(p.getX(), p.getY(), true).isEmpty()) return true;
				}
			}
		} else {
			for (Piece p : black) {
				if (p.getType() == "King" && p.isWhite() == false) {
					if (findKingMoves(p.getX(), p.getY(), false).isEmpty()) return true;
				}
			}
		}
		return false;
	}

	// precondition is that the move is valid
	public void makeMove(Square from, Square to) {
		Piece p = from.getPiece();
		
		// if the to square has a piece, remove it
		if (to.hasPiece()) {
			this.black.remove(to.getPiece());
			this.white.remove(to.getPiece());
			to.clearSquare();
		}
		
		// place the piece
		to.placePiece(p);
		
		// remove the piece from the old square
		from.clearSquare();
	}
	
	private boolean isValidMove(int x, int y, boolean isWhite) {
		if (!squares[x][y].hasPiece()) return true;
		if (squares[x][y].hasWhitePiece() != isWhite) return true;
		return false;
	}
}
