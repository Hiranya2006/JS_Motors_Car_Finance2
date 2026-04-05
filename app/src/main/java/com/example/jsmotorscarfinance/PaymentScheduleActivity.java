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
import java.util.ArrayList;
import java.util.List;

public class PaymentScheduleActivity extends AppCompatActivity {

    private RecyclerView rvSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_schedule);

        rvSchedule = findViewById(R.id.rvSchedule);
        rvSchedule.setLayoutManager(new LinearLayoutManager(this));

        // Dummy schedule for 60 months
        List<String> schedule = new ArrayList<>();
        for (int i = 1; i <= 60; i++) {
            schedule.add("Month " + i);
        }

        rvSchedule.setAdapter(new ScheduleAdapter(schedule));
    }

    private static class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
        private List<String> data;

        public ScheduleAdapter(List<String> data) {
            this.data = data;
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
            holder.tvDueDate.setText("2024-0" + ((position % 9) + 1) + "-15");
            holder.tvPaymentAmount.setText("$450.00");
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvInstallmentNo, tvDueDate, tvPaymentAmount;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvInstallmentNo = itemView.findViewById(R.id.tvInstallmentNo);
                tvDueDate = itemView.findViewById(R.id.tvDueDate);
                tvPaymentAmount = itemView.findViewById(R.id.tvPaymentAmount);
            }
        }
    }
}
