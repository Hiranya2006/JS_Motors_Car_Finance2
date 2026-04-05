package com.example.jsmotorscarfinance.models;

public class Loan {
    private int id;
    private int userId;
    private String vehicleName;
    private double amount;
    private String status; // Pending, Verified, Terms Set, Approved, Rejected, Forwarded
    private double interestRate;
    private int repaymentPeriod; // months
    private double installmentAmount;

    public Loan(int id, int userId, String vehicleName, double amount, String status) {
        this.id = id;
        this.userId = userId;
        this.vehicleName = vehicleName;
        this.amount = amount;
        this.status = status;
    }

    public Loan() {}

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getVehicleName() { return vehicleName; }
    public void setVehicleName(String vehicleName) { this.vehicleName = vehicleName; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }
    public int getRepaymentPeriod() { return repaymentPeriod; }
    public void setRepaymentPeriod(int repaymentPeriod) { this.repaymentPeriod = repaymentPeriod; }
    public double getInstallmentAmount() { return installmentAmount; }
    public void setInstallmentAmount(double installmentAmount) { this.installmentAmount = installmentAmount; }
}
