package com.example.jsmotorscarfinance;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.jsmotorscarfinance.database.DatabaseHelper;
import com.example.jsmotorscarfinance.models.User;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            User user = dbHelper.loginUser(email, password);
            if (user != null) {
                Toast.makeText(this, "Welcome " + user.getRole(), Toast.LENGTH_SHORT).show();
                Intent intent;
                switch (user.getRole()) {
                    case "Admin":
                        intent = new Intent(this, AdminDashboardActivity.class);
                        break;
                    case "Finance Agent":
                        intent = new Intent(this, FinanceAgentDashboardActivity.class);
                        break;
                    default:
                        intent = new Intent(this, CustomerDashboardActivity.class);
                        break;
                }
                intent.putExtra("USER_ID", user.getId());
                intent.putExtra("USER_NAME", user.getName());
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });

        tvRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }
}
