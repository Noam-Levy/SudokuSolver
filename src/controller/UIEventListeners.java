package controller;

public interface UIEventListeners {

	int requestBoardSize();
	int[][] requestGameBoard();
	int[][] startGame(int level);
	void endGame();
	void solveInteractively();
	boolean checkValidPosition(int row, int col, int value);
	
	

}
