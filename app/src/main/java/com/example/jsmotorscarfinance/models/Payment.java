package com.example.jsmotorscarfinance.models;

public class Payment {
    // Payment record fields
    private int id;
    private int userId;
    private String userName;
    private double amount;
    private String date;

    // Constructor to initialize a payment record
    public Payment(int id, int userId, double amount, String date) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.date = date;
    }

    // Default constructor
    public Payment() {}

    // Returns the unique payment ID
    public int getId() { return id; }
    // Sets the unique payment ID
    public void setId(int id) { this.id = id; }
    // Returns the ID of the user who made the payment
    public int getUserId() { return userId; }
    // Sets the ID of the user who made the payment
    public void setUserId(int userId) { this.userId = userId; }
    // Returns the name of the user who made the payment
    public String getUserName() { return userName; }
    // Sets the name of the user who made the payment
    public void setUserName(String userName) { this.userName = userName; }
    // Returns the payment amount
    public double getAmount() { return amount; }
    // Sets the payment amount
    public void setAmount(double amount) { this.amount = amount; }
    // Returns the date and time of the payment
    public String getDate() { return date; }
    // Sets the date and time of the payment
    public void setDate(String date) { this.date = date; }
}
