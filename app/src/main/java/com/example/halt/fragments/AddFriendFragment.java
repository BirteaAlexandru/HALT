package com.example.halt.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.halt.R;
import com.example.halt.constants.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddFriendFragment extends Fragment {
    EditText emailEt;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_friend_fragment, container, false);

        emailEt = view.findViewById(R.id.add_friend_tv);
        progressDialog = new ProgressDialog(this.getActivity());

        Button addFriendButton = view.findViewById(R.id.invite_friend_button);

        addFriendButton.setOnClickListener(v -> addFriend());

        return view;
    }

    public void addFriend(){
        String email = emailEt.getText().toString();
        emailEt.setText("");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();

        if (TextUtils.isEmpty(email)) {
            emailEt.setError(Constants.EMAIL_ERROR_MESSAGE);
            return;
        }

        progressDialog.setMessage(Constants.PROGRESS_DIALOG_MESSAGE);
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        findUser(email, new firebaseCallback() {
            @Override
            public void onCallback(String userId) {
                databaseReference.child("Users").child(firebaseAuth.getUid()).child("Friends").child(userId).setValue("true");
                Toast.makeText(getActivity(), Constants.FRIEND_ADDED_SUCCESSFULLY, Toast.LENGTH_SHORT).show();
            }
        });

        progressDialog.dismiss();

    }



    private void findUser(String email, firebaseCallback callback ){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference().child("Users");

        databaseReference.orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String str = snapshot.getValue().toString();
                str= str.substring(str.indexOf('{')+1, str.indexOf('='));
                callback.onCallback(str);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), Constants.FRIEND_NOT_FOUND, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private interface firebaseCallback{
        void onCallback(String userId);
    }

}


