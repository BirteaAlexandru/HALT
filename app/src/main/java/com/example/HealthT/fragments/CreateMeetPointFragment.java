package com.example.HealthT.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.HealthT.Models.MeetPoint;
import com.example.HealthT.R;
import com.example.HealthT.interfaces.CreateMeetPointActivityFragmentCommunication;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import static android.app.Activity.RESULT_OK;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class CreateMeetPointFragment extends Fragment{
    private CreateMeetPointActivityFragmentCommunication createMeetPointActivityFragmentCommunication;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase database ;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private ImageView profileImg;
    Spinner dropdown;
    public Uri imageUri;
    Button btnDatePicker, btnTimePicker, btnCreateMeetPoint;
    EditText txtDate, txtTime, numberOfPeople;
    private int mYear, mMonth, mDay, mHour, mMinute;
    boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    Location currentLocation ;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CreateMeetPointActivityFragmentCommunication) {
            createMeetPointActivityFragmentCommunication = (CreateMeetPointActivityFragmentCommunication) context;
        }
    }
    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_meet_point_fragment, container, false);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        firebaseStorage= FirebaseStorage.getInstance();
        storageReference= firebaseStorage.getReference();

        dropdown = view.findViewById(R.id.create_meet_point_spinner);
        initspinnerfooter();

        btnDatePicker=view.findViewById(R.id.btn_date);
        btnTimePicker=view.findViewById(R.id.btn_time);
        txtDate=view.findViewById(R.id.in_date);
        txtTime=view.findViewById(R.id.in_time);
        numberOfPeople= view.findViewById(R.id.create_meet_point_edit_text);
        btnCreateMeetPoint= view.findViewById(R.id.create_meet_point_button);

        BottomNavigationView bottomNavigationView= view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        profileImg= view.findViewById(R.id.meet_point_image);

        checkPermission();
        getDeviceLocation();
        downloadImage();

        profileImg.setOnClickListener(v -> chooseImage());
        btnDatePicker.setOnClickListener(v -> setDateToEditText());
        btnTimePicker.setOnClickListener((v -> setTimeToEditText()));
        btnCreateMeetPoint.setOnClickListener(v -> createMeetPoint());
        return view;
    }

    private void createMeetPoint(){

                System.out.println(currentLocation);

        MeetPoint meetPoint= new MeetPoint(dropdown.getSelectedItem().toString(), currentLocation,Integer.parseInt(numberOfPeople.getText().toString()), mYear, mMonth, mDay, mHour, mMinute);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                databaseReference.child("MeetPoints").child(Objects.requireNonNull(firebaseAuth.getUid())).setValue(meetPoint);
                Toast.makeText(getActivity(), "Meet point created", Toast.LENGTH_SHORT).show();

                createMeetPointActivityFragmentCommunication.openFriendsActivity();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Meet point failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDeviceLocation(){
        mFusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this.getContext());
        try {
            if(mLocationPermissionGranted){
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(getContext(), "location found", Toast.LENGTH_SHORT).show();
                            currentLocation = (Location) task.getResult();
                            System.out.println(currentLocation);

                        }
                        else  Toast.makeText(getContext(), "location not found", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }catch (SecurityException e){

        }
    }

    private void setDateToEditText(){

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        int Year = c.get(Calendar.YEAR);
        int Month = c.get(Calendar.MONTH);
        int Day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this.getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        mDay= dayOfMonth;
                        mMonth= monthOfYear;
                        mYear= year;
                    }
                }, Year, Month, Day);
        datePickerDialog.show();
    }

    private void setTimeToEditText(){

        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int Hour = c.get(Calendar.HOUR_OF_DAY);
        int Minute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this.getContext(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        txtTime.setText(hourOfDay + ":" + minute);
                        mHour= hourOfDay;
                        mMinute= minute;
                    }
                }, Hour, Minute, false);
        timePickerDialog.show();
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
        System.out.println("upload image 1");

        StorageReference ricersRef= storageReference.child("images/meetPoints/"+ firebaseAuth.getUid());

        ricersRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                Toast.makeText(getActivity(), "image uploaded", Toast.LENGTH_SHORT).show();

                System.out.println("upload image 2");

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
        StorageReference ricersRef= storageReference.child("images/meetPoints/"+ firebaseAuth.getUid());
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

    private void initspinnerfooter() {
        ArrayList<String> activitiesList= new ArrayList<String>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, activitiesList);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        dropdown.setAdapter(adapter);

        databaseReference.child("Activities").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    activitiesList.add(snapshot.getValue(String.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()){
                        case R.id.Home:
                            createMeetPointActivityFragmentCommunication.openDashboardActivity();
                            break;
                        case R.id.Profile:
                            createMeetPointActivityFragmentCommunication.openProfileActivity();
                            break;
                    }
                    return true;
                }
            };
    private void checkPermission() {
        Dexter.withContext(this.getContext()).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                mLocationPermissionGranted = true;
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Toast.makeText(getContext(), "location denied", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }
}
