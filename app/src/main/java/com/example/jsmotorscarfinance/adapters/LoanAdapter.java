package com.example.jsmotorscarfinance.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jsmotorscarfinance.R;
import com.example.jsmotorscarfinance.database.DatabaseHelper;
import com.example.jsmotorscarfinance.models.Loan;
import java.util.List;

public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.LoanViewHolder> {

    private List<Loan> loanList;
    private String userRole;
    private OnLoanActionListener actionListener;
    private DatabaseHelper dbHelper;

    public interface OnLoanActionListener {
        void onApprove(Loan loan);
        void onReject(Loan loan);
    }

    public LoanAdapter(List<Loan> loanList, String userRole, OnLoanActionListener actionListener) {
        this.loanList = loanList;
        this.userRole = userRole;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public LoanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        dbHelper = new DatabaseHelper(parent.getContext());
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loan, parent, false);
        return new LoanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoanViewHolder holder, int position) {
        Loan loan = loanList.get(position);
        holder.tvVehicleName.setText(loan.getVehicleName());
        holder.tvLoanAmount.setText("Amount: $" + String.format("%.2f", loan.getAmount()));
        holder.tvStatus.setText("Status: " + loan.getStatus());

        holder.adminControls.setVisibility(View.GONE);
        
        if (userRole.equals("Admin") || userRole.equals("Finance Agent")) {
            holder.adminControls.setVisibility(View.VISIBLE);
            
            // Logic for visibility of Approve/Reject based on status
            if (loan.getStatus().equals("Pending") || loan.getStatus().equals("Forwarded")) {
                holder.btnApprove.setVisibility(View.VISIBLE);
                holder.btnReject.setVisibility(View.VISIBLE);
            } else {
                holder.btnApprove.setVisibility(View.GONE);
                holder.btnReject.setVisibility(View.GONE);
            }

            holder.btnApprove.setOnClickListener(v -> actionListener.onApprove(loan));
            holder.btnReject.setOnClickListener(v -> actionListener.onReject(loan));
            
            holder.btnViewDocs.setOnClickListener(v -> {
                List<String> docs = dbHelper.getDocumentsForLoan(loan.getId());
                if (docs.isEmpty()) {
                    Toast.makeText(v.getContext(), "No documents uploaded", Toast.LENGTH_SHORT).show();
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (String d : docs) sb.append(d).append("\n");
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("Uploaded Documents")
                            .setMessage(sb.toString())
                            .setPositiveButton("OK", null)
                            .show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return loanList.size();
    }

    public static class LoanViewHolder extends RecyclerView.ViewHolder {
        TextView tvVehicleName, tvLoanAmount, tvStatus;
        LinearLayout adminControls;
        Button btnApprove, btnReject, btnViewDocs;

        public LoanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvVehicleName = itemView.findViewById(R.id.tvVehicleName);
            tvLoanAmount = itemView.findViewById(R.id.tvLoanAmount);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            adminControls = itemView.findViewById(R.id.adminControls);
            btnApprove = itemView.findViewById(R.id.btnApprove);
            btnReject = itemView.findViewById(R.id.btnReject);
            btnViewDocs = itemView.findViewById(R.id.btnViewDocs);
        }
    }
}
