package com.example.jsmotorscarfinance.models;

public class Sale {
    // Sale record fields
    private String customerName;
    private String vehicleModel;
    private double price;

    // Constructor to initialize a sale record
    public Sale(String customerName, String vehicleModel, double price) {
        this.customerName = customerName;
        this.vehicleModel = vehicleModel;
        this.price = price;
    }

    // Returns the name of the customer who made the purchase
    public String getCustomerName() { return customerName; }
    // Returns the model name of the vehicle sold
    public String getVehicleModel() { return vehicleModel; }
    // Returns the final sale price
    public double getPrice() { return price; }
}
