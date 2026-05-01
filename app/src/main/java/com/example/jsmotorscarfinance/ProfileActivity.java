package com.example.jsmotorscarfinance;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.jsmotorscarfinance.database.DatabaseHelper;
import com.example.jsmotorscarfinance.models.User;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class ProfileActivity extends AppCompatActivity {

    private TextInputEditText etName, etEmail, etPassword;
    private MaterialButton btnSave;
    private DatabaseHelper dbHelper;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dbHelper = new DatabaseHelper(this);
        userId = getIntent().getIntExtra("USER_ID", -1);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        etName = findViewById(R.id.etProfileName);
        etEmail = findViewById(R.id.etProfileEmail);
        etPassword = findViewById(R.id.etProfilePassword);
        btnSave = findViewById(R.id.btnSaveProfile);

        loadUserData();

        btnSave.setOnClickListener(v -> saveProfile());
    }

    private void loadUserData() {
        User user = dbHelper.getUserById(userId);
        if (user != null) {
            etName.setText(user.getName());
            etEmail.setText(user.getEmail());
            etPassword.setText(user.getPassword());
        } else {
            Toast.makeText(this, "Error loading profile", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveProfile() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int result = dbHelper.updateUser(userId, name, email, password);
        if (result > 0) {
            Toast.makeText(this, "Profile and login details updated!", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Update failed. Email might already be in use.", Toast.LENGTH_SHORT).show();
        }
    }
}
