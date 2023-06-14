package com.example.halt.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.halt.R;
import com.example.halt.fragments.AttendantsFragment;
import com.example.halt.fragments.CreateMeetPointFragment;
import com.example.halt.fragments.DeleteMeetPointFragment;
import com.example.halt.interfaces.CreateMeetPointActivityFragmentCommunication;

public class CreateMeetPointActivity extends AppCompatActivity implements CreateMeetPointActivityFragmentCommunication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_meet_point_activity);
        openCreateMeetPointFragment();
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
        Intent myIntent = new Intent(this, FriendsActivity.class);
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

