package com.example.jsmotorscarfinance.models;

public class User {
    // User account fields
    private int id;
    private String name;
    private String email;
    private String password;
    private String role;

    // Constructor to initialize a user object
    public User(int id, String name, String email, String password, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Default constructor
    public User() {}

    // Returns the unique user ID
    public int getId() { return id; }
    // Sets the unique user ID
    public void setId(int id) { this.id = id; }
    // Returns the user's full name
    public String getName() { return name; }
    // Sets the user's full name
    public void setName(String name) { this.name = name; }
    // Returns the user's email address
    public String getEmail() { return email; }
    // Sets the user's email address
    public void setEmail(String email) { this.email = email; }
    // Returns the user's password
    public String getPassword() { return password; }
    // Sets the user's password
    public void setPassword(String password) { this.password = password; }
    // Returns the user's role (Admin, Agent, or Customer)
    public String getRole() { return role; }
    // Sets the user's role
    public void setRole(String role) { this.role = role; }
}
