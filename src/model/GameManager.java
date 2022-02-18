package model;

import java.io.Serializable;
import java.util.ArrayList;

import controller.ModelEventListeners;

public class GameManager implements Serializable {
	private static final long serialVersionUID = 466609140751114435L;
	
	private int[][] gameBoard, solvedBoard; // solved board is used for real-time hints.
	private ArrayList<ModelEventListeners> listeners;
	private SudokuSolver solver;
	private BoardGenerator gameGenerator;
	
	public GameManager() {
		this.listeners = new ArrayList<>();
		this.gameGenerator = new BoardGenerator();
		initSolver();
	}
	
	private void initSolver() {
		/*
		 * helper function for constructor
		 */
		this.solver = SudokuSolver.getInstance(gameGenerator.getBoardSize());
	}

	public void startGame(int difficulty) {
		/*
		 * Generates new game board based on passed difficulty setting and getting the solved board in the background.
		 */
		gameBoard = gameGenerator.getBoard(difficulty);
		new Thread(new Runnable() {
			@Override
			public void run() {
				solvedBoard = solver.getSolvedBoard(gameBoard);
			}
		}).start();
		// pass game board to view to be displayed
	}
	
	public boolean isCorrectPlacement(int row, int col, int value) {
		/*
		 * checks if placed value matches solution 
		 * (real-time hinting)
		 */
		return solvedBoard[row][col] == value;
	}
	
	
    public int[][] getBoard() {
		return gameBoard;	
	}

	public boolean registerListener(ModelEventListeners newListener) {
    	return this.listeners.add(newListener);
    }
	
}
