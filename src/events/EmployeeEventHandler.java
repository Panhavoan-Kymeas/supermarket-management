package events;

import components.EmployeeManagementForm;
import models.Employee;
import db.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

public class EmployeeEventHandler implements ActionListener {

    private EmployeeManagementForm form;

    public EmployeeEventHandler(EmployeeManagementForm form) {
        this.form = form;
        form.getAddButton().addActionListener(this);
        form.getUpdateButton().addActionListener(this);
        form.getDeleteButton().addActionListener(this);
        form.getClearButton().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == form.getAddButton())
            addEmployee();
        else if (e.getSource() == form.getUpdateButton())
            updateEmployee();
        else if (e.getSource() == form.getDeleteButton())
            deleteEmployee();
        else if (e.getSource() == form.getClearButton())
            clearFields();
    }

    private void addEmployee() {
        String passwordHash = hashPassword(new String(form.getPasswordField().getPassword()));

        Employee emp = new Employee(
                null,
                form.getUsernameField().getText(),
                passwordHash,
                form.getFullNameField().getText(),
                form.getRoleCombo().getSelectedItem().toString(),
                form.getEmailField().getText(),
                form.getPhoneField().getText());

        String sql = "INSERT INTO Employees (Username, PasswordHash, FullName, Role, Email, PhoneNumber, HireDate) " +
                "VALUES (?, ?, ?, ?, ?, ?, GETDATE())";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, emp.getUsername());
            ps.setString(2, emp.getPasswordHash());
            ps.setString(3, emp.getFullName());
            ps.setString(4, emp.getRole());
            ps.setString(5, emp.getEmail());
            ps.setString(6, emp.getPhone());

            ps.executeUpdate();
            JOptionPane.showMessageDialog(form, "Employee added successfully!");
            refreshTable();
            clearFields();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(form, "Error: " + ex.getMessage());
        }
    }

    private void updateEmployee() {
        String idText = form.getEmployeeIdField().getText();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(form, "Please select an employee to update.");
            return;
        }

        String enteredPassword = new String(form.getPasswordField().getPassword());
        boolean updatePassword = !enteredPassword.isEmpty();
        String passwordHash = updatePassword ? hashPassword(enteredPassword) : null;

        String sql = updatePassword
                ? "UPDATE Employees SET Username = ?, PasswordHash = ?, FullName = ?, Role = ?, Email = ?, PhoneNumber = ? WHERE EmployeeID = ?"
                : "UPDATE Employees SET Username = ?, FullName = ?, Role = ?, Email = ?, PhoneNumber = ? WHERE EmployeeID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            int idx = 1;
            ps.setString(idx++, form.getUsernameField().getText());

            if (updatePassword) {
                ps.setString(idx++, passwordHash);
            }

            ps.setString(idx++, form.getFullNameField().getText());
            ps.setString(idx++, form.getRoleCombo().getSelectedItem().toString());
            ps.setString(idx++, form.getEmailField().getText());
            ps.setString(idx++, form.getPhoneField().getText());
            ps.setInt(idx, Integer.parseInt(idText)); // last parameter is EmployeeID

            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(form, "Employee updated successfully!");
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(form, "Employee ID not found.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(form, "Error: " + ex.getMessage());
        }
    }

    private void deleteEmployee() {
        String idText = form.getEmployeeIdField().getText();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(form, "Please select an employee to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(form, "Are you sure you want to delete this employee?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION)
            return;

        String sql = "DELETE FROM Employees WHERE EmployeeID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(idText));
            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(form, "Employee deleted successfully!");
                clearFields();
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(form, "Employee ID not found.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(form, "Error: " + ex.getMessage());
        }
    }

    public void refreshTable() {
        DefaultTableModel model = form.getTableModel();
        model.setRowCount(0);

        String sql = "SELECT EmployeeID, Username, FullName, Role, Email, PhoneNumber FROM Employees";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                java.sql.ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("EmployeeID"),
                        rs.getString("Username"),
                        rs.getString("FullName"),
                        rs.getString("Role"),
                        rs.getString("Email"),
                        rs.getString("PhoneNumber")
                };
                model.addRow(row);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void clearFields() {
        form.getEmployeeIdField().setText("");
        form.getUsernameField().setText("");
        form.getFullNameField().setText("");
        form.getPasswordField().setText("");
        form.getRoleCombo().setSelectedIndex(0);
        form.getEmailField().setText("");
        form.getPhoneField().setText("");
    }

    // Example password hashing (you can replace with a proper hash)
    private String hashPassword(String password) {
        // Simple placeholder hash, use proper hashing (e.g., BCrypt) in production
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }
}
