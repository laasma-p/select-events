package com.example.selectevents;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private EditText firstName, lastName, email, password, phoneNumber;
    private ProgressBar registerProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button registerButton;

        mAuth = FirebaseAuth.getInstance();

        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phoneNumber = findViewById(R.id.phoneNumber);

        registerProgressBar = findViewById(R.id.registerProgressBar);
    }

    @Override
    public void onClick(View v) {
        registerUser();
    }

    private void registerUser() {
        String firstNameString = firstName.getText().toString().trim();
        String lastNameString = lastName.getText().toString().trim();
        String emailString = email.getText().toString().trim();
        String passwordString = password.getText().toString().trim();
        String phoneNumberString = phoneNumber.getText().toString().trim();

        if (firstNameString.isEmpty()) {
            firstName.setError("First name is required");
            firstName.requestFocus();
            return;
        }

        if (lastNameString.isEmpty()) {
            lastName.setError("Last name is required");
            lastName.requestFocus();
            return;
        }

        if (emailString.isEmpty()) {
            email.setError("Email is required");
            email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {
            email.setError("Please provide a valid email");
            email.requestFocus();
            return;
        }

        if (phoneNumberString.isEmpty()) {
            phoneNumber.setError("Phone number is required");
            phoneNumber.requestFocus();
            return;
        }

        if (passwordString.isEmpty()) {
            password.setError("Password is required");
            password.requestFocus();
            return;
        }

        if (passwordString.length() < 6) {
            password.setError("The password must be at least 6 characters");
            password.requestFocus();
            return;
        }

        registerProgressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                User user = new User(firstNameString, lastNameString, emailString, phoneNumberString);

                FirebaseDatabase.getInstance().getReference("users")
                        .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                        .setValue(user)
                        .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "User has been successfully registered!", Toast.LENGTH_LONG).show();
                                registerProgressBar.setVisibility(View.GONE);

                                // Redirect to log in
                            } else {
                                Toast.makeText(RegisterActivity.this, "Failed to register - try again", Toast.LENGTH_LONG).show();
                                registerProgressBar.setVisibility(View.GONE);
                            }
                        });

            } else {
                Toast.makeText(RegisterActivity.this, "Failed to register", Toast.LENGTH_LONG).show();
                registerProgressBar.setVisibility(View.GONE);
            }

        });
    }
}