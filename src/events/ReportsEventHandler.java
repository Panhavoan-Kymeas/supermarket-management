package events;

import components.ReportsForm;
import db.DatabaseConnection;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ReportsEventHandler implements ActionListener {

    private ReportsForm form;

    public ReportsEventHandler(ReportsForm form) {
        this.form = form;
        form.getGenerateReportButton().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String reportType = form.getReportTypeCombo().getSelectedItem().toString();
        switch (reportType) {
            case "Daily Sales Report":
                generateDailySalesReport();
                break;
            case "Low Stock Inventory Report":
                generateLowStockReport();
                break;
            case "Top 10 Selling Products Report":
                generateTopSellingProductsReport();
                break;
        }
    }

    private void generateDailySalesReport() {
        String date = form.getFromDateField().getText();
        if (date == null || date.isEmpty()) {
            JOptionPane.showMessageDialog(form, "Please enter a date.");
            return;
        }

        StringBuilder report = new StringBuilder();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT COUNT(*) AS Transactions, SUM(TotalAmount) AS Revenue FROM Sales WHERE CAST(SaleDate AS DATE) = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, date);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    report.append("Date: ").append(date).append("\n");
                    report.append("Total Transactions: ").append(rs.getInt("Transactions")).append("\n");
                    report.append("Total Revenue: ").append(rs.getDouble("Revenue")).append("\n");
                } else {
                    report.append("No sales found for this date.\n");
                }
            }

            // Optional: breakdown by product category
            sql = "SELECT p.Category, SUM(si.QuantitySold) AS TotalSold, SUM(si.QuantitySold*p.Price) AS Revenue " +
                  "FROM SaleItems si " +
                  "JOIN Products p ON si.ProductID = p.ProductID " +
                  "JOIN Sales s ON si.SaleID = s.SaleID " +
                  "WHERE CAST(s.SaleDate AS DATE) = ? " +
                  "GROUP BY p.Category";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, date);
                ResultSet rs = ps.executeQuery();
                report.append("\nSales by Category:\n");
                while (rs.next()) {
                    report.append(rs.getString("Category"))
                          .append(": Sold ").append(rs.getInt("TotalSold"))
                          .append(", Revenue ").append(rs.getDouble("Revenue")).append("\n");
                }
            }

            form.getReportDisplayArea().setText(report.toString());

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(form, "Error generating report: " + ex.getMessage());
        }
    }

    private void generateLowStockReport() {
        StringBuilder report = new StringBuilder("Products with low stock:\n\n");
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT ProductID, ProductName, StockQuantity, ReorderLevel FROM Products WHERE StockQuantity <= ReorderLevel";
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    report.append("ID: ").append(rs.getInt("ProductID"))
                          .append(", Name: ").append(rs.getString("ProductName"))
                          .append(", Stock: ").append(rs.getInt("StockQuantity"))
                          .append(", Reorder Level: ").append(rs.getInt("ReorderLevel"))
                          .append("\n");
                }
            }
            form.getReportDisplayArea().setText(report.toString());
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(form, "Error generating report: " + ex.getMessage());
        }
    }

    private void generateTopSellingProductsReport() {
        String from = form.getFromDateField().getText();
        String to = form.getToDateField().getText();

        if (from == null || from.isEmpty() || to == null || to.isEmpty()) {
            JOptionPane.showMessageDialog(form, "Please enter both From and To dates.");
            return;
        }

        StringBuilder report = new StringBuilder("Top 10 Selling Products:\n\n");
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT TOP 10 p.ProductName, SUM(si.QuantitySold) AS TotalSold, SUM(si.QuantitySold*p.Price) AS Revenue " +
                         "FROM SaleItems si " +
                         "JOIN Products p ON si.ProductID = p.ProductID " +
                         "JOIN Sales s ON si.SaleID = s.SaleID " +
                         "WHERE CAST(s.SaleDate AS DATE) BETWEEN ? AND ? " +
                         "GROUP BY p.ProductName " +
                         "ORDER BY TotalSold DESC";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, from);
                ps.setString(2, to);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    report.append(rs.getString("ProductName"))
                          .append(": Sold ").append(rs.getInt("TotalSold"))
                          .append(", Revenue ").append(rs.getDouble("Revenue")).append("\n");
                }
            }
            form.getReportDisplayArea().setText(report.toString());
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(form, "Error generating report: " + ex.getMessage());
        }
    }
}
