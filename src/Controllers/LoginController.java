package Controllers;

import globalHelper.CurrentUserEmail;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.*;

public class LoginController extends Controller {

    public LoginController() {
        super();
    }

    @FXML
    private TextField email_tf;
    @FXML
    private TextField password_tf;
    @FXML
    private Button login_btn;
    @FXML
    private Label loginFailed_label;

    public void loginClick() {
        String email = email_tf.getText().toLowerCase();
        String password = password_tf.getText();

        String query = "SELECT * FROM USERS WHERE Email = ? AND Password = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            // TODO: uncomment, remove login with email for debugging
//            if (!resultSet.next()) {
//                loginFailed_label.setVisible(true);
//                System.err.println("Login Failed!");
//            } else {
//                loginFailed_label.setVisible(false);
//                showDialogue("Login successful", null, "Successful");
//                System.out.println("Successful login with email: "+ email);
//                // set current user email
//                CurrentUserEmail.getInstance().setEmail(email);
//                goToHome();
//            }
            CurrentUserEmail.getInstance().setEmail(email);
            goToHome();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private void goToHome() {
        openPage("Customer");
    }

    public void registerClick() {
        openPage("signUp");
    }

}
