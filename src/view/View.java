package view;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import controller.UIEventListeners;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class View implements Initializable{

	private ArrayList<UIEventListeners> listeners;
	private int BOARD_SIZE;
	private boolean showHints;
	@FXML private GridPane boardGrid;
	@FXML private CheckBox autoSolve, toggleHints;
	@FXML private Button newGameButton, loadGameButton, saveGameButton, quitButton;
	@FXML private Label gameTimer;
	@FXML private ToggleGroup levelSelector;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		toggleHints(null);
		addTextRestrictions();
		boardGrid.setDisable(true);
	}

	public View() {
		this.listeners = new ArrayList<UIEventListeners>();
	}

	public void setBoardSize(int size) {
		if (size > 0)
			this.BOARD_SIZE = size;
	}

	public boolean registerListener(UIEventListeners newListener) {
		return this.listeners.add(newListener);
	}

	@FXML
	private void startGame(ActionEvent event) {
		boardGrid.setDisable(false);
		clearBoard();
		int[][] gameBoard = null;
		int level = getLevelData();
		for (UIEventListeners l : listeners) {
			gameBoard = l.startGame(level);
		}
		if(gameBoard == null)
			//TODO - throw exception
			return;
		for (int i = 0; i < BOARD_SIZE; i++)
			for (int j = 0; j < BOARD_SIZE; j++)
				setCellValue(i, j, Integer.toString(gameBoard[i][j]));
	}

	private int getLevelData() {
		int level;
		Toggle tg = this.levelSelector.getSelectedToggle();
		switch (tg.selectedProperty().getName()) {
		case "Easy":
			level = 1;
			break;
		case "Medium":
			level = 2;
			break;
		case "Hard":
			level = 3;
			break;
		default:
			// defaults to medium difficulty.
			level = 2;
			break;
		}
		return level;
	}

	@FXML
	private void loadGame(ActionEvent event) {

	}

	@FXML
	private void saveGame(ActionEvent event) {

	}

	@FXML
	private void quit(ActionEvent event) {

	}


	@FXML
	void highlightCells(MouseEvent event) {
		TextField source = (TextField)event.getSource();
		Integer row = GridPane.getRowIndex(source) != null ? GridPane.getRowIndex(source) : 0;
		Integer column= GridPane.getColumnIndex(source) != null ? GridPane.getColumnIndex(source) : 0;
		if(this.showHints) {
			colorText(row,column, source);
			paintCells(row, column);
		}
	}

	private void paintCells(int row, int col) {
		if(!this.showHints)
			return;
		boardGrid.getChildren().forEach(node -> {
			if(node instanceof TextField) {
				Integer rIndex = GridPane.getRowIndex(node) != null ? GridPane.getRowIndex(node) : 0;
				Integer cIndex = GridPane.getColumnIndex(node) != null ? GridPane.getColumnIndex(node) : 0;
				// retain original text cell text fill
				int b = node.getStyle().indexOf("-fx-text-fill:");
				String textFill = (b != -1) ? node.getStyle().substring(b) + ";" : "";
				if (rIndex == row || cIndex  == col)
					node.setStyle("-fx-background-color: #9ac4e6;" + textFill);
				else
					node.setStyle("-fx-background-color: white;" + textFill);
			}
		});
	}

	private void colorText(int row, int col, TextField cell) {
		String s = cell.getText();
		if (s.equals("0") || s.isBlank() || !cell.isEditable())
			return;
		if(!checkValidPosition(row,col,s))				
			cell.setStyle("-fx-text-fill: darkred");
		else
			cell.setStyle("-fx-text-fill: #239afa");
	}

	private boolean checkValidPosition(int row, int col, String value) {
		if(value.equals("0") || value.isBlank())
			return false;
		boolean valid = false;
		for (UIEventListeners l : listeners) {
			valid = l.checkValidPosition(row,col,Integer.parseInt(value));
		}
		return valid;
	}

	@FXML
	private void checkUserValue(KeyEvent event) {
		TextField source = (TextField)event.getSource();
		Integer rIndex = GridPane.getRowIndex(source) != null ? GridPane.getRowIndex(source) : 0;
		Integer cIndex = GridPane.getColumnIndex(source) != null ? GridPane.getColumnIndex(source) : 0;
		colorText(rIndex, cIndex, source);
		if(isComplete())
			System.out.println("YOU WIN!");
		return; // TODO - END GAME
	}

	private boolean isComplete() {
		/*
		 * Checks if the board is full and correct. if so - returns true to end the game.
		 */
		boolean complete = false;
		for (Node node : boardGrid.getChildren()) {
			if(!(node instanceof TextField))
				continue;
			Integer rIndex = GridPane.getRowIndex(node) != null ? GridPane.getRowIndex(node) : 0;
			Integer cIndex = GridPane.getColumnIndex(node) != null ? GridPane.getColumnIndex(node) : 0;
			String text = ((TextField)node).getText();
			if(text.isBlank())
				return false;
			int value = Integer.parseInt(text);
			for (UIEventListeners l : listeners) {
				complete = l.checkValidPosition(rIndex, cIndex, value);
				if(!complete)
					break;
			}
		}
		return complete;
	}

	private void setCellValue(int row, int col, String cellValue) {
		// sets received cell values from the game manger and disables the ability for the user to manipulate their values.
		if (cellValue.equals("0"))
			return;
		boardGrid.getChildren().forEach(node -> {
			Integer rIndex = GridPane.getRowIndex(node) != null ? GridPane.getRowIndex(node) : 0;
			Integer cIndex = GridPane.getColumnIndex(node) != null ? GridPane.getColumnIndex(node) : 0;
			if(node instanceof TextField && rIndex == row && cIndex == col) {
				((TextField)node).setText(cellValue);
				((TextField)node).setEditable(false);
			}
		});
	}


	private void clearBoard() {
		boardGrid.getChildren().forEach(node -> {
			if(node instanceof TextField) {
				((TextField)node).setText("");
				((TextField)node).setDisable(false);
			}
		});
	}

	@FXML
	private void solveBoard(ActionEvent event) {

	}

	@FXML
	private void toggleHints(ActionEvent event) {
		this.showHints = this.toggleHints.isSelected();
		if(!showHints)
			boardGrid.getChildren().forEach(node -> node.setStyle("-fx-background-color: white;"));
	}

	private void addTextRestrictions() {
		// allows numbers between 1 to board size only to be written to the text fields.
		boardGrid.getChildren().forEach(node -> {
			if(node instanceof TextField) {
				((TextField)node).textProperty().addListener(new ChangeListener<String>() {
					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
						if(!newValue.matches("[1-" + BOARD_SIZE + "]"))
							((TextField)node).setText(newValue.replaceAll("[^\\d]", ""));
						if(newValue.equals("0"))
							((TextField)node).setText("");
						if(newValue.length() > 1)
							((TextField)node).setText(oldValue);
					}
				});
			}
		});
	}
}
