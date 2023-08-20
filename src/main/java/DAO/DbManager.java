package DAO;

import java.sql.*;

public class DbManager {

    private static final String URL = "jdbc:mysql://localhost:3306/ripetizioni_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static Connection conn = null;

    static {
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            System.out.println("Driver successfully registered");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public Connection openConnection() throws SQLException {
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connection opened");
            return conn;
        } catch (SQLException e) {
            System.out.println("Connection Error: " + e.getMessage());
            return null;
        }
    }

    public void closeConnection() throws SQLException {
        conn.close();
    }
}
