package com.example.selectevents;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class AddEvent extends BottomSheetDialogFragment {

    EditText rvEventTitle, rvEventDescription, rvEventTime, rvEventDate;
    Button addEventButton;
    public static final String TAG = "AddEvent";

    // Getting specific user
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public static AddEvent newInstance() {
        return new AddEvent();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_event_dialog, container, false);

        rvEventTitle = view.findViewById(R.id.rvEventTitle);
        rvEventDescription = view.findViewById(R.id.rvEventDescription);
        rvEventTime = view.findViewById(R.id.rvEventTime);
        rvEventDate = view.findViewById(R.id.rvEventDate);

        addEventButton = view.findViewById(R.id.addEventButton);

        addEventButton.setOnClickListener(v -> storeEventData());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void storeEventData() {
        String title = rvEventTitle.getText().toString().trim();
        String description = rvEventDescription.getText().toString().trim();
        String time = rvEventTime.getText().toString().trim();
        String date = rvEventDate.getText().toString().trim();

        Event event = new Event(title, description, time, date);

        // Generate random unique ID
        String id = UUID.randomUUID().toString();

        FirebaseDatabase.getInstance().getReference("events").child(user.getUid())
                .child(id).setValue(event).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Event is added.", Toast.LENGTH_LONG).show();
                        dismiss();
                    }
                }).addOnFailureListener(e -> Toast.makeText(getActivity(), "Event not added.", Toast.LENGTH_LONG).show());

    }
}