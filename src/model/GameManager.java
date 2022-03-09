package model;

import java.io.Serializable;
import java.util.ArrayList;

import controller.ModelEventListeners;

public class GameManager implements Serializable {
	private static final long serialVersionUID = 466609140751114435L;
	
	private int[][] gameBoard, solvedBoard; // solved board is used for real-time hints.
	@SuppressWarnings("unused") 
	private int current_difficulty; // for reference when saving
	private ArrayList<ModelEventListeners> listeners;
	private SudokuSolver solver;
	private BoardGenerator gameGenerator;
	
	public GameManager() {
		this.listeners = new ArrayList<>();
		this.gameGenerator = new BoardGenerator();
		this.solver = SudokuSolver.getInstance(gameGenerator.getBoardSize());
	}
	
	public void startGame(int difficulty) {
		/*
		 * Generates new game board based on passed difficulty setting and preparing the solved board in the background.
		 */
		this.gameBoard = gameGenerator.getBoard(difficulty);
		this.current_difficulty = difficulty;
		new Thread(new Runnable() {
			@Override
			public void run() {
				solveBoard();
			}
		}).start();
	}
	
	public boolean isCorrectPlacement(int row, int col, int value) {
		// checks if placed value matches solution (real-time hinting)
		return solvedBoard[row][col] == value;
	}
	
	
    public int[][] getBoard() {
    	return gameBoard;	
	}
    
    public void solveBoard() {
    	int[][] copiedBoard = this.gameGenerator.copyBoard(gameBoard);
    	this.solvedBoard = this.solver.getSolvedBoard(copiedBoard);	
	}
    
    public int getBoardSize() {
    	return gameGenerator.getBoardSize();
    }

	public boolean registerListener(ModelEventListeners newListener) {
    	return this.listeners.add(newListener);
    }
	
}
