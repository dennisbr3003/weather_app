package com.dennis_brink.android.myweatherapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Calendar;

public class LocationLibrary {

    private final String TAG = "DENNIS_B";

    LocationManager locationManager;
    LocationListener locationListener;
    Context activityContext;

    public LocationLibrary(Context activityContext) {
        this.activityContext = activityContext;
        /*
        if (!checkLocationAccessPermission()) {
            Log.d(TAG, "LocationLibrary.Constructor() no access granted to device location");
            // check if we can get permission
        }
        */
    }

    @SuppressLint("MissingPermission")
    public void setupLocationListener(Context context) {

        Log.d(TAG, "LocationLibrary.setupLocationListener()");

        locationManager = (LocationManager) Application.getContext().getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onProviderEnabled(@NonNull String provider) {
                LocationListener.super.onProviderEnabled(provider);
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                LocationListener.super.onProviderDisabled(provider);
            }

            @Override
            public void onLocationChanged(@NonNull android.location.Location location) { // user location

                Log.d(TAG, "LocationLibrary.setupLocationListener().onLocationChanged(): location changed");

                AppConfig.getInstance().setLatitude(location.getLatitude());
                AppConfig.getInstance().setLongitude(location.getLongitude());

                // send message that the location changed
                broadcastLocationChanged(context);
            }
        };

        Log.d(TAG, "LocationLibrary.setupLocationListener(): setup listener to check every 500m by 50m");
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 50, locationListener);
        Log.d(TAG, "LocationLibrary.setupLocationListener() completed");

    }
/*
    private boolean checkLocationAccessPermission() {
        try {
            if (ActivityCompat.checkSelfPermission(Application.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(Application.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "LocationLibrary.checkLocationAccessPermission() : access to location denied");
                return false;
            } else {
                Log.d(TAG, "LocationLibrary.checkLocationAccessPermission() : access to location granted");
                return true;
            }
        }catch(Exception e){
            Log.d(TAG, "LocationLibrary.checkLocationAccessPermission() : error (Exception) : " + e.getLocalizedMessage());
            return false;
        }
    }
*/
    @SuppressLint("MissingPermission")
    public void getCurrentLocation(Context context) {

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        // only use last know location if it is valid and if the location is less than 2 minutes old
        if(location != null && location.getTime() > Calendar.getInstance().getTimeInMillis() - 2 * 60 * 1000) {

            Log.d(TAG, "LocationLibrary.getCurrentLocation(): last known location is usable, age <= 2 mins");

            AppConfig.getInstance().setLatitude(location.getLatitude());
            AppConfig.getInstance().setLongitude(location.getLongitude());

            // send message that the location changed
            broadcastLocationChanged(context);

        } else { // location data is too old, get a new location

            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE); // for speed select rough estimate of location
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setSpeedRequired(false);
            criteria.setCostAllowed(false);
            criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
            criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);

            final Looper looper = null; // to force it to fire only once ?

            locationManager.requestSingleUpdate(criteria, locationListener, looper);

            Log.d(TAG, "LocationLibrary.getCurrentLocation(): Request current location completed. Waiting for listener");

        }

    }

    private void broadcastLocationChanged(Context context) {
        Log.d(TAG, "LocationLibrary.broadcastLocationChanged(): sending 'LOCATION_CHANGED'");
        Intent i = new Intent();
        i.setAction("LOCATION_CHANGED");
        Application.getContext().sendBroadcast(i);
    }

}
