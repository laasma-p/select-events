package com.example.selectevents;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

        deleteEventButton.setOnClickListener(v -> deleteEvent(id));
    }

    public void deleteEvent(String eventId) {
        reference = FirebaseDatabase.getInstance().getReference("events").child(user.getUid()).child(id);

        AlertDialog.Builder dialog = new AlertDialog.Builder(EventDetailsActivity.this);
        dialog.setTitle("Are you sure?");
        dialog.setMessage("Deleting the event is irreversible.");
        dialog.setPositiveButton("Delete", (dialog1, which) -> {
            deleteEventButton.setVisibility(View.VISIBLE);
            assert user != null;

            reference.removeValue();
            Toast.makeText(EventDetailsActivity.this,"Event Deleted", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), HomeScreenActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            finish();
        });

        dialog.setNegativeButton("Dismiss", (dialog12, which) -> dialog12.dismiss());

        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

}