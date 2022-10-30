package com.dennis_brink.android.myweatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

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

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    LocationManager locationManager;
    LocationListener locationListener;
    double lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getAPIkey();
        // dit moet naar een aparte proc, lon en lat moet overal beschikbaar zijn
        /*
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
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
            public void onLocationChanged(@NonNull Location location) { // user location
                lat = location.getLatitude();
                lon = location.getLongitude();
                Log.d("DENNIS_B", "Latitude " + lat + " Longitude " + lon);
                AppConfig.getInstance().setLat(lat);
                AppConfig.getInstance().setLon(lon);
                Log.d("DENNIS_B", "Secured values: " + AppConfig.getInstance().toString());
            }
        };
        */
        /*
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
           locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 50, locationListener);
        }
        */
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
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }

    private void getAPIkey(){

        String line;
        InputStream in;

        AssetManager am = getAssets();
        try {
            in = am.open("datasource.config.json");
        } catch (IOException e) {
            Log.d("DENNIS_B", "getApiKey() error (IOException) : " + e.getLocalizedMessage());
            return;
        }

        StringBuilder str = new StringBuilder();

        try(BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            while((line = br.readLine()) != null){
                str.append(line.replaceAll("\\s", ""));
            }
        } catch(Exception e) {
            Log.d("DENNIS_B", "getApiKey() error (Exception) : " + e.getLocalizedMessage() + e.getMessage());
            return;
        }

        Log.d("DENNIS_B", "getApiKey() str : " + str);

        ObjectMapper mapper = new ObjectMapper();
        try {
            OpenWeatherConfig openWeatherConfig = mapper.readValue(str.toString(), OpenWeatherConfig.class);
            Log.d("DENNIS_B", "getApiKey() key : " + openWeatherConfig.getDatasource().getKey());
            AppConfig.getInstance().setApi_key(openWeatherConfig.getDatasource().getKey());
        } catch (JsonProcessingException e) {
            Log.d("DENNIS_B", "getApiKey() error (JsonProcessingException) : " + e.getLocalizedMessage());
            return;
        } catch (Exception e) {
            Log.d("DENNIS_B", "getApiKey() error (Exception) : " + e.getLocalizedMessage());
            return;
        }
    }
/*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1 && permissions.length > 0 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 50, locationListener);
        }

    }
 */
}