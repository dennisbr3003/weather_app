package com.dennis_brink.android.myweatherapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Receiver extends BroadcastReceiver {

    private IWeatherListener weatherListener;

    public void setWeatherListener(IWeatherListener weatherListener){
        this.weatherListener = weatherListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("DENNIS_B", "Receiver.onReceive() :  Receiver reached with action " + intent.getAction());

        if(intent.getAction().equals("STOP_PROGRESS_BAR")) {
            if (weatherListener != null) {
                weatherListener.stopProgressBar();
            }
        }

    }

}
