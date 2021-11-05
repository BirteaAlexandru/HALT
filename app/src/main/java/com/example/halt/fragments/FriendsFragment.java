package com.example.halt.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.halt.R;
import com.example.halt.interfaces.FriendsActivityFragmentCommunication;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FriendsFragment extends Fragment {
    private FriendsActivityFragmentCommunication friendsActivityFragmentCommunication;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FriendsActivityFragmentCommunication) {
            friendsActivityFragmentCommunication = (FriendsActivityFragmentCommunication) context;
        }
    }
    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.friends_fragment, container, false);
        BottomNavigationView bottomNavigationView= view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

    return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()){
                        case R.id.Home:
                            friendsActivityFragmentCommunication.openDashboardActivity();
                            break;
                        case R.id.Friends:
                            friendsActivityFragmentCommunication.openFriendsFragment();
                            break;
                        case R.id.Profile:
                            friendsActivityFragmentCommunication.openProfileActivity();
                            break;
                    }
                    return true;
                }
            };

}
