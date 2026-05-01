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
    private static final int DATABASE_VERSION = 8;

    private static final String TABLE_USER = "user";
    private static final String TABLE_ADMIN = "admin";
    private static final String TABLE_AGENT = "agent";
    private static final String TABLE_VEHICLE_RECORDS = "vehicle_records";
    private static final String TABLE_DOCUMENTS = "documents";
    private static final String TABLE_CUSTOMER_DB = "customer_db";
    private static final String TABLE_LOAN_APPLICATION = "loan_application";
    private static final String TABLE_PAYMENTS = "payments";

    private static final String KEY_USER_ID = "userid";
    private static final String KEY_USER_NAME = "name";
    private static final String KEY_USER_EMAIL = "email";
    private static final String KEY_USER_PASSWORD = "password";
    private static final String KEY_ADMIN_ID = "adminid";
    private static final String KEY_ADMIN_PASSWORD = "password";
    private static final String KEY_AGENT_ID = "agentid";
    private static final String KEY_AGENT_EMAIL = "email";
    private static final String KEY_AGENT_PASSWORD = "password";
    private static final String KEY_AGENT_BANK = "bank";
    private static final String KEY_VEHICLE_ID = "vehicleid";
    private static final String KEY_VEHICLE_MODEL = "Model";
    private static final String KEY_VEHICLE_MILEAGE = "Mileage";
    private static final String KEY_VEHICLE_PRICE = "Price";
    private static final String KEY_VEHICLE_CONDITION = "Condition";
    private static final String KEY_VEHICLE_IMAGE_URL = "ImageUrl";
    private static final String KEY_DOC_ID = "documentid";
    private static final String KEY_DOC_USER_ID = "userid";
    private static final String KEY_DOC_PAYSLIP = "payslip";
    private static final String KEY_DOC_VEHICLE = "vehicle";
    private static final String KEY_DOC_LOAN_PAYMENT = "loanPayment";
    private static final String KEY_DOC_DOWN_PAYMENT = "downPayment";
    private static final String KEY_CUST_ID = "customerid";
    private static final String KEY_CUST_USER_ID = "userid";
    private static final String KEY_CUST_VEHICLE_ID = "vehicleid";
    private static final String KEY_CUST_INTEREST_RATE = "IntrestRate";
    private static final String KEY_CUST_TOTAL_PRICE = "totalPrice";
    private static final String KEY_CUST_PAYMENT_LEFT = "paymentLeft";
    private static final String KEY_CUST_PAYMENT_PAID = "paymentPaid";
    private static final String KEY_LOAN_APP_ID = "applicationid";
    private static final String KEY_LOAN_APP_CUST_ID = "customerid";
    private static final String KEY_LOAN_APP_INTEREST_RATE = "IntrestRate";
    private static final String KEY_LOAN_APP_REPAYMENT_PERIOD = "repaymentPeriod";
    private static final String KEY_LOAN_APP_MONTHLY_INSTALLMENT = "monthlyInstallment";
    private static final String KEY_LOAN_APP_STATUS = "status";
    
    private static final String KEY_PAYMENT_ID = "paymentid";
    private static final String KEY_PAYMENT_USER_ID = "userid";
    private static final String KEY_PAYMENT_AMOUNT = "amount";
    private static final String KEY_PAYMENT_DATE = "date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USER + "(" + KEY_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_USER_NAME + " TEXT," + KEY_USER_EMAIL + " TEXT UNIQUE," + KEY_USER_PASSWORD + " TEXT" + ")");
        db.execSQL("CREATE TABLE " + TABLE_ADMIN + "(" + KEY_ADMIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_ADMIN_PASSWORD + " TEXT" + ")");
        db.execSQL("CREATE TABLE " + TABLE_AGENT + "(" + KEY_AGENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_AGENT_EMAIL + " TEXT UNIQUE," + KEY_AGENT_PASSWORD + " TEXT," + KEY_AGENT_BANK + " TEXT" + ")");
        db.execSQL("CREATE TABLE " + TABLE_VEHICLE_RECORDS + "(" + KEY_VEHICLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_VEHICLE_MODEL + " TEXT," + KEY_VEHICLE_MILEAGE + " TEXT," + KEY_VEHICLE_PRICE + " REAL," + KEY_VEHICLE_CONDITION + " TEXT," + KEY_VEHICLE_IMAGE_URL + " TEXT" + ")");
        db.execSQL("CREATE TABLE " + TABLE_DOCUMENTS + "(" + KEY_DOC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_DOC_USER_ID + " INTEGER," + KEY_DOC_PAYSLIP + " TEXT," + KEY_DOC_VEHICLE + " TEXT," + KEY_DOC_LOAN_PAYMENT + " TEXT," + KEY_DOC_DOWN_PAYMENT + " TEXT," + "FOREIGN KEY(" + KEY_DOC_USER_ID + ") REFERENCES " + TABLE_USER + "(" + KEY_USER_ID + "))");
        db.execSQL("CREATE TABLE " + TABLE_CUSTOMER_DB + "(" + KEY_CUST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_CUST_USER_ID + " INTEGER," + KEY_CUST_VEHICLE_ID + " INTEGER," + KEY_CUST_INTEREST_RATE + " REAL," + KEY_CUST_TOTAL_PRICE + " REAL," + KEY_CUST_PAYMENT_LEFT + " REAL," + KEY_CUST_PAYMENT_PAID + " REAL," + "FOREIGN KEY(" + KEY_CUST_USER_ID + ") REFERENCES " + TABLE_USER + "(" + KEY_USER_ID + ")," + "FOREIGN KEY(" + KEY_CUST_VEHICLE_ID + ") REFERENCES " + TABLE_VEHICLE_RECORDS + "(" + KEY_VEHICLE_ID + "))");
        db.execSQL("CREATE TABLE " + TABLE_LOAN_APPLICATION + "(" + KEY_LOAN_APP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_LOAN_APP_CUST_ID + " INTEGER," + KEY_LOAN_APP_INTEREST_RATE + " REAL," + KEY_LOAN_APP_REPAYMENT_PERIOD + " INTEGER," + KEY_LOAN_APP_MONTHLY_INSTALLMENT + " REAL," + KEY_LOAN_APP_STATUS + " TEXT," + "FOREIGN KEY(" + KEY_LOAN_APP_CUST_ID + ") REFERENCES " + TABLE_CUSTOMER_DB + "(" + KEY_CUST_ID + "))");
        db.execSQL("CREATE TABLE " + TABLE_PAYMENTS + "(" + KEY_PAYMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_PAYMENT_USER_ID + " INTEGER," + KEY_PAYMENT_AMOUNT + " REAL," + KEY_PAYMENT_DATE + " TEXT" + ")");
        seedInitialData(db);
    }

    private void seedInitialData(SQLiteDatabase db) {
        ContentValues adminValues = new ContentValues();
        adminValues.put(KEY_ADMIN_PASSWORD, "admin123");
        db.insert(TABLE_ADMIN, null, adminValues);

        ContentValues agentValues = new ContentValues();
        agentValues.put(KEY_AGENT_EMAIL, "agent@jsmotors.com");
        agentValues.put(KEY_AGENT_PASSWORD, "agent123");
        agentValues.put(KEY_AGENT_BANK, "JS Finance");
        db.insert(TABLE_AGENT, null, agentValues);

        insertVehicle(db, "Toyota Camry 2024", "100", 32000, "Excellent", "https://images.unsplash.com/photo-1621007947382-bb3c3994e3fb?q=80&w=2070&auto=format&fit=crop");
        insertVehicle(db, "Honda Civic 2023", "5000", 28000, "Good", "https://images.unsplash.com/photo-1599912027806-cfec9f5944b6?q=80&w=2070&auto=format&fit=crop");
        insertVehicle(db, "Tesla Model 3", "10", 45000, "New", "https://images.unsplash.com/photo-1536700503339-1e4b06520771?q=80&w=2070&auto=format&fit=crop");
    }

    private void insertVehicle(SQLiteDatabase db, String model, String mileage, double price, String condition, String imageUrl) {
        ContentValues v = new ContentValues();
        v.put(KEY_VEHICLE_MODEL, model);
        v.put(KEY_VEHICLE_MILEAGE, mileage);
        v.put(KEY_VEHICLE_PRICE, price);
        v.put(KEY_VEHICLE_CONDITION, condition);
        v.put(KEY_VEHICLE_IMAGE_URL, imageUrl);
        db.insert(TABLE_VEHICLE_RECORDS, null, v);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADMIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AGENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VEHICLE_RECORDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCUMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER_DB);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOAN_APPLICATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENTS);
        onCreate(db);
    }

    public long registerUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(KEY_USER_NAME, user.getName());
        v.put(KEY_USER_EMAIL, user.getEmail());
        v.put(KEY_USER_PASSWORD, user.getPassword());
        return db.insert(TABLE_USER, null, v);
    }

    public User loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, null, KEY_USER_EMAIL + "=? AND " + KEY_USER_PASSWORD + "=?", new String[]{email, password}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            User u = new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), "Customer");
            cursor.close();
            return u;
        }
        cursor = db.query(TABLE_AGENT, null, KEY_AGENT_EMAIL + "=? AND " + KEY_AGENT_PASSWORD + "=?", new String[]{email, password}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            User u = new User(cursor.getInt(0), "Agent", cursor.getString(1), cursor.getString(2), "Finance Agent");
            cursor.close();
            return u;
        }
        if (email.equals("admin") || email.equals("admin@jsmotors.com")) {
            cursor = db.query(TABLE_ADMIN, null, KEY_ADMIN_PASSWORD + "=?", new String[]{password}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                User u = new User(cursor.getInt(0), "Admin", "admin@jsmotors.com", cursor.getString(1), "Admin");
                cursor.close();
                return u;
            }
        }
        if (cursor != null) cursor.close();
        return null;
    }

    public List<Vehicle> getAllVehicles() {
        List<Vehicle> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_VEHICLE_RECORDS, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                Vehicle v = new Vehicle();
                v.setId(c.getInt(0));
                v.setName(c.getString(1));
                v.setSpecs("Mileage: " + c.getString(2) + ", Condition: " + c.getString(4));
                v.setPrice(c.getDouble(3));
                v.setAvailability(1);
                v.setImageUrl(c.getString(5));
                list.add(v);
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public long addVehicle(Vehicle v) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_VEHICLE_MODEL, v.getName());
        values.put(KEY_VEHICLE_PRICE, v.getPrice());
        values.put(KEY_VEHICLE_CONDITION, "Excellent");
        values.put(KEY_VEHICLE_MILEAGE, "0");
        values.put(KEY_VEHICLE_IMAGE_URL, v.getImageUrl());
        return db.insert(TABLE_VEHICLE_RECORDS, null, values);
    }

    public void updateVehicle(Vehicle v) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_VEHICLE_MODEL, v.getName());
        values.put(KEY_VEHICLE_PRICE, v.getPrice());
        values.put(KEY_VEHICLE_IMAGE_URL, v.getImageUrl());
        db.update(TABLE_VEHICLE_RECORDS, values, KEY_VEHICLE_ID + "=?", new String[]{String.valueOf(v.getId())});
    }

    public void deleteVehicle(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_VEHICLE_RECORDS, KEY_VEHICLE_ID + "=?", new String[]{String.valueOf(id)});
    }

    public long applyLoan(Loan loan) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_CUST_USER_ID, loan.getUserId());
        cv.put(KEY_CUST_VEHICLE_ID, 1);
        cv.put(KEY_CUST_INTEREST_RATE, 5.0);
        cv.put(KEY_CUST_TOTAL_PRICE, loan.getAmount());
        cv.put(KEY_CUST_PAYMENT_LEFT, loan.getAmount());
        cv.put(KEY_CUST_PAYMENT_PAID, 0.0);
        long custId = db.insert(TABLE_CUSTOMER_DB, null, cv);
        ContentValues v = new ContentValues();
        v.put(KEY_LOAN_APP_CUST_ID, custId);
        v.put(KEY_LOAN_APP_INTEREST_RATE, 5.0);
        v.put(KEY_LOAN_APP_REPAYMENT_PERIOD, 60);
        v.put(KEY_LOAN_APP_MONTHLY_INSTALLMENT, loan.getAmount() / 60);
        v.put(KEY_LOAN_APP_STATUS, "Pending");
        return db.insert(TABLE_LOAN_APPLICATION, null, v);
    }

    public void uploadDocument(int loanId, String docName, String docPath) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT userid FROM customer_db WHERE customerid = (SELECT customerid FROM loan_application WHERE applicationid = ?)", new String[]{String.valueOf(loanId)});
        int userId = -1;
        if(c.moveToFirst()) userId = c.getInt(0);
        c.close();
        ContentValues v = new ContentValues();
        v.put(KEY_DOC_USER_ID, userId);
        v.put(KEY_DOC_PAYSLIP, docPath);
        v.put(KEY_DOC_VEHICLE, "Vehicle Detail");
        v.put(KEY_DOC_LOAN_PAYMENT, "0");
        v.put(KEY_DOC_DOWN_PAYMENT, "0");
        db.insert(TABLE_DOCUMENTS, null, v);
    }

    public List<String> getDocumentsForLoan(int loanId) {
        List<String> docs = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT payslip FROM documents WHERE userid = (SELECT userid FROM customer_db WHERE customerid = (SELECT customerid FROM loan_application WHERE applicationid = ?))", new String[]{String.valueOf(loanId)});
        if (c.moveToFirst()) {
            do {
                docs.add("Document: " + c.getString(0));
            } while (c.moveToNext());
        }
        c.close();
        return docs;
    }

    public List<Loan> getAllLoans() {
        List<Loan> loans = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT la.applicationid, c.userid, la.status, la.monthlyInstallment * la.repaymentPeriod as total FROM " + TABLE_LOAN_APPLICATION + " la JOIN " + TABLE_CUSTOMER_DB + " c ON la.customerid = c.customerid";
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                Loan l = new Loan(c.getInt(0), c.getInt(1), "Vehicle Record", c.getDouble(3), c.getString(2));
                loans.add(l);
            } while (c.moveToNext());
        }
        c.close();
        return loans;
    }

    public List<Loan> getLoansByUser(int userId) {
        List<Loan> loans = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT la.applicationid, c.userid, la.status, c.totalPrice, la.IntrestRate, la.repaymentPeriod, la.monthlyInstallment FROM " + TABLE_LOAN_APPLICATION + " la JOIN " + TABLE_CUSTOMER_DB + " c ON la.customerid = c.customerid WHERE c.userid = ?";
        Cursor c = db.rawQuery(query, new String[]{String.valueOf(userId)});
        if (c.moveToFirst()) {
            do {
                Loan l = new Loan(c.getInt(0), c.getInt(1), "Vehicle", c.getDouble(3), c.getString(2));
                l.setInterestRate(c.getDouble(4));
                l.setRepaymentPeriod(c.getInt(5));
                l.setInstallmentAmount(c.getDouble(6));
                loans.add(l);
            } while (c.moveToNext());
        }
        c.close();
        return loans;
    }

    public void updateLoanStatus(int loanId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(KEY_LOAN_APP_STATUS, status);
        db.update(TABLE_LOAN_APPLICATION, v, KEY_LOAN_APP_ID + "=?", new String[]{String.valueOf(loanId)});
    }

    public void setLoanTerms(int loanId, double interest, int period, double installment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(KEY_LOAN_APP_INTEREST_RATE, interest);
        v.put(KEY_LOAN_APP_REPAYMENT_PERIOD, period);
        v.put(KEY_LOAN_APP_MONTHLY_INSTALLMENT, installment);
        v.put(KEY_LOAN_APP_STATUS, "Approved");
        db.update(TABLE_LOAN_APPLICATION, v, KEY_LOAN_APP_ID + "=?", new String[]{String.valueOf(loanId)});
    }
    
    public long addPayment(Payment payment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(KEY_PAYMENT_USER_ID, payment.getUserId());
        v.put(KEY_PAYMENT_AMOUNT, payment.getAmount());
        v.put(KEY_PAYMENT_DATE, payment.getDate());
        return db.insert(TABLE_PAYMENTS, null, v);
    }

    public List<Payment> getPaymentsByUser(int userId) {
        List<Payment> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_PAYMENTS, null, KEY_PAYMENT_USER_ID + "=?", new String[]{String.valueOf(userId)}, null, null, KEY_PAYMENT_ID + " DESC");
        if (c.moveToFirst()) {
            do {
                list.add(new Payment(c.getInt(0), c.getInt(1), c.getDouble(2), c.getString(3)));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public List<Payment> getAllPayments() {
        List<Payment> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_PAYMENTS, null, null, null, null, null, KEY_PAYMENT_ID + " DESC");
        if (c.moveToFirst()) {
            do {
                list.add(new Payment(c.getInt(0), c.getInt(1), c.getDouble(2), c.getString(3)));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public List<Sale> getAllSales() {
        List<Sale> sales = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT u.name, v.Model, c.totalPrice FROM " + TABLE_LOAN_APPLICATION + " la " +
                "JOIN " + TABLE_CUSTOMER_DB + " c ON la.customerid = c.customerid " +
                "JOIN " + TABLE_USER + " u ON c.userid = u.userid " +
                "JOIN " + TABLE_VEHICLE_RECORDS + " v ON c.vehicleid = v.vehicleid " +
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
}
