package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/sports_db";
    private static final String USER = "user1";
    private static final String PASSWORD = "password"; // Default password

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Attempting to connect to: " + URL + " with user: " + USER);
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connected successfully!");
            return conn;
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found!");
            System.out.println("Error: " + e.getMessage());
            return null;
        } catch (SQLException e) {
            System.out.println("Connection failed with error: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Error Code: " + e.getErrorCode());
            return null;
        }
    }
}