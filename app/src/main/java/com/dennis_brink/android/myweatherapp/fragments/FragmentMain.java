package com.dennis_brink.android.myweatherapp.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dennis_brink.android.myweatherapp.ApplicationLibrary;
import com.dennis_brink.android.myweatherapp.IPermissionListener;
import com.dennis_brink.android.myweatherapp.IWeatherListener;
import com.dennis_brink.android.myweatherapp.R;
import com.dennis_brink.android.myweatherapp.Receiver;
import com.dennis_brink.android.myweatherapp.RetrofitLibrary;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FragmentMain extends Fragment implements IWeatherListener, IPermissionListener {

    LocationManager locationManager;
    LocationListener locationListener;
    Receiver receiver = null;
    RecyclerView rvForecastHour;

    private ImageView imageViewIcon;
    private ProgressBar progressBar;

    private double lat;
    private double lon;

    private ArrayList<ImageView> rating = new ArrayList<>();
    private Map<String, TextView> weatherData = new HashMap<>();
    private Map<String, TextView> weatherLabels = new HashMap<>();

    public static FragmentMain newInstance() {
        return new FragmentMain();
    }

    @Override
    public void onStart() {
        super.onStart();

        progressBar.setVisibility(View.VISIBLE);
        imageViewIcon.setVisibility(View.INVISIBLE);

        if(receiver == null){
            Log.d("DENNIS_B", "FragmentMain.onStart(): registering receiver");
            receiver = new Receiver();
            receiver.setWeatherListener(this);
            receiver.setPermissionListener(this);
        }
        getActivity().registerReceiver(receiver, getFilter());

        setupListenersAndInitData();

        Log.d("DENNIS_B", "FragmentMain.onStart(): done");
    }

    private void setupListenersAndInitData(){

        setupLocationListener();

        // permission was already obtained in the main activity, so we do not ask for it here.
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if(location != null && location.getTime() > Calendar.getInstance().getTimeInMillis() - 2 * 60 * 1000) {

                Log.d("DENNIS_B", "FragmentMain.onStart(): use the last known location (age <= 2 mins)");
                lat = location.getLatitude();
                lon = location.getLongitude();
                Log.d("DENNIS_B", "FragmentMain.onStart(): last known location lat/lon " + lat + "/" + lon);
                RetrofitLibrary.getWeatherDataLocal(lat, lon, rating, weatherData, imageViewIcon, getContext());
                RetrofitLibrary.getWeatherForecastData(lat, lon, rvForecastHour);

            }
            else {
                Log.d("DENNIS_B", "FragmentMain.onStart(): last known location unusable (age), request current location");
                getCurrentLocation();
            }
            Log.d("DENNIS_B", "FragmentMain.onStart(): setup listener to check every 500m by 50m");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 50, locationListener); // set op the listener parameters
        } else { // stop loading, something went wrong
            stopProgressBar();
        }
    }


    private IntentFilter getFilter(){
        IntentFilter intentFilter = new IntentFilter();
        Log.d("DENNIS_B", "FragmentMain.getFilter(): Registering for broadcast action LOCATION_PERMISSION_GRANTED, LOCAL_WEATHER_DATA_ERROR and STOP_PROGRESS_BAR");
        intentFilter.addAction("LOCAL_WEATHER_DATA_ERROR"); // only register receiver for this event
        intentFilter.addAction("STOP_PROGRESS_BAR");
        intentFilter.addAction("LOCATION_PERMISSION_GRANTED");
        return intentFilter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null){
            Log.d("DENNIS_B", "FragmentMain.onDestroy(): unregistering receiver");
            getActivity().unregisterReceiver(receiver);
            receiver = null;
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation(){

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

        Log.d("DENNIS_B", "FragmentMain.getCurrentLocation(): Request current location completed. Waiting for listener");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // do stuff here like in an Activity's onCreate
        progressBar = view.findViewById(R.id.progressBar);
        imageViewIcon = view.findViewById(R.id.imageViewIcon);

        rvForecastHour = view.findViewById(R.id.rvHours);
        rvForecastHour.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        setupWeatherData(view);
        setupRating(view);
        setupWeatherLabels(view);

        return view;

    }

    private void setupWeatherData(View view){

        TextView textHumidity, textMaxTemp, textMinTemp, textPressure,
                 textWind, textCity, textTemp, textCondition;

        textCity = view.findViewById(R.id.textViewCity);
        textCondition = view.findViewById(R.id.textViewWeaterCondition);
        textHumidity = view.findViewById(R.id.textViewHumidity);
        textWind = view.findViewById(R.id.textViewWind);
        textMaxTemp = view.findViewById(R.id.textViewMaxTemp);
        textMinTemp = view.findViewById(R.id.textViewMinTemp);
        textPressure = view.findViewById(R.id.textViewPressure);
        textTemp = view.findViewById(R.id.textViewTemperature);

        textCity.setText("");
        textCondition.setText("");
        textHumidity.setText("");
        textWind.setText("");
        textMaxTemp.setText("");
        textMinTemp.setText("");
        textPressure.setText("");
        textTemp.setText("");

        textCity.setVisibility(View.INVISIBLE);
        textTemp.setVisibility(View.INVISIBLE);
        textCondition.setVisibility(View.INVISIBLE);

        weatherData.put("city", textCity);
        weatherData.put("condition", textCondition);
        weatherData.put("humidity", textHumidity);
        weatherData.put("wind", textWind);
        weatherData.put("pressure", textPressure);
        weatherData.put("temp", textTemp);

        ApplicationLibrary.setColorTextView(weatherData);

        weatherData.put("maxtemp", textMaxTemp);
        weatherData.put("mintemp", textMinTemp);

    }

    private void setupWeatherLabels(View view){

        TextView textLabelHumidity, textLabelMaxTemp, textLabelMinTemp,
                 textLabelPressure, textLabelWind, textLabelAirQuality,
                 textLabelDetails;

        textLabelHumidity = view.findViewById(R.id.textViewLabelHumidity);
        textLabelWind = view.findViewById(R.id.textViewLabelWind);
        textLabelPressure = view.findViewById(R.id.textViewLabelPressure);
        textLabelAirQuality = view.findViewById(R.id.textViewLabelAirquality);

        weatherLabels.put("humidity", textLabelHumidity);
        weatherLabels.put("wind", textLabelWind);
        weatherLabels.put("pressure", textLabelPressure);
        weatherLabels.put("airquality", textLabelAirQuality);

        ApplicationLibrary.setColorTextView(weatherLabels);

    }


    private void setupRating(View view){

        ImageView imgStar1, imgStar2, imgStar3, imgStar4, imgStar5;

        imgStar1 = view.findViewById(R.id.star1);
        imgStar2 = view.findViewById(R.id.star2);
        imgStar3 = view.findViewById(R.id.star3);
        imgStar4 = view.findViewById(R.id.star4);
        imgStar5 = view.findViewById(R.id.star5);

        rating.add(imgStar1);
        rating.add(imgStar2);
        rating.add(imgStar3);
        rating.add(imgStar4);
        rating.add(imgStar5);

    }

    private void setupLocationListener(){

        Log.d("DENNIS_B", "FragmentMain.setupLocationListener() start");

        locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

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

                Log.d("DENNIS_B", "FragmentMain.setupLocationListener().onLocationChanged(): location changed");

                lat = location.getLatitude();
                lon = location.getLongitude();
                Log.d("DENNIS_B", "FragmentMain.setupLocationListener().onLocationChanged(): latitude " + lat + " longitude " + lon);

                RetrofitLibrary.getWeatherDataLocal(lat, lon, rating, weatherData, imageViewIcon, getContext());
                RetrofitLibrary.getWeatherForecastData(lat, lon, rvForecastHour);

            }
        };

        Log.d("DENNIS_B", "FragmentMain.setupLocationListener() end - setup listeners completed");

    }

    @Override
    public void showErrorMessage(String text) {
        // create a dialog here to show an error message
        Log.d("DENNIS_B", "FragmentMain.showErrorMessage() receiver reached");
    }

    @Override
    public void stopProgressBar() {
        Log.d("DENNIS_B", "FragmentMain.stopProgressBar() receiver reached");
        progressBar.setVisibility(View.INVISIBLE);
        weatherData.get("city").setVisibility(View.VISIBLE);
        weatherData.get("temp").setVisibility(View.VISIBLE);
        weatherData.get("condition").setVisibility(View.VISIBLE);
        imageViewIcon.setVisibility(View.VISIBLE);
    }

    @Override
    public void afterPermissionGranted() {
        // permission granted so get data
        Log.d("DENNIS_B", "FragmentMain.afterPermissionGranted() receiver reached");
        setupListenersAndInitData();
    }
}