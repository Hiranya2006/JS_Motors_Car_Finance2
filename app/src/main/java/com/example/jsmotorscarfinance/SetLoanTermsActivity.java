package com.example.jsmotorscarfinance;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.jsmotorscarfinance.database.DatabaseHelper;

public class SetLoanTermsActivity extends AppCompatActivity {

    private EditText etRate, etPeriod, etInstallment;
    private Button btnConfirm;
    private DatabaseHelper dbHelper;
    private int loanId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_loan_terms);

        dbHelper = new DatabaseHelper(this);
        loanId = getIntent().getIntExtra("LOAN_ID", -1);

        etRate = findViewById(R.id.etInterestRate);
        etPeriod = findViewById(R.id.etPeriod);
        etInstallment = findViewById(R.id.etInstallment);
        btnConfirm = findViewById(R.id.btnConfirmTerms);

        btnConfirm.setOnClickListener(v -> {
            String rateStr = etRate.getText().toString();
            String periodStr = etPeriod.getText().toString();
            String installmentStr = etInstallment.getText().toString();

            if (rateStr.isEmpty() || periodStr.isEmpty() || installmentStr.isEmpty()) {
                Toast.makeText(this, "Please fill all terms", Toast.LENGTH_SHORT).show();
                return;
            }

            double rate = Double.parseDouble(rateStr);
            int period = Integer.parseInt(periodStr);
            double installment = Double.parseDouble(installmentStr);

            dbHelper.setLoanTerms(loanId, rate, period, installment);
            dbHelper.updateLoanStatus(loanId, "Approved");
            
            Toast.makeText(this, "Loan Terms Set and Approved", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
