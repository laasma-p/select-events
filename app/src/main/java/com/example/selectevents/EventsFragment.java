package com.example.selectevents;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventsFragment extends Fragment {
    FloatingActionButton floatingActionButton;

    RecyclerView rv;
    List<Event> eventList;
    DatabaseReference reference;

    ValueEventListener valueEventListener;

    // Getting specific user
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public EventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        // Find FAB in the view
        floatingActionButton = view.findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(v -> AddEvent.newInstance().show(getParentFragmentManager(), AddEvent.TAG));

        // Finding Recycler View
        rv = view.findViewById(R.id.rv);

        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        rv.hasFixedSize();

        eventList = new ArrayList<>();

        EventAdapter eventAdapter = new EventAdapter(getActivity(), eventList);

        rv.setAdapter(eventAdapter);

        reference = FirebaseDatabase.getInstance().getReference("events").child(user.getUid());

        valueEventListener = reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    Event event = itemSnapshot.getValue(Event.class);

                    assert event != null;
                    event.setEventId(itemSnapshot.getKey());
                    eventList.add(event);

                    eventAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Do nothing
            }
        });

        return view;
    }

    // Preventing back button to not log out of the app
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        OnBackPressedCallback callback = new OnBackPressedCallback(
                true // default to enabled
        ) {
            @Override
            public void handleOnBackPressed() {
                // Do nothing
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(
                this, // LifecycleOwner
                callback);
    }
}