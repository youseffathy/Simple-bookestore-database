package Controllers;

import Model.Book;
import globalHelper.SceneShower;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.io.IOException;
import java.sql.*;

public class ManagerController extends Controller {

    public ManagerController() {
        super();
    }

    @FXML
    Hyperlink logOut_text;

    // search
    @FXML
    private TextField search_isbn_tf;
    @FXML
    private TextField search_title_tf;
    @FXML
    private TextField search_category_tf;
    @FXML
    private TextField search_author_tf;
    @FXML
    private TextField search_publisher_tf;
    @FXML
    private Button search_btn;

    @FXML
    private TableView<Book> search_tableView;
    @FXML
    private TableColumn<Book, String> col_isbn;
    @FXML
    private TableColumn<Book, String> col_title;
    @FXML
    private TableColumn<Book, String> col_author;
    @FXML
    private TableColumn<Book, String> col_pubId;
    @FXML
    private TableColumn<Book, String> col_pubYear;
    @FXML
    private TableColumn<Book, String> col_price;
    @FXML
    private TableColumn<Book, String> col_quantity;
    @FXML
    private TableColumn<Book, String> col_threshold;
    @FXML
    private TableColumn<Book, String> col_category;

    private ObservableList<Book> observableList = FXCollections.observableArrayList();

    // promote
    @FXML
    private TextField promote_email_tf;

    public void logOut() {
        openPage("login");
    }

    public void openCustomerPage() {
        openPage("Customer");
    }

    public void search() {
        String isbn = search_isbn_tf.getText();
        String title = search_title_tf.getText();
        String category = search_category_tf.getText();
        String author = search_author_tf.getText();
        String publisher = search_publisher_tf.getText();

        String query = "SELECT BOOK.ISBN,Title,AuthName,BOOK.PublisherID,PubYear,Price,Quantity,Threshold,Category " +
                "FROM BOOK LEFT JOIN AUTHOR ON BOOK.ISBN = AUTHOR.ISBN, PUBLISHER " +
                "WHERE (BOOK.ISBN = ? OR BOOK.Title = ? OR Category = ? OR AuthName = ? OR PubName = ?) AND " +
                "BOOK.PublisherID = PUBLISHER.PublisherID " +
                "ORDER BY ISBN";
        try {
            preparedStatement = connection.prepareStatement(query,
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1, isbn);
            preparedStatement.setString(2, title);
            preparedStatement.setString(3, category);
            preparedStatement.setString(4, author);
            preparedStatement.setString(5, publisher);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                showDialogue("No books found", null, "Error");
                System.out.println("no books found with ISBN & title: "+ isbn + " & " + title);
                observableList.clear();
                return;
            }

            showDialogue("Books Found", null, "Information");
            System.out.println("Successful found book with ISBN & title: "+ isbn + " & " + title);
            //printResultSet(resultSet);
            fillSearch(resultSet);

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            showDialogue(e.getMessage(), null, "Error");
        }
    }

    private void fillSearch(ResultSet r) {
        observableList.clear();
        col_isbn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        col_title.setCellValueFactory(new PropertyValueFactory<>("Title"));
        col_author.setCellValueFactory(new PropertyValueFactory<>("AuthName"));
        col_pubId.setCellValueFactory(new PropertyValueFactory<>("PublisherID"));
        col_pubYear.setCellValueFactory(new PropertyValueFactory<>("PubYear"));
        col_price.setCellValueFactory(new PropertyValueFactory<>("Price"));
        col_quantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        col_threshold.setCellValueFactory(new PropertyValueFactory<>("Threshold"));
        col_category.setCellValueFactory(new PropertyValueFactory<>("Category"));

        col_isbn.setCellFactory(TextFieldTableCell.forTableColumn());
        col_title.setCellFactory(TextFieldTableCell.forTableColumn());
        col_author.setCellFactory(TextFieldTableCell.forTableColumn());
        col_pubId.setCellFactory(TextFieldTableCell.forTableColumn());
        col_pubYear.setCellFactory(TextFieldTableCell.forTableColumn());
        col_price.setCellFactory(TextFieldTableCell.forTableColumn());
        col_quantity.setCellFactory(TextFieldTableCell.forTableColumn());
        col_threshold.setCellFactory(TextFieldTableCell.forTableColumn());
        col_category.setCellFactory(TextFieldTableCell.forTableColumn());

        try {
            r.beforeFirst();
            String prevISBN = "";
            Book book = null;
            while (r.next()) {
                String isbn = r.getString("ISBN");
                String authName = r.getString("AuthName");

                if (isbn.equals(prevISBN) && book != null) {
                    book.setAuthName(book.getAuthName()+", "+authName);
                } else {
                    prevISBN = isbn;
                    book = new Book(isbn, r.getString("Title"), authName,
                            r.getString("PublisherID"), r.getString("PubYear"),
                            r.getString("Price"), r.getString("Quantity"),
                            r.getString("Threshold"), r.getString("Category"));

                    observableList.add(book);
                }
            }

            search_tableView.setItems(observableList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editBook(TableColumn.CellEditEvent<Book, String> bookStringCellEditEvent) {
        Book book = search_tableView.getSelectionModel().getSelectedItem();

        ObservableList<TablePosition> list = search_tableView.getSelectionModel().getSelectedCells();
        String selectedCol = list.get(0).getTableColumn().getText();
        String newValue = bookStringCellEditEvent.getNewValue();

        if (!selectedCol.equals("Author")) {
            String query = "UPDATE BOOK SET "+selectedCol+" = ? WHERE ISBN = ?";
            try {
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, newValue);
                preparedStatement.setString(2, book.getISBN());
                preparedStatement.executeUpdate();

                showDialogue("Book Updated", null, "Information");
                System.out.println("Successful update " +selectedCol+" of book with ISBN : "+ book.getISBN());

            } catch (SQLException e) {
                System.err.println(e.getMessage());
                if (selectedCol.equals("PublisherID"))
                    showDialogue("Publisher ID not found!", null, "Error");
                else
                    showDialogue(e.getMessage(), null, "Error");
            }
        } else {
            // update author name in author table
            if (book.getAuthName() == null) { // book has no author
                String query = "INSERT INTO AUTHOR (ISBN, AuthName) VALUES (?, ?)";
                try {
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, book.getISBN());
                    preparedStatement.setString(2, newValue);
                    preparedStatement.executeUpdate();

                    showDialogue("Book Updated", null, "Information");
                    System.out.println("Successful add book author with ISBN & author: "+ book.getISBN()+" "+ newValue);

                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                    showDialogue(e.getMessage(), null, "Error");
                }
            } else {
                String query = "UPDATE AUTHOR SET AuthName = ? WHERE ISBN = ? AND AuthName = ?";
                String oldAuth = bookStringCellEditEvent.getOldValue();
                try {
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, newValue);
                    preparedStatement.setString(2, book.getISBN());
                    preparedStatement.setString(3, oldAuth);
                    preparedStatement.executeUpdate();

                    showDialogue("Book Updated", null, "Information");
                    System.out.println("Successful update book author with ISBN & author: "+ book.getISBN()+ " & "+oldAuth);

                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                    showDialogue(e.getMessage(), null, "Error");
                }
            }
        }
    }

    public void promoteUser() {
        String userEmail = promote_email_tf.getText();

        if (userEmail.isEmpty()) {
            showDialogue("Add user email!", null, "Error");
            System.err.println("promote missing email failed!");
            return;
        }

        String query = "INSERT INTO MANAGERS (Email) " +
                "VALUES (?);";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userEmail);
            preparedStatement.executeUpdate();

            showDialogue("User Promoted", null, "Information");
            System.out.println("Successful promote user with email: "+ userEmail);

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            if (e.getMessage().contains("Duplicate entry"))
                showDialogue("Email is already a manager", null, "Error");
            else
                showDialogue("Wrong email!", null, "Error");
        }
    }

    public void goToCustomer() throws IOException {
        SceneShower.getInstance().showScene("Customer");
    }

    public void close() throws IOException {
        SceneShower.getInstance().closeMainStage();
    }
}
