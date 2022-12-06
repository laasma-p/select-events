package com.example.selectevents;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class ProfileFragment extends Fragment {
    private static final String TAG = ".ProfileFragment";

    private DatabaseReference reference;

    private String userID;

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

        setupFirebaseListener();

        logoutButton.setOnClickListener(v -> {
            Log.d(TAG, "Attempting to log out the user.");
            FirebaseAuth.getInstance().signOut();
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

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