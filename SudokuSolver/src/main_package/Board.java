package main_package;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Board extends Square {
	private int xPos;
	private int yPos;
	final char[] NUMBERS = new char[] { '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	final int FONTSIZE = 60;
	final int OFFSET = FONTSIZE / 4;
	private Square[] cells = new Square[81];
	private int cellWidth;
	private int cellHeight;
	private Grid grid;
	private List<int[]> prefilledCellCenters;

	public Board(Color c, int width, int height, Grid grid) {
		super(c, width, height);
		this.grid = grid;
		cellWidth = width / 9;
		cellHeight = height / 9;
		xPos = (Main.WINDOWWIDTH - width) / 2;
		yPos = (Main.WINDOWHEIGHT - height) / 2;
		setX(xPos);
		setY(yPos);
	}

	public int getCellWidth() {
		return cellWidth;
	}

	public int getCellHeight() {
		return cellHeight;
	}

	// Paints white/gray cells on top of the black game board
	public void paintBoard(Graphics g) {

		int cellXPos = xPos;
		int cellYPos = yPos;
		Color cellColor;

		for (int i = 0; i < cells.length; i++) {
			if (i % 9 == 0 && i != 0) {
				cellXPos = xPos;
				cellYPos += cellHeight;
			}

			Square cell = new Square(Color.BLACK, cellXPos, cellYPos, cellWidth, cellHeight);
			cells[i] = cell;
			cellXPos += cellWidth;
		}
		
		for (int i = 0; i < cells.length; i++) {
			cellColor = calculateCellColor(cells[i].getX(), cells[i].getY());
			cells[i].setColor(cellColor);
			cells[i].paintSquare(g, cellColor);
		}
		
		System.out.println("Board painted");
	}

	public void paintCell(Graphics g, int cellXPos, int cellYPos) {
		Color cellColor = calculateCellColor(cellXPos, cellYPos);
		paintSquare(g, cellColor);
	}

	public int[] getSqrCenter(int x, int y) {
		int sqrXPos = (int) (Math.ceil((double) (x - xPos) / cellWidth + 0.01)) * cellWidth - (cellWidth / 2 - xPos);
		int sqrYPos = (int) (Math.ceil((double) (y - yPos) / cellHeight + 0.01)) * cellHeight - (cellHeight / 2 - yPos);
		return new int[] { sqrXPos, sqrYPos };
	}

	public void fillCells(Graphics g) {

		prefilledCellCenters = new ArrayList<>();
		char[] initialState = grid.getGridState();

		for (int i = 0; i < cells.length; i++) {

			if (Character.isDigit(initialState[i])) {

				int[] cellCenter = getSqrCenter(cells[i].getX(), cells[i].getY());
				prefilledCellCenters.add(cellCenter);
				g.drawChars(initialState, i, 1, cellCenter[0] - OFFSET, cellCenter[1] + OFFSET);
			}
		}
	}

	public int getOffset() {
		return OFFSET;
	}

	private Color calculateCellColor(int cellXPos, int cellYPos) {
		Color cellColor = null;
		
		try {
			int cellIndex = getCellIndex(new int[] { cellXPos, cellYPos });
			int boxNum = grid.getBoxNum(cellIndex);
			if (boxNum % 2 == 0) {
				cellColor = Color.white;
			} else {
				cellColor = Color.gray;
			}
			
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Failed to calculate cell color. x: " + cellXPos + ", y: " + cellYPos);
			e.printStackTrace();
		} catch (IllegalArgumentException i) {
			System.out.println("Failed to calculate cell color. x: " + cellXPos + ", y: " + cellYPos);
			i.printStackTrace();
		}
		return cellColor;
	}

	public boolean cellPrefilled(int[] coordinates) {
		for (int[] cellCenter : prefilledCellCenters) {
			if (cellCenter[0] == coordinates[0] && cellCenter[1] == coordinates[1]) {
				return true;
			}
		}
		return false;
	}

	public int getCellIndex(int[] coordinates) throws IllegalArgumentException {

		for (int i = 0; i < cells.length; i++) {
			
			if (cells[i] != null && coordinates[0] < cells[i].getX() + cells[i].getWidth()
					&& coordinates[1] < cells[i].getY() + cells[i].getHeight()) {
				return i;
			}

		}
		System.out.println("Tried to get cell index with coordinates that were not on the board.");
		throw new IllegalArgumentException();
	}


	public boolean checkValueValidity(int index, char value) {
		if (grid.validate(value, index)) {
			return true;
		} else {
			return false;
		}
	}

	public void addValueToCell(int index, char value) {
		grid.putValueToArrays(value, index);
	}

	public void removeValueFromCell(int index) {
		grid.clearCellValueFromArrays(index);
	}

	public int getEmptyCellCount() {
		char[] gridState = grid.getGridState();
		int emptyCellCount = 0;

		for (int i = 0; i < gridState.length; i++) {
			if (!Character.isDigit(gridState[i])) {
				emptyCellCount++;
			}
		}

		return emptyCellCount;
	}
}
