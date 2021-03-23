package Controllers;

import Model.Author;
import Model.Order;
import Model.Publisher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AddElementController extends Controller {

    public AddElementController() {
        super();
    }

    // add book tab
    @FXML
    private TextField add_book_isbn_tf;
    @FXML
    private TextField add_book_title_tf;
    @FXML
    private TextField add_book_category_tf;
    @FXML
    private TextField add_book_publisherId_tf;
    @FXML
    private TextField add_book_quantity_tf;
    @FXML
    private TextField add_book_threshold_tf;
    @FXML
    private TextField add_book_price_tf;
    @FXML
    private DatePicker add_book_pubYear_dp;
    @FXML
    private Button add_book_btn;

    // add author tab
    @FXML
    private TextField add_author_isbn_tf;
    @FXML
    private TextField add_author_authorName_tf;
    @FXML
    private TableView<Author> author_tableView;
    @FXML
    private TableColumn<Author, String> col_author_isbn;
    @FXML
    private TableColumn<Author, String> col_author_name;
    private ObservableList<Author> authorOL = FXCollections.observableArrayList();

    // add publisher tab
    @FXML
    private TextField add_publisher_id_tf;
    @FXML
    private TextField add_publisher_pubName_tf;
    @FXML
    private TextField add_publisher_address_tf;
    @FXML
    private TextField add_publisher_phone_tf;
    @FXML
    private TableView<Publisher> publisher_tableView;
    @FXML
    private TableColumn<Publisher, String> col_publisherId;
    @FXML
    private TableColumn<Publisher, String> col_pubName;
    @FXML
    private TableColumn<Publisher, String> col_pubAddress;
    @FXML
    private TableColumn<Publisher, String> col_pubPhone;
    private ObservableList<Publisher> publisherOL = FXCollections.observableArrayList();

    // add order tab
    @FXML
    private TextField add_order_isbn_tf;
    @FXML
    private TextField add_order_quantity_tf;
    @FXML
    private TableView<Order> orders_tableView;
    @FXML
    private TableColumn<Order, String> col_order_id;
    @FXML
    private TableColumn<Order, String> col_order_isbn;
    @FXML
    private TableColumn<Order, String> col_order_title;
    @FXML
    private TableColumn<Order, String> col_order_quantity;
    @FXML
    private TableColumn<Order, String> col_order_date;

    private ObservableList<Order> orderObservableList = FXCollections.observableArrayList();

    public void addBook() {
        String isbn = add_book_isbn_tf.getText();
        String title = add_book_title_tf.getText();
        String category = add_book_category_tf.getText();
        String pubId = add_book_publisherId_tf.getText();
        String pubYear = null;
        DateTimeFormatter dtf  = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (add_book_pubYear_dp.getValue() != null) {
            pubYear = add_book_pubYear_dp.getValue().format(dtf);
        }
        String quantity = add_book_quantity_tf.getText();
        String threshold = add_book_threshold_tf.getText();
        String price = add_book_price_tf.getText();

        String query = "INSERT INTO BOOK (ISBN, Title, PublisherID, PubYear, Price, Quantity, Threshold, Category)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, isbn);
            preparedStatement.setString(2, title);
            preparedStatement.setString(3, pubId);
            preparedStatement.setString(4, pubYear);
            preparedStatement.setString(5, price);
            preparedStatement.setString(6, quantity);
            preparedStatement.setString(7, threshold);
            preparedStatement.setString(8, category);

            preparedStatement.executeUpdate();

            showDialogue("Book Added", null, "Information");
            System.out.println("Successful add book with isbn & title: "+ isbn + " & " + title);

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            // Duplicate book
            if (e.getMessage().charAt(0) == 'D')
                showDialogue("Book ISBN already found!", null, "Error");
            else if (e.getMessage().contains("Cannot add or update a child row: a foreign key constraint fails"))
                showDialogue("Publisher ID not found!", null, "Error");
            else
                showDialogue(e.getMessage(), null, "Error");
        }
    }

    public void addAuthor() {
        String isbn = add_author_isbn_tf.getText();
        String name = add_author_authorName_tf.getText();

        if (isbn.isEmpty() || name.isEmpty()) {
            showDialogue("Some fields are missing!", null, "Error");
            System.err.println("add author missing fields failed!");
            return;
        }

        String query = "INSERT INTO AUTHOR (ISBN, AuthName) " +
                "VALUES (?, ?);";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, isbn);
            preparedStatement.setString(2, name);
            preparedStatement.executeUpdate();

            showDialogue("Author Added", null, "Information");
            System.out.println("Successful add author with isbn & name: "+ isbn + " " + name);

            fillAuthorTable();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            // Duplicate id
            if (e.getMessage().charAt(0) == 'D')
                showDialogue("Author already found!", null, "Error");
            else
                showDialogue(e.getMessage(), null, "Error");
        }
    }

    public void addPublisher() {
        String id = add_publisher_id_tf.getText();
        String name = add_publisher_pubName_tf.getText();
        String address = add_publisher_address_tf.getText();
        String phone = add_publisher_phone_tf.getText();

        if (id.isEmpty() || name.isEmpty()) {
            showDialogue("Some fields are missing!", null, "Error");
            System.err.println("add publisher missing fields failed!");
            return;
        }

        String query = "INSERT INTO PUBLISHER (PublisherID, PubName, Address, Phone) " +
                "VALUES (?, ?, ?, ?);";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, address);
            preparedStatement.setString(4, phone);

            preparedStatement.executeUpdate();

            showDialogue("Publisher Added", null, "Information");
            System.out.println("Successful add publisher with id & name: "+ id + " " + name);

            fillPublisherTable();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            // Duplicate id
            if (e.getMessage().charAt(0) == 'D')
                showDialogue("Publisher ID already found!", null, "Error");
            else
                showDialogue(e.getMessage(), null, "Error");
        }

    }

    public void addOrder() {
        String isbn = add_order_isbn_tf.getText();
        String quantity = add_order_quantity_tf.getText();
        String title = getBookTitle(isbn);
        System.out.println(title);
        DateTimeFormatter dtf  = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();

        if (isbn.isEmpty() || quantity.isEmpty()) {
            showDialogue("Some fields are missing!", null, "Error");
            System.err.println("add order missing fields failed!");
            return;
        }

        String query = "INSERT INTO ORDERS (ISBN, Quantity, bookTitle, OrderDate) " +
                "VALUES (?, ?, ?, ?);";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, isbn);
            preparedStatement.setString(2, quantity);
            preparedStatement.setString(3, title);
            preparedStatement.setString(4, dtf.format(now));

            preparedStatement.executeUpdate();

            showDialogue("Order Added", null, "Information");
            System.out.println("Successful order with ISBN & quantity: "+ isbn + " " + quantity);

            fillOrderTable();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            if (e.getMessage().contains("Cannot add or update a child row: a foreign key constraint fails"))
                showDialogue("Book ISBN not found!", null, "Error");
            else
                showDialogue(e.getMessage(), null, "Error");
        }
    }

    // on order tab clicked
    private void fillOrderTable() {
        orderObservableList.clear();
        col_order_id.setCellValueFactory(new PropertyValueFactory<>("OrderID"));
        col_order_isbn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        col_order_title.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        col_order_quantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        col_order_date.setCellValueFactory(new PropertyValueFactory<>("OrderDate"));

        String query = "SELECT * FROM ORDERS";
        try {
            preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                orderObservableList.add(new Order(resultSet.getString("OrderID"), resultSet.getString("ISBN"),
                        resultSet.getString("bookTitle"),
                        resultSet.getString("Quantity"), resultSet.getString("OrderDate")));
            }

            orders_tableView.setItems(orderObservableList);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private void fillAuthorTable() {
        authorOL.clear();
        col_author_isbn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        col_author_name.setCellValueFactory(new PropertyValueFactory<>("name"));

        String query = "SELECT * FROM AUTHOR ORDER BY ISBN";
        try {
            preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            String prevISBN = "";
            Author author = null;
            while (resultSet.next()) {
                String isbn = resultSet.getString("ISBN");
                String authName = resultSet.getString("AuthName");
                if (isbn.equals(prevISBN) && author != null) {
                    author.setName(author.getName()+", "+authName);
                } else {
                    author = new Author(isbn, authName);
                    authorOL.add(author);
                    prevISBN = isbn;
                }

            }

            author_tableView.setItems(authorOL);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private void fillPublisherTable() {
        publisherOL.clear();
        col_publisherId.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_pubName.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_pubAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        col_pubPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        String query = "SELECT * FROM PUBLISHER";
        try {
            preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                publisherOL.add(new Publisher(resultSet.getString("PublisherID"), resultSet.getString("PubName")
                        , resultSet.getString("Address"), resultSet.getString("Phone")));
            }

            publisher_tableView.setItems(publisherOL);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void confirmOrder() {
        String orderId = orders_tableView.getSelectionModel().getSelectedItem().getOrderID();

        String query = "DELETE FROM ORDERS WHERE OrderID = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, orderId);
            preparedStatement.executeUpdate();

            showDialogue("Order Confirmed", null, "Information");
            System.out.println("Successful order confirmed with ID : "+ orderId);

            fillOrderTable();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            showDialogue(e.getMessage(), null, "Error");
        }

    }

    private String getBookTitle(String isbn) {
        String query = "SELECT Title FROM BOOK WHERE ISBN = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getString("Title");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    @FXML
    public void initialize() {
        // fill table with current values
        fillOrderTable();
        fillPublisherTable();
        fillAuthorTable();
    }
}
