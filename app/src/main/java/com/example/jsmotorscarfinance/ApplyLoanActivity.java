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

public class ApplyLoanActivity extends AppCompatActivity {

    private EditText etVehicleName, etVehiclePrice, etDownPayment, etLoanAmount;
    private Button btnUploadDoc, btnSubmitLoan;
    private TextView tvFilePath;
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

        if (prefilledVehicle != null) {
            etVehicleName.setText(prefilledVehicle);
            etVehiclePrice.setText(String.valueOf(prefilledPrice));
        }

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateLoanAmount();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        };

        etVehiclePrice.addTextChangedListener(textWatcher);
        etDownPayment.addTextChangedListener(textWatcher);

        btnUploadDoc.setOnClickListener(v -> {
            isDocUploaded = true;
            tvFilePath.setText("Uploaded: id_and_income_proof.pdf");
            Toast.makeText(this, "Documents Uploaded Successfully", Toast.LENGTH_SHORT).show();
        });

        btnSubmitLoan.setOnClickListener(v -> {
            String vehicle = etVehicleName.getText().toString().trim();
            String amountStr = etLoanAmount.getText().toString().trim();

            if (vehicle.isEmpty() || amountStr.isEmpty()) {
                Toast.makeText(this, "Please complete the form", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isDocUploaded) {
                Toast.makeText(this, "Please upload required documents first", Toast.LENGTH_SHORT).show();
                return;
            }

            Loan loan = new Loan();
            loan.setUserId(userId);
            loan.setVehicleName(vehicle);
            loan.setAmount(Double.parseDouble(amountStr));
            loan.setStatus("Pending");

            long loanId = dbHelper.applyLoan(loan);
            if (loanId != -1) {
                // Save the document reference in the database
                dbHelper.uploadDocument((int) loanId, "ID & Income Proof", "dummy_path/id_and_income_proof.pdf");
                Toast.makeText(this, "Application Submitted with Documents", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "Submission Error", Toast.LENGTH_SHORT).show();
            }
        });
        
        calculateLoanAmount();
    }

    private void calculateLoanAmount() {
        try {
            double price = Double.parseDouble(etVehiclePrice.getText().toString());
            double downPayment = etDownPayment.getText().toString().isEmpty() ? 0 : Double.parseDouble(etDownPayment.getText().toString());
            double loanAmount = price - downPayment;
            etLoanAmount.setText(String.valueOf(loanAmount));
        } catch (NumberFormatException e) {
            etLoanAmount.setText("");
        }
    }
}
