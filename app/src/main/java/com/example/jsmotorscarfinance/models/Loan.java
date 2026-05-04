package com.example.jsmotorscarfinance.models;

public class Loan {
    // Model fields for storing loan data
    private int id;
    private int userId;
    private String vehicleName;
    private double amount; 
    private String status; 
    private double interestRate;
    private int repaymentPeriod; 
    private double installmentAmount;
    private double remainingAmount;
    private double paidAmount;

    // Constructor to initialize a loan object with core details
    public Loan(int id, int userId, String vehicleName, double amount, String status) {
        this.id = id;
        this.userId = userId;
        this.vehicleName = vehicleName;
        this.amount = amount;
        this.status = status;
    }

    // Default constructor
    public Loan() {}

    // Returns the unique loan ID
    public int getId() { return id; }
    // Sets the unique loan ID
    public void setId(int id) { this.id = id; }
    // Returns the ID of the user associated with this loan
    public int getUserId() { return userId; }
    // Sets the ID of the user associated with this loan
    public void setUserId(int userId) { this.userId = userId; }
    // Returns the name of the vehicle being financed
    public String getVehicleName() { return vehicleName; }
    // Sets the name of the vehicle being financed
    public void setVehicleName(String vehicleName) { this.vehicleName = vehicleName; }
    // Returns the total loan principal amount
    public double getAmount() { return amount; }
    // Sets the total loan principal amount
    public void setAmount(double amount) { this.amount = amount; }
    // Returns the current status of the loan (e.g., Pending, Approved)
    public String getStatus() { return status; }
    // Sets the current status of the loan
    public void setStatus(String status) { this.status = status; }
    // Returns the annual interest rate
    public double getInterestRate() { return interestRate; }
    // Sets the annual interest rate
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }
    // Returns the repayment period in months
    public int getRepaymentPeriod() { return repaymentPeriod; }
    // Sets the repayment period in months
    public void setRepaymentPeriod(int repaymentPeriod) { this.repaymentPeriod = repaymentPeriod; }
    // Returns the calculated monthly installment amount
    public double getInstallmentAmount() { return installmentAmount; }
    // Sets the calculated monthly installment amount
    public void setInstallmentAmount(double installmentAmount) { this.installmentAmount = installmentAmount; }
    // Returns the amount remaining to be paid
    public double getRemainingAmount() { return remainingAmount; }
    // Sets the amount remaining to be paid
    public void setRemainingAmount(double remainingAmount) { this.remainingAmount = remainingAmount; }
    // Returns the total amount paid so far
    public double getPaidAmount() { return paidAmount; }
    // Sets the total amount paid so far
    public void setPaidAmount(double paidAmount) { this.paidAmount = paidAmount; }
}
