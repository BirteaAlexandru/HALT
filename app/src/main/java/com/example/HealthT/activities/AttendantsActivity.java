package com.example.HealthT.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.HealthT.R;
import com.example.HealthT.fragments.DeleteMeetPointFragment;
import com.example.HealthT.fragments.AttendantsFragment;
import com.example.HealthT.interfaces.FriendsActivityFragmentCommunication;

public class AttendantsActivity extends AppCompatActivity implements FriendsActivityFragmentCommunication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_activity);
        openFriendsFragment();
    }

    @Override
    public void openFriendsFragment() {
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
    public void openDashboardActivity() {
        Intent myIntent = new Intent(this, DashboardActivity.class);
        this.startActivity(myIntent);
    }

    @Override
    public void openProfileActivity() {
        Intent myIntent = new Intent(this, ProfileActivity.class);
        this.startActivity(myIntent);
    }

    @Override
    public void openCreateMeetPointActivity() {
        Intent myIntent = new Intent(this, CreateMeetPointActivity.class);
        this.startActivity(myIntent);
    }

    @Override
    public void openAddFriendFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        String tag = DeleteMeetPointFragment.class.getName();
        DeleteMeetPointFragment dashboardFragment = new DeleteMeetPointFragment();
        FragmentTransaction addTransaction = transaction.add(
                R.id.attendants_frame_layout, dashboardFragment, tag
        );
        addTransaction.commit();
    }
}
