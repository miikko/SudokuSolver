package main_package;

import javax.swing.SwingUtilities;

import java.awt.Color;
import javax.swing.JFrame;

public class Main {
	// TODO: Create Grid class that stores cells and their contents. The class can
	// check what numbers are valid for each cell.
	// Change the Squares paintSquare()-method so that it doesnt paint
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	private static void createAndShowGUI() {
		System.out.println("Created GUI on EDT? " + SwingUtilities.isEventDispatchThread());
		JFrame f = new JFrame("Swing Paint Demo");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Grid grid = new Grid();
		Board board = new Board(Color.GRAY, 900, 900, grid);
		BoardPanel boardPanel = new BoardPanel(board);
		boardPanel.setFocusable(true);

		f.add(boardPanel);
		f.pack();
		f.setVisible(true);
	}

}
