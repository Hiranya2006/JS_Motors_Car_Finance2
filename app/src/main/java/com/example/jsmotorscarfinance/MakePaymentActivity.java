package com.example.jsmotorscarfinance;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.jsmotorscarfinance.database.DatabaseHelper;
import com.example.jsmotorscarfinance.models.Loan;
import com.example.jsmotorscarfinance.models.Payment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MakePaymentActivity extends AppCompatActivity {
    
    private DatabaseHelper dbHelper;
    private int userId;
    private TextView tvOutstandingBalance;
    private TextInputEditText etAmount;
    private MaterialButton btnPay;
    private double currentBalance = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);
        
        dbHelper = new DatabaseHelper(this);
        userId = getIntent().getIntExtra("USER_ID", -1);
        double prefilledAmount = getIntent().getDoubleExtra("PAYMENT_AMOUNT", 0.0);
        
        tvOutstandingBalance = findViewById(R.id.tvOutstandingBalance);
        etAmount = findViewById(R.id.etPaymentAmount);
        btnPay = findViewById(R.id.btnConfirmPayment);
        
        loadBalance();
        
        if (prefilledAmount > 0) {
            etAmount.setText(String.format(Locale.US, "%.2f", prefilledAmount));
        }
        
        btnPay.setOnClickListener(v -> processPayment());
    }

    private void loadBalance() {
        List<Loan> loans = dbHelper.getLoansByUser(userId);
        if (!loans.isEmpty()) {
            currentBalance = loans.get(0).getRemainingAmount();
            tvOutstandingBalance.setText(String.format(Locale.getDefault(), "Rs %,.2f", currentBalance));
            
            if (currentBalance <= 0) {
                btnPay.setEnabled(false);
                btnPay.setText("Loan Fully Paid");
                etAmount.setEnabled(false);
            }
        }
    }

    private void processPayment() {
        String amtStr = etAmount.getText().toString().trim();
        if (amtStr.isEmpty()) {
            etAmount.setError("Enter amount");
            return;
        }

        double amt = Double.parseDouble(amtStr);
        
        if (amt <= 0) {
            etAmount.setError("Enter a valid amount");
            return;
        }

        if (amt > currentBalance + 0.01) {
            etAmount.setError("Amount exceeds outstanding balance (Rs " + String.format("%.2f", currentBalance) + ")");
            return;
        }

        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
        
        Payment p = new Payment();
        p.setUserId(userId);
        p.setAmount(amt);
        p.setDate(date);
        
        long result = dbHelper.addPayment(p);
        if (result != -1) {
            Toast.makeText(this, "Payment of Rs " + String.format("%.2f", amt) + " successful!", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Payment failed. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
