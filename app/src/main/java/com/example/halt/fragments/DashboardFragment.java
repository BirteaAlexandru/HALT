package com.example.halt.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.audiofx.Equalizer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.halt.R;
import com.example.halt.interfaces.DashboardActivityFragmentCommunication;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import static com.example.halt.constants.Constants.MAPVIEW_BUNDLE_KEY;

public class DashboardFragment extends Fragment implements OnMapReadyCallback {
    private static final float DEFAULT_ZOOM = 15f;

    private DashboardActivityFragmentCommunication dashboardActivityFragmentCommunication;
    boolean mLocationPermissionGranted = false;
    MapView mMapView;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

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

        mMapView = view.findViewById(R.id.mapView);

        checkPermission();

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

        getDeviceLocation();

        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
             return;
        }
        mMap.setMyLocationEnabled(true);


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
                            Location currentLocation= (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
                        }
                        else  Toast.makeText(getContext(), "location not found", Toast.LENGTH_SHORT).show();
                    }
                });
            }
         }catch (SecurityException e){

         }
    }

    private void moveCamera(LatLng latLng, float zoom){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            mMapView.onResume();
        }
        catch (Exception e){};
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            mMapView.onPause();
        }
        catch (Exception e){};
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            mMapView.onDestroy();
        }
        catch (Exception e){};
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        try {
            mMapView.onLowMemory();
        }
        catch (Exception e){};
    }
}
