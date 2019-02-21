package main_package;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class BoardPanel extends JPanel {

	private Board board;
	private final char[] NUMBERS = new char[] { '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	private int indexOfNumber;
	private int[] coordinates;
	private boolean numberFlag = false;
	private final int FONTSIZE = 60;
	private final int OFFSET = FONTSIZE / 4;
	private final Font FONT = new Font("Arial", Font.BOLD, FONTSIZE);
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

				if (mouseXPos > board.getX() && mouseXPos < board.getWidth() + board.getX() && mouseYPos > board.getY()
						&& mouseYPos < board.getHeight() + board.getY()) {
					coordinates = board.getSqrCenter(mouseXPos, mouseYPos);
				} else {
					coordinates = new int[] { -1, -1 };
				}
			}
		};
	}

	private void paintNumber() {

		if (!board.cellPrefilled(coordinates) && coordinates[0] > board.getX() && coordinates[1] > board.getY()) {

			int cellIndex = board.getCellIndex(coordinates);
			if (board.checkValueValidity(cellIndex, NUMBERS[indexOfNumber])) {
				
				board.addValueToCell(cellIndex, NUMBERS[indexOfNumber]);
				numberFlag = true;
				// offsets are added so that the repaint area does not cover the cell borders.
				repaint(coordinates[0] - board.getCellWidth() / 2 + 10, coordinates[1] - board.getCellHeight() / 2 + 10,
						board.getCellWidth() - 30, board.getCellHeight() - 30);
				
				if (board.getEmptyCellCount() == 0) {
					//TODO add sudoku completion event
					System.out.println("Sudoku complete.");
				}
			}
		}
		
	}

	private void clearCell() {

		if (!board.cellPrefilled(coordinates)) {
			int cellIndex = board.getCellIndex(coordinates);
			board.removeValueFromCell(cellIndex);
			numberFlag = false;
			repaint(coordinates[0] - board.getCellWidth() / 2, coordinates[1] - board.getCellHeight() / 2,
					board.getCellWidth() - 1, board.getCellHeight() - 1);
		}
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
