package events;

import components.SupplierManagementForm;
import models.Supplier;
import db.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SupplierEventHandler implements ActionListener {

    private SupplierManagementForm form;

    public SupplierEventHandler(SupplierManagementForm form) {
        this.form = form;
        // Attach listener to buttons
        form.getAddButton().addActionListener(this);
        form.getUpdateButton().addActionListener(this);
        form.getDeleteButton().addActionListener(this);
        form.getClearButton().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == form.getAddButton()) {
            addSupplier();
        } else if (e.getSource() == form.getUpdateButton()) {
            updateSupplier();
        } else if (e.getSource() == form.getDeleteButton()) {
            deleteSupplier();
        } else if (e.getSource() == form.getClearButton()) {
            clearFields();
        }
    }

    private void addSupplier() {
        Supplier s = new Supplier(
                form.getSupplierIdField().getText(),
                form.getSupplierNameField().getText(),
                form.getContactPersonField().getText(),
                form.getEmailField().getText(),
                form.getPhoneField().getText(),
                form.getAddressField().getText());

        String sql = "INSERT INTO Suppliers (SupplierName, ContactPerson, Email, PhoneNumber, Address) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, s.getName());
            ps.setString(2, s.getContactPerson());
            ps.setString(3, s.getEmail());
            ps.setString(4, s.getPhone());
            ps.setString(5, s.getAddress());

            ps.executeUpdate();
            JOptionPane.showMessageDialog(form, "Supplier added successfully!");
            refreshTable();
            clearFields();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(form, "Error: " + ex.getMessage());
        }

    }

    private void updateSupplier() {
        String idText = form.getSupplierIdField().getText();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(form, "Please select a supplier to update.");
            return;
        }

        Supplier s = new Supplier(
                idText,
                form.getSupplierNameField().getText(),
                form.getContactPersonField().getText(),
                form.getEmailField().getText(),
                form.getPhoneField().getText(),
                form.getAddressField().getText());

        String sql = "UPDATE Suppliers SET SupplierName = ?, ContactPerson = ?, Email = ?, PhoneNumber = ?, Address = ? WHERE SupplierID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, s.getName());
            ps.setString(2, s.getContactPerson());
            ps.setString(3, s.getEmail());
            ps.setString(4, s.getPhone());
            ps.setString(5, s.getAddress());
            ps.setInt(6, Integer.parseInt(s.getId())); // ID is INT in DB

            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(form, "Supplier updated successfully!");
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(form, "Supplier ID not found.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(form, "Error: " + ex.getMessage());
        }
    }

    private void deleteSupplier() {
        String supplierId = form.getSupplierIdField().getText();
        if (supplierId.isEmpty()) {
            JOptionPane.showMessageDialog(form, "Please enter Supplier ID to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(form, "Are you sure you want to delete this supplier?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION)
            return;

        String sql = "DELETE FROM suppliers WHERE SupplierID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, supplierId);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(form, "Supplier deleted successfully!");
                clearFields();
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(form, "Supplier ID not found.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(form, "Error: " + ex.getMessage());
        }
    }

    public void refreshTable() {
        DefaultTableModel model = form.getTableModel();
        model.setRowCount(0); // clear table

        String sql = "SELECT * FROM suppliers";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                java.sql.ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("SupplierID"),
                        rs.getString("SupplierName"),
                        rs.getString("ContactPerson"),
                        rs.getString("Email"),
                        rs.getString("PhoneNumber"),
                        rs.getString("Address")
                };
                model.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void clearFields() {
        form.getSupplierIdField().setText("");
        form.getSupplierNameField().setText("");
        form.getContactPersonField().setText("");
        form.getEmailField().setText("");
        form.getPhoneField().setText("");
        form.getAddressField().setText("");
    }
}