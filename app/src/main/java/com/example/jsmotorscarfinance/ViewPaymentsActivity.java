// View Payments Activity: Allows administrators to monitor all incoming customer payments
package com.example.jsmotorscarfinance;

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
import com.example.jsmotorscarfinance.models.Payment;
import java.util.List;
import java.util.Locale;

public class ViewPaymentsActivity extends AppCompatActivity {

    private RecyclerView rvAllPayments;
    private DatabaseHelper dbHelper;

    // Initializes the global payment tracking screen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_payments);

        dbHelper = new DatabaseHelper(this);
        rvAllPayments = findViewById(R.id.rvAllPayments);
        rvAllPayments.setLayoutManager(new LinearLayoutManager(this));

        loadPayments();
    }

    // Fetches the entire payment history from the database for administrative review
    private void loadPayments() {
        List<Payment> payments = dbHelper.getAllPayments();
        rvAllPayments.setAdapter(new PaymentsAdapter(payments));
    }

    // Inner class Adapter to manage the list of all system payments
    private static class PaymentsAdapter extends RecyclerView.Adapter<PaymentsAdapter.ViewHolder> {
        private List<Payment> payments;

        public PaymentsAdapter(List<Payment> payments) {
            this.payments = payments;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_payment, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Payment p = payments.get(position);
            holder.tvName.setText("User ID: " + p.getUserId()); 
            holder.tvDate.setText("Date: " + p.getDate());
            holder.tvAmount.setText(String.format(Locale.getDefault(), "Rs %,.2f", p.getAmount()));
        }

        @Override
        public int getItemCount() {
            return payments.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvName, tvDate, tvAmount;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvAdminCustomerName);
                tvDate = itemView.findViewById(R.id.tvAdminPaymentDate);
                tvAmount = itemView.findViewById(R.id.tvAdminPaymentAmount);
            }
        }
    }
}
