package main_package;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Grid {

	//private static Square[] cells;
	private final char[] NUMBERS = new char[] { '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	private char[][] cellRowValues;
	private char[][] cellColumnValues;
	private char[][] cellBoxValues;
	private char[] gridState;
	private Random random;
	private SolvingMethods sMethods;

	public Grid() {
		cellRowValues = new char[9][9];
		cellColumnValues = new char[9][9];
		cellBoxValues = new char[9][9];
		gridState = new char[81];
		random = new Random();
		sMethods = new SolvingMethods();
		createSudoku();
	}

	private void createSudoku() {

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

					gridState[index] = value;
					cellRowValues[i][j] = value;
					cellColumnValues[j][i] = value;

					int[] cellBoxCoordinates = getCellBoxCoordinates(i, j);
					cellBoxValues[cellBoxCoordinates[0]][cellBoxCoordinates[1]] = value;

					index++;
					invalidCellValues.clear();

				} else {
					j--;
					invalidCellValues.add(value);

					// If there are no valid values for this cell, begins filling the cells from the start
					if (invalidCellValues.size() == NUMBERS.length) {

						cellRowValues = new char[9][9];
						cellColumnValues = new char[9][9];
						cellBoxValues = new char[9][9];
						gridState = new char[81];
						invalidCellValues.clear();
						i = -1;
						j = 8;
						index = 0;

					}
				}
			}
		}

		// Grid is filled, now take out values
		removeValuePairsFromGrid();
		
	}

	// Checks if the given value is valid for the given cell
	private boolean validate(char value, int rowNumber, int columnNumber) {

		if (cellRowValues[rowNumber][columnNumber] != 0) {
			return false;
		}
		
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
	
	public boolean validate(char value, int index) {

		int[] rowAndColumnNums = getRowAndColumnNums(index);
		int rowNumber = rowAndColumnNums[0];
		int columnNumber = rowAndColumnNums[1];
		
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

	private int[] getCellBoxCoordinates(int rowNumber, int columnNumber) throws IllegalArgumentException {
		
		if (rowNumber < 0 || columnNumber < 0 || rowNumber > 8 || columnNumber > 8) {
			throw new IllegalArgumentException();
		}
		
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

	private void clearCellValueFromArrays(int rowNumber, int columnNumber) {
		cellRowValues[rowNumber][columnNumber] = 0;
		cellColumnValues[columnNumber][rowNumber] = 0;
		int[] cellBoxCoordinates = getCellBoxCoordinates(rowNumber, columnNumber);
		cellBoxValues[cellBoxCoordinates[0]][cellBoxCoordinates[1]] = 0;
		gridState[getIndex(rowNumber, columnNumber)] = 0;
	}
	
	public void clearCellValueFromArrays(int index) {
		
		int[] rowAndColumnNums = getRowAndColumnNums(index);
		int rowNumber = rowAndColumnNums[0];
		int columnNumber = rowAndColumnNums[1];
		
		cellRowValues[rowNumber][columnNumber] = 0;
		cellColumnValues[columnNumber][rowNumber] = 0;
		int[] cellBoxCoordinates = getCellBoxCoordinates(rowNumber, columnNumber);
		cellBoxValues[cellBoxCoordinates[0]][cellBoxCoordinates[1]] = 0;
		gridState[getIndex(rowNumber, columnNumber)] = 0;
	}

	public char[] getGridState() {
		return gridState;
	}

	// Pair coordinates are mirrored. Example: cell1(1,2), cell2(7,6)
	private boolean removeValuePairsFromGrid() {

		int lastIndex = 40;
		int counter = 0;
		boolean[] checkedCellIndexes = new boolean[lastIndex + 1];
		
		while (counter < checkedCellIndexes.length) {
			
			int cellIndex = random.nextInt(lastIndex + 1);
			
			if (checkedCellIndexes[cellIndex] == false) {
				
				counter++;
				checkedCellIndexes[cellIndex] = true;
				int[] rowAndColumnNums = getRowAndColumnNums(cellIndex);
				int rowNum = rowAndColumnNums[0];
				int columnNum = rowAndColumnNums[1];
				
				char[] copyValues = new char[] { cellRowValues[rowNum][columnNum], cellRowValues[8 - rowNum][8 - columnNum] };
				clearCellValueFromArrays(rowNum, columnNum);
				clearCellValueFromArrays(8 - rowNum, 8 - columnNum);
				
				if (!sMethods.checkForSingleSolution(cellRowValues, cellColumnValues, cellBoxValues)) {
					cellRowValues[rowNum][columnNum] = copyValues[0];
					cellColumnValues[columnNum][rowNum] = copyValues[0];
					int[] boxCoordinates = getCellBoxCoordinates(rowNum, columnNum);
					cellBoxValues[boxCoordinates[0]][boxCoordinates[1]] = copyValues[0];
					gridState[getIndex(rowNum, columnNum)] = copyValues[0];

					cellRowValues[8 - rowNum][8 - columnNum] = copyValues[1];
					cellColumnValues[8 - columnNum][8 - rowNum] = copyValues[1];
					boxCoordinates = getCellBoxCoordinates(8 - rowNum, 8 - columnNum);
					cellBoxValues[boxCoordinates[0]][boxCoordinates[1]] = copyValues[1];
					gridState[getIndex(8 - rowNum, 8 - columnNum)] = copyValues[1];

				}
			}
		}
		
		return false;
	}

	private int getIndex(int rowNumber, int columnNumber) {
		int index = columnNumber + rowNumber * 9;
		return index;
	}
	
	private int[] getRowAndColumnNums(int index) throws IndexOutOfBoundsException {
		if (index > 80 || index < 0) {
			throw new IndexOutOfBoundsException();
		}
		int rowNumber = index / 9;
		int columnNumber = index % 9;
		return new int[] {rowNumber, columnNumber};
	}
	
	public void putValueToArrays(char value, int index) {
		int rowNumber = index / 9;
		int columnNumber = index % 9;

		gridState[index] = value;
		cellRowValues[rowNumber][columnNumber] = value;
		cellColumnValues[columnNumber][rowNumber] = value;
		int[] boxCoordinates = getCellBoxCoordinates(rowNumber, columnNumber);
		cellBoxValues[boxCoordinates[0]][boxCoordinates[1]] = value;
	}
	
	public int getBoxNum(int index) throws IndexOutOfBoundsException {
		if (index > 80 || index < 0) {
			throw new IndexOutOfBoundsException();
		}
		int[] rowAndColumnNums = getRowAndColumnNums(index);
		int boxNum = getCellBoxCoordinates(rowAndColumnNums[0], rowAndColumnNums[1])[0];
		return boxNum;
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
