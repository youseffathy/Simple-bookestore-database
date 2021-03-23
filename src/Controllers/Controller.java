package Controllers;

import database.DatabaseConnection;
import globalHelper.SceneShower;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.sql.*;

public class Controller {
    public static Connection connection;
    public static PreparedStatement preparedStatement;

    public Controller() {
        connection = DatabaseConnection.getInstance().getConnection();
        preparedStatement = null;
    }

    public void openPage(String xmlFile) {
        try {
            SceneShower.getInstance().showScene(xmlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showDialogue(String note, String header, String title) {
        Alert alert = null;
        if (title.equals("Confirmation"))
            alert = new Alert(Alert.AlertType.CONFIRMATION);
        if (title.equals("Error"))
            alert = new Alert(Alert.AlertType.ERROR);
        if (title.equals("Information"))
            alert = new Alert(Alert.AlertType.INFORMATION);
        if (alert == null)
            return;

        alert.setContentText(note);
        alert.setHeaderText(header);
        alert.showAndWait();
    }

}
