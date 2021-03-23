package Controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Observer;

import Model.CustomerBook;
import database.DatabaseConnection;
import globalHelper.AlertWindow;
import globalHelper.CurrentUserEmail;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class CheckoutController {
	private Connection connection;
	private PreparedStatement prepStmt;
	private Statement statement;

	public CheckoutController() {
		connection = DatabaseConnection.getInstance().getConnection();
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private TextField cardNotf;
	@FXML
	private TextField cardPasstf;

	private TableView<CustomerBook> cartTable;
	private Label totalPrice;

	public void initData(TableView<CustomerBook> cartTable, Label totalPrice) {
		this.cartTable = cartTable;
		this.totalPrice = totalPrice;
	}

	public void checkedOut() throws SQLException {

		// Validating card number and password
		String cardNo = cardNotf.getText();
		String password = cardPasstf.getText();
		if (cardNo.equals("") || password.equals("")) {
			AlertWindow.getInstance().showAlert(AlertType.WARNING, "Card number or passwrod is missing!", "",
					"Warning");
			return;
		}

		String query = "SELECT * FROM customer_credit_card WHERE CustomerEmail = '"
				+ CurrentUserEmail.getInstance().getEmail() + "' AND CardNumber = '" + cardNo + "' ;";
		ResultSet rs = null;
		Statement st;
		st = connection.createStatement();
		rs = st.executeQuery(query);

		if (!rs.isBeforeFirst()) {
			// new card number

			// INSERT INTO `bookstore`.`customer_credit_card` (`CustomerEmail`,
			// `CardNumber`, `Password`) VALUES ('yousef', '12', '12');
			query = "INSERT INTO customer_credit_card(CustomerEmail, CardNumber, Password) VALUES ('"
					+ CurrentUserEmail.getInstance().getEmail() + "' , '" + cardNo + "', '" + password + "' );";
			st = connection.createStatement();
			st.executeUpdate(query);
			AlertWindow.getInstance().showAlert(AlertType.INFORMATION, "New credit card has been added", "",
					"Information");
		}
		// editing books quantities
		ObservableList<CustomerBook> soldBooks = cartTable.getItems();
		for (CustomerBook book : soldBooks) {
			
			Double newQuantity = Double.parseDouble(book.getQuantity()) - 1;
			book.setQuantity(String.valueOf(newQuantity));
			query = "UPDATE Book Set Quantity = '" + Double.toString(newQuantity) + "' WHERE ISBN = '" + book.getISBN()
					+ "';";
			connection.createStatement().executeUpdate(query);
		}

		// clear cart table and price label after checking out
		cartTable.getItems().clear();
		totalPrice.setText("");
		((Stage) cardPasstf.getScene().getWindow()).close();
	}
}
