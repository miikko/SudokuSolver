package main_package;

import javax.swing.SwingUtilities;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

public class Main {

	public static final int WINDOWHEIGHT = 1000;
	public static final int WINDOWWIDTH = 1200;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	private static void createAndShowGUI() {
		System.out.println("Created GUI on EDT? " + SwingUtilities.isEventDispatchThread());
		JFrame f = new JFrame("Sudoku");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Grid grid = new Grid();
		Board board = new Board(Color.GRAY, 900, 900, grid);
		BoardPanel boardPanel = new BoardPanel(board);
		boardPanel.setFocusable(true);
		JLabel keyBinds = new JLabel("To add a number to a specific cell, move your cursor on top of the cell and press the desired number key on the keyboard.");
		keyBinds.setFont(new Font("Arial", Font.BOLD, 20));
		JLayeredPane containerPane = new JLayeredPane();
		containerPane.setPreferredSize(new Dimension(WINDOWWIDTH, WINDOWHEIGHT));
		containerPane.setLayout(new BorderLayout());
		containerPane.add(boardPanel, BorderLayout.CENTER);
		containerPane.add(keyBinds, BorderLayout.NORTH);

		f.add(containerPane);
		f.pack();
		f.setVisible(true);
	}

}
