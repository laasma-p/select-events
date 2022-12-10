package com.example.selectevents;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ProfileFragment extends Fragment {
    private static final String TAG = ".ProfileFragment";

    private FirebaseAuth.AuthStateListener mAuthStateListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Button logoutButton = view.findViewById(R.id.logoutButton);
        Button deleteProfileButton = view.findViewById(R.id.deleteProfileButton);
        ProgressBar deleteProgressBar = view.findViewById(R.id.deleteProgressBar);

        // For displaying the data from Firebase
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        setupFirebaseListener();

        logoutButton.setOnClickListener(v -> {
            Log.d(TAG, "Attempting to log out the user.");
            FirebaseAuth.getInstance().signOut();
        });

        // Deleting a user from database
        deleteProfileButton.setOnClickListener(v -> {
            Log.d(TAG, "Deleting user...");
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setTitle("Are you sure?");
            dialog.setMessage("Deleting this account will result in completely removing all data.");
            dialog.setPositiveButton("Delete", (dialog1, which) -> {
                deleteProfileButton.setVisibility(View.VISIBLE);
                assert user != null;
                user.delete().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Account successfully deleted!",
                                Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        deleteProgressBar.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(getActivity(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                        deleteProgressBar.setVisibility(View.GONE);
                    }

                });

            });

            dialog.setNegativeButton("Dismiss", (dialog12, which) -> dialog12.dismiss());

            AlertDialog alertDialog = dialog.create();
            alertDialog.show();

        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        assert user != null;
        String userID = user.getUid();

        final TextView firstNameTextView = view.findViewById(R.id.firstName);
        final TextView lastNameTextView = view.findViewById(R.id.lastName);
        final TextView emailTextView = view.findViewById(R.id.email);
        final TextView phoneNumberTextView = view.findViewById(R.id.phoneNumber);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    String firstName = userProfile.firstName;
                    String lastName = userProfile.lastName;
                    String email = userProfile.email;
                    String phoneNumber = userProfile.phoneNumber;

                    firstNameTextView.setText(firstName);
                    lastNameTextView.setText(lastName);
                    emailTextView.setText(email);
                    phoneNumberTextView.setText(phoneNumber);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Cannot display data", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void setupFirebaseListener() {
        Log.d(TAG, "setupFirebaseListener: setting up the auth state listened.");
        mAuthStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if ( user != null) {
                Log.d(TAG, "onAuthStateChanged: signed_in: " + user.getUid());
            } else {
                Log.d(TAG, "onAuthStateChanged: signed out");
                Toast.makeText(getActivity(), "Signing out...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthStateListener);
        }
    }

}