import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Stack;

public class ChessBoard {
	public static final int BOARD_START = 0;
	public static final int BOARD_END = 7;
	
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

	public Square[][] getSquares() {
		return squares;
	}
	
	public Square getSquare(int x, int y, Square[][] boardState) {
		return boardState[x][y];
	}

	public ArrayList<Piece> getWhitePieces() {
		return white;
	}

	public ArrayList<Piece> getBlackPieces() {
		return black;
	}

	// clears the board and then places pieces in their starting positions
	public void setup() {
		this.clear(squares);
		int x, y;
		
		y = 7;
		//white back line
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
		//black back line
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
	
	// Finds all moves that are allowed by a piece from a specified square
	public ArrayList<Square> findLegalMoves(Square squareFrom, Square[][] boardState) throws ClassNotFoundException, IOException {
		ArrayList<Square> moves = findAllMoves(squareFrom, boardState, true);
		ArrayList<Square> toRemove = new ArrayList<Square>();
		for (Square squareTo : moves) {
			if (moveCausesCheck(squareFrom, squareTo, boardState)) {
				toRemove.add(squareTo);
			}
		}
		moves.removeAll(toRemove);
		return moves;
	}
	
	// Returns true if the current board state is in check
	public static boolean boardIsChecked(Square[][] board, Player toMove) {
		// Find all the pieces of the opposing color
		// Find the current space of the player's king
		ArrayList<Square> enemySquares = new ArrayList<Square>();
		Square kingSquare = null;
		for (Square[] col : board) {
			for (Square s : col) {
				if (s.getColor() != toMove && s.hasPiece()) enemySquares.add(s);
				if (s.hasPiece() && s.getColor() == toMove && s.getPieceType().equals("King")) kingSquare = s;
			}
		}
		// If any of the enemy pieces can hit the king, return true
		for (Square s : enemySquares) {
			if (findAllMoves(s, board, false).contains(kingSquare)) return true;
		}
		return false;
	}
	
	// Finds all moves possible from a piece (ignoring moves that are illegal due to check)
	public static ArrayList<Square> findAllMoves(Square squareFrom, Square[][] boardState, boolean checkCastling) {
		ArrayList<Square> possibleSquaresTo = new ArrayList<Square>();
		int x = squareFrom.getX();
		int y = squareFrom.getY();
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
			possibleSquaresTo.addAll(findKingMoves(x, y, boardState, checkCastling));
		}
		return possibleSquaresTo;
	}

	// Move a piece from one square to another
	// If the move is a castling move, the king and castle are moved simultaneously
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
		p.moveTo(to.getX(), to.getY());
		
		// if the piece is a pawn at the end of the board, promote it into a queen
		if (p.getType().equals("Pawn")) {
			if ((p.getY() == 7 && p.getColor().equals(Player.black)) || (p.getY() == 0 && p.getColor().equals(Player.white))) p.promote();
		}
		
		// remove the piece from the old square
		from.clearSquare();
		
		// if the move is a castling move, move the castle too
		if (p.getType().equals("King") && Math.abs(from.getX() - to.getX()) > 1) {
			int dir = -1;
			if (to.getX() > 4) dir = 1;
			
			dir *= -1;
			
			Square castleTo = squares[p.getX() + dir][p.getY()];
			Square castleFrom = squares[p.getX() - dir][p.getY()];
			Piece castle = castleFrom.getPiece();
			
			castleTo.placePiece(castle);
			castleFrom.clearSquare();
			castle.moveTo(castleTo.getX(), castleTo.getY());
			
		}
	}

	private static ArrayList<Square> findPawnMoves(int x, int y, Square[][] boardState) {
		Player currTurn = Player.white;
		if (boardState[x][y].getColor() == Player.black) currTurn = Player.black;
		
		ArrayList<Square> moves = new ArrayList<Square>();
		
		if (y == 7 || y == 0) return moves;
		
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
	private static ArrayList<Square> findCastleMoves(int x, int y, Square[][] boardState) {
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
	private static ArrayList<Square> findKnightMoves(int x, int y, Square[][] boardState) {
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
	private static ArrayList<Square> findBishopMoves(int x, int y, Square[][] boardState) {
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
	private static ArrayList<Square> findQueenMoves(int x, int y, Square[][] boardState) {
		ArrayList<Square> moves = new ArrayList<Square>();
		moves.addAll(findCastleMoves(x, y, boardState));
		moves.addAll(findBishopMoves(x, y, boardState));
		return moves;
	}
	private static ArrayList<Square> findKingMoves(int x, int y, Square[][] boardState, boolean checkCastling) {
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
			}
		}	
		
		// Check for availability of castling
		// spaces between king and castle must be free
		// king and castle in question must both have hasMoved() return false
		
		// if king hasn't moved
		if (!boardState[x][y].getPiece().hasMoved() && checkCastling) {
			if (currTurn == Player.white) {
				// check left castle
				if (!boardState[0][7].getPiece().hasMoved()) {
					int[][] spaces = {{1,7},{2,7},{3,7}};
					if (spacesAreEmpty(boardState, spaces) && !spacesAreThreatened(boardState, spaces, currTurn)) moves.add(boardState[1][7]);
				}
				// check right castle
				if (!boardState[7][7].getPiece().hasMoved()) {
					int[][] spaces = {{5,7},{6,7}};
					if (spacesAreEmpty(boardState, spaces) && !spacesAreThreatened(boardState, spaces, currTurn)) moves.add(boardState[6][7]);
				}
			} else {
				// check left castle
				if (!boardState[0][0].getPiece().hasMoved()) {
					int[][] spaces = {{1,0},{2,0},{3,0}};
					if (spacesAreEmpty(boardState, spaces) && !spacesAreThreatened(boardState, spaces, currTurn)) moves.add(boardState[1][0]);
				}
				// check right castle
				if (!boardState[7][0].getPiece().hasMoved()) {
					int[][] spaces = {{5,0},{6,0}};
					if (spacesAreEmpty(boardState, spaces) && !spacesAreThreatened(boardState, spaces, currTurn)) moves.add(boardState[6][0]);
				}
			}
		}
		
		return moves;
	}


	private static boolean spacesAreThreatened(Square[][] boardState, int[][] checkCoords, Player toMove) {
		
		ArrayList<Square> enemySquares = new ArrayList<Square>();
		ArrayList<Square> checkSquares = new ArrayList<Square>();
		
		// Getting the squares that must be safe
		for (int[] coords : checkCoords) {
			checkSquares.add(boardState[coords[0]][coords[1]]);
		}
		
		// Getting the squares the enemy pieces are on
		for (Square[] col : boardState) {
			for (Square s : col) {
				if (s.getColor() != toMove && s.hasPiece()) enemySquares.add(s);
			}
		}
		
		// if any of the enemy pieces can hit a checkSquare, return true
		for (Square s : enemySquares) {
			for (Square t : checkSquares) {
				if (findAllMoves(s, boardState, false).contains(t)) return true;
			}
		}
		return false;
	}

	// finds moves that are illegal due to ending in check
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
	    
	    Piece p = cloneFrom.getPiece();
	    cloneTo.placePiece(p);
	    cloneFrom.clearSquare();
	    
	    return boardIsChecked(copy, color);
	}
	private static boolean spacesAreEmpty(Square[][] board, int[][] coords) {
		for (int[] coord : coords) {
			if (board[coord[0]][coord[1]].hasPiece()) return false;
		}
		return true;
	}

	public void printBoard(Square[][] board) {
		for (Square[] col : board) {
			for (Square s : col) {
				if (!s.hasPiece()) {
					System.out.print("#");
					continue;
				}
				if (s.getColor() == Player.white) System.out.print("O");
				else System.out.print("X");
			}
			System.out.println();
		}
	}
	
	
}
