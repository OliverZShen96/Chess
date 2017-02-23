import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

public class ChessPanel extends JPanel implements MouseListener {
	private static int SQUARE_SIZE = 64;
	private ChessBoard board;
	private Square clickedSquare;
	private ArrayList<Square> possibleMoves;
	private boolean isWhiteTurn;
	
	public ChessPanel(ChessBoard board) {
		super();
		this.addMouseListener(this);
		this.board = board;
		this.clickedSquare = null;
		this.possibleMoves = new ArrayList<Square>();
		this.isWhiteTurn = true;
	}
	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.paintBoard(g);
        this.paintPieces(g);
	}
	
	private void paintPieces(Graphics g) {
		for (Piece p : board.getBlackPieces()) {
			paintPiece(g, p.getType(), false, SQUARE_SIZE*p.getX(), SQUARE_SIZE*p.getY());
		}
		
		for (Piece p : board.getWhitePieces()) {
			paintPiece(g, p.getType(), true, SQUARE_SIZE*p.getX(), SQUARE_SIZE*p.getY());
		}
	}
	private void paintPiece(Graphics g, String pieceType, boolean isWhite, int x, int y) {
		String color = "";
		if (isWhite) color = "white";
		if (!isWhite) color = "black";
		try {
			g.drawImage(ImageIO.read(new File("images/" + color + "_" + pieceType.toLowerCase() + ".png")), x, y, SQUARE_SIZE, SQUARE_SIZE, null);
		} catch (IOException e) {
			System.out.println("images/" + color + "_" + pieceType.toLowerCase() + ".png not found");
		}
	}
	
	private void paintBoard(Graphics g) {
		g.setColor(Color.WHITE);
        int x, y;
        for (x = 0; x <= 7; x++) {
        	for (y = 0; y <= 7; y++) {
        		drawSquare(x, y, g);
        	}
        }
	}
	
	private void drawSquare(int x, int y, Graphics g) {
		if ((x+y)%2 == 0) g.setColor(Color.WHITE);
		if ((x+y)%2 == 1) g.setColor(Color.GRAY);
		if (clickedSquare != null) {
			if (board.getSquare(x, y, board.getSquares()) == clickedSquare) g.setColor(new Color(160,40,200));
			if (possibleMoves.contains(board.getSquare(x, y, board.getSquares()))) {
				if ((x+y)%2 == 0) g.setColor(new Color(150,200,200));
				if ((x+y)%2 == 1) g.setColor(new Color(80,100,200));
			}
		}
		g.fillRect(SQUARE_SIZE*x, SQUARE_SIZE*y, SQUARE_SIZE, SQUARE_SIZE);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	// on mouse press, find the pressed square
	// if the move is currently highlighted, make the move
	// if not, get all possible moves from that square
	public void mousePressed(MouseEvent e) {	
		Square oldSquare = this.clickedSquare;
		this.clickedSquare = board.getSquare(e.getX()/64, e.getY()/64, board.getSquares());
		
		if (this.possibleMoves.contains(clickedSquare)) {
			this.board.makeMove(oldSquare, clickedSquare);
			this.isWhiteTurn = !isWhiteTurn;
			this.possibleMoves.clear();
		} else {
			this.possibleMoves.clear();
			if (clickedSquare.getPieceType() != null) {
				if (clickedSquare.hasWhitePiece() == this.isWhiteTurn) {
					try {
						this.possibleMoves = board.findLegalMoves(clickedSquare, board.getSquares());
					} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {	
	}
}
