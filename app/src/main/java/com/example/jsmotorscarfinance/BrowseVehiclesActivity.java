package com.example.jsmotorscarfinance;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jsmotorscarfinance.adapters.VehicleAdapter;
import com.example.jsmotorscarfinance.database.DatabaseHelper;
import com.example.jsmotorscarfinance.models.Vehicle;
import java.util.List;

public class BrowseVehiclesActivity extends AppCompatActivity {

    private RecyclerView rvVehicles;
    private DatabaseHelper dbHelper;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_vehicles);

        userId = getIntent().getIntExtra("USER_ID", -1);
        dbHelper = new DatabaseHelper(this);
        rvVehicles = findViewById(R.id.rvBrowseVehicles);
        rvVehicles.setLayoutManager(new LinearLayoutManager(this));

        loadVehicles();
    }

    private void loadVehicles() {
        List<Vehicle> list = dbHelper.getAllVehicles();
        VehicleAdapter adapter = new VehicleAdapter(list, "Apply for Loan", vehicle -> {
            Intent intent = new Intent(this, ApplyLoanActivity.class);
            intent.putExtra("USER_ID", userId);
            intent.putExtra("VEHICLE_NAME", vehicle.getName());
            intent.putExtra("VEHICLE_PRICE", vehicle.getPrice());
            startActivity(intent);
        });
        rvVehicles.setAdapter(adapter);
    }
}
