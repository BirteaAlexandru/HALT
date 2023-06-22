package com.example.HealthT.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.HealthT.R;
import com.example.HealthT.fragments.AttendantsFragment;
import com.example.HealthT.fragments.CreateMeetPointFragment;
import com.example.HealthT.fragments.DeleteMeetPointFragment;
import com.example.HealthT.interfaces.CreateMeetPointActivityFragmentCommunication;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateMeetPointActivity extends AppCompatActivity implements CreateMeetPointActivityFragmentCommunication {
    FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_meet_point_activity);

      databaseReference.child("MeetPoints").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(firebaseAuth.getUid())) {
                    openFriendsActivity();
                }
                else openCreateMeetPointFragment();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void openCreateMeetPointFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        String tag = CreateMeetPointFragment.class.getName();
        CreateMeetPointFragment createFragment = new CreateMeetPointFragment();
        FragmentTransaction addTransaction = transaction.add(
                R.id.create_meet_point_frame_layout, createFragment, tag
        );
        addTransaction.commit();
    }

    @Override
    public void openDeleteMeetPointFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        String tag = DeleteMeetPointFragment.class.getName();
        DeleteMeetPointFragment dashboardFragment = new DeleteMeetPointFragment();
        FragmentTransaction addTransaction = transaction.add(
                R.id.attendants_frame_layout, dashboardFragment, tag
        );
        addTransaction.commit();
    }

    @Override
    public void openAttendantsFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        String tag = AttendantsFragment.class.getName();
        AttendantsFragment dashboardFragment = new AttendantsFragment();
        FragmentTransaction addTransaction = transaction.add(
                R.id.attendants_frame_layout, dashboardFragment, tag
        );
        addTransaction.commit();
    }
    @Override
    public void openFriendsActivity() {
        Intent myIntent = new Intent(this, AttendantsActivity.class);
        this.startActivity(myIntent);
    }
    @Override
    public void openDashboardActivity() {
        Intent myIntent = new Intent(this, DashboardActivity.class);
        this.startActivity(myIntent);
    }
    @Override
    public void openProfileActivity() {
        Intent myIntent = new Intent(this, ProfileActivity.class);
        this.startActivity(myIntent);
    }
}

