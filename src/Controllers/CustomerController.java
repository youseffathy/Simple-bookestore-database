package Controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.HashMap;

import Model.CustomerBook;
import globalHelper.CurrentUserEmail;
import globalHelper.SceneShower;
import database.DatabaseConnection;
import globalHelper.AlertWindow;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class CustomerController {

	private Connection connection;
	private PreparedStatement prepStmt;
	private Statement statement;

	public CustomerController() {
		connection = DatabaseConnection.getInstance().getConnection();
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// tab pane
	@FXML
	private TabPane tabpane;

	// tabs
	@FXML
	private Tab homeTabe;
	@FXML
	private Tab searchTabe;
	@FXML
	private Tab buyBooksTabe;
	@FXML
	private Tab profileTabe;
	@FXML
	private Tab logOutTabe;

	// profile tab elements
	@FXML
	private GridPane gridPane;
	@FXML
	private Button applayBtn;
	@FXML
	private Button editBtn;
	@FXML
	private Button cancelBtn;
	@FXML
	private TextField Emailtf;
	@FXML
	private TextField untf;
	@FXML
	private TextField passtf;
	@FXML
	private TextField fnametf;
	@FXML
	private TextField Lnametf;
	@FXML
	private TextField phonetf;
	@FXML
	private TextField addresstf;

	// buy books tab
	@FXML
	private Label searchKeyLbl;
	@FXML
	private TextField searchTf;
	@FXML
	private TableView<CustomerBook> searchTable;
	@FXML
	private TableView<CustomerBook> cartTable;
	@FXML
	private Label totalPrice;

	@FXML
	private TableColumn<CustomerBook, String> searchTitleCol;
	@FXML
	private TableColumn<CustomerBook, String> searchPublisherCol;
	@FXML
	private TableColumn<CustomerBook, String> searchAuthorCol;
	@FXML
	private TableColumn<CustomerBook, String> searchPriceCol;
	@FXML
	private TableColumn<CustomerBook, String> searchCategoryCol;

	@FXML
	private TableColumn<CustomerBook, String> cartTitleCol;
	@FXML
	private TableColumn<CustomerBook, String> cartPriceCol;

	@FXML
	private MenuItem managerMI;

	private HashMap<String, String> searchFilter = new HashMap<String, String>();
	private String SearchKeySelection;

	/*
	 * profile tab methods
	 */
	public void showProfile() throws SQLException {
		String query = "SELECT * FROM users WHERE Email = '" + CurrentUserEmail.getInstance().getEmail() + "' ;";
		ResultSet resultSet = null;
		resultSet = statement.executeQuery(query);
		if (resultSet.next()) {
			Emailtf.setText(resultSet.getString(1));
			untf.setText(resultSet.getString(2));
			passtf.setText(resultSet.getString(3));
			fnametf.setText(resultSet.getString(4));
			Lnametf.setText(resultSet.getString(5));
			phonetf.setText(resultSet.getString(6));
			addresstf.setText(resultSet.getString(7));
		}
	}

	public void editingButtons(ActionEvent event) throws SQLException {
		if (event.getSource() == editBtn) {
			editMode(true);
		} else { // Apply button event handler
			AlertWindow alert = AlertWindow.getInstance();
			alert.showAlert(AlertType.CONFIRMATION, "Save changes ?!", "For Safety", "Confirmation");
			if (alert.isBool()) {
				try {
					String query = "UPDATE users SET Email = ?, Username = ?, Password = ?, Fname = ?, Lname = ?, Phone = ?, Address = ? "
							+ " WHERE Email = ?;";
					prepStmt = connection.prepareStatement(query);
					prepStmt.setString(1, getTextFieldValue(Emailtf));
					prepStmt.setString(2, getTextFieldValue(untf));
					prepStmt.setString(3, getTextFieldValue(passtf));
					prepStmt.setString(4, getTextFieldValue(fnametf));
					prepStmt.setString(5, getTextFieldValue(Lnametf));
					prepStmt.setString(6, getTextFieldValue(phonetf));
					prepStmt.setString(7, getTextFieldValue(addresstf));
					prepStmt.setString(8, CurrentUserEmail.getInstance().getEmail());
					prepStmt.executeUpdate();
				} catch (SQLIntegrityConstraintViolationException e) {
					e.printStackTrace();
					alert.showAlert(AlertType.ERROR, "Some field(s) can not be empty !!", "Error", "Error");
					return;
				} catch (SQLException e) {
					e.printStackTrace();
					alert.showAlert(AlertType.ERROR, "please check your changes and try again !!", "Error", "Error");
					return;
				}
			}
			editMode(false);
			showProfile();
		}
	}

	private String getTextFieldValue(TextField tf) {
		if (!tf.getText().equals(""))
			return tf.getText();
		return null;
	}

	private void editMode(boolean editing) {
		editBtn.setVisible(!editing);
		applayBtn.setVisible(editing);
		cancelBtn.setVisible(editing);
		Emailtf.setEditable(editing);
		untf.setEditable(editing);
		passtf.setEditable(editing);
		fnametf.setEditable(editing);
		Lnametf.setEditable(editing);
		phonetf.setEditable(editing);
		addresstf.setEditable(editing);
	}

	/*
	 * buy books methods
	 */
	public void buyBooks() {
		cartTable.getItems().clear();
		searchTable.getItems().clear();
		SearchKeySelection = null;
		searchKeyLbl.setText("");
		totalPrice.setText("0");
		searchFilter.clear();
		searchTable.setPlaceholder(new Label("No rows to display"));
		cartTable.setPlaceholder(new Label("No rows to display"));
		searchTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		cartTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		cartTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
		cartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
		searchTitleCol.setCellValueFactory(new PropertyValueFactory<CustomerBook, String>("title"));
		searchPublisherCol.setCellValueFactory(new PropertyValueFactory<CustomerBook, String>("publisher"));
		searchAuthorCol.setCellValueFactory(new PropertyValueFactory<CustomerBook, String>("author"));
		searchPriceCol.setCellValueFactory(new PropertyValueFactory<CustomerBook, String>("price"));
		searchCategoryCol.setCellValueFactory(new PropertyValueFactory<CustomerBook, String>("category"));
	}

	public void selectSearchFilter(ActionEvent event) {
		searchKeyLbl.setText(((MenuItem) event.getSource()).getText());
		String value = searchTf.getText();
		if (value != null && !value.isEmpty() && SearchKeySelection != null) {
			searchFilter.put(SearchKeySelection, value);
		}
		SearchKeySelection = ((MenuItem) event.getSource()).getText();
		searchTf.setText("");
		searchTf.setText(searchFilter.get(SearchKeySelection));
	}

	public void searchForBooks() throws SQLException {
		searchTable.getItems().clear();
		if (SearchKeySelection != null && searchTf.getText() != "") {
			searchFilter.put(SearchKeySelection, searchTf.getText());
		}
		String title = searchFilter.get("Title");
		String publisher = searchFilter.get("Publisher");
		String category = searchFilter.get("Category");
		String author = searchFilter.get("Author");
		if (title == null)
			title = "";
		if (publisher == null)
			publisher = "";
		if (category == null)
			category = "";
		if (author == null)
			author = "";
		String query = "SELECT b.Title, b.ISBN, b.Quantity, b.Category, b.Price, AuthName, PubName\r\n"
				+ "FROM book as b left outer join bookstore.author on b.ISBN =  author.ISBN,"
				+ " book as c left outer join publisher on c.PublisherID =  publisher.PublisherID\r\n"
				+ "WHERE b.ISBN = c.ISBN AND  (  b.Title = '" + title + "'  OR b.Category = '" + category
				+ "'  OR AuthName = '" + author + "' OR PubName =  '" + publisher + "' ) ORDER BY ISBN;";
		ResultSet rs = null;
		try {
			Statement st = connection.createStatement();
			rs = st.executeQuery(query);
			if (!rs.isBeforeFirst()) {
				searchTable.setPlaceholder(new Label("No books found!"));
				// AlertWindow.getInstanece().showAlert(AlertType.INFORMATION, " no result
				// found", null, "Information");
				return;
			}
			// AlertWindow.getInstanece().showAlert(AlertType.INFORMATION, "result found",
			// null, "Information");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String prevISBN = "";
		CustomerBook cBook = null;
		while (rs.next()) {
			if (Double.parseDouble(rs.getString("Quantity")) > 0) { // only books with positive quantity appears
				String isbn = rs.getString("ISBN");
				String authName = rs.getString("AuthName");

				if (isbn.equals(prevISBN) && cBook != null) {
					cBook.setAuthor(cBook.getAuthor() + ", " + authName);
				} else {
					prevISBN = isbn;
					cBook = new CustomerBook(rs.getString("Title"), authName, rs.getString("PubName"),
							rs.getString("Category"), rs.getString("Price"), isbn, rs.getString("Quantity"));

					searchTable.getItems().add(cBook);
				}
			}
		}
	}

	public void addToCart() throws SQLException {
		ObservableList<CustomerBook> selectedBooks = searchTable.getSelectionModel().getSelectedItems();
		if (selectedBooks.isEmpty())
			return;

		for (CustomerBook book : selectedBooks) {
//			Double quantity = Double.parseDouble(book.getQuantity());
//			quantity -= 1;
//
//			String query = "UPDATE Book Set Quantity = '" + Double.toString(quantity) + "' WHERE ISBN = '"
//					+ book.getISBN() + "';";
//			connection.createStatement().executeUpdate(query);
			cartTable.getItems().add(book);
		}
		updateTotalPrice();
	}

	public void deleteFromCart() {
		ObservableList<CustomerBook> selectedBooks = cartTable.getSelectionModel().getSelectedItems();
		for (CustomerBook book : selectedBooks) {
			cartTable.getItems().remove(book);
		}
		updateTotalPrice();
	}

	public void checkOut() throws IOException {
		if (cartTable.getItems().isEmpty())
			return;
		//loading checkout controller and passing cart table and price label  to adjust books quantities after checking out
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../XML/checkOut.fxml"));
		Stage checkOutStage = new Stage();
		checkOutStage.setScene(new Scene(loader.load()));
		CheckoutController checkouController = loader.getController();
		checkouController.initData(cartTable, totalPrice);
		checkOutStage.show();
		return;
	}
	
	public void editQuantityAfterCO() throws SQLException {
		ObservableList<CustomerBook> soldBooks = cartTable.getItems();

		for (CustomerBook book : soldBooks) {
			Double quantity = Double.parseDouble(book.getQuantity());
			quantity -= 1;

			String query = "UPDATE Book Set Quantity = '" + Double.toString(quantity) + "' WHERE ISBN = '"
					+ book.getISBN() + "';";
			connection.createStatement().executeUpdate(query);
		}
		cartTable.getItems().clear();
		updateTotalPrice();
	}


	private void updateTotalPrice() {
		ObservableList<CustomerBook> cartBooks = cartTable.getItems();
		double totalPrice = 0;

		for (CustomerBook book : cartBooks) {
			totalPrice += Double.parseDouble(book.getPrice());
		}
		this.totalPrice.setText(Double.toString(totalPrice));
	}

	public void logOut() throws IOException {
		SceneShower.getInstance().showScene("login");
		// empty current user email
		CurrentUserEmail.getInstance().setEmail("");
	}

	public void close() throws IOException {
		SceneShower.getInstance().closeMainStage();
	}

	private void checkIfManager() {
		if (managerMI == null)
			return;
		final String email = CurrentUserEmail.getInstance().getEmail();
		String checkManager = "SELECT * FROM MANAGERS WHERE Email = ?";

		try {
			prepStmt = connection.prepareStatement(checkManager);
			prepStmt.setString(1, email);
			ResultSet resultSet = prepStmt.executeQuery();

			if (resultSet.next()) { // current user is a manager
				System.out.println("current user is a manager with email : " + email);
				managerMI.setVisible(true);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	public void goToManager() throws IOException {
		SceneShower.getInstance().showScene("manager");
	}


	@FXML
	public void initialize() {
		checkIfManager();
	}
}
