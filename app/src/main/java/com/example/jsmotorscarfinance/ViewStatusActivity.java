package com.example.jsmotorscarfinance;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jsmotorscarfinance.adapters.LoanAdapter;
import com.example.jsmotorscarfinance.database.DatabaseHelper;
import com.example.jsmotorscarfinance.models.Loan;
import java.util.List;

public class ViewStatusActivity extends AppCompatActivity implements LoanAdapter.OnLoanActionListener {

    private RecyclerView rvLoans;
    private LoanAdapter adapter;
    private DatabaseHelper dbHelper;
    private int userId;
    private boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_status);

        userId = getIntent().getIntExtra("USER_ID", -1);
        isAdmin = getIntent().getBooleanExtra("IS_ADMIN", false);
        dbHelper = new DatabaseHelper(this);
        rvLoans = findViewById(R.id.rvLoans);
        rvLoans.setLayoutManager(new LinearLayoutManager(this));

        loadLoans();
    }

    private void loadLoans() {
        List<Loan> loanList;
        if (isAdmin) {
            loanList = dbHelper.getAllLoans();
        } else {
            loanList = dbHelper.getLoansByUser(userId);
        }
        
        String role = isAdmin ? "Admin" : "Customer";
        adapter = new LoanAdapter(loanList, role, this);
        rvLoans.setAdapter(adapter);
    }

    @Override
    public void onApprove(Loan loan) {
        if (isAdmin) {
            dbHelper.updateLoanStatus(loan.getId(), "Forwarded");
            Toast.makeText(this, "Application Verified and Forwarded", Toast.LENGTH_SHORT).show();
            loadLoans();
        }
    }

    @Override
    public void onReject(Loan loan) {
        if (isAdmin) {
            dbHelper.updateLoanStatus(loan.getId(), "Rejected by Admin");
            Toast.makeText(this, "Application Rejected", Toast.LENGTH_SHORT).show();
            loadLoans();
        }
    }
}
