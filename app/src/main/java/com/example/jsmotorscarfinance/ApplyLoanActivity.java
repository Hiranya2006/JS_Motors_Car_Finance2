package com.example.jsmotorscarfinance;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.jsmotorscarfinance.database.DatabaseHelper;
import com.example.jsmotorscarfinance.models.Loan;
import java.util.Locale;

public class ApplyLoanActivity extends AppCompatActivity {

    private EditText etVehicleName, etVehiclePrice, etDownPayment, etLoanAmount;
    private Button btnUploadDoc, btnSubmitLoan;
    private TextView tvFilePath, tvMonthlyEstimate;
    private DatabaseHelper dbHelper;
    private int userId;
    private boolean isDocUploaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_loan);

        dbHelper = new DatabaseHelper(this);
        userId = getIntent().getIntExtra("USER_ID", -1);
        String prefilledVehicle = getIntent().getStringExtra("VEHICLE_NAME");
        double prefilledPrice = getIntent().getDoubleExtra("VEHICLE_PRICE", 0);

        etVehicleName = findViewById(R.id.etVehicleName);
        etVehiclePrice = findViewById(R.id.etVehiclePrice);
        etDownPayment = findViewById(R.id.etDownPayment);
        etLoanAmount = findViewById(R.id.etLoanAmount);
        btnUploadDoc = findViewById(R.id.btnUploadDoc);
        btnSubmitLoan = findViewById(R.id.btnSubmitLoan);
        tvFilePath = findViewById(R.id.tvFilePath);
        tvMonthlyEstimate = findViewById(R.id.tvMonthlyEstimate);

        if (prefilledVehicle != null) {
            etVehicleName.setText(prefilledVehicle);
            etVehiclePrice.setText(String.format(Locale.US, "%.0f", prefilledPrice));
        }

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateLoanAndEstimate();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        };

        etVehiclePrice.addTextChangedListener(textWatcher);
        etDownPayment.addTextChangedListener(textWatcher);

        btnUploadDoc.setOnClickListener(v -> simulateDocumentProcessing());
        btnSubmitLoan.setOnClickListener(v -> submitApplication());
        
        calculateLoanAndEstimate();
    }

    private void calculateLoanAndEstimate() {
        try {
            double price = etVehiclePrice.getText().toString().isEmpty() ? 0 : Double.parseDouble(etVehiclePrice.getText().toString());
            double downPayment = etDownPayment.getText().toString().isEmpty() ? 0 : Double.parseDouble(etDownPayment.getText().toString());
            double loanAmount = price - downPayment;
            
            if (loanAmount < 0) loanAmount = 0;
            etLoanAmount.setText(String.format(Locale.US, "%.2f", loanAmount));

            double annualRate = 0.089;
            double monthlyRate = annualRate / 12;
            int months = 60;
            
            if (loanAmount > 0) {
                double monthlyPayment = (loanAmount * monthlyRate) / (1 - Math.pow(1 + monthlyRate, -months));
                tvMonthlyEstimate.setText(String.format(Locale.US, "Estimated: Rs %,.2f /mo", monthlyPayment));
            } else {
                tvMonthlyEstimate.setText("Estimated: Rs 0.00 /mo");
            }
        } catch (NumberFormatException e) {
            etLoanAmount.setText("0.00");
        }
    }

    private void simulateDocumentProcessing() {
        btnUploadDoc.setEnabled(false);
        btnUploadDoc.setText("Scanning Documents...");
        tvFilePath.postDelayed(() -> {
            isDocUploaded = true;
            btnUploadDoc.setEnabled(true);
            btnUploadDoc.setText("ID Verified ✓");
            tvFilePath.setText("Verified via Open Banking");
            tvFilePath.setTextColor(getResources().getColor(R.color.primary));
            Toast.makeText(this, "Verification Successful", Toast.LENGTH_SHORT).show();
        }, 1500);
    }

    private void submitApplication() {
        String vehicle = etVehicleName.getText().toString().trim();
        String amountStr = etLoanAmount.getText().toString().trim();

        if (vehicle.isEmpty() || amountStr.isEmpty() || Double.parseDouble(amountStr) <= 0) {
            Toast.makeText(this, "Enter valid vehicle and price", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isDocUploaded) {
            Toast.makeText(this, "Please verify your ID first", Toast.LENGTH_SHORT).show();
            return;
        }

        Loan loan = new Loan();
        loan.setUserId(userId);
        loan.setVehicleName(vehicle);
        loan.setAmount(Double.parseDouble(amountStr));
        loan.setStatus("Pending");

        long loanId = dbHelper.applyLoan(loan);
        if (loanId != -1) {
            btnSubmitLoan.setText("Decision in progress...");
            btnSubmitLoan.setEnabled(false);
            btnSubmitLoan.postDelayed(() -> {
                Toast.makeText(this, "JS MOTORS: Application Pre-Approved!", Toast.LENGTH_LONG).show();
                finish();
            }, 2000);
        } else {
            Toast.makeText(this, "Submission Error", Toast.LENGTH_SHORT).show();
        }
    }
}
