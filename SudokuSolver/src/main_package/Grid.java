package main_package;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Grid {

	public static Square[] cells;
	private final char[] NUMBERS = new char[] { '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	private char[][] cellRowValues;
	private char[][] cellColumnValues;
	private char[][] cellBoxValues;
	private char[] initialState;
	private Random random;
	private SolvingMethods sMethods;

	public Grid() {
		cells = new Square[81];
		cellRowValues = new char[9][9];
		cellColumnValues = new char[9][9];
		cellBoxValues = new char[9][9];
		random = new Random();
		sMethods = new SolvingMethods();
		initialState = createSudoku();
	}

	public static void addCellToGrid(Square cell, int index) {
		if (index < 81) {
			cells[index] = cell;
		}
	}

	private char[] createSudoku() {

		char[] initialCellValues = new char[cells.length];
		int index = 0;
		List<Character> invalidCellValues = new ArrayList<>();

		// fills cells one row at a time
		for (int i = 0; i < 9; i++) {

			for (int j = 0; j < 9; j++) {

				char value = NUMBERS[random.nextInt(9)];
				while (invalidCellValues.contains(value)) {
					value = NUMBERS[random.nextInt(9)];
				}

				if (validate(value, i, j)) {

					initialCellValues[index] = value;
					cellRowValues[i][j] = value;
					cellColumnValues[j][i] = value;

					int[] cellBoxCoordinates = getCellBoxCoordinates(i, j);
					cellBoxValues[cellBoxCoordinates[0]][cellBoxCoordinates[1]] = value;

					index++;
					invalidCellValues.clear();

				} else {
					j--;
					invalidCellValues.add(value);

					// If there are no valid values for this cell, the program restarts the process
					if (invalidCellValues.size() == NUMBERS.length) {

						cellRowValues = new char[9][9];
						cellColumnValues = new char[9][9];
						cellBoxValues = new char[9][9];
						initialCellValues = new char[cells.length];
						invalidCellValues.clear();
						i = -1;
						j = 8;
						index = 0;

					}
				}
			}
		}

		// Grid is filled, now take out values
		removeValuePairsFromGrid(initialCellValues);
		
		return initialCellValues;
	}

	// Checks if the given value is valid for the given cell
	// Assumes that the index specified cell is empty
	private boolean validate(char value, int rowNumber, int columnNumber) {

		for (int i = 0; i < 9; i++) {
			if (cellRowValues[rowNumber][i] == value) {
				return false;
			}
		}

		for (int i = 0; i < 9; i++) {
			if (cellColumnValues[columnNumber][i] == value) {
				return false;
			}
		}

		int[] cellBoxCoordinates = getCellBoxCoordinates(rowNumber, columnNumber);
		for (int i = 0; i < 9; i++) {
			if (cellBoxValues[cellBoxCoordinates[0]][i] == value) {
				return false;
			}
		}

		return true;
	}

	private int[] getCellBoxCoordinates(int rowNumber, int columnNumber) {
		int boxNum = 0;
		int boxIndex = 0;

		if (rowNumber < 3) {
			boxNum = 0;
		} else if (rowNumber < 6) {
			boxNum = 3;
		} else {
			boxNum = 6;
		}

		if (columnNumber < 3) {

		} else if (columnNumber < 6) {
			boxNum += 1;
		} else {
			boxNum += 2;
		}

		int boxColumn = columnNumber;
		if (columnNumber < 3) {

		} else if (columnNumber < 6) {
			boxColumn -= 3;
		} else {
			boxColumn -= 6;
		}

		int boxRow = rowNumber;
		if (rowNumber < 3) {

		} else if (rowNumber < 6) {
			boxRow -= 3;
		} else {
			boxRow -= 6;
		}

		boxIndex = boxColumn;
		if (boxRow == 0) {

		} else if (boxRow == 1) {
			boxIndex += 3;
		} else {
			boxIndex += 6;
		}

		return new int[] { boxNum, boxIndex };
	}

	private void clearCellValueFromArrays(int rowNumber, int columnNumber, char[] initialCellValues) {
		cellRowValues[rowNumber][columnNumber] = 0;
		cellColumnValues[columnNumber][rowNumber] = 0;
		int[] cellBoxCoordinates = getCellBoxCoordinates(rowNumber, columnNumber);
		cellBoxValues[cellBoxCoordinates[0]][cellBoxCoordinates[1]] = 0;
		initialCellValues[getIndex(rowNumber, columnNumber)] = 0;
	}

	public char[] getInitialState() {
		return initialState;
	}

	// Pair coordinates are mirrored. Example: cell1(1,2), cell2(7,6)
	private boolean removeValuePairsFromGrid(char[] initialCellValues) {

		outerloop:
		for (int i = 0; i < 9; i++) {

			for (int j = 0; j < 9; j++) {
				
				//Exit loops when all of the cells have been checked
				if (i == 4 && j == 5) {
					break outerloop;
				}

				if (Character.isDigit(cellRowValues[i][j])) {
					
					// This line might not work, reference instead of copy
					char[] copyValues = new char[] { cellRowValues[i][j], cellRowValues[8 - i][8 - j] };
					clearCellValueFromArrays(i, j, initialCellValues);
					clearCellValueFromArrays(8 - i, 8 - j, initialCellValues);
					
					if (!sMethods.checkForSingleSolution(cellRowValues, cellColumnValues, cellBoxValues)) {

						cellRowValues[i][j] = copyValues[0];
						cellColumnValues[j][i] = copyValues[0];
						int[] boxCoordinates = getCellBoxCoordinates(i, j);
						cellBoxValues[boxCoordinates[0]][boxCoordinates[1]] = copyValues[0];
						initialCellValues[getIndex(i, j)] = copyValues[0];

						cellRowValues[8 - i][8 - j] = copyValues[1];
						cellColumnValues[8 - j][8 - i] = copyValues[1];
						boxCoordinates = getCellBoxCoordinates(8 - i, 8 - j);
						cellBoxValues[boxCoordinates[0]][boxCoordinates[1]] = copyValues[1];
						initialCellValues[getIndex(8 - i, 8 - j)] = copyValues[1];

					}
				}
			}
		}

		return false;
	}

	private int getIndex(int rowNumber, int columnNumber) {
		int index = columnNumber + rowNumber * 9;
		return index;
	}

	private void printArrays() {

		System.out.println("ROW");
		for (int z = 0; z < 9; z++) {
			for (int k = 0; k < 9; k++) {
				System.out.print("[" + cellRowValues[z][k] + "]");
			}
			System.out.println();
		}
		System.out.println();

		System.out.println("COLUMN");
		for (int z = 0; z < 9; z++) {
			for (int k = 0; k < 9; k++) {
				System.out.print("[" + cellColumnValues[k][z] + "]");
			}
			System.out.println();
		}
		System.out.println();

		System.out.println("BOX");
		for (int z = 0; z < 9; z++) {
			for (int k = 0; k < 9; k++) {
				int boxCoordinates[] = getCellBoxCoordinates(z, k);
				System.out.print("[" + cellBoxValues[boxCoordinates[0]][boxCoordinates[1]] + "]");
			}
			System.out.println();
		}
		System.out.println();

	}

}
