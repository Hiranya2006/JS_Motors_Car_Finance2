package com.example.jsmotorscarfinance.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jsmotorscarfinance.R;
import com.example.jsmotorscarfinance.models.Payment;
import java.util.List;
import java.util.Locale;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {
    private final List<Payment> paymentList;

    public PaymentAdapter(List<Payment> paymentList) {
        this.paymentList = paymentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Payment p = paymentList.get(position);
        holder.tvDate.setText(p.getDate());
        holder.tvInstallment.setText("Payment Received");
        holder.tvAmount.setText(String.format(Locale.US, "Rs. %,.2f", p.getAmount()));
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDueDate, tvInstallment, tvAmount, tvDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Re-using item_payment layout but matching IDs
            tvDate = itemView.findViewById(R.id.tvDueDate);
            tvInstallment = itemView.findViewById(R.id.tvInstallmentNo);
            tvAmount = itemView.findViewById(R.id.tvPaymentAmount);
        }
    }
}
