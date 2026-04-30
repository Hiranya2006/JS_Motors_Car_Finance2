package com.example.jsmotorscarfinance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CustomerDashboardActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private View btnBrowse, btnViewStatus, btnMakePayment, btnLogout, btnPaymentPlan;
    private int userId;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);

        userId = getIntent().getIntExtra("USER_ID", -1);
        userName = getIntent().getStringExtra("USER_NAME");

        tvWelcome = findViewById(R.id.tvWelcome);
        btnBrowse = findViewById(R.id.btnBrowseVehicles);
        btnViewStatus = findViewById(R.id.btnViewStatus);
        btnMakePayment = findViewById(R.id.btnMakePayment);
        btnPaymentPlan = findViewById(R.id.btnPaymentPlan);
        btnLogout = findViewById(R.id.btnLogout);

        if (userName != null) {
            tvWelcome.setText("Hi, " + userName + "!");
        }

        btnBrowse.setOnClickListener(v -> {
            Intent intent = new Intent(this, BrowseVehiclesActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });

        btnViewStatus.setOnClickListener(v -> {
            Intent intent = new Intent(this, ViewStatusActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });

        btnPaymentPlan.setOnClickListener(v -> {
            Intent intent = new Intent(this, PaymentScheduleActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });

        btnMakePayment.setOnClickListener(v -> {
            Intent intent = new Intent(this, MakePaymentActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
