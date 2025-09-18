package components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class EmployeeManagementForm extends JPanel {

    private JTextField employeeIdField, usernameField, fullNameField, emailField, phoneField, passwordField;
    private JComboBox<String> roleCombo;
    private JButton addButton, updateButton, deleteButton, clearButton;
    private JTable employeeTable;
    private DefaultTableModel tableModel;

    public EmployeeManagementForm() {
        setLayout(new BorderLayout(10, 10));

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(8, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Employee Details"));

        employeeIdField = new JTextField();
        employeeIdField.setEditable(false);

        usernameField = new JTextField();
        fullNameField = new JTextField();
        passwordField = new JPasswordField();
        roleCombo = new JComboBox<>(new String[] { "Manager", "Cashier" });
        emailField = new JTextField();
        phoneField = new JTextField();

        inputPanel.add(new JLabel("Employee ID:"));
        inputPanel.add(employeeIdField);
        inputPanel.add(new JLabel("Username:"));
        inputPanel.add(usernameField);
        inputPanel.add(new JLabel("Full Name:"));
        inputPanel.add(fullNameField);
        inputPanel.add(new JLabel("Password:"));
        inputPanel.add(passwordField);
        inputPanel.add(new JLabel("Role:"));
        inputPanel.add(roleCombo);
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(emailField);
        inputPanel.add(new JLabel("Phone Number:"));
        inputPanel.add(phoneField);

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
        String[] columnNames = { "ID", "Username", "Full Name", "Role", "Email", "Phone" };
        tableModel = new DefaultTableModel(columnNames, 0);
        employeeTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(employeeTable);

        add(inputPanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);

        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && employeeTable.getSelectedRow() != -1) {
                int row = employeeTable.getSelectedRow();
                employeeIdField.setText(tableModel.getValueAt(row, 0).toString());
                usernameField.setText(tableModel.getValueAt(row, 1).toString());
                fullNameField.setText(tableModel.getValueAt(row, 2).toString());
                roleCombo.setSelectedItem(tableModel.getValueAt(row, 3).toString());
                emailField.setText(tableModel.getValueAt(row, 4).toString());
                phoneField.setText(tableModel.getValueAt(row, 5).toString());
                passwordField.setText(""); // Do not prefill password for security
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
    public JTextField getEmployeeIdField() {
        return employeeIdField;
    }

    public JTextField getUsernameField() {
        return usernameField;
    }

    public JTextField getFullNameField() {
        return fullNameField;
    }

    public JTextField getEmailField() {
        return emailField;
    }

    public JTextField getPhoneField() {
        return phoneField;
    }

    public JPasswordField getPasswordField() {
        return (JPasswordField) passwordField;
    }

    public JComboBox<String> getRoleCombo() {
        return roleCombo;
    }

    // Table
    public JTable getEmployeeTable() {
        return employeeTable;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

}
