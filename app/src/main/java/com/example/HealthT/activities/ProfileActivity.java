package com.example.HealthT.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.HealthT.R;
import com.example.HealthT.fragments.ProfileFragment;
import com.example.HealthT.interfaces.ProfileActivityFragmentCommunication;

public class ProfileActivity extends AppCompatActivity implements ProfileActivityFragmentCommunication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        openProfileFragment();
    }


    @Override
    public void openProfileFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        String tag = ProfileFragment.class.getName();
        ProfileFragment profileFragment = new ProfileFragment();
        FragmentTransaction addTransaction = transaction.add(
                R.id.profile_frame_layout, profileFragment, tag
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
    public void openCreateMeetPointActivity() {
        Intent myIntent = new Intent(this, CreateMeetPointActivity.class);
        this.startActivity(myIntent);
    }
}
