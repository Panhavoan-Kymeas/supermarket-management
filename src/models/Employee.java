package models;

public class Employee {
    private String id;
    private String username;
    private String passwordHash;
    private String fullName;
    private String role;
    private String email;
    private String phone;

    public Employee(String id, String username, String passwordHash, String fullName, String role, String email, String phone) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.role = role;
        this.email = email;
        this.phone = phone;
    }

    // Getters
    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getFullName() { return fullName; }
    public String getRole() { return role; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }

    // Setters
    public void setUsername(String username) { this.username = username; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setRole(String role) { this.role = role; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
}

