package com.example.jsmotorscarfinance.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.jsmotorscarfinance.models.Loan;
import com.example.jsmotorscarfinance.models.User;
import com.example.jsmotorscarfinance.models.Vehicle;
import com.example.jsmotorscarfinance.models.Payment;
import com.example.jsmotorscarfinance.models.Sale;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "JSMotors.db";
    private static final int DATABASE_VERSION = 11;

    // Constructor to initialize the database helper
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creates the database schema and all necessary tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE user(userid INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT UNIQUE, password TEXT)");
        db.execSQL("CREATE TABLE admin(adminid INTEGER PRIMARY KEY AUTOINCREMENT, password TEXT)");
        db.execSQL("CREATE TABLE agent(agentid INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT UNIQUE, password TEXT, bank TEXT)");
        db.execSQL("CREATE TABLE vehicle_records(vehicleid INTEGER PRIMARY KEY AUTOINCREMENT, Model TEXT, Mileage TEXT, Price REAL, Condition TEXT, ImageUrl TEXT)");
        db.execSQL("CREATE TABLE documents(documentid INTEGER PRIMARY KEY AUTOINCREMENT, userid INTEGER, payslip TEXT, vehicle TEXT, loanPayment TEXT, downPayment TEXT)");
        db.execSQL("CREATE TABLE customer_db(customerid INTEGER PRIMARY KEY AUTOINCREMENT, userid INTEGER, vehicleid INTEGER, IntrestRate REAL, totalPrice REAL, paymentLeft REAL, paymentPaid REAL)");
        db.execSQL("CREATE TABLE loan_application(applicationid INTEGER PRIMARY KEY AUTOINCREMENT, customerid INTEGER, IntrestRate REAL, repaymentPeriod INTEGER, monthlyInstallment REAL, status TEXT)");
        db.execSQL("CREATE TABLE payments(paymentid INTEGER PRIMARY KEY AUTOINCREMENT, userid INTEGER, amount REAL, date TEXT)");
        seedInitialData(db);
    }

    // Populates the database with initial administrative and vehicle data
    private void seedInitialData(SQLiteDatabase db) {
        ContentValues adminValues = new ContentValues();
        adminValues.put("password", "admin123");
        db.insert("admin", null, adminValues);

        ContentValues agentValues = new ContentValues();
        agentValues.put("email", "agent@jsmotors.com");
        agentValues.put("password", "agent123");
        agentValues.put("bank", "JS Finance");
        db.insert("agent", null, agentValues);
        
        insertVehicle(db, "Toyota Camry 2026", 32000, "https://images.unsplash.com/photo-1621007947382-bb3c3994e3fb?q=80&w=2070&auto=format&fit=crop");
        insertVehicle(db, "Honda Civic 2026", 28000, "https://images.unsplash.com/photo-1599912027806-cfec9f5944b6?q=80&w=2070&auto=format&fit=crop");
    }

    // Helper method to insert a new vehicle record during initialization
    private void insertVehicle(SQLiteDatabase db, String model, double price, String imageUrl) {
        ContentValues v = new ContentValues();
        v.put("Model", model);
        v.put("Price", price);
        v.put("ImageUrl", imageUrl);
        v.put("Mileage", "0");
        v.put("Condition", "New");
        db.insert("vehicle_records", null, v);
    }

    // Manages database upgrades and schema migrations
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS admin");
        db.execSQL("DROP TABLE IF EXISTS agent");
        db.execSQL("DROP TABLE IF EXISTS vehicle_records");
        db.execSQL("DROP TABLE IF EXISTS documents");
        db.execSQL("DROP TABLE IF EXISTS customer_db");
        db.execSQL("DROP TABLE IF EXISTS loan_application");
        db.execSQL("DROP TABLE IF EXISTS payments");
        onCreate(db);
    }

    // Records a new customer account in the user table
    public long registerUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("name", user.getName());
        v.put("email", user.getEmail());
        v.put("password", user.getPassword());
        return db.insert("user", null, v);
    }

    // Retrieves a specific user's details using their unique ID
    public User getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("user", null, "userid=?", new String[]{String.valueOf(userId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            User u = new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), "Customer");
            cursor.close();
            return u;
        }
        return null;
    }

    // Updates an existing user's profile details
    public int updateUser(int userId, String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("name", name);
        v.put("email", email);
        v.put("password", password);
        return db.update("user", v, "userid=?", new String[]{String.valueOf(userId)});
    }

    // Authenticates a user and identifies their specific role
    public User loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query("user", null, "email=? AND password=?", new String[]{email, password}, null, null, null);
        if (c != null && c.moveToFirst()) {
            User u = new User(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), "Customer");
            c.close();
            return u;
        }
        c = db.query("agent", null, "email=? AND password=?", new String[]{email, password}, null, null, null);
        if (c != null && c.moveToFirst()) {
            User u = new User(c.getInt(0), "Agent", c.getString(1), c.getString(2), "Finance Agent");
            c.close();
            return u;
        }
        if ("admin@jsmotors.com".equals(email) || "admin".equals(email)) {
            c = db.query("admin", null, "password=?", new String[]{password}, null, null, null);
            if (c != null && c.moveToFirst()) {
                User u = new User(c.getInt(0), "Admin", "admin@jsmotors.com", c.getString(1), "Admin");
                c.close();
                return u;
            }
        }
        return null;
    }

    // Returns a list of all available car records
    public List<Vehicle> getAllVehicles() {
        List<Vehicle> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query("vehicle_records", null, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                Vehicle v = new Vehicle();
                v.setId(c.getInt(0));
                v.setName(c.getString(1));
                v.setPrice(c.getDouble(3));
                v.setImageUrl(c.getString(5));
                v.setSpecs(c.getString(2) + " | " + c.getString(4));
                list.add(v);
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    // Adds a new car entry to the vehicle_records table
    public long addVehicle(Vehicle v) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Model", v.getName());
        values.put("Price", v.getPrice());
        values.put("ImageUrl", v.getImageUrl());
        values.put("Mileage", "0");
        values.put("Condition", "New");
        return db.insert("vehicle_records", null, values);
    }

    // Updates car details such as model, price, and image
    public void updateVehicle(Vehicle v) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Model", v.getName());
        values.put("Price", v.getPrice());
        values.put("ImageUrl", v.getImageUrl());
        db.update("vehicle_records", values, "vehicleid=?", new String[]{String.valueOf(v.getId())});
    }

    // Deletes a car from the inventory by its ID
    public void deleteVehicle(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("vehicle_records", "vehicleid=?", new String[]{String.valueOf(id)});
    }

    // Saves a customer's loan application to the database
    public long applyLoan(Loan loan) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("userid", loan.getUserId());
        cv.put("vehicleid", 1);
        cv.put("totalPrice", loan.getAmount());
        cv.put("paymentLeft", loan.getAmount());
        cv.put("paymentPaid", 0.0);
        cv.put("IntrestRate", 8.9);
        long custId = db.insert("customer_db", null, cv);
        ContentValues v = new ContentValues();
        v.put("customerid", custId);
        v.put("status", "Pending Approval");
        v.put("monthlyInstallment", 0.0);
        v.put("IntrestRate", 8.9);
        v.put("repaymentPeriod", 60);
        return db.insert("loan_application", null, v);
    }

    // Retrieves every loan application stored in the system
    public List<Loan> getAllLoans() {
        List<Loan> loans = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT la.applicationid, c.userid, la.status, c.totalPrice FROM loan_application la JOIN customer_db c ON la.customerid = c.customerid", null);
        if (c.moveToFirst()) {
            do {
                Loan l = new Loan(c.getInt(0), c.getInt(1), "Vehicle Record", c.getDouble(3), c.getString(2));
                loans.add(l);
            } while (c.moveToNext());
        }
        c.close();
        return loans;
    }

    // Fetches all loan applications linked to a specific user ID
    public List<Loan> getLoansByUser(int userId) {
        List<Loan> loans = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT la.applicationid, c.userid, la.status, c.totalPrice, c.paymentLeft, c.paymentPaid, la.monthlyInstallment, la.IntrestRate, la.repaymentPeriod FROM loan_application la JOIN customer_db c ON la.customerid = c.customerid WHERE c.userid=?", new String[]{String.valueOf(userId)});
        if (c.moveToFirst()) {
            do {
                Loan l = new Loan(c.getInt(0), c.getInt(1), "Vehicle", c.getDouble(3), c.getString(2));
                l.setRemainingAmount(c.getDouble(4));
                l.setPaidAmount(c.getDouble(5));
                l.setInstallmentAmount(c.getDouble(6));
                l.setInterestRate(c.getDouble(7));
                l.setRepaymentPeriod(c.getInt(8));
                loans.add(l);
            } while (c.moveToNext());
        }
        c.close();
        return loans;
    }

    // Updates the workflow status of a specific loan application
    public void updateLoanStatus(int loanId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("status", status);
        db.update("loan_application", v, "applicationid=?", new String[]{String.valueOf(loanId)});
    }

    // Sets the final financial terms and approves a loan application
    public void setLoanTerms(int loanId, double rate, int period, double installment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("IntrestRate", rate);
        v.put("repaymentPeriod", period);
        v.put("monthlyInstallment", installment);
        v.put("status", "Approved");
        db.update("loan_application", v, "applicationid=?", new String[]{String.valueOf(loanId)});
        
        double total = installment * period;
        db.execSQL("UPDATE customer_db SET totalPrice = ?, paymentLeft = ? WHERE customerid = (SELECT customerid FROM loan_application WHERE applicationid = ?)", new Object[]{total, total, loanId});
    }

    // Records a payment and automatically updates the remaining loan balance
    public long addPayment(Payment p) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("userid", p.getUserId());
        v.put("amount", p.getAmount());
        v.put("date", p.getDate());
        long pid = db.insert("payments", null, v);
        if (pid != -1) {
            db.execSQL("UPDATE customer_db SET paymentLeft = paymentLeft - ?, paymentPaid = paymentPaid + ? WHERE userid = ?", new Object[]{p.getAmount(), p.getAmount(), p.getUserId()});
        }
        return pid;
    }

    // Returns the payment transaction history for a specific customer
    public List<Payment> getPaymentsByUser(int userId) {
        List<Payment> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query("payments", null, "userid=?", new String[]{String.valueOf(userId)}, null, null, "paymentid DESC");
        if (c.moveToFirst()) {
            do {
                list.add(new Payment(c.getInt(0), c.getInt(1), c.getDouble(2), c.getString(3)));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    // Fetches all payments recorded in the system for admin reporting
    public List<Payment> getAllPayments() {
        List<Payment> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query("payments", null, null, null, null, null, "paymentid DESC");
        if (c.moveToFirst()) {
            do {
                list.add(new Payment(c.getInt(0), c.getInt(1), c.getDouble(2), c.getString(3)));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    // Generates a report of all successful vehicle sales and total prices
    public List<Sale> getAllSales() {
        List<Sale> sales = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT u.name, v.Model, c.totalPrice FROM loan_application la " +
                       "JOIN customer_db c ON la.customerid = c.customerid " +
                       "JOIN user u ON c.userid = u.userid " +
                       "JOIN vehicle_records v ON c.vehicleid = v.vehicleid " +
                       "WHERE la.status = 'Approved'";
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                sales.add(new Sale(c.getString(0), c.getString(1), c.getDouble(2)));
            } while (c.moveToNext());
        }
        c.close();
        return sales;
    }

    // Returns a list of verification documents associated with a loan ID
    public List<String> getDocumentsForLoan(int loanId) {
        List<String> docs = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT payslip FROM documents WHERE userid = (SELECT userid FROM customer_db WHERE customerid = (SELECT customerid FROM loan_application WHERE applicationid = ?))", new String[]{String.valueOf(loanId)});
        if (c.moveToFirst()) {
            do {
                docs.add("Digital ID Verified: " + c.getString(0));
            } while (c.moveToNext());
        }
        c.close();
        return docs;
    }
}
