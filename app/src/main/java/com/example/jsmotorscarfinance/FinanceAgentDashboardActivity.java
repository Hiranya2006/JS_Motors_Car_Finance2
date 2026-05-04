// Finance Agent Dashboard Activity: Workspace for agents to review and finalize loan applications
package com.example.jsmotorscarfinance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jsmotorscarfinance.adapters.LoanAdapter;
import com.example.jsmotorscarfinance.database.DatabaseHelper;
import com.example.jsmotorscarfinance.models.Loan;
import java.util.List;

public class FinanceAgentDashboardActivity extends AppCompatActivity implements LoanAdapter.OnLoanActionListener {

    private RecyclerView rvAgentLoans;
    private LoanAdapter adapter;
    private DatabaseHelper dbHelper;
    private Button btnLogoutAgent, btnViewPaymentsAgent;

    // Initializes the agent dashboard and sets up the application queue
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_agent_dashboard);

        dbHelper = new DatabaseHelper(this);
        rvAgentLoans = findViewById(R.id.rvAgentLoans);
        btnLogoutAgent = findViewById(R.id.btnLogoutAgent);
        btnViewPaymentsAgent = findViewById(R.id.btnViewPaymentsAgent);

        rvAgentLoans.setLayoutManager(new LinearLayoutManager(this));
        loadApplications();

        btnViewPaymentsAgent.setOnClickListener(v -> {
            startActivity(new Intent(this, ViewPaymentsActivity.class));
        });

        btnLogoutAgent.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    // Fetches all loan applications for the agent to review
    private void loadApplications() {
        List<Loan> loanList = dbHelper.getAllLoans();
        adapter = new LoanAdapter(loanList, "Finance Agent", this);
        rvAgentLoans.setAdapter(adapter);
    }

    // Opens the terms configuration screen for the selected loan
    @Override
    public void onApprove(Loan loan) {
        Intent intent = new Intent(this, SetLoanTermsActivity.class);
        intent.putExtra("LOAN_ID", loan.getId());
        startActivity(intent);
    }

    // Rejects the loan application and updates its status in the database
    @Override
    public void onReject(Loan loan) {
        dbHelper.updateLoanStatus(loan.getId(), "Rejected by Agent");
        Toast.makeText(this, "Loan Rejected", Toast.LENGTH_SHORT).show();
        loadApplications();
    }

    // Refreshes the application list when the activity is resumed
    @Override
    protected void onResume() {
        super.onResume();
        loadApplications();
    }
}
