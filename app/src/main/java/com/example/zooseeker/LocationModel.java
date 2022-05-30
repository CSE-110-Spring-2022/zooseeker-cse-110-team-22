package com.example.zooseeker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.activity.ComponentActivity;

/* Handles location for the app. Use requestLocationUpdates to register new listener*/
public class LocationModel {
    String provider;
    LocationManager locationManager;

    public LocationModel(ComponentActivity activity){
        provider = LocationManager.GPS_PROVIDER;
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
    }

    @SuppressLint("MissingPermission")
    public void requestLocationUpdates(LocationListener ll){
        locationManager.requestLocationUpdates(provider, 0, 0f, ll);
    };


}
