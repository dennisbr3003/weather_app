package com.dennis_brink.android.myweatherapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.util.Log;

public class NetworkLibrary {

    private final String TAG = "DENNIS_B";

    public NetworkLibrary() {
    }

    public void setupNetworkStateListener(){

        Log.d(TAG, "NetworkLibrary.setupNetworkStateListener()");

        ConnectivityManager cm = (ConnectivityManager) Application.getContext().getSystemService(Application.getContext().CONNECTIVITY_SERVICE);

        NetworkRequest.Builder builder = new NetworkRequest.Builder();

        builder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);

        ConnectivityManager.NetworkCallback callback = new ConnectivityManager.NetworkCallback() {

            @Override
            public void onAvailable(Network network) {
                super.onAvailable(network);
                Log.d(TAG, "NetworkLibrary.onAvailable(): connection available");
                broadcastNetworkState(Application.getContext(), "NETWORK_CONNECTION_AVAILABLE");
            }

            @Override
            public void onLost(Network network) {
                super.onLost(network);
                Log.d(TAG, "NetworkLibrary.onLost(): connection lost");
                broadcastNetworkState(Application.getContext(), "NETWORK_CONNECTION_LOST");
            }

        };
        cm.registerNetworkCallback(builder.build(), callback);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) Application.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        Log.d(TAG, "NetworkLibrary.isNetworkAvailable(): " + (activeNetworkInfo != null && activeNetworkInfo.isConnected()));
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void broadcastNetworkState(Context context, String state) {

        Log.d(TAG, "NetworkLibrary.broadcastNetworkState(): sending " + state);
        Intent i = new Intent();
        i.setAction(state);
        context.sendBroadcast(i);

    }

}
