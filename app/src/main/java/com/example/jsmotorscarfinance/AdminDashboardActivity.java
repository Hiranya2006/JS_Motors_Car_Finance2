package com.example.jsmotorscarfinance;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {
    private Button btnManageVehicles, btnViewApplications, btnViewSales, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        btnManageVehicles = findViewById(R.id.btnManageVehicles);
        btnViewApplications = findViewById(R.id.btnViewApplications);
        btnViewSales = findViewById(R.id.btnViewSales);
        btnLogout = findViewById(R.id.btnLogoutAdmin);

        btnManageVehicles.setOnClickListener(v -> {
            startActivity(new Intent(this, ManageVehiclesActivity.class));
        });

        btnViewApplications.setOnClickListener(v -> {
            Intent intent = new Intent(this, ViewStatusActivity.class);
            intent.putExtra("IS_ADMIN", true);
            startActivity(intent);
        });

        btnViewSales.setOnClickListener(v -> {
            startActivity(new Intent(this, ViewSalesActivity.class));
        });

        btnLogout.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
