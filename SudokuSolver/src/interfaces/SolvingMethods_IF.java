package interfaces;

public interface SolvingMethods_IF {
	
	public abstract boolean checkForSingleSolution(char[][] rowValues, char[][] columnValues, char[][] boxValues);
	
	//add more difficulties progressively
	
	//EASY//
	public abstract void singlePosition();
	public abstract void singleCandidate();
	
	//MEDIUM//
	
	//ADVANCED//
	
	//MASTER//
}
