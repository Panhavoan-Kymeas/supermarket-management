package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=Supermarket;encrypt=true;trustServerCertificate=true;";
    private static final String USER = "User_Supermarket";
    private static final String PASSWORD = "Voan1281";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
