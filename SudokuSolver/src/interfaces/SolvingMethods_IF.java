package interfaces;

public interface SolvingMethods_IF {
	
	public abstract boolean checkForSingleSolution(char[][] rowValues, char[][] columnValues, char[][] boxValues);
	
	//add more difficulties progressively
	
	//EASY//
	
	/**
	 * Iterates through rows/columns/boxes to find a value that can only fit in a single cell in that specific row/column/box.
	 * If such a cell exists, the methods fills it with that value.
	 * The method checks the rows first, then the columns and finally the boxes. 
	 */
	public abstract void singlePosition();
	
	/**
	 * Checks if the current grid has a row/column/box that has an empty cell which can only have one possible value.
	 * If such a cell exists, the method fills it with that value.
	 */
	public abstract void singleCandidate();
	
	//MEDIUM//
	
	/**
	 * Goes through all the boxes to find possible values that only exist in one of that box's row/column.
	 * If such a possible value exists, it removes all appearances of that possible value in that row/column (except the ones inside the box). 
	 */
	public abstract void candidateLine();
	
	/**
	 * Goes through each row/column set with the size of box width
	 * Tries to find two pairs of possible values in the matching rows/columns
	 * If such pairs are found, the possible value is removed from the remaining boxes row/column
	 */
	public abstract void doublePairs();
	
	//ADVANCED//
	
	//MASTER//
}
