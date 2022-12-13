package com.example.selectevents;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class EventDetailsActivity extends AppCompatActivity {

    TextView detailsTitle, detailsDescription, detailsTime, detailsDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        detailsTitle = findViewById(R.id.detailsTitle);
        detailsDescription = findViewById(R.id.detailsDescription);
        detailsTime = findViewById(R.id.detailsTime);
        detailsDate = findViewById(R.id.detailsDate);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            detailsTitle.setText(bundle.getString("Title"));
            detailsDescription.setText(bundle.getString("Description"));
            detailsTime.setText(bundle.getString("Time"));
            detailsDate.setText(bundle.getString("Date"));

        }

    }

}