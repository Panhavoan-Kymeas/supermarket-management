package components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class POSForm extends JPanel {

    private JTextField productIdField;
    private JButton addItemButton;
    private JTable saleItemsTable;
    private DefaultTableModel tableModel;
    private JLabel totalLabel;
    private JButton processPaymentButton;
    private JComboBox<String> paymentMethodCombo;

    public POSForm() {
        setLayout(new BorderLayout(10, 10));

        // Top panel for input
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        productIdField = new JTextField(15);
        addItemButton = new JButton("Add Item");
        topPanel.add(new JLabel("Product ID:"));
        topPanel.add(productIdField);
        topPanel.add(addItemButton);
        add(topPanel, BorderLayout.NORTH);

        // Center table for items
        String[] columnNames = { "Product ID", "Product Name", "Quantity", "Price", "Subtotal" };
        tableModel = new DefaultTableModel(columnNames, 0);
        saleItemsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(saleItemsTable);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom panel for totals and payment
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        bottomPanel.add(new JLabel("Total Amount:"), gbc);

        gbc.gridx = 1;
        totalLabel = new JLabel("0.00");
        bottomPanel.add(totalLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        bottomPanel.add(new JLabel("Payment Method:"), gbc);

        gbc.gridx = 1;
        paymentMethodCombo = new JComboBox<>(new String[] { "Cash", "Card" });
        bottomPanel.add(paymentMethodCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        processPaymentButton = new JButton("Process Payment");
        bottomPanel.add(processPaymentButton, gbc);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Getters for POSForm components
    public JTextField getProductIdField() {
        return productIdField;
    }

    public JButton getAddItemButton() {
        return addItemButton;
    }

    public JTable getSaleItemsTable() {
        return saleItemsTable;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public JLabel getTotalLabel() {
        return totalLabel;
    }

    public JButton getProcessPaymentButton() {
        return processPaymentButton;
    }

    public JComboBox<String> getPaymentMethodCombo() {
        return paymentMethodCombo;
    }
}
