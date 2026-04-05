package com.example.jsmotorscarfinance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jsmotorscarfinance.adapters.VehicleAdapter;
import com.example.jsmotorscarfinance.database.DatabaseHelper;
import com.example.jsmotorscarfinance.models.Vehicle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class ManageVehiclesActivity extends AppCompatActivity {

    private RecyclerView rvVehicles;
    private FloatingActionButton fabAdd;
    private DatabaseHelper dbHelper;
    private VehicleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_vehicles);

        dbHelper = new DatabaseHelper(this);
        rvVehicles = findViewById(R.id.rvManageVehicles);
        fabAdd = findViewById(R.id.fabAddVehicle);

        rvVehicles.setLayoutManager(new LinearLayoutManager(this));
        loadVehicles();

        fabAdd.setOnClickListener(v -> showVehicleDialog(null));
    }

    private void loadVehicles() {
        List<Vehicle> list = dbHelper.getAllVehicles();
        adapter = new VehicleAdapter(list, "Edit", this::showVehicleDialog);
        rvVehicles.setAdapter(adapter);
    }

    private void showVehicleDialog(Vehicle vehicle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_vehicle, null);
        EditText etName = view.findViewById(R.id.etVehicleName);
        EditText etSpecs = view.findViewById(R.id.etVehicleSpecs);
        EditText etPrice = view.findViewById(R.id.etVehiclePrice);

        if (vehicle != null) {
            etName.setText(vehicle.getName());
            etSpecs.setText(vehicle.getSpecs());
            etPrice.setText(String.valueOf(vehicle.getPrice()));
            builder.setTitle("Edit Vehicle");
            builder.setNeutralButton("Delete", (d, w) -> {
                dbHelper.deleteVehicle(vehicle.getId());
                loadVehicles();
            });
        } else {
            builder.setTitle("Add Vehicle");
        }

        builder.setView(view)
                .setPositiveButton("Save", (d, w) -> {
                    String name = etName.getText().toString();
                    String specs = etSpecs.getText().toString();
                    String priceStr = etPrice.getText().toString();
                    if (name.isEmpty() || priceStr.isEmpty()) return;

                    Vehicle v = vehicle != null ? vehicle : new Vehicle();
                    v.setName(name);
                    v.setSpecs(specs);
                    v.setPrice(Double.parseDouble(priceStr));
                    v.setAvailability(1);

                    if (vehicle == null) dbHelper.addVehicle(v);
                    else dbHelper.updateVehicle(v);
                    
                    loadVehicles();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
