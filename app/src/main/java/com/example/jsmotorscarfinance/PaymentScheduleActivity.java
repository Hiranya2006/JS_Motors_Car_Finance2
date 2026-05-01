package com.example.jsmotorscarfinance;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jsmotorscarfinance.database.DatabaseHelper;
import com.example.jsmotorscarfinance.models.Loan;
import com.google.android.material.appbar.MaterialToolbar;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PaymentScheduleActivity extends AppCompatActivity {

    private RecyclerView rvSchedule;
    private TextView tvSummaryTotal, tvSummaryPaid, tvSummaryOutstanding, tvSummaryInterest;
    private DatabaseHelper dbHelper;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_schedule);

        dbHelper = new DatabaseHelper(this);
        userId = getIntent().getIntExtra("USER_ID", -1);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        tvSummaryTotal = findViewById(R.id.tvSummaryTotal);
        tvSummaryPaid = findViewById(R.id.tvSummaryPaid);
        tvSummaryOutstanding = findViewById(R.id.tvSummaryOutstanding);
        tvSummaryInterest = findViewById(R.id.tvSummaryInterest);

        rvSchedule = findViewById(R.id.rvSchedule);
        rvSchedule.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLoanDetails();
    }

    private void loadLoanDetails() {
        List<Loan> loans = dbHelper.getLoansByUser(userId);
        if (!loans.isEmpty()) {
            Loan loan = loans.get(0);

            tvSummaryTotal.setText(String.format(Locale.getDefault(), "Rs %,.2f", loan.getAmount()));
            tvSummaryInterest.setText(String.format(Locale.getDefault(), "%.1f%%", loan.getInterestRate()));
            tvSummaryPaid.setText(String.format(Locale.getDefault(), "Rs %,.2f", loan.getPaidAmount()));
            tvSummaryOutstanding.setText(String.format(Locale.getDefault(), "Rs %,.2f", Math.max(0, loan.getRemainingAmount())));

            double monthlyAmount = loan.getInstallmentAmount();
            int totalMonths = loan.getRepaymentPeriod();
            double totalPaid = loan.getPaidAmount();

            List<String> schedule = new ArrayList<>();
            for (int i = 1; i <= totalMonths; i++) {
                schedule.add("Installment #" + i);
            }
            rvSchedule.setAdapter(new ScheduleAdapter(schedule, monthlyAmount, totalPaid));
        }
    }

    private class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
        private List<String> data;
        private double amount;
        private double totalPaid;

        public ScheduleAdapter(List<String> data, double amount, double totalPaid) {
            this.data = data;
            this.amount = amount;
            this.totalPaid = totalPaid;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.tvInstallmentNo.setText(data.get(position));
            
            int month = (position % 12) + 1;
            int year = 2024 + (position / 12);
            holder.tvDueDate.setText(String.format(Locale.getDefault(), "%d-%02d-15", year, month));
            holder.tvPaymentAmount.setText(String.format(Locale.getDefault(), "Rs %,.2f", amount));
            
            // Check if this installment is covered by totalPaid
            double requiredToCoverThis = (position + 1) * amount;
            if (totalPaid >= requiredToCoverThis - 0.01) { // small delta for double precision
                holder.btnPayItem.setText("PAID");
                holder.btnPayItem.setEnabled(false);
                holder.btnPayItem.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            } else {
                holder.btnPayItem.setText("PAY NOW");
                holder.btnPayItem.setEnabled(true);
                holder.btnPayItem.setOnClickListener(v -> {
                    Intent intent = new Intent(PaymentScheduleActivity.this, MakePaymentActivity.class);
                    intent.putExtra("USER_ID", userId);
                    intent.putExtra("PAYMENT_AMOUNT", amount);
                    startActivity(intent);
                });
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvInstallmentNo, tvDueDate, tvPaymentAmount, btnPayItem;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvInstallmentNo = itemView.findViewById(R.id.tvInstallmentNo);
                tvDueDate = itemView.findViewById(R.id.tvDueDate);
                tvPaymentAmount = itemView.findViewById(R.id.tvPaymentAmount);
                btnPayItem = itemView.findViewById(R.id.btnPayItem);
            }
        }
    }
}
