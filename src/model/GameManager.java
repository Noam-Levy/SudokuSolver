package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import controller.ModelEventListeners;
import javafx.application.Platform;

public class GameManager implements Serializable {
	private static final long serialVersionUID = 466609140751114435L;
	
	@SuppressWarnings("unused") 
	private int current_difficulty; // for reference when saving
	private int[][] gameBoard, solvedBoard; // solved board is used for real-time hints.
	private ArrayList<ModelEventListeners> listeners;
	private SudokuSolver solver;
	private BoardGenerator gameGenerator;
	private Timer timer;
	private int[] timeTracker;
	
	public GameManager() {
		this.listeners = new ArrayList<>();
		this.gameGenerator = new BoardGenerator();
		this.solver = SudokuSolver.getInstance(gameGenerator.getBoardSize());
		this.timer = new Timer();
		this.timeTracker = new int[] {0,0};
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
		toggleTimer(true);
	}
	
	public void endGame() {
		toggleTimer(false);
		this.timeTracker = new int[] {0,0};
	}
	
	public boolean isCorrectPlacement(int row, int col, int value) {
		// checks if placed value matches solution (real-time hinting)
		return solvedBoard[row][col] == value;
	}
	
	
    public int[][] getBoard() {
    	return gameBoard;	
	}
        
    public int getBoardSize() {
    	return gameGenerator.getBoardSize();
    }
    
	public boolean registerListener(ModelEventListeners newListener) {
    	return this.listeners.add(newListener);
    }
	
	public void solveInteractively() {
		solveInteractivelyHelper(gameBoard);
	}
	
    private void solveBoard() {
    	int[][] copiedBoard = this.gameGenerator.copyBoard(gameBoard);
    	this.solvedBoard = this.solver.getSolvedBoard(copiedBoard);	
	}
	
	private void toggleTimer(boolean run) {
		if(!run) {
			this.timer.cancel();
			this.timeTracker = new int[] {0,0};
			return;
		}
		this.timer.scheduleAtFixedRate(new TimerTask() {	
			@Override
			public void run() {
				tickTimeTracker();
		         Platform.runLater(new Runnable() {
		             public void run() {
		 				for (ModelEventListeners l : listeners)
							l.updateTimer(timeTracker[1], timeTracker[0]);
		             }
		         });
			}
		}, 0, 1000);
	}
		
	private void tickTimeTracker() {
		timeTracker[0]++;
		if(timeTracker[0] == 60) {
			timeTracker[0] = 0;
			timeTracker[1]++;
		}
	}

	private boolean solveInteractivelyHelper(int[][] board) {
		int BOARD_SIZE = board.length;
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				if (board[row][col] == 0) {
					for (int i = 1; i <= BOARD_SIZE; i++) {
						board[row][col] = i;
						for (ModelEventListeners l : listeners)
							l.setCellValue(row,col,i);
						if (solver.isValidPlacement(board,row,col) && solveInteractivelyHelper(board))
							return true;
						board[row][col] = 0;
						for (ModelEventListeners l : listeners)
							l.setCellValue(row,col,0);
					}
					return false;
				}
			}
		}
		return true;
	}
	
}
