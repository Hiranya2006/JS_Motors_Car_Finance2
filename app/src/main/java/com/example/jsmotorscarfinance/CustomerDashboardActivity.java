// Customer Dashboard Activity: High-level overview and navigation hub for car finance customers
package com.example.jsmotorscarfinance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.jsmotorscarfinance.database.DatabaseHelper;
import com.example.jsmotorscarfinance.models.Loan;
import com.example.jsmotorscarfinance.models.User;
import java.util.List;

public class CustomerDashboardActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private View btnBrowse, btnViewStatus, btnMakePayment, btnLogout, btnPaymentPlan, btnSettings;
    private int userId;
    private DatabaseHelper dbHelper;

    // Initializes the dashboard and sets up the primary user navigation tiles
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);

        dbHelper = new DatabaseHelper(this);
        userId = getIntent().getIntExtra("USER_ID", -1);

        tvWelcome = findViewById(R.id.tvWelcome);
        btnBrowse = findViewById(R.id.btnBrowseVehicles);
        btnViewStatus = findViewById(R.id.btnViewStatus);
        btnMakePayment = findViewById(R.id.btnMakePayment);
        btnPaymentPlan = findViewById(R.id.btnPaymentPlan);
        btnSettings = findViewById(R.id.btnSettings);
        btnLogout = findViewById(R.id.btnLogout);

        setupClickListeners();
    }

    // Refreshes the welcome message and interactive button states upon returning to the screen
    @Override
    protected void onResume() {
        super.onResume();
        refreshUI();
    }

    // Updates the UI text and enables/disables features based on the latest loan approval status
    private void refreshUI() {
        User user = dbHelper.getUserById(userId);
        if (user != null && user.getName() != null) {
            tvWelcome.setText(getString(R.string.welcome_user_format, user.getName()));
        }
        
        boolean isApproved = isLoanApproved();
        btnMakePayment.setAlpha(isApproved ? 1.0f : 0.5f);
        btnPaymentPlan.setAlpha(isApproved ? 1.0f : 0.5f);
    }

    // Logic to determine if the user has an active, approved finance plan
    private boolean isLoanApproved() {
        List<Loan> loans = dbHelper.getLoansByUser(userId);
        if (loans != null && !loans.isEmpty()) {
            String status = loans.get(0).getStatus();
            return "Approved".equalsIgnoreCase(status) || "Fully Paid".equalsIgnoreCase(status);
        }
        return false;
    }

    // Configures the redirection logic for all dashboard interaction tiles
    private void setupClickListeners() {
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
            if (isLoanApproved()) {
                Intent intent = new Intent(this, PaymentScheduleActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Payment plan will be available once your loan is approved.", Toast.LENGTH_SHORT).show();
            }
        });

        btnMakePayment.setOnClickListener(v -> {
            if (isLoanApproved()) {
                Intent intent = new Intent(this, MakePaymentActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Payments are only enabled after loan approval.", Toast.LENGTH_SHORT).show();
            }
        });

        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);
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
