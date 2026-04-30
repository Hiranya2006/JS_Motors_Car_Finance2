package com.example.jsmotorscarfinance;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    private Button btnManageVehicles, btnViewApplications, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        btnManageVehicles = findViewById(R.id.btnManageVehicles);
        btnViewApplications = findViewById(R.id.btnViewApplications);
        btnLogout = findViewById(R.id.btnLogoutAdmin);

        btnManageVehicles.setOnClickListener(v -> {
            startActivity(new Intent(this, ManageVehiclesActivity.class));
        });

        btnViewApplications.setOnClickListener(v -> {
            Intent intent = new Intent(this, ViewStatusActivity.class);
            intent.putExtra("IS_ADMIN", true);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
