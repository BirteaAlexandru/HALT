package com.example.halt.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.halt.R;
import com.example.halt.interfaces.ProfileActivityFragmentCommunication;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {
    private ProfileActivityFragmentCommunication profileActivityFragmentCommunication;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ProfileActivityFragmentCommunication) {
            profileActivityFragmentCommunication = (ProfileActivityFragmentCommunication) context;
        }
    }
    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
       CheckBox checkBox= view.findViewById(R.id.smoking_availability);
        BottomNavigationView bottomNavigationView= view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        getAvailabilityFromDataBase(checkBox);

        checkBox.setOnClickListener(v -> writeAvailabilityToDataBase());
        return view;
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()){
                        case R.id.Home:
                            profileActivityFragmentCommunication.openDashboardActivity();
                            break;
                        case R.id.Friends:
                            profileActivityFragmentCommunication.openFriendsActivity();
                            break;
                        case R.id.Profile:
                            profileActivityFragmentCommunication.openProfileFragment();
                            break;
                    }
                    return true;
                }
            };
    private void writeAvailabilityToDataBase(){
        databaseReference.child("Users").child(firebaseAuth.getUid()).child("smoking_available").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue().toString().equals("true"))
                    databaseReference.child("Users").child(firebaseAuth.getUid()).child("smoking_available").setValue(false);
                else
                    databaseReference.child("Users").child(firebaseAuth.getUid()).child("smoking_available").setValue(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getAvailabilityFromDataBase(CheckBox checkBox){
        databaseReference.child("Users").child(firebaseAuth.getUid()).child("smoking_available").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue().toString().equals("true") ) {
                    checkBox.setChecked(true);
                }
                else {
                    checkBox.setSelected(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
