package controller;

public interface UIEventListeners {

	int requestBoardSize();
	int[][] requestGameBoard();
	int[][] startGame(int level);
	boolean checkValidPosition(int row, int col, int value);

}
