package com.example.halt.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.halt.R;
import com.example.halt.fragments.DashboardFragment;
import com.example.halt.fragments.FriendsFragment;
import com.example.halt.fragments.ProfileFragment;
import com.example.halt.interfaces.DashboardActivityFragmentCommunication;

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
        Intent myIntent = new Intent(this, FriendsActivity.class);
        this.startActivity(myIntent);
    }

    @Override
    public void openProfileActivity() {
        Intent myIntent = new Intent(this, ProfileActivity.class);
        this.startActivity(myIntent);
    }
}
