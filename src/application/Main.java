package application;

import java.net.URL;

/**
 * 
 * @author Noam Levy
 *
 **/

import model.BoardGenerator;
import model.SudokuSolver;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
//		try {
//			URL location = getClass().getResource("/view/GamePage.fxml");
//			FXMLLoader fxmlLoader = new FXMLLoader(location);
//			Parent root = (Pane)fxmlLoader.load();
//			//this.view = (View)fxmlLoader.getController();
//			Scene scene = new Scene(root);
//			primaryStage.setScene(scene);
//			primaryStage.show();
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
	}
	
	public static void main(String[] args) {
		BoardGenerator bg = new BoardGenerator();
		int[][] gameBoard = bg.getBoard(3);
		boolean result = SudokuSolver.getInstance().solveBoard(gameBoard);
		System.out.println("- - - - - - - - -");
		if(result)
			bg.printBoard(gameBoard);
		else
			System.out.println("cannot solve board :(");
		//launch(args);
	}
	
	
}
