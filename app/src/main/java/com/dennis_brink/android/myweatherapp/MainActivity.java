package com.dennis_brink.android.myweatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "DENNIS_B";
    LocationLibrary locationLibrary = new LocationLibrary();
    NetworkLibrary networkLibrary = new NetworkLibrary();

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

        TabLayout tabLayout;
        ViewPager2 viewPager2;

        if(AppConfig.getInstance().getApi_key().equals("")){
            Log.d(TAG, "MainActivity.initWeatherAppFragments(): no api key configured" );
            Toast.makeText(this, "Api key is not found, not configured or corrupted", Toast.LENGTH_SHORT).show();
            finish();
        }

        if(AppConfig.getInstance().getWindDirections()==null){
            Log.d(TAG, "MainActivity.initWeatherAppFragments(): no wind direction data configured" );
            Toast.makeText(this, "Wind direction data not found, not configured or corrupted", Toast.LENGTH_SHORT).show();
            finish();
        }

        locationLibrary.setupLocationListener(); // this will work for all fragments
        networkLibrary.setupNetworkStateListener();

        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPagerMain);

        ViewPagerMainAdapter adapter = new ViewPagerMainAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager2.setAdapter(adapter);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, true, true, (tab, position) -> {

            switch(position) {
                case 0:
                    tab.setCustomView(R.layout.tab0);
                    ApplicationLibrary.setTabTextColor((TextView) tab.getCustomView().findViewById(R.id.textViewTab0));
                    break;
                case 1:
                    tab.setCustomView(R.layout.tab1);
                    ApplicationLibrary.setTabTextColor((TextView) tab.getCustomView().findViewById(R.id.textViewTab1));
                    break;
                case 2:
                    tab.setCustomView(R.layout.tab2);
                    ApplicationLibrary.setTabTextColor((TextView) tab.getCustomView().findViewById(R.id.textViewTab2));
                    break;
            }

        });
        tabLayoutMediator.attach();

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

    public void getCurrentLocation(){
        locationLibrary.getCurrentLocation(Application.getContext());
    }

    public boolean isNetworkAvailable(){
        return networkLibrary.isNetworkAvailable();
    }

}