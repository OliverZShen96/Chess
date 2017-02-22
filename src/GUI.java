import java.awt.*;
import java.io.IOException;
import javax.swing.*;

public class GUI extends JFrame {
	JPanel components = new JPanel(new GridLayout());

	public static void main(String[] args) throws IOException {
		new GUI();
	}
	public GUI() {
		super("GUI");
		
		// create new instance of chessboard and set up pieces
		ChessBoard b = new ChessBoard();
		
		// create the ChessPanel and add it to the components to be drawn
		components.add(new ChessPanel(b));
		this.add(components);
		
		this.setSize(600, 600);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);	
	}
}
