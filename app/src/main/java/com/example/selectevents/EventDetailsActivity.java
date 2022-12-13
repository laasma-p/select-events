package com.example.selectevents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EventDetailsActivity extends AppCompatActivity {

    TextView detailsTitle, detailsDescription, detailsTime, detailsDate;
    Button deleteEventButton;
    String id = "";

    DatabaseReference reference;

    // Getting specific user
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        detailsTitle = findViewById(R.id.detailsTitle);
        detailsDescription = findViewById(R.id.detailsDescription);
        detailsTime = findViewById(R.id.detailsTime);
        detailsDate = findViewById(R.id.detailsDate);

        deleteEventButton = findViewById(R.id.deleteEventButton);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            detailsTitle.setText(bundle.getString("Title"));
            detailsDescription.setText(bundle.getString("Description"));
            detailsTime.setText(bundle.getString("Time"));
            detailsDate.setText(bundle.getString("Date"));
            id = bundle.getString("EventId");
        }

        deleteEventButton.setOnClickListener(v -> {
            reference = FirebaseDatabase.getInstance().getReference("events").child(user.getUid()).child(id);
            reference.removeValue();
            Toast.makeText(EventDetailsActivity.this,"Event Deleted", Toast.LENGTH_LONG).show();
            finish();
        });

    }

}