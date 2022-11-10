package com.dennis_brink.android.myweatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.dennis_brink.android.myweatherapp.model_config.OpenWeatherConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements ILocationListener {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private final String TAG = "DENNIS_B";

    LocationLibrary locationLibrary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            // we do not have permission to access the location...
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            //we have permission to access the location...
            initWeatherAppFragments();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void initWeatherAppFragments(){

        if(!getAPIkey()){ // API key is essential to getting data
            finish();
        }

        setupNetworkStateListener();
        initWeatherAppLocation();

        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPagerMain);

        ViewPagerMainAdapter adapter = new ViewPagerMainAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager2.setAdapter(adapter);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, true, true, (tab, position) -> {
            switch(position) {
                case 0:
                    tab.setCustomView(R.layout.tab0);
                    break;
                case 1:
                    tab.setCustomView(R.layout.tab1);
                    break;
                case 2:
                    tab.setCustomView(R.layout.tab2);
                    break;
            }

        });
        tabLayoutMediator.attach();

        AppConfig.getInstance().setConnectionOnStartup(isNetworkAvailable());
    }

    private boolean getAPIkey(){

        String line;
        InputStream in;

        AssetManager am = getAssets();
        try {
            in = am.open("datasource.config.json");
        } catch (IOException e) {
            Log.d(TAG, "MainActivity.getApiKey(): error (IOException) : " + e.getLocalizedMessage());
            return false;
        }

        StringBuilder str = new StringBuilder();

        try(BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            while((line = br.readLine()) != null){
                str.append(line.replaceAll("\\s", ""));
            }
        } catch(Exception e) {
            Log.d(TAG, "MainActivity.getApiKey(): error (Exception) : " + e.getLocalizedMessage() + e.getMessage());
            return false;
        }

        Log.d(TAG, "MainActivity.getApiKey(): string : " + str);

        ObjectMapper mapper = new ObjectMapper();
        try {
            OpenWeatherConfig openWeatherConfig = mapper.readValue(str.toString(), OpenWeatherConfig.class);
            AppConfig.getInstance().setApi_key(openWeatherConfig.getDatasource().getKey());
            return true;
        } catch (JsonProcessingException e) {
            Log.d(TAG, "MainActivity.getApiKey(): error (JsonProcessingException) : " + e.getLocalizedMessage());
            return false;
        } catch (Exception e) {
            Log.d(TAG, "MainActivity.getApiKey(): error (Exception) : " + e.getLocalizedMessage());
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1 && permissions.length > 0 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            initWeatherAppFragments();
        } else {
            Log.d(TAG, "MainActivity.onRequestPermissionsResult(): access to device location denied by user");
            Toast.makeText(this, "Access to location is needed in order to get data", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        Log.d("DENNIS_B", "MainActivity.isNetworkAvailable() : " + (activeNetworkInfo != null && activeNetworkInfo.isConnected()));
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void initWeatherAppLocation(){

        locationLibrary = new LocationLibrary(this); // used in initWeatherApp

        locationLibrary.setupLocationListener(this);
        locationLibrary.getCurrentLocation(this);

    }


    private void setupNetworkStateListener(){

        Log.d("DENNIS_B", "MainActivity.setupNetworkStateListener()");

        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        NetworkRequest.Builder builder = new NetworkRequest.Builder();

        builder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);

        ConnectivityManager.NetworkCallback callback = new ConnectivityManager.NetworkCallback() {

            @Override
            public void onAvailable(Network network) {
                super.onAvailable(network);
                Log.d("DENNIS_B", "Connection available");
                broadcastNetworkState(getApplicationContext(), "NETWORK_CONNECTION_AVAILABLE");
            }

            @Override
            public void onLost(Network network) {
                super.onLost(network);
                Log.d("DENNIS_B", "Connection lost");
                broadcastNetworkState(getApplicationContext(), "NETWORK_CONNECTION_LOST");
            }

        };
        cm.registerNetworkCallback(builder.build(), callback);
    }

    private static void broadcastNetworkState(Context context, String state) {

        Log.d("DENNIS_B", String.format("MainActivity.broadcastNetworkState(): sending " + state));
        Intent i = new Intent();
        i.setAction(state);
        context.sendBroadcast(i);

    }

    @Override
    public void locationChanged() {

    }

}