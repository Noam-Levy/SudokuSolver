package model;

import java.util.Random;

public class BoardGenerator {
	
	private final int BOARD_SIZE = 9, BLOCK_SIZE = 3;
	private int[][] board;
	private Random random;

	public BoardGenerator() {
		// create valid (trivial) board to shuffle 
		this.board 	= new int[][] {
			{1,2,3,  4,5,6,  7,8,9},
			{4,5,6,  7,8,9,  1,2,3},
			{7,8,9,  1,2,3,  4,5,6},

			{2,3,1,  5,6,4,  8,9,7},
			{5,6,4,  8,9,7,  2,3,1},
			{8,9,7,  2,3,1,  5,6,4},

			{3,1,2,  6,4,5,  9,7,8},
			{6,4,5,  9,7,8,  3,1,2},
			{9,7,8,  3,1,2,  6,4,5}
		};
		this.random = new Random();
		shuffleBoard();
	}

	public void printBoard(int[][] board) {
		for (int i = 0; i < BOARD_SIZE; i++) {
			if(i > 0 && i % 3 == 0) {
				for (int j = 0; j < BLOCK_SIZE; j++)
					System.out.print("-\t-\t-\t\t");
				System.out.println();
			}
			for (int j = 0; j < BOARD_SIZE; j++) {
				if(j > 0 && j % 3 == 0)
					System.out.print("|\t");
				System.out.print(board[i][j]+"\t");
			}
			System.out.println();
		}
	}

	public void shuffleBoard() {
		shuffleNumbers();
		shuffleRows();
		shuffleCols();
		shuffle3X3Rows();
		shuffle3X3Cols();
	}
	
	public int[][] getBoard(int difficulty) {
		int diffucltyModifier;
		int[][] puzzleBoard = copyBoard(board);
		if(difficulty == 1) // easy
			diffucltyModifier = random.nextInt(29, 40);
		else if (difficulty == 2) // medium
			diffucltyModifier = random.nextInt(40, 55);
		else // hard
			diffucltyModifier = random.nextInt(55, 75);
		
		return generatePuzzleBoard(puzzleBoard, diffucltyModifier);
	}
	
	public int getBoardSize() {
		return BOARD_SIZE;
	}

	private int[][] generatePuzzleBoard(int[][] puzzleBoard, int diffucltyModifier) {
		int row, col;
		do {
			row = random.nextInt(BOARD_SIZE);
			col = random.nextInt(BOARD_SIZE);
			if(puzzleBoard[row][col] != 0) {
				puzzleBoard[row][col] = 0;
				diffucltyModifier--;
			}
		} while (diffucltyModifier > 0);
		return puzzleBoard;
	}

	private int[][] copyBoard(int[][] original) {
		int[][] copy = new int[BOARD_SIZE][BOARD_SIZE];
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				copy[i][j] = original[i][j];
			}
		}
		return copy;
	}

	private void shuffleNumbers() {
		for (int i = 1; i <= BOARD_SIZE; i++) {
			int ranNum = random.nextInt(1,BOARD_SIZE+1);
			swapNumbers(i, ranNum);
		}
	}

	private void swapNumbers(int n1, int n2) {
		for (int y = 0; y < BOARD_SIZE; y++) {
			for (int x = 0; x < BOARD_SIZE; x++) {
				if (board[x][y] == n1) {
					board[x][y] = n2;
				} else if (board[x][y] == n2) {
					board[x][y] = n1;
				}
			}
		}
	}

	private void shuffleRows() {
		int blockNumber;

		for (int i = 0; i < BOARD_SIZE; i++) {
			int ranNum = random.nextInt(BLOCK_SIZE);
			blockNumber = i / BLOCK_SIZE;
			swapRows(i, blockNumber * BLOCK_SIZE + ranNum);
		}
	}

	private void swapRows(int r1, int r2) {
		int[] row = board[r1];
		board[r1] = board[r2];
		board[r2] = row;
	}

	private void shuffleCols() {
		int blockNumber;

		for (int i = 0; i < BOARD_SIZE; i++) {
			int ranNum = random.nextInt(BLOCK_SIZE);
			blockNumber = i / BLOCK_SIZE;
			swapCols(i, blockNumber * BLOCK_SIZE + ranNum);
		}
	}
	
	private void swapCols(int c1, int c2) {
		int colVal;
		for (int i = 0; i < BOARD_SIZE; i++){
			colVal = board[i][c1];
			board[i][c1] = board[i][c2];
			board[i][c2] = colVal;
		}
	}

	private void shuffle3X3Rows() {

		for (int i = 0; i < BLOCK_SIZE; i++) {
			int ranNum = random.nextInt(BLOCK_SIZE);
			swap3X3Rows(i, ranNum);
		}
	}

	private void swap3X3Rows(int r1, int r2) {
		for (int i = 0; i < BLOCK_SIZE; i++) {
			swapRows(r1 * BLOCK_SIZE + i, r2 * BLOCK_SIZE + i);
		}
	}

	private void shuffle3X3Cols() {

		for (int i = 0; i < BLOCK_SIZE; i++) {
			int ranNum = random.nextInt(BLOCK_SIZE);
			swap3X3Cols(i, ranNum);
		}
	}
	
	private void swap3X3Cols(int c1, int c2) {
		for (int i = 0; i < BLOCK_SIZE; i++) {
			swapCols(c1 * BLOCK_SIZE + i, c2 * BLOCK_SIZE + i);
		}
	}
}
