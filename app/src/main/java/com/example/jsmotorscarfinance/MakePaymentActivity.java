package com.example.jsmotorscarfinance;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jsmotorscarfinance.adapters.PaymentAdapter;
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
    private RecyclerView rvPayments;
    private double currentBalance = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);
        
        dbHelper = new DatabaseHelper(this);
        userId = getIntent().getIntExtra("USER_ID", -1);
        
        tvOutstandingBalance = findViewById(R.id.tvOutstandingBalance);
        etAmount = findViewById(R.id.etPaymentAmount);
        btnPay = findViewById(R.id.btnConfirmPayment);
        rvPayments = findViewById(R.id.rvPayments);
        
        rvPayments.setLayoutManager(new LinearLayoutManager(this));
        
        loadData();
        
        btnPay.setOnClickListener(v -> processPayment());
    }

    private void loadData() {
        // Load Balance
        List<Loan> loans = dbHelper.getLoansByUser(userId);
        if (!loans.isEmpty()) {
            // Find the loan with the highest remaining balance or just the first approved one
            Loan activeLoan = null;
            for (Loan l : loans) {
                if ("Approved".equalsIgnoreCase(l.getStatus())) {
                    activeLoan = l;
                    break;
                }
            }
            
            if (activeLoan != null) {
                currentBalance = activeLoan.getRemainingAmount();
                tvOutstandingBalance.setText(String.format(Locale.US, "Rs. %,.2f", currentBalance));
                
                if (currentBalance <= 0) {
                    btnPay.setEnabled(false);
                    btnPay.setText("LOAN FULLY PAID");
                    etAmount.setEnabled(false);
                }
            } else {
                tvOutstandingBalance.setText("Rs. 0.00");
                btnPay.setEnabled(false);
                btnPay.setText("NO ACTIVE LOAN");
                etAmount.setEnabled(false);
            }
        }

        // Load History
        List<Payment> payments = dbHelper.getPaymentsByUser(userId);
        rvPayments.setAdapter(new PaymentAdapter(payments));
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
            etAmount.setError("Exceeds balance (Rs. " + String.format("%.2f", currentBalance) + ")");
            return;
        }

        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
        
        Payment p = new Payment();
        p.setUserId(userId);
        p.setAmount(amt);
        p.setDate(date);
        
        long result = dbHelper.addPayment(p);
        if (result != -1) {
            Toast.makeText(this, "Payment of Rs. " + String.format("%.2f", amt) + " successful!", Toast.LENGTH_LONG).show();
            etAmount.setText("");
            loadData(); // Refresh balance and history
        } else {
            Toast.makeText(this, "Payment failed. Try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
