package com.example.halt.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.halt.R;
import com.example.halt.interfaces.CreateMeetPointActivityFragmentCommunication;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DeleteMeetPointFragment extends Fragment {

    CreateMeetPointActivityFragmentCommunication createMeetPointActivityFragmentCommunication;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference().child("MeetPoints");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.delete_meet_point_fragment, container, false);

        Button chancelBtn = view.findViewById(R.id.chancel_button);
        Button deleteBtn = view.findViewById(R.id.delete_meet_point_button);

        chancelBtn.setOnClickListener(v -> OnChancel());
        deleteBtn.setOnClickListener(v -> OnDeleteMeetPoint());

        return view;
    }

    void OnChancel(){
        createMeetPointActivityFragmentCommunication.openFriendsActivity();
    }

    void OnDeleteMeetPoint(){
        databaseReference.child(firebaseAuth.getUid()).removeValue();
        createMeetPointActivityFragmentCommunication.openCreateMeetPointFragment();
    }

}


