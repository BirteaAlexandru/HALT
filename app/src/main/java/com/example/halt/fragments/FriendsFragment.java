package com.example.halt.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.halt.R;
import com.example.halt.interfaces.FriendsActivityFragmentCommunication;

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
        View view = inflater.inflate(R.layout.friends_fragment, container, false);
        Button dashboardButton= view.findViewById(R.id.home_button);
        Button profileButton= view.findViewById(R.id.profile_button);


        dashboardButton.setOnClickListener(v -> friendsActivityFragmentCommunication.openDashboardActivity());
        profileButton.setOnClickListener(v -> friendsActivityFragmentCommunication.openProfileActivity());

        return view;
    }
}
