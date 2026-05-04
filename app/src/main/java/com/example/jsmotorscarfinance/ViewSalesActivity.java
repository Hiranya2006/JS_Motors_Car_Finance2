// View Sales Activity: Provides administrators with a detailed report of all successful vehicle sales
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
import com.example.jsmotorscarfinance.models.Sale;
import java.util.List;
import java.util.Locale;

public class ViewSalesActivity extends AppCompatActivity {

    private RecyclerView rvSales;
    private TextView tvTotalSalesCount;
    private DatabaseHelper dbHelper;

    // Initializes the sales report screen and UI components
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sales);

        dbHelper = new DatabaseHelper(this);
        rvSales = findViewById(R.id.rvSales);
        tvTotalSalesCount = findViewById(R.id.tvTotalSalesCount);
        
        rvSales.setLayoutManager(new LinearLayoutManager(this));

        loadSales();
    }

    // Fetches all approved sales records from the database and updates the display
    private void loadSales() {
        List<Sale> sales = dbHelper.getAllSales();
        tvTotalSalesCount.setText(String.valueOf(sales.size()));
        rvSales.setAdapter(new SalesAdapter(sales));
    }

    // Inner class Adapter to manage the display of individual sales items
    private static class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.ViewHolder> {
        private List<Sale> sales;

        public SalesAdapter(List<Sale> sales) {
            this.sales = sales;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_payment, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Sale s = sales.get(position);
            holder.tvCustomerName.setText(s.getCustomerName());
            holder.tvCarModel.setText(s.getVehicleModel());
            holder.tvPrice.setText(String.format(Locale.getDefault(), "Rs %,.2f", s.getPrice()));
        }

        @Override
        public int getItemCount() {
            return sales.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvCustomerName, tvCarModel, tvPrice;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvCustomerName = itemView.findViewById(R.id.tvAdminCustomerName);
                tvCarModel = itemView.findViewById(R.id.tvAdminPaymentDate);
                tvPrice = itemView.findViewById(R.id.tvAdminPaymentAmount);
            }
        }
    }
}
