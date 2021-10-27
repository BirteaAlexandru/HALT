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
import com.example.halt.interfaces.ProfileActivityFragmentCommunication;

public class ProfileFragment extends Fragment {
    private ProfileActivityFragmentCommunication profileActivityFragmentCommunication;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ProfileActivityFragmentCommunication) {
            profileActivityFragmentCommunication = (ProfileActivityFragmentCommunication) context;
        }
    }
    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        Button friendsButton= view.findViewById(R.id.friends_button);
        Button dashboardButton= view.findViewById(R.id.home_button);


        friendsButton.setOnClickListener(v -> profileActivityFragmentCommunication.openFriendsActivity());
        dashboardButton.setOnClickListener(v -> profileActivityFragmentCommunication.openDashboardActivity());

        return view;
    }
}
