package GUI;

import globalHelper.CurrentUserEmail;
import globalHelper.SceneShower;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		SceneShower.getInstance().setPrimaryStage(primaryStage);
		Parent root = FXMLLoader.load(getClass().getResource("../XML/login.fxml"));
		primaryStage.setTitle("Online Bookstore");
		primaryStage.setScene(new Scene(root, 460, 480));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
