package models;

import java.util.List;

public class Sale {
    private String employeeId;
    private List<SaleItem> items;
    private double totalAmount;
    private String paymentMethod;

    public Sale(String employeeId, List<SaleItem> items, String paymentMethod) {
        this.employeeId = employeeId;
        this.items = items;
        this.paymentMethod = paymentMethod;
        this.totalAmount = items.stream().mapToDouble(SaleItem::getSubtotal).sum();
    }

    public String getEmployeeId() { return employeeId; }
    public List<SaleItem> getItems() { return items; }
    public double getTotalAmount() { return totalAmount; }
    public String getPaymentMethod() { return paymentMethod; }
}
