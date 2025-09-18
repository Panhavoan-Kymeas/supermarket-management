package components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SupplierManagementForm extends JPanel {

    private JTextField supplierIdField, supplierNameField, contactPersonField, emailField, phoneField;
    private JButton addButton, updateButton, deleteButton, clearButton;
    private JTable supplierTable;
    private DefaultTableModel tableModel;
    private JTextField addressField;

    public SupplierManagementForm() {
        setLayout(new BorderLayout(10, 10));

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Supplier Details"));

        supplierIdField = new JTextField();
        supplierIdField.setEditable(false);

        supplierNameField = new JTextField();
        contactPersonField = new JTextField();
        emailField = new JTextField();
        phoneField = new JTextField();
        addressField = new JTextField();

        inputPanel.add(new JLabel("Supplier ID:"));
        inputPanel.add(supplierIdField);
        inputPanel.add(new JLabel("Supplier Name:"));
        inputPanel.add(supplierNameField);
        inputPanel.add(new JLabel("Contact Person:"));
        inputPanel.add(contactPersonField);
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(emailField);
        inputPanel.add(new JLabel("Phone Number:"));
        inputPanel.add(phoneField);
        inputPanel.add(new JLabel("Address:"));
        inputPanel.add(addressField);

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
        String[] columnNames = { "ID", "Name", "Contact Person", "Email", "Phone", "Address" };
        tableModel = new DefaultTableModel(columnNames, 0);
        supplierTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(supplierTable);

        add(inputPanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);

        supplierTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && supplierTable.getSelectedRow() != -1) {
                int row = supplierTable.getSelectedRow();
                supplierIdField.setText(tableModel.getValueAt(row, 0).toString());
                supplierNameField.setText(tableModel.getValueAt(row, 1).toString());
                contactPersonField.setText(tableModel.getValueAt(row, 2).toString());
                emailField.setText(tableModel.getValueAt(row, 3).toString());
                phoneField.setText(tableModel.getValueAt(row, 4).toString());
                addressField.setText(tableModel.getValueAt(row, 5).toString());
            }
        });

    }

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
    public JTextField getSupplierIdField() {
        return supplierIdField;
    }

    public JTextField getSupplierNameField() {
        return supplierNameField;
    }

    public JTextField getContactPersonField() {
        return contactPersonField;
    }

    public JTextField getEmailField() {
        return emailField;
    }

    public JTextField getPhoneField() {
        return phoneField;
    }

    public JTextField getAddressField() {
        return addressField;
    }

    // Table (optional, if you want to refresh or update it)
    public JTable getSupplierTable() {
        return supplierTable;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

}
