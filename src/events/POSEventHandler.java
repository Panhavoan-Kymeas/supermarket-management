package events;

import components.POSForm;
import db.DatabaseConnection;
import models.Product;
import models.Sale;
import models.SaleItem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class POSEventHandler implements ActionListener {

    private POSForm form;
    private List<SaleItem> currentSaleItems;

    public POSEventHandler(POSForm form) {
        this.form = form;
        currentSaleItems = new ArrayList<>();

        form.getAddItemButton().addActionListener(this);
        form.getProcessPaymentButton().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == form.getAddItemButton()) addItemToSale();
        else if (e.getSource() == form.getProcessPaymentButton()) processPayment();
    }

    private void addItemToSale() {
        String productId = form.getProductIdField().getText().trim();
        if (productId.isEmpty()) {
            JOptionPane.showMessageDialog(form, "Enter Product ID.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM Products WHERE ProductID = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, Integer.parseInt(productId));
                ResultSet rs = ps.executeQuery();

                if (!rs.next()) {
                    JOptionPane.showMessageDialog(form, "Product not found.");
                    return;
                }

                Product product = new Product(
                        String.valueOf(rs.getInt("ProductID")),
                        rs.getString("ProductName"),
                        rs.getString("Category"),
                        rs.getDouble("Price"),
                        rs.getInt("StockQuantity"),
                        rs.getInt("ReorderLevel"),
                        String.valueOf(rs.getInt("SupplierID"))
                );

                String qtyStr = JOptionPane.showInputDialog(form, "Enter quantity:");
                if (qtyStr == null) return; // user canceled
                int quantity = Integer.parseInt(qtyStr);
                if (quantity <= 0 || quantity > product.getStockQuantity()) {
                    JOptionPane.showMessageDialog(form, "Invalid quantity.");
                    return;
                }

                // Check if already in table
                boolean found = false;
                for (SaleItem item : currentSaleItems) {
                    if (item.getProduct().getId().equals(product.getId())) {
                        item.setQuantity(item.getQuantity() + quantity);
                        found = true;
                        break;
                    }
                }
                if (!found) currentSaleItems.add(new SaleItem(product, quantity));

                refreshTable();
                form.getProductIdField().setText("");

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(form, "Error: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(form, "Invalid number format.");
        }
    }

    public void refreshTable() {
        DefaultTableModel model = form.getTableModel();
        model.setRowCount(0);
        double total = 0;
        for (SaleItem item : currentSaleItems) {
            model.addRow(new Object[]{
                    item.getProduct().getId(),
                    item.getProduct().getName(),
                    item.getQuantity(),
                    item.getProduct().getPrice(),
                    item.getSubtotal()
            });
            total += item.getSubtotal();
        }
        form.getTotalLabel().setText(String.format("%.2f", total));
    }

    private void processPayment() {
        if (currentSaleItems.isEmpty()) {
            JOptionPane.showMessageDialog(form, "No items in the sale.");
            return;
        }

        String paymentMethod = form.getPaymentMethodCombo().getSelectedItem().toString();
        String employeeId = "1"; // TODO: get logged-in employee ID dynamically

        Sale sale = new Sale(employeeId, currentSaleItems, paymentMethod);

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            // Insert sale
            String sqlSale = "INSERT INTO Sales (SaleDate, EmployeeID, TotalAmount, PaymentMethod) VALUES (GETDATE(), ?, ?, ?)";
            try (PreparedStatement psSale = conn.prepareStatement(sqlSale, Statement.RETURN_GENERATED_KEYS)) {
                psSale.setInt(1, Integer.parseInt(sale.getEmployeeId()));
                psSale.setDouble(2, sale.getTotalAmount());
                psSale.setString(3, sale.getPaymentMethod());
                psSale.executeUpdate();

                ResultSet keys = psSale.getGeneratedKeys();
                if (!keys.next()) throw new SQLException("Failed to get SaleID.");
                int saleId = keys.getInt(1);

                // Insert sale items and update stock
                String sqlItem = "INSERT INTO SaleItems (SaleID, ProductID, QuantitySold, Price) VALUES (?, ?, ?, ?)";
                try (PreparedStatement psItem = conn.prepareStatement(sqlItem)) {
                    for (SaleItem item : sale.getItems()) {
                        psItem.setInt(1, saleId);
                        psItem.setInt(2, Integer.parseInt(item.getProduct().getId()));
                        psItem.setInt(3, item.getQuantity());
                        psItem.setDouble(4, item.getProduct().getPrice());
                        psItem.addBatch();

                        String sqlUpdateStock = "UPDATE Products SET StockQuantity = StockQuantity - ? WHERE ProductID = ?";
                        try (PreparedStatement psStock = conn.prepareStatement(sqlUpdateStock)) {
                            psStock.setInt(1, item.getQuantity());
                            psStock.setInt(2, Integer.parseInt(item.getProduct().getId()));
                            psStock.executeUpdate();
                        }
                    }
                    psItem.executeBatch();
                }
            }

            conn.commit();
            JOptionPane.showMessageDialog(form, "Payment processed successfully!");

            // Generate receipt
            generateReceipt(sale);

            currentSaleItems.clear();
            refreshTable();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(form, "Error: " + ex.getMessage());
        }
    }

    private void generateReceipt(Sale sale) {
        StringBuilder receipt = new StringBuilder();
        receipt.append("        My Store POS\n");
        receipt.append("Date: ").append(java.time.LocalDateTime.now()).append("\n");
        receipt.append("--------------------------------\n");
        receipt.append("ID\tName\tQty\tPrice\tSubtotal\n");

        for (SaleItem item : sale.getItems()) {
            receipt.append(item.getProduct().getId()).append("\t");
            receipt.append(item.getProduct().getName()).append("\t");
            receipt.append(item.getQuantity()).append("\t");
            receipt.append(item.getProduct().getPrice()).append("\t");
            receipt.append(item.getSubtotal()).append("\n");
        }

        receipt.append("--------------------------------\n");
        receipt.append("Total: ").append(String.format("%.2f", sale.getTotalAmount())).append("\n");
        receipt.append("Payment Method: ").append(sale.getPaymentMethod()).append("\n");
        receipt.append("Thank you for your purchase!\n");

        JTextArea receiptArea = new JTextArea(receipt.toString());
        receiptArea.setEditable(false);
        JOptionPane.showMessageDialog(form, new JScrollPane(receiptArea), "Receipt", JOptionPane.INFORMATION_MESSAGE);
    }
}
