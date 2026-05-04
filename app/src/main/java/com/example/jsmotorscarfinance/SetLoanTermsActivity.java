// Set Loan Terms Activity: Allows Finance Agents to define final interest rates and installments
package com.example.jsmotorscarfinance;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.jsmotorscarfinance.database.DatabaseHelper;
import com.example.jsmotorscarfinance.models.Loan;
import java.util.Locale;

public class SetLoanTermsActivity extends AppCompatActivity {

    private EditText etRate, etPeriod, etInstallment;
    private Button btnConfirm;
    private DatabaseHelper dbHelper;
    private int loanId;
    private double principalAmount = 0;

    // Initializes the underwriting screen and sets up data listeners
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_loan_terms);

        dbHelper = new DatabaseHelper(this);
        loanId = getIntent().getIntExtra("LOAN_ID", -1);
        
        loadLoanData();

        etRate = findViewById(R.id.etInterestRate);
        etPeriod = findViewById(R.id.etPeriod);
        etInstallment = findViewById(R.id.etInstallment);
        btnConfirm = findViewById(R.id.btnConfirmTerms);

        TextWatcher calculator = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateInstallment();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        };

        etRate.addTextChangedListener(calculator);
        etPeriod.addTextChangedListener(calculator);

        btnConfirm.setOnClickListener(v -> {
            saveTerms();
        });
    }

    // Loads the requested loan amount from the database for calculation
    private void loadLoanData() {
        for (Loan l : dbHelper.getAllLoans()) {
            if (l.getId() == loanId) {
                principalAmount = l.getAmount();
                break;
            }
        }
    }

    // Automatically derives the monthly Rs. payment based on interest and period
    private void calculateInstallment() {
        try {
            String rateStr = etRate.getText().toString();
            String periodStr = etPeriod.getText().toString();

            if (rateStr.isEmpty() || periodStr.isEmpty() || principalAmount <= 0) {
                etInstallment.setText("");
                return;
            }

            double annualRate = Double.parseDouble(rateStr) / 100;
            double monthlyRate = annualRate / 12;
            int months = Integer.parseInt(periodStr);

            if (monthlyRate == 0) {
                double installment = principalAmount / months;
                etInstallment.setText(String.format(Locale.US, "%.2f", installment));
            } else {
                double installment = (principalAmount * monthlyRate * Math.pow(1 + monthlyRate, months)) 
                                    / (Math.pow(1 + monthlyRate, months) - 1);
                etInstallment.setText(String.format(Locale.US, "%.2f", installment));
            }
        } catch (Exception e) {
            etInstallment.setText("");
        }
    }

    // Saves the finalized contract terms and updates the loan status to Approved
    private void saveTerms() {
        String rateStr = etRate.getText().toString();
        String periodStr = etPeriod.getText().toString();
        String instStr = etInstallment.getText().toString();

        if (rateStr.isEmpty() || periodStr.isEmpty() || instStr.isEmpty()) {
            Toast.makeText(this, "Please define all terms first", Toast.LENGTH_SHORT).show();
            return;
        }

        double rate = Double.parseDouble(rateStr);
        int period = Integer.parseInt(periodStr);
        double installment = Double.parseDouble(instStr);

        dbHelper.setLoanTerms(loanId, rate, period, installment);
        Toast.makeText(this, "Loan Terms Approved for JS MOTORS", Toast.LENGTH_LONG).show();
        finish();
    }
}
