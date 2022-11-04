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

        // ask for permission to access user location if needed
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

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
            Log.d("DENNIS_B", "MainActivity.getApiKey(): error (IOException) : " + e.getLocalizedMessage());
            return;
        }

        StringBuilder str = new StringBuilder();

        try(BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            while((line = br.readLine()) != null){
                str.append(line.replaceAll("\\s", ""));
            }
        } catch(Exception e) {
            Log.d("DENNIS_B", "MainActivity.getApiKey(): error (Exception) : " + e.getLocalizedMessage() + e.getMessage());
            return;
        }

        Log.d("DENNIS_B", "MainActivity.getApiKey(): string : " + str);

        ObjectMapper mapper = new ObjectMapper();
        try {
            OpenWeatherConfig openWeatherConfig = mapper.readValue(str.toString(), OpenWeatherConfig.class);
            Log.d("DENNIS_B", "getApiKey() key : " + openWeatherConfig.getDatasource().getKey());
            AppConfig.getInstance().setApi_key(openWeatherConfig.getDatasource().getKey());
        } catch (JsonProcessingException e) {
            Log.d("DENNIS_B", "MainActivity.getApiKey(): error (JsonProcessingException) : " + e.getLocalizedMessage());
            return;
        } catch (Exception e) {
            Log.d("DENNIS_B", "MainActivity.getApiKey(): error (Exception) : " + e.getLocalizedMessage());
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1 && permissions.length > 0 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            // granted
            broadcastLocationPermission(this);
        } else {
            // no permission to access user location. Shut down.
            Toast.makeText(this, "Access to location is needed in order to get data", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private static void broadcastLocationPermission(Context context) {

        Log.d("DENNIS_B", String.format("MainActivity.broadcastLocationPermission(): sending 'LOCATION_PERMISSION_GRANTED' "));
        Intent i = new Intent();
        i.setAction("LOCATION_PERMISSION_GRANTED");
        context.sendBroadcast(i);

    }

}