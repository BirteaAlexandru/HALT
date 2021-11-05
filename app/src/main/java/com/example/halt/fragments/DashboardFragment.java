package com.example.halt.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.halt.R;
import com.example.halt.activities.AuthenticationActivity;
import com.example.halt.constants.Constants;
import com.example.halt.interfaces.DashboardActivityFragmentCommunication;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardFragment extends Fragment {
    private DashboardActivityFragmentCommunication dashboardActivityFragmentCommunication;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof DashboardActivityFragmentCommunication) {
            dashboardActivityFragmentCommunication = (DashboardActivityFragmentCommunication) context;
        }
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboard_fragment, container, false);
        BottomNavigationView bottomNavigationView= view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);


        /*
        CardView logoutCard = view.findViewById(R.id.logout_card);
        CardView topicsCard = view.findViewById(R.id.topics_card);
        CardView manageCard = view.findViewById(R.id.manage_card);
        TextView userEmailTv = view.findViewById(R.id.user_email_tv);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(Constants.SHARED_PREFERENCES_USER_EMAIL, Context.MODE_PRIVATE);
        userEmailTv.setText(Constants.DASHBOARD_HELLO_MESSAGE + sharedPreferences.getString("email", null));
        logoutCard.setOnClickListener(v -> openAuthenticationActivity());
        topicsCard.setOnClickListener(v -> dashboardActivityFragmentCommunication.openTopicsActivity());
        manageCard.setOnClickListener(v -> dashboardActivityFragmentCommunication.openManagementActivity());

      */
        return view;
    }

    public void openAuthenticationActivity() {
        Intent myIntent = new Intent(getActivity(), AuthenticationActivity.class);
        this.startActivity(myIntent);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()){
                        case R.id.Home:
                            dashboardActivityFragmentCommunication.openDashboardFragment();
                            break;
                        case R.id.Friends:
                            dashboardActivityFragmentCommunication.openFriendsActivity();
                            break;
                        case R.id.Profile:
                            dashboardActivityFragmentCommunication.openProfileActivity();
                            break;
                    }
                    return true;
                }
            };
}
