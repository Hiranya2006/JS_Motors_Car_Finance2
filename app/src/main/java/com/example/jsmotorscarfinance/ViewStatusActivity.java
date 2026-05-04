// View Status Activity: Displays a list of loan applications for both customers and admins
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

    // Initializes the activity and sets up the loan list view
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

    // Fetches loans from the database based on user role and updates the UI
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

    // Handles the approval action for a loan application (Admin only)
    @Override
    public void onApprove(Loan loan) {
        if (isAdmin) {
            dbHelper.updateLoanStatus(loan.getId(), "Forwarded");
            Toast.makeText(this, "Application Verified and Forwarded", Toast.LENGTH_SHORT).show();
            loadLoans();
        }
    }

    // Handles the rejection action for a loan application (Admin only)
    @Override
    public void onReject(Loan loan) {
        if (isAdmin) {
            dbHelper.updateLoanStatus(loan.getId(), "Rejected by Admin");
            Toast.makeText(this, "Application Rejected", Toast.LENGTH_SHORT).show();
            loadLoans();
        }
    }
}
