package com.example.HealthT.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.HealthT.R;
import com.example.HealthT.fragments.DashboardFragment;
import com.example.HealthT.interfaces.DashboardActivityFragmentCommunication;

public class DashboardActivity extends AppCompatActivity implements DashboardActivityFragmentCommunication {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);
        openDashboardFragment();
    }

    @Override
    public void openDashboardFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        String tag = DashboardFragment.class.getName();
        DashboardFragment dashboardFragment = new DashboardFragment();
        FragmentTransaction addTransaction = transaction.add(
                R.id.dashboard_frame_layout, dashboardFragment, tag
        );
        addTransaction.commit();
    }

    @Override
    public void openFriendsActivity() {
        Intent myIntent = new Intent(this, AttendantsActivity.class);
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
}
