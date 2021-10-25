package com.example.halt.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.halt.R;
import com.example.halt.fragments.LoginFragment;
import com.example.halt.fragments.ResetPasswordFragment;
import com.example.halt.fragments.SignUpFragment;
import com.example.halt.interfaces.AuthenticationActivityFragmentCommunication;

public class AuthenticationActivity extends AppCompatActivity implements AuthenticationActivityFragmentCommunication {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentication_activity);
        openLoginFragment();
    }

    @Override
    public void openLoginFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        String tag = LoginFragment.class.getName();
        LoginFragment loginFragment = new LoginFragment();
        FragmentTransaction addTransaction = transaction.add(
                R.id.authentication_frame_layout, loginFragment, tag
        );
        addTransaction.commit();
    }

    @Override
    public void openSignUpFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        String tag = SignUpFragment.class.getName();
        SignUpFragment signUpFragment = new SignUpFragment();
        FragmentTransaction replaceTransaction = transaction.replace(
                R.id.authentication_frame_layout, signUpFragment, tag
        );
        replaceTransaction.commit();
    }


    @Override
    public void openChangePasswordFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        String tag = ResetPasswordFragment.class.getName();
        ResetPasswordFragment changePassword = new ResetPasswordFragment();
        FragmentTransaction replaceTransaction = transaction.replace(
                R.id.authentication_frame_layout, changePassword, tag
        );
        replaceTransaction.commit();
    }

    @Override
    public void openDashboardActivity() {
        Intent myIntent = new Intent(this, DashboardActivity.class);
        this.startActivity(myIntent);
    }


}