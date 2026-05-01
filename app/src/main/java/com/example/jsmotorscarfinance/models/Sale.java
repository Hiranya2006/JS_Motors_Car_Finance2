package com.example.jsmotorscarfinance.models;

public class Sale {
    private String customerName;
    private String vehicleModel;
    private double price;

    public Sale(String customerName, String vehicleModel, double price) {
        this.customerName = customerName;
        this.vehicleModel = vehicleModel;
        this.price = price;
    }

    public String getCustomerName() { return customerName; }
    public String getVehicleModel() { return vehicleModel; }
    public double getPrice() { return price; }
}
