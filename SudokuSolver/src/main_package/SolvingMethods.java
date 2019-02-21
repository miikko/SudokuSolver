package main_package;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import interfaces.SolvingMethods_IF;

public class SolvingMethods implements SolvingMethods_IF {

	private final char[] NUMBERS = new char[] { '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	private char[][] copyRowValues;
	private char[][] copyColumnValues;
	private char[][] copyBoxValues;
	private LinkedHashMap<Integer, List<Character>> possibleValues;
	private boolean progressMade = true;

	public SolvingMethods() {

	}

	@Override
	public boolean checkForSingleSolution(char[][] rowValues, char[][] columnValues, char[][] boxValues) {
		copyRowValues = copy2DimensionalArray(rowValues, 9, 9);
		copyColumnValues = copy2DimensionalArray(columnValues, 9, 9);
		copyBoxValues = copy2DimensionalArray(boxValues, 9, 9);
		progressMade = true;
		setPossibleValues(copyRowValues);

		while (progressMade) {
			progressMade = false;
			candidateLine();
			singleCandidate();
			singlePosition();
		}
		
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (!Character.isDigit(copyRowValues[i][j])) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public void singlePosition() {

		Character[][][] possibleRowValues = new Character[9][9][9];
		Character[][][] possibleColumnValues = new Character[9][9][9];
		Character[][][] possibleBoxValues = new Character[9][9][9];

		// Finds possible values
		for (int i = 0; i < 9; i++) {

			for (int j = 0; j < 9; j++) {

				int index = getIndex(i, j, 1);
				if (possibleValues.get(index) != null && possibleValues.get(index).size() > 0) {

					List<Character> possibleValuesForThisCell = possibleValues.get(index);
					possibleRowValues[i][j] = listToArray(possibleValuesForThisCell);
					possibleColumnValues[j][i] = listToArray(possibleValuesForThisCell);
					int[] boxCoordinates = getCellBoxCoordinates(i, j);
					possibleBoxValues[boxCoordinates[0]][boxCoordinates[1]] = listToArray(possibleValuesForThisCell);
				}
			}
		}

		if (findSingles(possibleRowValues, 1)) {
			progressMade = true;
		} else if (findSingles(possibleColumnValues, 2)) {
			progressMade = true;
		} else if (findSingles(possibleBoxValues, 3)) {
			progressMade = true;
		}
	}

	@Override
	public void singleCandidate() {

		Integer[] keys = possibleValues.keySet().toArray(new Integer[possibleValues.keySet().size()]);

		for (int i = 0; i < keys.length; i++) {

			if (possibleValues.get(keys[i]).size() == 1) {
				putValueToArrays(possibleValues.get(keys[i]).get(0), keys[i]);
			}
		}

	}

	@Override
	public void candidateLine() {

		Character[][][] possibleBoxValues = new Character[9][9][9];

		// Filling the array for better iterability
		for (int i = 0; i < 9; i++) {

			for (int j = 0; j < 9; j++) {

				int index = getIndex(i, j, 1);
				if (possibleValues.get(index) != null && possibleValues.get(index).size() > 0) {

					List<Character> possibleValuesForThisCell = possibleValues.get(index);
					int[] boxCoordinates = getCellBoxCoordinates(i, j);
					possibleBoxValues[boxCoordinates[0]][boxCoordinates[1]] = listToArray(possibleValuesForThisCell);
				}
			}
		}

		// Checking each box individually
		for (int i = 0; i < possibleBoxValues.length; i++) {

			LinkedHashMap<Character, List<Integer>> pBVByRow = new LinkedHashMap<>();
			LinkedHashMap<Character, List<Integer>> pBVByColumn = new LinkedHashMap<>();
			int boxRowNumber = 0;
			int boxColumnNumber = 0;

			for (int j = 0; j < possibleBoxValues[i].length; j++) {

				if (boxColumnNumber == 3) {
					boxRowNumber++;
					boxColumnNumber = 0;
				}

				List<Character> thisCellPV = Arrays.asList(possibleBoxValues[i][j]);
				for (int k = 0; k < thisCellPV.size(); k++) {

					Character thisPV = thisCellPV.get(k);
					if (thisPV != null && Character.isDigit(thisPV)) {

						if (pBVByRow.get(thisPV) == null) {
							pBVByRow.put(thisPV, new ArrayList<Integer>());
						}
						if (pBVByColumn.get(thisPV) == null) {
							pBVByColumn.put(thisPV, new ArrayList<Integer>());
						}

						if (!pBVByRow.get(thisPV).contains(boxRowNumber)) {
							pBVByRow.get(thisPV).add(boxRowNumber);
						}
						if (!pBVByColumn.get(thisPV).contains(boxColumnNumber)) {
							pBVByColumn.get(thisPV).add(boxColumnNumber);
						}
					}
				}

				boxColumnNumber++;
			}

			Character[] rowKeys = pBVByRow.keySet().toArray(new Character[pBVByRow.keySet().size()]);
			for (int k = 0; k < rowKeys.length; k++) {

				if (pBVByRow.get(rowKeys[k]).size() == 1) {

					int gridRowNumber = pBVByRow.get(rowKeys[k]).get(0) + (i / 3) * 3;
					List<Integer> protectedIndexes = new ArrayList<>();
					for (int z = 0; z < 3; z++) {
						protectedIndexes.add(gridRowNumber * 9 + (i % 3) * 3 + z);
					}
					removePossibleValuesFromRow(rowKeys[k], gridRowNumber, protectedIndexes);
				}
			}
			
			Character[] columnKeys = pBVByColumn.keySet().toArray(new Character[pBVByColumn.keySet().size()]);
			for (int k = 0; k < columnKeys.length; k++) {
				
				if (pBVByColumn.get(columnKeys[k]).size() == 1) {
					
					int gridColumnNumber = pBVByColumn.get(columnKeys[k]).get(0) + (i % 3) * 3;
					List<Integer> protectedIndexes = new ArrayList<>();
					int startingIndex = gridColumnNumber + (i / 3) * 27;
					for (int z = 0; z < 3; z++) {
						protectedIndexes.add(startingIndex + z * 9);
					}
					removePossibleValuesFromColumn(columnKeys[k], gridColumnNumber, protectedIndexes);
				}
			}

		}
	}

	@Override
	public void doublePairs() {
		 
	}
	
	//Selection type: 1 = row, 2 = column, 3 = box
	private boolean findSingles(Character[][][] possibleValues, int selectionType) {

		LinkedHashMap<Character, Integer> singles = new LinkedHashMap<Character, Integer>();
		List<Character> bannedValues = new ArrayList<>();

		for (int i = 0; i < 9; i++) {

			for (int j = 0; j < 9; j++) {

				if (possibleValues[i][j][0] != null && Character.isDigit(possibleValues[i][j][0])) {

					for (int k = 0; k < possibleValues[i][j].length; k++) {

						if (singles.containsKey(possibleValues[i][j][k])
								&& !bannedValues.contains(possibleValues[i][j][k])) {

							bannedValues.add(possibleValues[i][j][k]);

						} else if (!singles.containsKey(possibleValues[i][j][k])) {

							singles.put(possibleValues[i][j][k], getIndex(i, j, selectionType));
						}
					}
				}
			}

			// Check for singles
			Character[] singleKeys = singles.keySet().toArray(new Character[singles.keySet().size()]);
			for (int z = 0; z < singleKeys.length; z++) {

				if (!bannedValues.contains(singleKeys[z])) {

					putValueToArrays(singleKeys[z], singles.get(singleKeys[z]));
					return true;
				}
			}
			singles.clear();
			bannedValues.clear();
		}
		return false;
	}

	private char[][] copy2DimensionalArray(char[][] twoDimensionalArray, int firstLength, int secondLength) {
		char[][] copy = new char[firstLength][secondLength];

		for (int i = 0; i < firstLength; i++) {
			for (int j = 0; j < secondLength; j++) {
				copy[i][j] = twoDimensionalArray[i][j];
			}
		}

		return copy;
	}

	/**
	 * Combines the two parameters into a single index. Selection types are as
	 * follows: 1 for row, 2 for column and 3 for box.
	 * 
	 * @param firstNumber
	 * @param secondNumber
	 * @param selectionType
	 * @return index of the specified cell
	 */
	private int getIndex(int firstNumber, int secondNumber, int selectionType) {
		int index = 0;

		switch (selectionType) {
		case 1:
			index = secondNumber + firstNumber * 9;
			break;
		case 2:
			index = firstNumber + secondNumber * 9;
			break;
		case 3:

			int rowNumber;
			int columnNumber;

			if (firstNumber < 3) {
				rowNumber = 0;
			} else if (firstNumber < 6) {
				rowNumber = 3;
			} else {
				rowNumber = 6;
			}

			if (secondNumber < 3) {

			} else if (secondNumber < 6) {
				rowNumber += 1;
			} else {
				rowNumber += 2;
			}

			if (firstNumber % 3 == 0) {
				columnNumber = 0;
			} else if (firstNumber % 3 == 1) {
				columnNumber = 3;
			} else {
				columnNumber = 6;
			}

			if (secondNumber % 3 == 0) {

			} else if (secondNumber % 3 == 1) {
				columnNumber += 1;
			} else {
				columnNumber += 2;
			}
			index = columnNumber + rowNumber * 9;
			break;

		default:
			System.out.println("Tried called getIndex with an invalid selectionType.");
			break;
		}
		return index;
	}

	/**
	 * Checks if specified value is found on the same row/column/box
	 */
	private boolean validate(char value, int rowNumber, int columnNumber) {

		for (int i = 0; i < 9; i++) {
			if (copyRowValues[rowNumber][i] == value) {
				return false;
			}
		}

		for (int i = 0; i < 9; i++) {
			if (copyColumnValues[columnNumber][i] == value) {
				return false;
			}
		}

		int[] cellBoxCoordinates = getCellBoxCoordinates(rowNumber, columnNumber);
		for (int i = 0; i < 9; i++) {
			if (copyBoxValues[cellBoxCoordinates[0]][i] == value) {
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

	private void putValueToArrays(char value, int index) {
		int rowNumber = index / 9;
		int columnNumber = index % 9;

		copyRowValues[rowNumber][columnNumber] = value;
		copyColumnValues[columnNumber][rowNumber] = value;
		int[] boxCoordinates = getCellBoxCoordinates(rowNumber, columnNumber);
		copyBoxValues[boxCoordinates[0]][boxCoordinates[1]] = value;
		updatePossibleValues(index, value);
	}

	/**
	 * Iterates through all the cells. Upon finding an empty cell, the method will
	 * fill a list with all the possible values for that cell. After filling the
	 * list, it will be put into a HashMap that uses the cell's index as a key.
	 * 
	 * @param copyRowValues
	 */
	private void setPossibleValues(char[][] copyRowValues) {

		possibleValues = new LinkedHashMap<Integer, List<Character>>();

		for (int i = 0; i < 9; i++) {

			for (int j = 0; j < 9; j++) {

				if (!Character.isDigit(copyRowValues[i][j])) {

					for (int k = 0; k < NUMBERS.length; k++) {

						if (validate(NUMBERS[k], i, j)) {

							int index = getIndex(i, j, 1);
							if (possibleValues.get(index) == null) {
								possibleValues.put(index, new ArrayList<Character>());
							}
							possibleValues.get(index).add(NUMBERS[k]);
						}
					}
				}
			}
		}

	}

	private void updatePossibleValues(int indexOfValue, Character value) {

		possibleValues.put(indexOfValue, new ArrayList<Character>());

		int rowNumber = indexOfValue / 9;
		int columnNumber = indexOfValue % 9;
		int[] cellBoxCoordinates = getCellBoxCoordinates(rowNumber, columnNumber);

		removePossibleValuesFromRow(value, rowNumber, new ArrayList<Integer>());

		removePossibleValuesFromColumn(value, columnNumber, new ArrayList<Integer>());

		// Checks the box in which the value was put
		for (int i = 0; i < 9; i++) {

			int index = getIndex(cellBoxCoordinates[0], i, 3);
			if (possibleValues.containsKey(index) && possibleValues.get(index).contains(value)) {
				possibleValues.get(index).remove(value);
				progressMade = true;
			}
		}
	}

	private void removePossibleValuesFromRow(Character value, int rowNumber, List<Integer> protectedIndexes) {
		int startingIndex = 0 + 9 * rowNumber;
	
		for (int i = 0; i < 9; i++) {

			if (possibleValues.containsKey(startingIndex + i) && !protectedIndexes.contains(startingIndex + i)) {
				if (possibleValues.get(startingIndex + i).contains(value)) {
					possibleValues.get(startingIndex + i).remove(value);
					progressMade = true;
				}
			}
		}
	}
	
	private void removePossibleValuesFromColumn(Character value, int columnNumber, List<Integer> protectedIndexes) {
		int startingIndex = columnNumber;
		
		for (int i = 0; i < 9; i++) {
			
			if (possibleValues.containsKey(startingIndex + i * 9) && !protectedIndexes.contains(startingIndex + i * 9)) {
				if (possibleValues.get(startingIndex + i * 9).contains(value)) {
					possibleValues.get(startingIndex + i * 9).remove(value);
					progressMade = true;
				}
			}
		}
	}

	private Character[] listToArray(List<Character> list) {
		Character[] array = new Character[list.size()];

		for (int i = 0; i < list.size(); i++) {
			array[i] = list.get(i);
		}

		return array;
	}

	private void printArrays() {

		System.out.println("ROW");
		for (int z = 0; z < 9; z++) {
			for (int k = 0; k < 9; k++) {
				System.out.print("[" + copyRowValues[z][k] + "]");
			}
			System.out.println();
		}
		System.out.println();

		System.out.println("COLUMN");
		for (int z = 0; z < 9; z++) {
			for (int k = 0; k < 9; k++) {
				System.out.print("[" + copyColumnValues[k][z] + "]");
			}
			System.out.println();
		}
		System.out.println();

		System.out.println("BOX");
		for (int z = 0; z < 9; z++) {
			for (int k = 0; k < 9; k++) {
				int boxCoordinates[] = getCellBoxCoordinates(z, k);
				System.out.print("[" + copyBoxValues[boxCoordinates[0]][boxCoordinates[1]] + "]");
			}
			System.out.println();
		}
		System.out.println();

	}

}
