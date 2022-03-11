package model;

public class SudokuSolver {

	private static SudokuSolver _instance;
	private int BOARD_SIZE, BOX_SIZE;

	private SudokuSolver() {
		this.BOARD_SIZE = 9;
		this.BOX_SIZE = 3;
	}

	private SudokuSolver(int boardSize) {
		this.BOARD_SIZE = boardSize;
		Double box = Math.sqrt(boardSize);
		this.BOX_SIZE = box.intValue();
	}

	public static SudokuSolver getInstance() {
		if (_instance == null)
			_instance = new SudokuSolver();
		return _instance;
	}

	public static SudokuSolver getInstance(int boardSize) {
		if (_instance == null || _instance.BOARD_SIZE != boardSize)
			_instance = new SudokuSolver(boardSize);
		return _instance;
	}

	public boolean solveBoard(int[][] board) {
		if(board == null || board.length != BOARD_SIZE)
			return false;
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				if (board[row][col] == 0) {
					for (int i = 1; i <= BOARD_SIZE; i++) {
						board[row][col] = i;
						if (isValidPlacement(board, row, col) && solveBoard(board))
							return true;
						board[row][col] = 0;
					}
					return false;
				}
			}
		}
		return true;
	}
	
	public int[][] getSolvedBoard(int[][] board) {
		if(solveBoard(board))
			return board;
		return null;
	}

	public boolean isValidPlacement(int[][] board, int row, int col) {
		return isValidRow(board, row) && isValidColumn(board, col) && isValidBox(board, row, col);
	}

	private boolean isValidRow(int[][] board, int row) {
		int[] appearances = new int[BOARD_SIZE];
		for (int i = 0; i < board.length; i++) {
			int index = board[row][i] - 1;
			if(index >= 0) {
				if(appearances[index] > 0)
					return false;
				else
					appearances[index] += 1;	
			}
		}
		return true;
	}

	private boolean isValidColumn(int[][] board, int col) {
		int[] appearances = new int[BOARD_SIZE];
		for (int i = 0; i < board.length; i++) {
			int index = board[i][col] - 1;
			if(index >= 0) {
				if(appearances[index] > 0)
					return false;
				else
					appearances[index] += 1;
			}
		}
		return true;
	}

	private boolean isValidBox(int[][] board, int row, int col) {
		int[] appearances = new int[BOARD_SIZE];
		// find upper left corner of the box
		int startRowPos = row - (row % BOX_SIZE);
		int startColPos = col -(col % BOX_SIZE);

		for (int i = 0; i < BOX_SIZE; i++) {
			for (int j = 0; j < BOX_SIZE; j++) {
				int index = board[startRowPos + i][startColPos + j]  - 1;
				if(index >= 0) {
					if(appearances[index] > 0)
						return false;
					else
						appearances[index] += 1;
				}
			}
		}
		return true;
	}

	public boolean isSolveable(int[][] board) {
		if(board.length != BOARD_SIZE)
			return false;
		return solveBoard(board);
	}
}
