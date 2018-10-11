package main_package;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

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

		while (progressMade) {
			progressMade = false;
			setPossibleValues(copyRowValues);
			singleCandidate();
			setPossibleValues(copyRowValues);
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

		Character[][][] possibleRowValues = new Character[9][9][1];
		Character[][][] possibleColumnValues = new Character[9][9][1];
		Character[][][] possibleBoxValues = new Character[9][9][0];
		Integer[] keys = possibleValues.keySet().toArray(new Integer[possibleValues.keySet().size()]);

		for (int i = 0; i < 9; i++) {

			for (int j = 0; j < 9; j++) {

				int index = getIndex(i, j);
				if (possibleValues.get(index) != null && possibleValues.get(index).size() > 1) {

					List<Character> possibleValuesForThisCell = possibleValues.get(index);
					possibleRowValues[i][j] = listToArray(possibleValuesForThisCell);
					possibleColumnValues[j][i] = listToArray(possibleValuesForThisCell);
					int[] boxCoordinates = getCellBoxCoordinates(i, j);
					possibleBoxValues[boxCoordinates[0]][boxCoordinates[1]] = listToArray(possibleValuesForThisCell);
				}
			}
		}

		LinkedHashMap<Character, Integer> singles = new LinkedHashMap<>();
		List<Character> bannedValues = new ArrayList<>();

		outerloop:
		for (int i = 0; i < 9; i++) {

			for (int j = 0; j < 9; j++) {

				if (possibleRowValues[i][j][0] != null && Character.isDigit(possibleRowValues[i][j][0])) {

					for (int k = 0; k < possibleRowValues[i][j].length; k++) {

						if (singles.containsKey(possibleRowValues[i][j][k]) && !bannedValues.contains(possibleRowValues[i][j][k])) {
							
							bannedValues.add(possibleRowValues[i][j][k]);
							
						} else if (!singles.containsKey(possibleRowValues[i][j][k])){
							
							singles.put(possibleRowValues[i][j][k], getIndex(i, j));
						}
					}
				}
			}
			
			//Check for singles
			Character[] singleKeys = singles.keySet().toArray(new Character[singles.keySet().size()]);
			for (int z = 0; z < singleKeys.length; z++) {
				
				if (!bannedValues.contains(singleKeys[z])) {
					putValueToArrays(singleKeys[z], singles.get(singleKeys[z]));
					progressMade = true;
					break outerloop;
				}
			}
			singles.clear();
			bannedValues.clear();
		}

	}

	@Override
	public void singleCandidate() {

		Integer[] keys = possibleValues.keySet().toArray(new Integer[possibleValues.keySet().size()]);

		for (int i = 0; i < keys.length; i++) {

			if (possibleValues.get(keys[i]).size() == 1) {

				putValueToArrays(possibleValues.get(keys[i]).get(0), keys[i]);
				possibleValues.get(keys[i]).remove(0);
				progressMade = true;
			}
		}

	}
	
	private void sortPossibleValues(LinkedHashMap<Character, Integer> singles, List<Character> bannedValues, Character[][][] possibleValues) {
		outerloop:
			for (int i = 0; i < 9; i++) {

				for (int j = 0; j < 9; j++) {

					if (possibleValues[i][j][0] != null && Character.isDigit(possibleValues[i][j][0])) {

						for (int k = 0; k < possibleValues[i][j].length; k++) {

							if (singles.containsKey(possibleValues[i][j][k]) && !bannedValues.contains(possibleValues[i][j][k])) {
								
								bannedValues.add(possibleValues[i][j][k]);
								
							} else if (!singles.containsKey(possibleValues[i][j][k])){
								
								singles.put(possibleValues[i][j][k], getIndex(i, j));
							}
						}
					}
				}
				
				//Check for singles
				Character[] singleKeys = singles.keySet().toArray(new Character[singles.keySet().size()]);
				for (int z = 0; z < singleKeys.length; z++) {
					
					if (!bannedValues.contains(singleKeys[z])) {
						putValueToArrays(singleKeys[z], singles.get(singleKeys[z]));
						progressMade = true;
						break outerloop;
					}
				}
				singles.clear();
				bannedValues.clear();
			}
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

	private int getIndex(int rowNumber, int columnNumber) {
		int index = columnNumber + rowNumber * 9;
		return index;
	}

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
	}
	
	private void setPossibleValues(char[][] copyRowValues) {
		
		possibleValues = new LinkedHashMap<Integer, List<Character>>();

		for (int i = 0; i < 9; i++) {

			for (int j = 0; j < 9; j++) {

				if (!Character.isDigit(copyRowValues[i][j])) {

					for (int k = 0; k < NUMBERS.length; k++) {

						if (validate(NUMBERS[k], i, j)) {

							int index = getIndex(i, j);
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
	
	private Character[] listToArray(List<Character> list) {
		Character[] array = new Character[list.size()];
		
		for (int i = 0; i < list.size(); i++) {
			array[i] = list.get(i);
		}
		
		return array;
	}

}
