package controller;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.GameManager;
import view.View;

public class Controller implements UIEventListeners, ModelEventListeners {
	
	private View view;
	private GameManager gameManager;
	
	public Controller(GameManager model) {
		this.gameManager = model;
		this.gameManager.registerListener(this);
	}
	
	public void start(Stage primaryStage) throws IOException {
			URL location = getClass().getResource("/view/View.fxml");
			FXMLLoader fxmlLoader = new FXMLLoader(location);
			Parent root = (Pane)fxmlLoader.load();
			this.view = (View)fxmlLoader.getController();
			this.view.registerListener(this);
			this.view.setBoardSize(gameManager.getBoardSize());
			Scene scene = new Scene(root);
			primaryStage.centerOnScreen();
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Sudoku Solver");
			primaryStage.show();
	}

	@Override
	public int requestBoardSize() {
		return gameManager.getBoardSize();
	}

	@Override
	public int[][] requestGameBoard() {
		return gameManager.getBoard();
	}

	@Override
	public int[][] startGame(int level) {
		gameManager.startGame(level);
		return requestGameBoard();
	}
	

	@Override
	public void endGame() {
		gameManager.endGame();
	}
	
	@Override
	public boolean checkValidPosition(int row, int col, int value) {
		return gameManager.isCorrectPlacement(row, col, value);
	}

	@Override
	public void updateTimer(int minutes, int seconds) {
		view.updateTimer(minutes, seconds);
		
	}

	@Override
	public void solveInteractively() {
		gameManager.solveInteractively();
	}

	@Override
	public void setCellValue(int row, int col, int i) {
		view.setCellValue(row, col, Integer.toString(i));
	}

}
