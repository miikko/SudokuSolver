package main_package;

import java.awt.Color;
import java.awt.Graphics;

public class Board extends Square {
	private final int XPOS = 10;
	private final int YPOS = 10;
	final char[] NUMBERS = new char[] { '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	final int FONTSIZE = 60;
	final int OFFSET = FONTSIZE / 4;
	private Square[] cells = new Square[81];
	private int cellWidth;
	private int cellHeight;
	private Grid grid;

	public Board(Color c, int width, int height, Grid grid) {
		super(c, width, height);
		this.grid = grid;
		cellWidth = width / 9;
		cellHeight = height / 9;
		setX(XPOS);
		setY(YPOS);
	}

	public int getCellWidth() {
		return cellWidth;
	}

	public int getCellHeight() {
		return cellHeight;
	}

	public void paintBoard(Graphics g) {

		int cellXPos = XPOS;
		int cellYPos = YPOS;
		Color cellColor;

		for (int i = 0; i < 81; i++) {
			// Painting white/gray cells on top of the black game board

			if (i % 9 == 0 && i != 0) {
				cellXPos = 10;
				cellYPos += cellHeight;
			}

			cellColor = calculateCellColor(cellXPos, cellYPos);

			Square cell = new Square(cellColor, cellXPos, cellYPos, cellWidth, cellHeight);
			cell.paintSquare(g, cellColor);
			Grid.addCellToGrid(cell, i);
			cells[i] = cell;
			cellXPos += cellWidth;
		}

	}

	public void paintCell(Graphics g, int cellXPos, int cellYPos) {
		Color cellColor = calculateCellColor(cellXPos, cellYPos);
		paintSquare(g, cellColor);
	}

	public int[] getSqrCenter(int x, int y) {
		int sqrXPos = (int) (Math.ceil((double) (x - 10) / cellWidth + 0.01)) * cellWidth - (cellWidth / 2 - 10);
		int sqrYPos = (int) (Math.ceil((double) (y - 10) / cellHeight + 0.01)) * cellHeight - (cellHeight / 2 - 10);
		return new int[] { sqrXPos, sqrYPos };
	}

	public void fillCells(Graphics g) {
		char[] initialState = grid.getInitialState();
		for (int i = 0; i < cells.length; i++) {
			if (Character.isDigit(initialState[i])) {
				int[] cellCenter = getSqrCenter(cells[i].getX(), cells[i].getY());
				g.drawChars(initialState, i, 1, cellCenter[0] - OFFSET, cellCenter[1] + OFFSET);
			}
		}
	}

	public int getOffset() {
		return OFFSET;
	}

	private Color calculateCellColor(int cellXPos, int cellYPos) {
		Color cellColor;

		if ((cellXPos >= cellWidth * 3 && cellXPos <= cellWidth * 6
				&& (cellYPos <= cellHeight * 3 || cellYPos > cellHeight * 6))
				|| (cellYPos >= cellHeight * 3 && cellYPos <= cellHeight * 6
						&& (cellXPos <= cellWidth * 3 || cellXPos >= cellWidth * 6))) {
			cellColor = Color.gray;
		} else {
			cellColor = Color.white;
		}

		return cellColor;
	}
}
