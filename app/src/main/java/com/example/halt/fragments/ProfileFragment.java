package com.example.halt.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.halt.Models.MeetPoint;
import com.example.halt.Models.User;
import com.example.halt.R;
import com.example.halt.adapters.MeetPointAdapter;
import com.example.halt.adapters.MyAdapter;
import com.example.halt.constants.Constants;
import com.example.halt.interfaces.ProfileActivityFragmentCommunication;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    private ProfileActivityFragmentCommunication profileActivityFragmentCommunication;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase database ;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private EditText changeNameET;
    private ImageView profileImg;
    public Uri imageUri;
    public ArrayList<MeetPoint> meetPoints;
    MeetPointAdapter myAdapter;

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
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        firebaseStorage= FirebaseStorage.getInstance();
        storageReference= firebaseStorage.getReference();
        meetPoints= new ArrayList<MeetPoint>();
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_meet_points);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        myAdapter = new MeetPointAdapter(meetPoints);
        recyclerView.setAdapter(myAdapter);

        changeNameET= view.findViewById(R.id.change_name_et);
        Button changeNameBt= view.findViewById(R.id.change_name_button);
        BottomNavigationView bottomNavigationView= view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        profileImg= view.findViewById(R.id.profile_image);

        downloadImage();
        setRecycleViewData();

        changeNameBt.setOnClickListener(v -> changeName(changeNameET.getText().toString()));
        profileImg.setOnClickListener(v -> chooseImage());
        return view;
    }

    void setRecycleViewData(){
        databaseReference.child("MeetPoints").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                meetPoints.clear();
                for (DataSnapshot snapshotIndex:snapshot.getChildren())
                {
                    String str = snapshotIndex.getKey();//get friends user id

                    if(snapshotIndex.child("Participants").child(firebaseAuth.getUid()).getValue()!=null)
                    if(snapshotIndex.child("Participants").child(firebaseAuth.getUid()).getValue().toString().equals("true")){
                        System.out.println("meet point in");
                        meetPoints.add(new MeetPoint(str, snapshotIndex.child("activity").getValue().toString(),
                                snapshotIndex.child("date").child("hours").getValue().toString(), snapshotIndex.child("date").child("minutes").getValue().toString()));
                        myAdapter.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "User's friends error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()){
                        case R.id.Home:
                            profileActivityFragmentCommunication.openDashboardActivity();
                            break;
                        case R.id.Friends:
                            profileActivityFragmentCommunication.openFriendsActivity();
                            break;
                        case R.id.Create:
                            profileActivityFragmentCommunication.openCreateMeetPointActivity();
                            break;
                    }
                    return true;
                }
            };




    private void changeName(String newName){
        changeNameET.setText("");
        databaseReference.child("Users").child(firebaseAuth.getUid()).child("username").setValue(newName);
    }

    private void chooseImage(){
        Intent intent= new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri= data.getData();
            profileImg.setImageURI(imageUri);
            uploadImage();
        }
    }

    private void uploadImage() {
        final ProgressDialog pd= new ProgressDialog(this.getContext());
        pd.setTitle("uploading image...");
        pd.show();

        StorageReference ricersRef= storageReference.child("images/profile/"+ firebaseAuth.getUid());

        ricersRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                Toast.makeText(getActivity(), "image uploaded", Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(getActivity(), "failed to upload", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress= (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                pd.setMessage("percentage: " + (int) progress + "%");
            }
        });
    }

    private void downloadImage(){
        StorageReference ricersRef= storageReference.child("images/profile/"+ firebaseAuth.getUid());
        final long ONE_MEGABYTE = 1024 * 1024;
        ricersRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                profileImg.setImageBitmap(bmp);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }
}
