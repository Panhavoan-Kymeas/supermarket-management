package events;

import components.ProductManagementForm;
import models.Product;
import db.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProductEventHandler implements ActionListener {

    private ProductManagementForm form;

    public ProductEventHandler(ProductManagementForm form) {
        this.form = form;
        form.getAddButton().addActionListener(this);
        form.getUpdateButton().addActionListener(this);
        form.getDeleteButton().addActionListener(this);
        form.getClearButton().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == form.getAddButton()) addProduct();
        else if (e.getSource() == form.getUpdateButton()) updateProduct();
        else if (e.getSource() == form.getDeleteButton()) deleteProduct();
        else if (e.getSource() == form.getClearButton()) clearFields();
    }

    private void addProduct() {
        Product p = new Product(
                null,
                form.getProductNameField().getText(),
                form.getCategoryField().getText(),
                Double.parseDouble(form.getPriceField().getText()),
                Integer.parseInt(form.getStockField().getText()),
                Integer.parseInt(form.getReorderField().getText()),
                form.getSupplierIdField().getText()
        );

        String sql = "INSERT INTO Products (ProductName, Category, Price, StockQuantity, ReorderLevel, SupplierID) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getName());
            ps.setString(2, p.getCategory());
            ps.setDouble(3, p.getPrice());
            ps.setInt(4, p.getStockQuantity());
            ps.setInt(5, p.getReorderLevel());
            if (p.getSupplierId().isEmpty()) ps.setNull(6, java.sql.Types.INTEGER);
            else ps.setInt(6, Integer.parseInt(p.getSupplierId()));

            ps.executeUpdate();
            JOptionPane.showMessageDialog(form, "Product added successfully!");
            refreshTable();
            clearFields();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(form, "Error: " + ex.getMessage());
        }
    }

    private void updateProduct() {
        String idText = form.getProductIdField().getText();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(form, "Please select a product to update.");
            return;
        }

        Product p = new Product(
                idText,
                form.getProductNameField().getText(),
                form.getCategoryField().getText(),
                Double.parseDouble(form.getPriceField().getText()),
                Integer.parseInt(form.getStockField().getText()),
                Integer.parseInt(form.getReorderField().getText()),
                form.getSupplierIdField().getText()
        );

        String sql = "UPDATE Products SET ProductName = ?, Category = ?, Price = ?, StockQuantity = ?, ReorderLevel = ?, SupplierID = ? WHERE ProductID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getName());
            ps.setString(2, p.getCategory());
            ps.setDouble(3, p.getPrice());
            ps.setInt(4, p.getStockQuantity());
            ps.setInt(5, p.getReorderLevel());
            if (p.getSupplierId().isEmpty()) ps.setNull(6, java.sql.Types.INTEGER);
            else ps.setInt(6, Integer.parseInt(p.getSupplierId()));
            ps.setInt(7, Integer.parseInt(p.getId()));

            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(form, "Product updated successfully!");
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(form, "Product ID not found.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(form, "Error: " + ex.getMessage());
        }
    }

    private void deleteProduct() {
        String idText = form.getProductIdField().getText();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(form, "Please select a product to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(form, "Are you sure you want to delete this product?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        String sql = "DELETE FROM Products WHERE ProductID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(idText));
            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(form, "Product deleted successfully!");
                clearFields();
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(form, "Product ID not found.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(form, "Error: " + ex.getMessage());
        }
    }

    public void refreshTable() {
        DefaultTableModel model = form.getTableModel();
        model.setRowCount(0);

        String sql = "SELECT * FROM Products";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("ProductID"),
                        rs.getString("ProductName"),
                        rs.getString("Category"),
                        rs.getDouble("Price"),
                        rs.getInt("StockQuantity"),
                        rs.getInt("ReorderLevel"),
                        rs.getInt("SupplierID")
                };
                model.addRow(row);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void clearFields() {
        form.getProductIdField().setText("");
        form.getProductNameField().setText("");
        form.getCategoryField().setText("");
        form.getPriceField().setText("");
        form.getStockField().setText("");
        form.getReorderField().setText("");
        form.getSupplierIdField().setText("");
    }
}
