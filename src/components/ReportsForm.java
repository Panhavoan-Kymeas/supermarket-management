package components;

import javax.swing.*;
import java.awt.*;

public class ReportsForm extends JPanel {

    private JComboBox<String> reportTypeCombo;
    private JButton generateReportButton;
    private JPanel parameterPanel;
    private JTextArea reportDisplayArea;
    private JScrollPane scrollPane;

    // ===== Parameter inputs =====
    private JLabel fromLabel, toLabel;
    private JTextField fromDateField, toDateField;

    public ReportsForm() {
        setLayout(new BorderLayout(10, 10));

        // ===== Top Panel: Report Selection and Parameters =====
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        // Report selection subpanel
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        reportTypeCombo = new JComboBox<>(
                new String[]{"Daily Sales Report", "Low Stock Inventory Report", "Top 10 Selling Products Report"});
        generateReportButton = new JButton("Generate Report");
        selectionPanel.add(new JLabel("Select Report:"));
        selectionPanel.add(reportTypeCombo);
        selectionPanel.add(generateReportButton);

        // Parameter panel (for optional inputs like date)
        parameterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        parameterPanel.setBorder(BorderFactory.createTitledBorder("Report Parameters"));
        // Initial update based on default selection
        updateParameterPanel(reportTypeCombo.getSelectedItem().toString());

        // Add listener to update panel when report type changes
        reportTypeCombo.addActionListener(_ -> {
            String selected = reportTypeCombo.getSelectedItem().toString();
            updateParameterPanel(selected);
        });

        topPanel.add(selectionPanel);
        topPanel.add(parameterPanel);
        add(topPanel, BorderLayout.NORTH);

        // ===== Center Panel: Report Display =====
        reportDisplayArea = new JTextArea();
        reportDisplayArea.setEditable(false);
        reportDisplayArea.setLineWrap(true);
        reportDisplayArea.setWrapStyleWord(true);
        scrollPane = new JScrollPane(reportDisplayArea);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Dynamically updates the parameter panel based on selected report type
     */
    public void updateParameterPanel(String reportType) {
        parameterPanel.removeAll(); // Clear old inputs

        switch (reportType) {
            case "Daily Sales Report":
                fromLabel = new JLabel("Select Date (YYYY-MM-DD):");
                fromDateField = new JTextField(10);
                parameterPanel.add(fromLabel);
                parameterPanel.add(fromDateField);
                break;

            case "Top 10 Selling Products Report":
                fromLabel = new JLabel("From (YYYY-MM-DD):");
                toLabel = new JLabel("To (YYYY-MM-DD):");
                fromDateField = new JTextField(10);
                toDateField = new JTextField(10);
                parameterPanel.add(fromLabel);
                parameterPanel.add(fromDateField);
                parameterPanel.add(toLabel);
                parameterPanel.add(toDateField);
                break;

            case "Low Stock Inventory Report":
                parameterPanel.add(new JLabel("No parameters required"));
                break;
        }

        parameterPanel.revalidate();
        parameterPanel.repaint();
    }

    // ===== Getters =====
    public JComboBox<String> getReportTypeCombo() {
        return reportTypeCombo;
    }

    public JButton getGenerateReportButton() {
        return generateReportButton;
    }

    public JPanel getParameterPanel() {
        return parameterPanel;
    }

    public JTextArea getReportDisplayArea() {
        return reportDisplayArea;
    }

    public JTextField getFromDateField() {
        return fromDateField;
    }

    public JTextField getToDateField() {
        return toDateField;
    }
}
