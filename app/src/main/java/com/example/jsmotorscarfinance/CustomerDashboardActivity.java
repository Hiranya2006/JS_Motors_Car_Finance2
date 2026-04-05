package com.example.jsmotorscarfinance;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CustomerDashboardActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private Button btnBrowse, btnViewStatus, btnViewSchedule, btnMakePayment, btnLogout;
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
        btnViewSchedule = findViewById(R.id.btnViewSchedule);
        btnMakePayment = findViewById(R.id.btnMakePayment);
        btnLogout = findViewById(R.id.btnLogout);

        tvWelcome.setText("Welcome, " + userName + "!");

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

        btnViewSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(this, PaymentScheduleActivity.class);
            startActivity(intent);
        });

        btnMakePayment.setOnClickListener(v -> {
            Intent intent = new Intent(this, MakePaymentActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
