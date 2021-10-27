package com.example.halt.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.halt.R;
import com.example.halt.fragments.FriendsFragment;
import com.example.halt.interfaces.FriendsActivityFragmentCommunication;

public class FriendsActivity extends AppCompatActivity implements FriendsActivityFragmentCommunication {
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
        String tag = FriendsFragment.class.getName();
        FriendsFragment dashboardFragment = new FriendsFragment();
        FragmentTransaction addTransaction = transaction.add(
                R.id.friend_frame_layout, dashboardFragment, tag
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
}
