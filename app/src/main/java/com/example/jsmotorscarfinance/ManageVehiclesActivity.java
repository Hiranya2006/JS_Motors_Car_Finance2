package com.example.jsmotorscarfinance;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.jsmotorscarfinance.adapters.VehicleAdapter;
import com.example.jsmotorscarfinance.database.DatabaseHelper;
import com.example.jsmotorscarfinance.models.Vehicle;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

public class ManageVehiclesActivity extends AppCompatActivity {

    private RecyclerView rvVehicles;
    private ExtendedFloatingActionButton fabAdd;
    private DatabaseHelper dbHelper;
    private VehicleAdapter adapter;
    private String currentBase64Image = "";
    private ImageView ivDialogPreview;

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    processAndSetImage(imageUri);
                }
            }
    );

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

    private void processAndSetImage(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            
            // Resize image to keep DB small
            Bitmap resized = Bitmap.createScaledBitmap(bitmap, 800, 600, true);
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
            byte[] bytes = outputStream.toByteArray();
            currentBase64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
            
            if (ivDialogPreview != null) {
                ivDialogPreview.setImageBitmap(resized);
                ivDialogPreview.setColorFilter(0); // Clear tint
            }
        } catch (Exception e) {
            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
        }
    }

    private void showVehicleDialog(Vehicle vehicle) {
        currentBase64Image = (vehicle != null) ? vehicle.getImageUrl() : "";
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_vehicle, null);
        
        ivDialogPreview = view.findViewById(R.id.ivDialogVehicle);
        View cardSelect = view.findViewById(R.id.cardSelectImage);
        EditText etName = view.findViewById(R.id.etVehicleName);
        EditText etSpecs = view.findViewById(R.id.etVehicleSpecs);
        EditText etPrice = view.findViewById(R.id.etVehiclePrice);

        if (vehicle != null) {
            etName.setText(vehicle.getName());
            etSpecs.setText(vehicle.getSpecs());
            etPrice.setText(String.valueOf(vehicle.getPrice()));
            
            if (currentBase64Image != null && !currentBase64Image.isEmpty() && currentBase64Image.length() > 100) {
                byte[] decoded = Base64.decode(currentBase64Image, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
                ivDialogPreview.setImageBitmap(bitmap);
                ivDialogPreview.setColorFilter(0);
            }

            builder.setTitle("Update Car");
            builder.setNeutralButton("Delete", (d, w) -> {
                dbHelper.deleteVehicle(vehicle.getId());
                loadVehicles();
            });
        }

        cardSelect.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

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
                    v.setImageUrl(currentBase64Image);
                    v.setAvailability(1);

                    if (vehicle == null) dbHelper.addVehicle(v);
                    else dbHelper.updateVehicle(v);
                    
                    loadVehicles();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
