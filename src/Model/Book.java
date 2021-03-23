package Model;

public class Book {
    private String ISBN;
    private String Title;
    private String AuthName;
    private String PublisherID;
    private String PubYear;
    private String Price;
    private String Quantity;
    private String Threshold;
    private String Category;

    public Book(String ISBN, String title, String authName, String publisherID, String pubYear, String price,
                String quantity, String threshold, String category) {
        this.ISBN = ISBN;
        Title = title;
        AuthName = authName;
        PublisherID = publisherID;
        PubYear = pubYear;
        Price = price;
        Quantity = quantity;
        Threshold = threshold;
        Category = category;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPublisherID() {
        return PublisherID;
    }

    public void setPublisherID(String publisherID) {
        PublisherID = publisherID;
    }

    public String getPubYear() {
        return PubYear;
    }

    public void setPubYear(String pubYear) {
        PubYear = pubYear;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getThreshold() {
        return Threshold;
    }

    public void setThreshold(String threshold) {
        Threshold = threshold;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getAuthName() {
        return AuthName;
    }

    public void setAuthName(String authName) {
        AuthName = authName;
    }
}