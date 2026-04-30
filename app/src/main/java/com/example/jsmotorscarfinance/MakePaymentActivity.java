package com.example.jsmotorscarfinance;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.jsmotorscarfinance.database.DatabaseHelper;
import com.example.jsmotorscarfinance.models.Payment;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MakePaymentActivity extends AppCompatActivity {
    
    private DatabaseHelper dbHelper;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);
        
        dbHelper = new DatabaseHelper(this);
        userId = getIntent().getIntExtra("USER_ID", -1);
        double prefilledAmount = getIntent().getDoubleExtra("PAYMENT_AMOUNT", 0.0);
        
        EditText etAmount = findViewById(R.id.etPaymentAmount);
        if (prefilledAmount > 0) {
            etAmount.setText(String.valueOf(prefilledAmount));
        }
        
        Button btnPay = findViewById(R.id.btnConfirmPayment);
        
        btnPay.setOnClickListener(v -> {
            String amtStr = etAmount.getText().toString();
            if(!amtStr.isEmpty()) {
                double amt = Double.parseDouble(amtStr);
                String date = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
                
                Payment p = new Payment();
                p.setUserId(userId);
                p.setAmount(amt);
                p.setDate(date);
                
                long result = dbHelper.addPayment(p);
                if (result != -1) {
                    Toast.makeText(this, "Payment of Rs " + amt + " successful!", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(this, "Payment failed. Please try again.", Toast.LENGTH_SHORT).show();
                }
            } else {
                etAmount.setError("Enter amount");
            }
        });
    }
}
