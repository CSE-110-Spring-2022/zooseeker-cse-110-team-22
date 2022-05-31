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
    double last_known_lat;
    double last_known_lng;

    public LocationModel(ComponentActivity activity){
        provider = LocationManager.GPS_PROVIDER;
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
    }

    @SuppressLint("MissingPermission")
    public void requestLocationUpdates(LocationListener ll){
        locationManager.requestLocationUpdates(provider, 0, 0f, ll);
    };

    public void setLastKnown(double lat, double lng){
        last_known_lat = lat;
        last_known_lng = lng;
    }

    public double getLat(){
        return last_known_lat;
    }

    public double getLng(){
        return  last_known_lng;
    }

}
