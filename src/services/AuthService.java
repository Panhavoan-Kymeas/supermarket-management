package services;

import models.Employee;
import org.mindrot.jbcrypt.BCrypt;
import db.DatabaseConnection;

import java.sql.*;

public class AuthService {

    public static Employee login(String username, String password) throws SQLException {
        String sql = "SELECT * FROM Employees WHERE Username = ? AND IsActive = 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String hash = rs.getString("PasswordHash");
                if (BCrypt.checkpw(password, hash)) {
                    // Return full employee object
                    return new Employee(
                            rs.getString("EmployeeID"),
                            rs.getString("Username"),
                            rs.getString("PasswordHash"), // stored hash, not really needed in session
                            rs.getString("FullName"),
                            rs.getString("Role"),
                            rs.getString("Email"),
                            rs.getString("PhoneNumber")
                    );
                }
            }
        }
        return null; // invalid login
    }
}
