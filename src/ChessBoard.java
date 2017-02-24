import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;

public class ChessBoard {
	private ArrayList<Piece> white;
	private ArrayList<Piece> black;
	private Square[][] squares;
	
	public Square[][] getSquares() {
		return squares;
	}
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
	
	public boolean whiteIsChecked(Square[][] boardState) {
		Square kingSquare = null;
		for (Piece p : white) {
			if (p.getType().equals("King")) {
				kingSquare = boardState[p.getX()][p.getY()];
			}
		}
		
		for (Piece p : black) {
			ArrayList<Square> moves = this.findAllMoves(boardState[p.getX()][p.getY()], boardState);
			if (moves.contains(kingSquare))  {
				return true;
			}
		}
		return false;
	}
	
	public boolean blackIsChecked(Square[][] boardState) {
		Square kingSquare = null;
		for (Piece p : black) {
			if (p.getType().equals("King")) {
				kingSquare = boardState[p.getX()][p.getY()];
			}
		}
		
		for (Piece p : white) {
			ArrayList<Square> moves = this.findAllMoves(boardState[p.getX()][p.getY()], boardState);
			if (moves.contains(kingSquare)) {
				return true;
			}
		}
		return false;
	}
	
	// removes all pieces from the board
	public void clear(Square[][] boardState) {
		
		for(int x = 0; x <= 7; x++) {
			for(int y = 0; x <= 7; x++) {
				boardState[x][y].clearSquare();
			}
		}
		// remove all pieces from white and black
		white.clear();
		black.clear();
	}
	
	// places pieces in their starting positions
	public void setup() {
		this.clear(squares);
		int x, y;
		
		y = 7;
		//white backline
		for (x = 0; x <= 7; x++) {
			Piece p = null;
			if (x == 0 || x == 7) {
				p = new Piece(x, y, "Castle", Player.white);
			} else if (x == 1 || x == 6) {
				p = new Piece(x, y, "Knight", Player.white);
			} else if (x == 2 || x == 5) {
				p = new Piece(x, y, "Bishop", Player.white);
			} else if (x == 3) {
				p = new Piece(x, y, "Queen", Player.white);
			} else if (x == 4) {
				p = new Piece(x, y, "King", Player.white);
			}
			this.white.add(p);
			squares[x][y].placePiece(p);
		}
		
		y = 6;
		//white pawns
		for (x = 0; x <= 7; x++) {
			Piece p = new Piece(x,y, "Pawn", Player.white);
			this.white.add(p);
			squares[x][y].placePiece(p);
		}
		
		y = 0;
		//black backline
		for (x = 0; x <= 7; x++) {
			Piece p = null;
			if (x == 0 || x == 7) {
				p = new Piece(x, y, "Castle", Player.black);
			} else if (x == 1 || x == 6) {
				p = new Piece(x, y, "Knight", Player.black);
			} else if (x == 2 || x == 5) {
				p = new Piece(x, y, "Bishop", Player.black);
			} else if (x == 3) {
				p = new Piece(x, y, "Queen", Player.black);
			} else if (x == 4) {
				p = new Piece(x, y, "King", Player.black);
			}
			this.black.add(p);
			squares[x][y].placePiece(p);
		}
		
		y = 1;
		//black pawns
		for (x = 0; x <= 7; x++) {
			Piece p = new Piece(x,y, "Pawn", Player.black);
			this.black.add(p);
			squares[x][y].placePiece(p);
		}
	}
	
	public ArrayList<Piece> getWhitePieces() {
		return white;
	}
	public ArrayList<Piece> getBlackPieces() {
		return black;
	}
	
	public Square getSquare(int x, int y, Square[][] boardState) {
		return boardState[x][y];
	}

	public ArrayList<Square> findLegalMoves(Square squareFrom, Square[][] boardState) throws ClassNotFoundException, IOException {
		ArrayList<Square> moves = findAllMoves(squareFrom, boardState);
		/*ArrayList<Square> toRemove = new ArrayList<Square>();
		for (Square squareTo : moves) {
			if (moveCausesCheck(squareFrom, squareTo, boardState)) {
				System.out.println("Move to " + squareTo.getX() + " " + squareTo.getY() + " causes check");
				toRemove.add(squareTo);
			}
		}
		moves.removeAll(toRemove);*/
		return moves;
	}
	
	// finds moves that are illegal due to ending in check
	// NEED TO FIX THIS
	private boolean moveCausesCheck(Square squareFrom, Square squareTo, Square[][] boardState) throws IOException, ClassNotFoundException {
		Player color = squareFrom.getColor();
		
		// deep clone current game state
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    ObjectOutputStream oos = new ObjectOutputStream(baos);
	    oos.writeObject((Object)boardState);
	    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
	    ObjectInputStream ois = new ObjectInputStream(bais);
	    Square[][] copy = (Square[][]) ois.readObject();
	    
	    // get the from and to squares in the clone
	    int xf = squareFrom.getX();
	    int yf = squareFrom.getY();
	    int xt = squareTo.getX();
	    int yt = squareTo.getY();
	    
	    Square cloneFrom = copy[xf][yf];
	    Square cloneTo = copy[xt][yt];
	    
	    // make the move in the copy
	    Piece p = cloneFrom.getPiece();
	    cloneTo.placePiece(p);
	    cloneFrom.clearSquare();
	    
	    if (color == Player.white) return (whiteIsChecked(copy));
	    else return (blackIsChecked(copy));
	}

	public ArrayList<Square> findAllMoves(Square squareFrom, Square[][] boardState) {
		ArrayList<Square> possibleSquaresTo = new ArrayList<Square>();
		int x = squareFrom.getX();
		int y = squareFrom.getY();
		Player color = squareFrom.getColor();
		
		if (squareFrom.getPieceType().equals("Pawn")) {
			possibleSquaresTo.addAll(findPawnMoves(x, y, boardState));
		} else if (squareFrom.getPieceType().equals("Castle")) {
			possibleSquaresTo.addAll(findCastleMoves(x, y, boardState));
		} else if (squareFrom.getPieceType().equals("Knight")) {
			possibleSquaresTo.addAll(findKnightMoves(x, y, boardState));
		} else if (squareFrom.getPieceType().equals("Bishop")) {
			possibleSquaresTo.addAll(findBishopMoves(x, y, boardState));
		} else if (squareFrom.getPieceType().equals("Queen")) {
			possibleSquaresTo.addAll(findQueenMoves(x, y, boardState));
		} else if (squareFrom.getPieceType().equals("King")) {
			possibleSquaresTo.addAll(findKingMoves(x, y, boardState));
		}
		return possibleSquaresTo;
	}

	private ArrayList<Square> findPawnMoves(int x, int y, Square[][] boardState) {
		Player currTurn = Player.white;
		if (boardState[x][y].getColor() == Player.black) currTurn = Player.black;
		
		ArrayList<Square> moves = new ArrayList<Square>();
		int yDir = 1;
		if (currTurn == Player.white) yDir = -1;
		
		// first space in front
		if (!boardState[x][y+yDir].hasPiece()) {
			moves.add(boardState[x][y+yDir]);
			
			// extra space if first move
			if ((currTurn == Player.white && y == 6 || currTurn == Player.black && y == 1)) {
				if (!boardState[x][y+yDir*2].hasPiece()) moves.add(boardState[x][y+yDir*2]);
			}
		}	
		// capturing
		if (x < 7) {
			if (boardState[x+1][y+yDir].hasPiece() && boardState[x+1][y+yDir].getColor() != currTurn) {
				moves.add(boardState[x+1][y+yDir]);
			}
		}
		if (x > 0) {
			if (boardState[x-1][y+yDir].hasPiece() && boardState[x-1][y+yDir].getColor() != currTurn) {
				moves.add(boardState[x-1][y+yDir]);
			}
		}
		return moves;
	}
	private ArrayList<Square> findCastleMoves(int x, int y, Square[][] boardState) {
		Player currTurn = Player.white;
		if (boardState[x][y].getColor() == Player.black) currTurn = Player.black;
		
		ArrayList<Square> moves = new ArrayList<Square>();
		int xVal, yVal;
		for (xVal = x+1, yVal = y; xVal <= 7; xVal++) {
			if (boardState[xVal][yVal].hasPiece()) {
				if (boardState[xVal][yVal].getColor() == currTurn) break;
			}
			moves.add(boardState[xVal][yVal]);
			if (boardState[xVal][yVal].hasPiece()) break;
		}
		for (xVal = x-1, yVal = y; xVal >= 0; xVal--) {
			if (boardState[xVal][yVal].hasPiece()) {
				if (boardState[xVal][yVal].getColor() == currTurn) break;
			}
			moves.add(boardState[xVal][yVal]);
			if (boardState[xVal][yVal].hasPiece()) break;
		}
		for (xVal = x, yVal = y+1; yVal <= 7; yVal++) {
			if (boardState[xVal][yVal].hasPiece()) {
				if (boardState[xVal][yVal].getColor() == currTurn) break;
			}
			moves.add(boardState[xVal][yVal]);
			if (boardState[xVal][yVal].hasPiece()) break;
		}
		for (xVal = x, yVal = y-1; yVal >= 0; yVal--) {
			if (boardState[xVal][yVal].hasPiece()) {
				if (boardState[xVal][yVal].getColor() == currTurn) break;
			}
			moves.add(boardState[xVal][yVal]);
			if (boardState[xVal][yVal].hasPiece()) break;
		}
		return moves;
	}
	private ArrayList<Square> findKnightMoves(int x, int y, Square[][] boardState) {
		Player currTurn = Player.white;
		if (boardState[x][y].getColor() == Player.black) currTurn = Player.black;
		
		ArrayList<Square> moves = new ArrayList<Square>();
		int xBuff;
		int yBuff;
		for (xBuff = -2; xBuff <= 2; xBuff++) {
			if (xBuff == 0) continue;
			yBuff = 2;
			if (xBuff%2 == 0) yBuff = 1;
			
			if (xBuff + x >= 0 && xBuff + x <= 7) {
				if (y + yBuff >= 0 && y + yBuff <= 7) {
					if (!boardState[xBuff + x][yBuff + y].hasPiece() || boardState[xBuff + x][yBuff + y].getColor() != currTurn) moves.add(boardState[xBuff+x][y+yBuff]);
				}
				if (y - yBuff >= 0 && y - yBuff <= 7) {
					if (!boardState[xBuff + x][y - yBuff].hasPiece() || boardState[xBuff + x][y - yBuff].getColor() != currTurn) moves.add(boardState[xBuff+x][y-yBuff]);
				}
			}
		}
		return moves;
	}
	private ArrayList<Square> findBishopMoves(int x, int y, Square[][] boardState) {
		Player currTurn = Player.white;
		if (boardState[x][y].getColor() == Player.black) currTurn = Player.black;
		
		ArrayList<Square> moves = new ArrayList<Square>();
		int xVal, yVal;
		for (xVal = x+1, yVal = y+1; xVal <= 7 && yVal <= 7; xVal++, yVal++) {
			if (boardState[xVal][yVal].hasPiece()) {
				if (boardState[xVal][yVal].getColor() == currTurn) break;
			}
			moves.add(boardState[xVal][yVal]);
			if (boardState[xVal][yVal].hasPiece()) break;
		}
		for (xVal = x+1, yVal = y-1; xVal <= 7 && yVal >= 0; xVal++, yVal--) {
			if (boardState[xVal][yVal].hasPiece()) {
				if (boardState[xVal][yVal].getColor() == currTurn) break;
			}
			moves.add(boardState[xVal][yVal]);
			if (boardState[xVal][yVal].hasPiece()) break;
		}
		for (xVal = x-1, yVal = y-1; xVal >= 0 && yVal >= 0; xVal--, yVal--) {
			if (boardState[xVal][yVal].hasPiece()) {
				if (boardState[xVal][yVal].getColor() == currTurn) break;
			}
			moves.add(boardState[xVal][yVal]);
			if (boardState[xVal][yVal].hasPiece()) break;
		}
		for (xVal = x-1, yVal = y+1; xVal >= 0 && yVal <= 7; xVal--, yVal++) {
			if (boardState[xVal][yVal].hasPiece()) {
				if (boardState[xVal][yVal].getColor() == currTurn) break;
			}
			moves.add(boardState[xVal][yVal]);
			if (boardState[xVal][yVal].hasPiece()) break;
		}
		return moves;
	}
	private ArrayList<Square> findQueenMoves(int x, int y, Square[][] boardState) {
		ArrayList<Square> moves = new ArrayList<Square>();
		moves.addAll(findCastleMoves(x, y, boardState));
		moves.addAll(findBishopMoves(x, y, boardState));
		return moves;
	}
	private ArrayList<Square> findKingMoves(int x, int y, Square[][] boardState) {
		Player currTurn = Player.white;
		if (boardState[x][y].getColor() == Player.black) currTurn = Player.black;
		
		ArrayList<Square> moves = new ArrayList<Square>();
		for (int xVal = x-1; xVal <= x+1; xVal++) {
			for (int yVal = y-1; yVal <= y+1; yVal++) {
				if (xVal == x && yVal == y) continue;
				if (xVal > 7 || xVal < 0 || yVal > 7 || yVal < 0) continue;
				if (!boardState[xVal][yVal].hasPiece() || boardState[xVal][yVal].getColor() != currTurn) {
					moves.add(boardState[xVal][yVal]);
				}
				System.out.println(xVal + " " + yVal);
			}
		}	
		return moves;
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
	
	// checks if the proposed move already has a piece in it of the player's own color
	private boolean squareIsOccupied(int x, int y, Player color, Square[][] boardState) {
		if (!boardState[x][y].hasPiece()) return true;
		if (boardState[x][y].getColor() != color) return true;
		return false;
	}
}
