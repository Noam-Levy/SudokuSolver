package application;

/**
 * 
 * @author Noam Levy
 *
 **/

import java.io.IOException;
import controller.Controller;
import model.GameManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		GameManager game = new GameManager();
		Controller controller = new Controller(game);
		try {
			controller.start(primaryStage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}


