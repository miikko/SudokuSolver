package main_package;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class BoardPanel extends JPanel {
	Square redSquare = new Square(Color.red, 10, 10, 90, 90);
	Board board;
	final char[] NUMBERS = new char[] { '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	int indexOfNumber;
	int[] coordinates;
	boolean numberFlag = false;
	final int FONTSIZE = 60;
	final int OFFSET = FONTSIZE / 4;
	final Font FONT = new Font("Arial", Font.BOLD, FONTSIZE);
	private int boardLoadCounter;
	private MouseAdapter mouseAdapter;

	public BoardPanel(Board board) {

		setBorder(BorderFactory.createLineBorder(Color.black));
		this.board = board;

		createMouseAdapter();
		addMouseMotionListener(mouseAdapter);

		addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char ch = e.getKeyChar();
				for (int i = 0; i < NUMBERS.length; i++) {
					if (ch == NUMBERS[i]) {
						indexOfNumber = i;
						paintNumber();
						break;
					} else if (ch == '\b') {
						clearCell();
					}
				}
			}
		});

	}

	private void createMouseAdapter() {
		mouseAdapter = new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {
				int mouseXPos = e.getX();
				int mouseYPos = e.getY();

				if (mouseXPos > 10 && mouseXPos < 910 && mouseYPos > 10 && mouseYPos < 910) {
					coordinates = board.getSqrCenter(mouseXPos, mouseYPos);
				}
			}
		};
	}

	private void paintNumber() {

		if (!board.cellPrefilled(coordinates)) {
			numberFlag = true;
			repaint(coordinates[0] - board.getCellWidth() / 2, coordinates[1] - board.getCellHeight() / 2,
					board.getCellWidth() - 1, board.getCellHeight() - 1);
		}
	}

	private void clearCell() {

		if (!board.cellPrefilled(coordinates)) {
			numberFlag = false;
			repaint(coordinates[0] - board.getCellWidth() / 2, coordinates[1] - board.getCellHeight() / 2,
					board.getCellWidth() - 1, board.getCellHeight() - 1);
		}
	}

	public Dimension getPreferredSize() {
		return new Dimension(920, 920);
	}

	public void paintComponent(Graphics g) {
		// Let UI Delegate paint first, which
		// includes background filling since
		// this component is opaque.
		super.paintComponent(g);
		g.setFont(FONT);

		if (boardLoadCounter < 2) {
			board.paintBoard(g);
			g.setColor(new Color(244, 164, 96));
			board.fillCells(g);
			g.setColor(Color.BLACK);
		} else {
			board.paintCell(g, coordinates[0], coordinates[1]);
		}

		if (numberFlag) {
			g.drawChars(NUMBERS, indexOfNumber, 1, coordinates[0] - OFFSET, coordinates[1] + OFFSET);
			numberFlag = false;
		}

		boardLoadCounter++;
	}
}
