import java.awt.*;
import java.awt.image.*;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class ChessPanel extends JPanel {
	private static int SQUARE_SIZE = 64;
	private ChessBoard board;
	
	public ChessPanel(ChessBoard board) {
		super();
		this.board = board;
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
        for (x = 0; x <= 7; x+=2) {
        	for (y = 0; y <= 7; y+=2) {
        		g.fillRect(SQUARE_SIZE*x, SQUARE_SIZE*y, SQUARE_SIZE, SQUARE_SIZE);
        	}
        }
        for (x = 1; x <= 7; x+=2) {
        	for (y = 1; y <= 7; y+=2) {
        		g.fillRect(SQUARE_SIZE*x, SQUARE_SIZE*y, SQUARE_SIZE, SQUARE_SIZE);
        	}
        }
        
        g.setColor(Color.GRAY);
        for (x = 1; x <= 7; x+=2) {
        	for (y = 0; y <= 7; y+=2) {
        		g.fillRect(SQUARE_SIZE*x, SQUARE_SIZE*y, SQUARE_SIZE, SQUARE_SIZE);
        	}
        }
        for (x = 0; x <= 7; x+=2) {
        	for (y = 1; y <= 7; y+=2) {
        		g.fillRect(SQUARE_SIZE*x, SQUARE_SIZE*y, SQUARE_SIZE, SQUARE_SIZE);
        	}
        }
	}
}
