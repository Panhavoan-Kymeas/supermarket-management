package components;

import javax.swing.*;
import java.awt.*;
import models.Session;
import models.Employee;
import events.*;

public class MainAppFrame extends JFrame {

    private JPanel mainPanel;
    private CardLayout cardLayout;

    public MainAppFrame() {
        setTitle("Supermarket Inventory & Sales System");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Get current logged-in user
        Employee current = Session.getCurrentUser();

        // CardLayout for switching between forms
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create instances of each form panel
        JPanel posPanel = new POSForm();
        POSEventHandler posHandler = new POSEventHandler((POSForm) posPanel);
        posHandler.refreshTable();

        JPanel productPanel = new ProductManagementForm();
        ProductEventHandler productHandler = new ProductEventHandler((ProductManagementForm) productPanel);
        productHandler.refreshTable();

        JPanel employeePanel = new EmployeeManagementForm();
        EmployeeEventHandler employeeHandler = new EmployeeEventHandler((EmployeeManagementForm) employeePanel);
        employeeHandler.refreshTable();

        JPanel supplierPanel = new SupplierManagementForm();
        SupplierEventHandler supplierHandler = new SupplierEventHandler((SupplierManagementForm) supplierPanel);
        supplierHandler.refreshTable();

        JPanel reportsPanel = new ReportsForm();
        new ReportsEventHandler((ReportsForm) reportsPanel);

        // Add panels to the main panel
        mainPanel.add(posPanel, "POS");
        mainPanel.add(productPanel, "PRODUCT_MANAGEMENT");
        mainPanel.add(employeePanel, "EMPLOYEE_MANAGEMENT");
        mainPanel.add(supplierPanel, "SUPPLIER_MANAGEMENT");
        mainPanel.add(reportsPanel, "REPORTS");

        // Sidebar with buttons
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(5, 1, 5, 5));

        JButton posButton = new JButton("POS");
        posButton.addActionListener(_ -> showCard("POS"));
        sidebar.add(posButton);

        JButton productButton = new JButton("Products");
        productButton.addActionListener(_ -> showCard("PRODUCT_MANAGEMENT"));
        sidebar.add(productButton);

        JButton employeeButton = new JButton("Employees");
        employeeButton.addActionListener(_ -> showCard("EMPLOYEE_MANAGEMENT"));
        sidebar.add(employeeButton);

        JButton supplierButton = new JButton("Suppliers");
        supplierButton.addActionListener(_ -> showCard("SUPPLIER_MANAGEMENT"));
        sidebar.add(supplierButton);

        JButton reportsButton = new JButton("Reports");
        reportsButton.addActionListener(_ -> showCard("REPORTS"));
        sidebar.add(reportsButton);

        // Role-based access: only managers can access employee management
        if (current != null && !"Manager".equals(current.getRole())) {
            employeeButton.setEnabled(false); // disable button
            // productButton.setEnabled(false);
            // supplierButton.setEnabled(false);
            reportsButton.setEnabled(false);
        }

        // Layout
        setLayout(new BorderLayout());
        add(sidebar, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);

        // Show POS form first
        showCard("POS");
    }

    // Switch between forms with access check
    public void showCard(String cardName) {
        Employee current = Session.getCurrentUser();

        if ("EMPLOYEE_MANAGEMENT".equals(cardName) && current != null && !"Manager".equals(current.getRole())) {
            JOptionPane.showMessageDialog(this, "Access denied. Managers only.");
            return;
        }

        cardLayout.show(mainPanel, cardName);
    }
}
