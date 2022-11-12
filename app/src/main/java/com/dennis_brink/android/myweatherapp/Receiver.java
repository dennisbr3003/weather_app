package com.dennis_brink.android.myweatherapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Receiver extends BroadcastReceiver {

    private IWeatherListener weatherListener;
    private IPermissionListener permissionListener;
    private INetworkStateListener networkStateListener;
    private ILocationListener wlocationListener;

    public void setWeatherListener(IWeatherListener weatherListener){
        this.weatherListener = weatherListener;
    }

    public void setPermissionListener(IPermissionListener permissionListener){
        this.permissionListener = permissionListener;
    }

    public void setNetworkStateListener(INetworkStateListener networkStateListener){
        this.networkStateListener = networkStateListener;
    }

    public void setLocationListener(ILocationListener wlocationListener){
        this.wlocationListener = wlocationListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("DENNIS_B", "Receiver.onReceive() :  Receiver reached with action " + intent.getAction());

        if(intent.getAction().equals("CALL_COMPLETE")) {
            if (weatherListener != null) {
                weatherListener.callComplete(intent.getStringExtra("type"));
            }
        }

        if(intent.getAction().equals("LOCAL_WEATHER_DATA_ERROR")) {
            if (weatherListener != null) {
                weatherListener.showErrorMessage(intent.getStringExtra("text"), intent.getStringExtra("type"));
            }
        }

        if(intent.getAction().equals("LOCATION_PERMISSION_GRANTED")) {
            if (permissionListener != null) {
                permissionListener.afterPermissionGranted();
            }
        }

        if(intent.getAction().equals("NETWORK_CONNECTION_LOST") || intent.getAction().equals("NETWORK_CONNECTION_AVAILABLE")) {
            if (networkStateListener != null) {
                networkStateListener.networkStateChanged(intent.getAction());
            }
        }

        if(intent.getAction().equals("LOCATION_CHANGED")) {
            if (wlocationListener != null) {
                wlocationListener.locationChanged(intent.getDoubleExtra("lat", 0), intent.getDoubleExtra("lon", 0));
            }
        }

    }

}
