package components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ProductManagementForm extends JPanel {

    private JTextField productIdField, productNameField, categoryField, priceField, stockField, reorderField,
            supplierIdField;
    private JButton addButton, updateButton, deleteButton, clearButton;
    private JTable productTable;
    private DefaultTableModel tableModel;

    public ProductManagementForm() {
        setLayout(new BorderLayout(10, 10));

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(8, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Product Details"));

        productIdField = new JTextField();
        productIdField.setEditable(false); // Product ID is auto-generated
        productNameField = new JTextField();
        categoryField = new JTextField();
        priceField = new JTextField();
        stockField = new JTextField();
        reorderField = new JTextField();
        supplierIdField = new JTextField();

        inputPanel.add(new JLabel("Product ID:"));
        inputPanel.add(productIdField);
        inputPanel.add(new JLabel("Product Name:"));
        inputPanel.add(productNameField);
        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryField);
        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(priceField);
        inputPanel.add(new JLabel("Stock Quantity:"));
        inputPanel.add(stockField);
        inputPanel.add(new JLabel("Reorder Level:"));
        inputPanel.add(reorderField);
        inputPanel.add(new JLabel("Supplier ID:"));
        inputPanel.add(supplierIdField);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        clearButton = new JButton("Clear");
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        inputPanel.add(buttonPanel);

        // Table Panel
        String[] columnNames = { "ID", "Name", "Category", "Price", "Stock", "Reorder Level", "Supplier ID" };
        tableModel = new DefaultTableModel(columnNames, 0);
        productTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(productTable);

        add(inputPanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);

        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && productTable.getSelectedRow() != -1) {
                int row = productTable.getSelectedRow();
                productIdField.setText(tableModel.getValueAt(row, 0).toString());
                productNameField.setText(tableModel.getValueAt(row, 1).toString());
                categoryField.setText(tableModel.getValueAt(row, 2).toString());
                priceField.setText(tableModel.getValueAt(row, 3).toString());
                stockField.setText(tableModel.getValueAt(row, 4).toString());
                reorderField.setText(tableModel.getValueAt(row, 5).toString());
                supplierIdField.setText(tableModel.getValueAt(row, 6).toString());
            }
        });
    }

    // ===================== Getters =====================

    // Buttons
    public JButton getAddButton() {
        return addButton;
    }

    public JButton getUpdateButton() {
        return updateButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public JButton getClearButton() {
        return clearButton;
    }

    // Text fields
    public JTextField getProductIdField() {
        return productIdField;
    }

    public JTextField getProductNameField() {
        return productNameField;
    }

    public JTextField getCategoryField() {
        return categoryField;
    }

    public JTextField getPriceField() {
        return priceField;
    }

    public JTextField getStockField() {
        return stockField;
    }

    public JTextField getReorderField() {
        return reorderField;
    }

    public JTextField getSupplierIdField() {
        return supplierIdField;
    }

    // Table
    public JTable getProductTable() {
        return productTable;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }
}
