package com.example.jsmotorscarfinance.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.jsmotorscarfinance.models.Loan;
import com.example.jsmotorscarfinance.models.User;
import com.example.jsmotorscarfinance.models.Vehicle;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SmartLeasePro.db";
    private static final int DATABASE_VERSION = 3;

    // Table Names
    private static final String TABLE_USER = "USER";
    private static final String TABLE_LOAN = "LOAN";
    private static final String TABLE_VEHICLE = "VEHICLE";
    private static final String TABLE_DOCUMENT = "DOCUMENT";

    // Common column names
    private static final String KEY_ID = "id";

    // USER Table
    private static final String KEY_USER_NAME = "name";
    private static final String KEY_USER_EMAIL = "email";
    private static final String KEY_USER_PASSWORD = "password";
    private static final String KEY_USER_ROLE = "role";

    // VEHICLE Table
    private static final String KEY_VEHICLE_NAME = "name";
    private static final String KEY_VEHICLE_SPECS = "specs";
    private static final String KEY_VEHICLE_PRICE = "price";
    private static final String KEY_VEHICLE_AVAILABILITY = "availability";

    // LOAN Table
    private static final String KEY_LOAN_USER_ID = "user_id";
    private static final String KEY_LOAN_VEHICLE_NAME = "vehicle_name";
    private static final String KEY_LOAN_AMOUNT = "amount";
    private static final String KEY_LOAN_STATUS = "status";
    private static final String KEY_LOAN_INTEREST = "interest_rate";
    private static final String KEY_LOAN_PERIOD = "repayment_period";
    private static final String KEY_LOAN_INSTALLMENT = "installment_amount";

    // DOCUMENT Table
    private static final String KEY_DOC_LOAN_ID = "loan_id";
    private static final String KEY_DOC_NAME = "doc_name";
    private static final String KEY_DOC_PATH = "doc_path";

    // Table Create Statements
    private static final String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_USER_NAME + " TEXT,"
            + KEY_USER_EMAIL + " TEXT UNIQUE,"
            + KEY_USER_PASSWORD + " TEXT,"
            + KEY_USER_ROLE + " TEXT" + ")";

    private static final String CREATE_TABLE_VEHICLE = "CREATE TABLE " + TABLE_VEHICLE + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_VEHICLE_NAME + " TEXT,"
            + KEY_VEHICLE_SPECS + " TEXT,"
            + KEY_VEHICLE_PRICE + " REAL,"
            + KEY_VEHICLE_AVAILABILITY + " INTEGER" + ")";

    private static final String CREATE_TABLE_LOAN = "CREATE TABLE " + TABLE_LOAN + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_LOAN_USER_ID + " INTEGER,"
            + KEY_LOAN_VEHICLE_NAME + " TEXT,"
            + KEY_LOAN_AMOUNT + " REAL,"
            + KEY_LOAN_STATUS + " TEXT,"
            + KEY_LOAN_INTEREST + " REAL,"
            + KEY_LOAN_PERIOD + " INTEGER,"
            + KEY_LOAN_INSTALLMENT + " REAL" + ")";

    private static final String CREATE_TABLE_DOCUMENT = "CREATE TABLE " + TABLE_DOCUMENT + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_DOC_LOAN_ID + " INTEGER,"
            + KEY_DOC_NAME + " TEXT,"
            + KEY_DOC_PATH + " TEXT,"
            + "FOREIGN KEY(" + KEY_DOC_LOAN_ID + ") REFERENCES " + TABLE_LOAN + "(" + KEY_ID + "))";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_VEHICLE);
        db.execSQL(CREATE_TABLE_LOAN);
        db.execSQL(CREATE_TABLE_DOCUMENT);
        seedInitialData(db);
    }

    private void seedInitialData(SQLiteDatabase db) {
        insertUser(db, "System Admin", "admin@smartlease.com", "admin123", "Admin");
        insertUser(db, "Finance Manager", "agent@smartlease.com", "agent123", "Finance Agent");
        insertVehicle(db, "Toyota Camry 2024", "Hybrid, LE, White", 32000, 1);
        insertVehicle(db, "Honda Civic 2023", "Sport, Black, 2.0L", 28000, 1);
        insertVehicle(db, "Tesla Model Y", "Long Range, Dual Motor", 48000, 1);
    }

    private void insertUser(SQLiteDatabase db, String name, String email, String password, String role) {
        ContentValues v = new ContentValues();
        v.put(KEY_USER_NAME, name);
        v.put(KEY_USER_EMAIL, email);
        v.put(KEY_USER_PASSWORD, password);
        v.put(KEY_USER_ROLE, role);
        db.insert(TABLE_USER, null, v);
    }

    private void insertVehicle(SQLiteDatabase db, String name, String specs, double price, int avail) {
        ContentValues v = new ContentValues();
        v.put(KEY_VEHICLE_NAME, name);
        v.put(KEY_VEHICLE_SPECS, specs);
        v.put(KEY_VEHICLE_PRICE, price);
        v.put(KEY_VEHICLE_AVAILABILITY, avail);
        db.insert(TABLE_VEHICLE, null, v);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VEHICLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOAN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCUMENT);
        onCreate(db);
    }

    public long registerUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(KEY_USER_NAME, user.getName());
        v.put(KEY_USER_EMAIL, user.getEmail());
        v.put(KEY_USER_PASSWORD, user.getPassword());
        v.put(KEY_USER_ROLE, user.getRole());
        return db.insert(TABLE_USER, null, v);
    }

    public User loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, null, KEY_USER_EMAIL + "=? AND " + KEY_USER_PASSWORD + "=?",
                new String[]{email, password}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            User u = new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            cursor.close();
            return u;
        }
        return null;
    }

    public List<Vehicle> getAllVehicles() {
        List<Vehicle> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_VEHICLE, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                list.add(new Vehicle(c.getInt(0), c.getString(1), c.getString(2), c.getDouble(3), c.getInt(4)));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public long addVehicle(Vehicle v) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_VEHICLE_NAME, v.getName());
        values.put(KEY_VEHICLE_SPECS, v.getSpecs());
        values.put(KEY_VEHICLE_PRICE, v.getPrice());
        values.put(KEY_VEHICLE_AVAILABILITY, v.getAvailability());
        return db.insert(TABLE_VEHICLE, null, values);
    }

    public void updateVehicle(Vehicle v) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_VEHICLE_NAME, v.getName());
        values.put(KEY_VEHICLE_SPECS, v.getSpecs());
        values.put(KEY_VEHICLE_PRICE, v.getPrice());
        values.put(KEY_VEHICLE_AVAILABILITY, v.getAvailability());
        db.update(TABLE_VEHICLE, values, KEY_ID + "=?", new String[]{String.valueOf(v.getId())});
    }

    public void deleteVehicle(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_VEHICLE, KEY_ID + "=?", new String[]{String.valueOf(id)});
    }

    public long applyLoan(Loan loan) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(KEY_LOAN_USER_ID, loan.getUserId());
        v.put(KEY_LOAN_VEHICLE_NAME, loan.getVehicleName());
        v.put(KEY_LOAN_AMOUNT, loan.getAmount());
        v.put(KEY_LOAN_STATUS, "Pending");
        return db.insert(TABLE_LOAN, null, v);
    }

    public void uploadDocument(int loanId, String docName, String docPath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(KEY_DOC_LOAN_ID, loanId);
        v.put(KEY_DOC_NAME, docName);
        v.put(KEY_DOC_PATH, docPath);
        db.insert(TABLE_DOCUMENT, null, v);
    }

    public List<String> getDocumentsForLoan(int loanId) {
        List<String> docs = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_DOCUMENT, new String[]{KEY_DOC_NAME, KEY_DOC_PATH}, KEY_DOC_LOAN_ID + "=?", new String[]{String.valueOf(loanId)}, null, null, null);
        if (c.moveToFirst()) {
            do {
                docs.add(c.getString(0) + ": " + c.getString(1));
            } while (c.moveToNext());
        }
        c.close();
        return docs;
    }

    public List<Loan> getAllLoans() {
        List<Loan> loans = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_LOAN, null);
        if (c.moveToFirst()) {
            do {
                loans.add(new Loan(c.getInt(0), c.getInt(1), c.getString(2), c.getDouble(3), c.getString(4)));
            } while (c.moveToNext());
        }
        c.close();
        return loans;
    }

    public List<Loan> getLoansByUser(int userId) {
        List<Loan> loans = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_LOAN, null, KEY_LOAN_USER_ID + "=?", new String[]{String.valueOf(userId)}, null, null, null);
        if (c.moveToFirst()) {
            do {
                loans.add(new Loan(c.getInt(0), c.getInt(1), c.getString(2), c.getDouble(3), c.getString(4)));
            } while (c.moveToNext());
        }
        c.close();
        return loans;
    }

    public void updateLoanStatus(int loanId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(KEY_LOAN_STATUS, status);
        db.update(TABLE_LOAN, v, KEY_ID + "=?", new String[]{String.valueOf(loanId)});
    }

    public void setLoanTerms(int loanId, double interest, int period, double installment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(KEY_LOAN_INTEREST, interest);
        v.put(KEY_LOAN_PERIOD, period);
        v.put(KEY_LOAN_INSTALLMENT, installment);
        v.put(KEY_LOAN_STATUS, "Terms Defined");
        db.update(TABLE_LOAN, v, KEY_ID + "=?", new String[]{String.valueOf(loanId)});
    }
}
