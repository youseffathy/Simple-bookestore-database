package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignUpController extends Controller {

    public SignUpController() {
        super();
    }

    @FXML
    private TextField email_tf;
    @FXML
    private TextField password_tf;
    @FXML
    private TextField fName_tf;
    @FXML
    private TextField lName_tf;
    @FXML
    private TextField username_tf;
    @FXML
    private TextField phone_tf;
    @FXML
    private TextField address_tf;
    @FXML
    private Button signUp_btn;

    public void createAccount() {
        String email = email_tf.getText();
        String password = password_tf.getText();
        String username = username_tf.getText();
        String fName = fName_tf.getText();
        String lName = lName_tf.getText();
        String phone = phone_tf.getText();
        String address = address_tf.getText();

        if (email.isEmpty() || password.isEmpty() || username.isEmpty() || fName.isEmpty() || lName.isEmpty()) {
            showDialogue("Some fields are missing!", null, "Error");
            System.err.println("sign up missing fields failed!");
            return;
        }

        // valid username & email
        if (checkEmailAndUsername(email, username)) {
            if (phone.isEmpty())    phone = null;
            if (address.isEmpty())  address = null;

            String query = "INSERT INTO USERS (Email, Username, Password, Fname, Lname, Phone, Address) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?);";

            try {
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, username);
                preparedStatement.setString(3, password);
                preparedStatement.setString(4, fName);
                preparedStatement.setString(5, lName);
                preparedStatement.setString(6, phone);
                preparedStatement.setString(7, address);

                preparedStatement.executeUpdate();

                showDialogue("Sign up successful", null, "Confirmation");
                System.out.println("Successful sign up with email: "+ email);

                loginClick();

            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

    }

    private boolean checkEmailAndUsername(String email, String username) {
        String checkEmail = "SELECT * FROM USERS WHERE Email = ?";
        String checkUsername = "SELECT * FROM USERS WHERE Username = ?";

        try {
            preparedStatement = connection.prepareStatement(checkEmail);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            preparedStatement = connection.prepareStatement(checkUsername);
            preparedStatement.setString(1, username);
            ResultSet usernameResultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                showDialogue("This email is already a user", null, "Error");
                System.err.println("sign up email failed!");
            } else if (usernameResultSet.next()) {
                showDialogue("Username is already taken", null, "Error");
                System.err.println("sign up username failed!");
            } else {
                return true;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    public void loginClick() {
       openPage("login");
    }

}
