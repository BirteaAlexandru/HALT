package com.example.halt.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.halt.R;
import com.example.halt.interfaces.DashboardActivityFragmentCommunication;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.gms.maps.OnMapReadyCallback;

import static com.example.halt.constants.Constants.MAPVIEW_BUNDLE_KEY;

public class DashboardFragment extends Fragment implements OnMapReadyCallback{
    private DashboardActivityFragmentCommunication dashboardActivityFragmentCommunication;
    boolean  mLocationPermissionGranted= false;
    MapView mMapView;

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
        BottomNavigationView bottomNavigationView= view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        mMapView = view.findViewById(R.id.mapView);

        mMapView.getMapAsync(this);
        mMapView.onCreate(savedInstanceState);
        return view;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()){
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

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(this.getContext(), R.raw.style)
        );

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}
