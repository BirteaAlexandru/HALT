package com.example.halt.Models;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class ClusterMarker implements ClusterItem {

    private LatLng position;
    private String title;
    private String snippet;
    private String userId;
    private Bitmap imageBtm;

    public ClusterMarker(LatLng position, String title, String snippet, String userId, Bitmap imageBtm) {
        this.position = position;
        this.title = title;
        this.snippet = snippet;
        this.userId = userId;
        this.imageBtm = imageBtm;
    }
    public ClusterMarker() {

    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Bitmap getImageBtm() {
        return imageBtm;
    }

    public void setImageBtm(Uri imageUri) {
        this.imageBtm = imageBtm;
    }

    @NonNull
    @Override
    public LatLng getPosition() {
        return position;
    }

    @Nullable
    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return snippet;
    }
}
