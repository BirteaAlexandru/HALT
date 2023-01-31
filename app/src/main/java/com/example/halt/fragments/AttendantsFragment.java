package com.example.halt.fragments;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.halt.Models.User;
import com.example.halt.R;
import com.example.halt.adapters.MyAdapter;
import com.example.halt.interfaces.FriendsActivityFragmentCommunication;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class AttendantsFragment extends Fragment {
    private FriendsActivityFragmentCommunication friendsActivityFragmentCommunication;
    private ArrayList<User> userList;

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
        super.onCreate(savedInstanceState);
        userList = new ArrayList<User>();
        FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        FloatingActionButton floatingActionButton= view.findViewById(R.id.add_friend_button);

        setHasOptionsMenu(true);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_friends);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        MyAdapter myAdapter = new MyAdapter(userList);
        recyclerView.setAdapter(myAdapter);

        floatingActionButton.setOnClickListener(v -> friendsActivityFragmentCommunication.openAddFriendFragment());
        BottomNavigationView bottomNavigationView= view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        databaseReference.child("MeetPoints").child(firebaseAuth.getUid()).child("Participants").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                userList.clear();
                for (DataSnapshot snapshotIndex:snapshot.getChildren())
                {
                    String userId = snapshotIndex.getKey();//get friends user id
                    String accepted = snapshotIndex.getValue().toString();

                    databaseReference.child("Users").child(userId).addValueEventListener(new ValueEventListener() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String name = Objects.requireNonNull(snapshot.child("username").getValue()).toString();
                            String email = Objects.requireNonNull(snapshot.child("email").getValue()).toString();

                            if(accepted.equals("true")){
                                userList.add(0, new User(name, email, userId));
                            }else {
                                userList.add(new User(name, email, userId));
                            }
                            myAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getContext(), "accessing friends database error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "User's friends error", Toast.LENGTH_SHORT).show();
            }
        });

    return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @SuppressLint("NonConstantResourceId")
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()){
                        case R.id.Home:
                            friendsActivityFragmentCommunication.openDashboardActivity();
                            break;
                        case R.id.Create:
                            friendsActivityFragmentCommunication.openCreateMeetPointActivity();
                            break;
                        case R.id.Profile:
                            friendsActivityFragmentCommunication.openProfileActivity();
                            break;
                    }
                    return true;
                }
            };

}
