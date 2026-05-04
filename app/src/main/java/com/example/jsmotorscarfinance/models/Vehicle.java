package com.example.jsmotorscarfinance.models;

public class Vehicle {
    // Vehicle record fields
    private int id;
    private String name;
    private String specs;
    private double price;
    private int availability; 
    private String imageUrl; 

    // Constructor to initialize a vehicle object
    public Vehicle(int id, String name, String specs, double price, int availability, String imageUrl) {
        this.id = id;
        this.name = name;
        this.specs = specs;
        this.price = price;
        this.availability = availability;
        this.imageUrl = imageUrl;
    }

    // Default constructor
    public Vehicle() {}

    // Returns the unique vehicle ID
    public int getId() { return id; }
    // Sets the unique vehicle ID
    public void setId(int id) { this.id = id; }
    // Returns the vehicle model name
    public String getName() { return name; }
    // Sets the vehicle model name
    public void setName(String name) { this.name = name; }
    // Returns the vehicle specifications
    public String getSpecs() { return specs; }
    // Sets the vehicle specifications
    public void setSpecs(String specs) { this.specs = specs; }
    // Returns the vehicle price
    public double getPrice() { return price; }
    // Sets the vehicle price
    public void setPrice(double price) { this.price = price; }
    // Returns the availability status (1 for available, 0 for sold)
    public int getAvailability() { return availability; }
    // Sets the availability status
    public void setAvailability(int availability) { this.availability = availability; }
    // Returns the image URL or Base64 data string
    public String getImageUrl() { return imageUrl; }
    // Sets the image URL or Base64 data string
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
