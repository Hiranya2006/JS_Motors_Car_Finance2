// Loan Adapter: Bridges the loan data from SQLite to the modern card UI items
package com.example.jsmotorscarfinance.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jsmotorscarfinance.R;
import com.example.jsmotorscarfinance.models.Loan;
import java.util.List;

public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.LoanViewHolder> {

    private final List<Loan> loanList;
    private final String userRole;
    private final OnLoanActionListener actionListener;

    // Interface to delegate loan approval and rejection actions to the host Activity
    public interface OnLoanActionListener {
        void onApprove(Loan loan);
        void onReject(Loan loan);
    }

    // Constructor to initialize the adapter with data and role context
    public LoanAdapter(List<Loan> loanList, String userRole, OnLoanActionListener actionListener) {
        this.loanList = loanList;
        this.userRole = userRole;
        this.actionListener = actionListener;
    }

    // Inflates the custom loan item layout when the RecyclerView creates a new row
    @NonNull
    @Override
    public LoanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loan, parent, false);
        return new LoanViewHolder(view);
    }

    // Binds loan data to UI elements and handles role-based visibility for control buttons
    @Override
    public void onBindViewHolder(@NonNull LoanViewHolder holder, int position) {
        Loan loan = loanList.get(position);
        Context context = holder.itemView.getContext();

        holder.tvVehicleName.setText(loan.getVehicleName());
        holder.tvLoanAmount.setText(context.getString(R.string.loan_amount_label, loan.getAmount()));
        holder.tvStatus.setText(context.getString(R.string.loan_status_label, loan.getStatus()));

        holder.adminControls.setVisibility(View.GONE);
        
        if ("Admin".equals(userRole) || "Finance Agent".equals(userRole)) {
            holder.adminControls.setVisibility(View.VISIBLE);
            
            boolean canAction = false;
            if ("Admin".equals(userRole) && "Pending Approval".equalsIgnoreCase(loan.getStatus())) {
                canAction = true;
            } else if ("Finance Agent".equals(userRole) && "Forwarded".equalsIgnoreCase(loan.getStatus())) {
                canAction = true;
            }

            holder.btnApprove.setVisibility(canAction ? View.VISIBLE : View.GONE);
            holder.btnReject.setVisibility(canAction ? View.VISIBLE : View.GONE);

            holder.btnApprove.setOnClickListener(v -> actionListener.onApprove(loan));
            holder.btnReject.setOnClickListener(v -> actionListener.onReject(loan));
        }
    }

    // Returns the total number of loans in the list
    @Override
    public int getItemCount() {
        return loanList.size();
    }

    // ViewHolder class to optimize UI performance by caching view references
    public static class LoanViewHolder extends RecyclerView.ViewHolder {
        TextView tvVehicleName, tvLoanAmount, tvStatus;
        LinearLayout adminControls;
        Button btnApprove, btnReject;

        public LoanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvVehicleName = itemView.findViewById(R.id.tvVehicleName);
            tvLoanAmount = itemView.findViewById(R.id.tvLoanAmount);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            adminControls = itemView.findViewById(R.id.adminControls);
            btnApprove = itemView.findViewById(R.id.btnApprove);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }
}
