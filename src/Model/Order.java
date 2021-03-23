package Model;

public class Order {
    private String OrderID;
    private String ISBN;
    private String bookTitle;
    private String Quantity;
    private String OrderDate;

    public Order(String orderID, String ISBN, String bookTitle, String quantity, String orderDate) {
        OrderID = orderID;
        this.ISBN = ISBN;
        this.bookTitle = bookTitle;
        Quantity = quantity;
        OrderDate = orderDate;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

}
