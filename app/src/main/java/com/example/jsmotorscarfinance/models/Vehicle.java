package com.example.jsmotorscarfinance.models;

public class Vehicle {
    private int id;
    private String name;
    private String specs;
    private double price;
    private int availability; // 1 for available, 0 for sold

    public Vehicle(int id, String name, String specs, double price, int availability) {
        this.id = id;
        this.name = name;
        this.specs = specs;
        this.price = price;
        this.availability = availability;
    }

    public Vehicle() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSpecs() { return specs; }
    public void setSpecs(String specs) { this.specs = specs; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getAvailability() { return availability; }
    public void setAvailability(int availability) { this.availability = availability; }
}
