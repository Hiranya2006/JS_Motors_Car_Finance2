package com.example.jsmotorscarfinance.adapters;

import android.content.Context;
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

    private final List<Loan> loanList;
    private final String userRole;
    private final OnLoanActionListener actionListener;
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
        Context context = holder.itemView.getContext();

        holder.tvVehicleName.setText(loan.getVehicleName());
        
        // Using string resources with placeholders to avoid lint errors and support localization
        holder.tvLoanAmount.setText(context.getString(R.string.loan_amount_label, loan.getAmount()));
        holder.tvStatus.setText(context.getString(R.string.loan_status_label, loan.getStatus()));

        holder.adminControls.setVisibility(View.GONE);
        
        if ("Admin".equals(userRole) || "Finance Agent".equals(userRole)) {
            holder.adminControls.setVisibility(View.VISIBLE);
            
            // Visibility logic: Admin reviews "Pending Approval", Agents review "Forwarded"
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
            
            holder.btnViewDocs.setOnClickListener(v -> {
                List<String> docs = dbHelper.getDocumentsForLoan(loan.getId());
                if (docs.isEmpty()) {
                    Toast.makeText(context, R.string.no_docs_found, Toast.LENGTH_SHORT).show();
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (String d : docs) sb.append(d).append("\n");
                    new AlertDialog.Builder(context)
                            .setTitle(R.string.uploaded_docs_title)
                            .setMessage(sb.toString())
                            .setPositiveButton(R.string.ok, null)
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
