package globalHelper;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneShower {
	private static SceneShower sceneShower = null;
	private static Stage primaryStage = null;
	private static String  currentScene = null;

	private SceneShower() {

	}
	
	public static SceneShower getInstance() {
		if(sceneShower == null)
			sceneShower = new SceneShower();
		return sceneShower;
	}
	
	
	public void setPrimaryStage(Stage primaryStage) {
		SceneShower.primaryStage = primaryStage;
	}

	public String getCurrentScene() {
		return currentScene;
	}

	public void showScene(String xmlName) throws IOException {
		SceneShower.currentScene = xmlName;
		Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../XML/"+ xmlName +".fxml")));
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	
	public void closeMainStage() {
		primaryStage.close();
	}
	
	

}
