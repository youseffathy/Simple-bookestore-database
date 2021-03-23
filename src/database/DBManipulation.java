package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class DBManipulation {

	private PreparedStatement preparedStatementstmt;
	private Connection connection;

	public DBManipulation(Connection connection) {
		this.connection = connection;
	}

	/*
	 * add a new book with fields in arraylist book. any non specified field can be null in book arraylist.
	 * return a number of affected row (should be 1).
	 * throw an sql exception in case of dublicating any candidate key or specifing a null value for a field with not null key.
	 */
	public int addNewBook(ArrayList<String> book) throws SQLException {

		String query = "INSERT INTO bookstore.BOOK VALUES (?, ?, ?, ?, ?, ?, ?, ?) ";
		preparedStatementstmt = connection.prepareStatement(query);
		for (int i = 0; i < book.size(); i++) {
			preparedStatementstmt.setString(i + 1, book.get(i));
		}
		return preparedStatementstmt.executeUpdate();

	}

	/*
	 * modify book with its ISBN as selection condition and new modifications in arraylist book.
	 * return a number of affected row (should be 1).
	 * throw an sql exception in case of dublicating any candidate key or specifing a null value for a field with not null key.
	 */
	public int modifyBook(String ISBN, ArrayList<String> book) throws SQLException {
		String query = "UPDATE bookstore.BOOK SET ISBN = ?, Title = ?, PublisherID = ?, PubYear = ?, Price = ?, Quantity = ?, "
				+ "Threshold = ?, Category = ?  WHERE  ISBN = ?";
		preparedStatementstmt = connection.prepareStatement(query);
		for (int i = 0; i < book.size(); i++) {
			preparedStatementstmt.setString(i + 1, book.get(i));
		}
		// add selection condition parameter : ISBN
		preparedStatementstmt.setString(9, ISBN);
		return preparedStatementstmt.executeUpdate();

	}

	/*
	 * adding order for a book with ISBN specified in arraylist order.
	 * return a number of affected row (should be 1).
	 * throw an sql exception in the case of duplicating an order with the same ISBN or specifing 
	 * a null value for a field with not null key.  
	 */
	public int placeOrder(ArrayList<String> order) throws SQLException {
		String query = "INSERT INTO bookstore.ORDERS VALUES (?, ?, ?, ?)";
		preparedStatementstmt = connection.prepareStatement(query);
		for (int i = 0; i < order.size(); i++) {
			preparedStatementstmt.setString(i + 1, order.get(i));
		}
		return preparedStatementstmt.executeUpdate();
	}

	/*
	 * confirm an order using ISBN of the book.
	 * return a number of affected row (should be 1).
	 * throw an sql exception in the case of confirming a non existing order
	 * or specifing a null value for a field with not null key.
	 */
	public int confrimOrder(String ISBN) throws SQLException {
		String query = "DELETE FROM bookstore.ORDERS WHERE ISBN = ?";
		preparedStatementstmt = connection.prepareStatement(query);
		preparedStatementstmt.setString(1, ISBN);
		return preparedStatementstmt.executeUpdate();
	}

	/*
	 * search for a book using any field(s) specified in a Map with Key as a label of the field an Value as field value.
	 * return an object of result set contains the result of search operation 
	 * or specifing a null value for a field with not null key.
	 */
	public ResultSet searchForBook(Map<String, String> selecionConditions) throws SQLException {
		String query = "Select * From bookstore.BOOK WHERE ";
		int size = selecionConditions.size();
		int counter = 0;
		for (Map.Entry<String, String> entry : selecionConditions.entrySet()) {
			counter++;
			query += entry.getKey() + " = " + entry.getValue();
			if (counter < size)
				query += " AND ";
		}
		System.out.println(query);
		preparedStatementstmt = connection.prepareStatement(query);
		return preparedStatementstmt.executeQuery();

	}

}
