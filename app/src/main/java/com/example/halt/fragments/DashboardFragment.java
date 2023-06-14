package com.example.halt.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.halt.Models.ClusterMarker;
import com.example.halt.R;
import com.example.halt.interfaces.DashboardActivityFragmentCommunication;
import com.example.halt.interfaces.FirebaseCallback;
import com.example.halt.util.MyClusterManagerRenderer;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.Objects;

public class DashboardFragment extends Fragment implements OnMapReadyCallback  {
    private static final float DEFAULT_ZOOM = 15f;

    private DashboardActivityFragmentCommunication dashboardActivityFragmentCommunication;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference().child("MeetPoints");
    FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
    StorageReference storageReference= firebaseStorage.getReference();
    boolean mLocationPermissionGranted = false;
    MapView mMapView;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private ClusterManager mClusterManager;
    private MyClusterManagerRenderer mClusterManagerRenderer;
    private ArrayList<ClusterMarker> mClusterMarkers = new ArrayList<>();
    private Button joinBtn;
    private ClusterMarker lastItemSelected;

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
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        joinBtn= view.findViewById(R.id.join_meet_point_btn);
        mMapView = view.findViewById(R.id.mapView);

        checkPermission();
        joinBtn.setVisibility(View.GONE);
        joinBtn.setOnClickListener(v -> JoinBtnOnClick());

        if (mLocationPermissionGranted) {
            mMapView.getMapAsync(this);
            mMapView.onCreate(savedInstanceState);
        } else Toast.makeText(this.getContext(), "location permission denied", Toast.LENGTH_SHORT);

        return view;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.Create:
                            dashboardActivityFragmentCommunication.openCreateMeetPointActivity();
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


    private void addMapMarkers() {

        if (mMap != null)
        {
            if (mClusterManager == null) {
                mClusterManager = new ClusterManager<ClusterMarker>(getActivity().getApplicationContext(), mMap);
            }
            if (mClusterManagerRenderer == null) {
                mClusterManagerRenderer = new MyClusterManagerRenderer(
                        getActivity(),
                        mMap,
                        mClusterManager
                );
                mClusterManager.setRenderer(mClusterManagerRenderer);
            }

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot snapshotIndex : snapshot.getChildren()) {
                        double latitude= (double) Objects.requireNonNull(snapshotIndex.child("location").child("latitude").getValue());
                        double longitude= (double) Objects.requireNonNull(snapshotIndex.child("location").child("longitude").getValue());
                        String userId= Objects.requireNonNull(snapshotIndex.getKey());
                        String activity= Objects.requireNonNull(snapshotIndex.child("activity").getValue()).toString();
                        String day= Objects.requireNonNull(snapshotIndex.child("date").child("date").getValue()).toString();
                        String month=Objects.requireNonNull(snapshotIndex.child("date").child("month").getValue()).toString();
                        String year= Objects.requireNonNull(snapshotIndex.child("date").child("year").getValue()).toString();
                        String hour=Objects.requireNonNull(snapshotIndex.child("date").child("hours").getValue()).toString();
                        String minute= Objects.requireNonNull(snapshotIndex.child("date").child("minutes").getValue()).toString();

                        String snippet = hour + ':' + minute + "   " + "expected: " + Objects.requireNonNull(snapshotIndex.child("numberOfPersons").getValue()).toString();

                        DownloadImage(snapshotIndex.getKey(), new FirebaseCallback<Bitmap>() {
                            @Override
                            public void onCallback(Bitmap image) {

                                try {
                                    ClusterMarker newClusterMarker = new ClusterMarker(
                                            new LatLng(latitude, longitude),
                                            activity,
                                            snippet,
                                            userId,
                                            image
                                    );

                                    mClusterManager.addItem(newClusterMarker);
                                    mClusterMarkers.add(newClusterMarker);
                                    mClusterManager.cluster();
                                } catch (NullPointerException e) {

                                }
                            }
                        });
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<ClusterMarker>() {
                @Override
                public boolean onClusterItemClick(ClusterMarker item) {
                    joinBtn.setVisibility(View.VISIBLE);
                    lastItemSelected= item;
                    return false;
                }
            });

        }
    }

    private void JoinBtnOnClick(){
        databaseReference.child(lastItemSelected.getUserId()).child("Participants").child(firebaseAuth.getUid()).setValue("false");
        Toast.makeText(getContext(), "request send", Toast.LENGTH_SHORT).show();
    }

    private void DownloadImage(String userId, FirebaseCallback<Bitmap> callback){
        StorageReference ricersRef= storageReference.child("images/meetPoints/"+ userId);
        final long ONE_MEGABYTE = 1024 * 1024;

        ricersRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                callback.onCallback(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

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

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;
        googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(this.getContext(), R.raw.style)
        );

        mMap.setOnMarkerClickListener(mClusterManager);
        getDeviceLocation();

        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        addMapMarkers();
    }

    private void getDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getContext());
        try {
            if (mLocationPermissionGranted) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "location found", Toast.LENGTH_SHORT).show();
                            Location currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
                        } else
                            Toast.makeText(getContext(), "location not found", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (SecurityException e) {

        }
    }

    private void moveCamera(LatLng latLng, float zoom) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            mMapView.onResume();
        } catch (Exception e) {
        }
        ;
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            mMapView.onPause();
        } catch (Exception e) {
        }
        ;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            mMapView.onDestroy();
        } catch (Exception e) {
        }
        ;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        try {
            mMapView.onLowMemory();
        } catch (Exception e) {
        }
        ;
    }



}
