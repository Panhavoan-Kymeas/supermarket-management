package models;

public class Product {
    private String id;
    private String name;
    private String category;
    private double price;
    private int stockQuantity;
    private int reorderLevel;
    private String supplierId;

    public Product(String id, String name, String category, double price, int stockQuantity, int reorderLevel, String supplierId) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.reorderLevel = reorderLevel;
        this.supplierId = supplierId;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int getStockQuantity() { return stockQuantity; }
    public int getReorderLevel() { return reorderLevel; }
    public String getSupplierId() { return supplierId; }

    // Setters (optional)
    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setPrice(double price) { this.price = price; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
    public void setReorderLevel(int reorderLevel) { this.reorderLevel = reorderLevel; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }
}
