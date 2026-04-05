package com.example.jsmotorscarfinance;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MakePaymentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);
        
        EditText etAmount = findViewById(R.id.etPaymentAmount);
        Button btnPay = findViewById(R.id.btnConfirmPayment);
        
        btnPay.setOnClickListener(v -> {
            String amt = etAmount.getText().toString();
            if(!amt.isEmpty()) {
                Toast.makeText(this, "Payment of $" + amt + " successful!", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
