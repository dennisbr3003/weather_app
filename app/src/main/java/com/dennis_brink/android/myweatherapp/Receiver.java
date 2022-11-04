package com.dennis_brink.android.myweatherapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Receiver extends BroadcastReceiver {

    private IWeatherListener weatherListener;
    private IPermissionListener permissionListener;

    public void setWeatherListener(IWeatherListener weatherListener){
        this.weatherListener = weatherListener;
    }

    public void setPermissionListener(IPermissionListener permissionListener){
        this.permissionListener = permissionListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("DENNIS_B", "Receiver.onReceive() :  Receiver reached with action " + intent.getAction());

        if(intent.getAction().equals("STOP_PROGRESS_BAR")) {
            if (weatherListener != null) {
                weatherListener.stopProgressBar();
            }
        }

        if(intent.getAction().equals("LOCATION_PERMISSION_GRANTED")) {
            if (permissionListener != null) {
                permissionListener.afterPermissionGranted();
            }
        }

    }

}
