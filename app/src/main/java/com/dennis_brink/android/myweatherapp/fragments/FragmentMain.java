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

import com.dennis_brink.android.myweatherapp.AppConfig;
import com.dennis_brink.android.myweatherapp.ApplicationLibrary;
import com.dennis_brink.android.myweatherapp.INetworkStateListener;
import com.dennis_brink.android.myweatherapp.IPermissionListener;
import com.dennis_brink.android.myweatherapp.IWeatherListener;
import com.dennis_brink.android.myweatherapp.R;
import com.dennis_brink.android.myweatherapp.Receiver;
import com.dennis_brink.android.myweatherapp.RetrofitLibrary;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FragmentMain extends Fragment implements IWeatherListener, IPermissionListener, INetworkStateListener {

    LocationManager locationManager;
    LocationListener locationListener;
    Receiver receiver = null;
    RecyclerView rvForecastHour;

    private ImageView imageViewIcon, imageViewHumidityLocal, imageViewNoNetworkLocal;
    private ProgressBar progressBar;
    private View viewIbeam;

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

        if(receiver == null){
            Log.d("DENNIS_B", "FragmentMain.onStart(): registering receiver");
            receiver = new Receiver();
            receiver.setWeatherListener(this);
            receiver.setPermissionListener(this);
            receiver.setNetworkStateListener(this);
        }
        getActivity().registerReceiver(receiver, getFilter());

        setupListenersAndInitData();

        Log.d("DENNIS_B", "FragmentMain.onStart(): done");
    }

    private void setupListenersAndInitData(){

        setupLocationListener();

        // permission was already obtained in the main activity, so we do not ask for it here.

        Log.d("DENNIS_B", "Connection avail on startup " + AppConfig.getInstance().hasConnectionOnStartup());

        if(ContextCompat.checkSelfPermission(getActivity(),
                                              Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if(location != null && location.getTime() > Calendar.getInstance().getTimeInMillis() - 2 * 60 * 1000) {

                Log.d("DENNIS_B", "FragmentMain.onStart(): use the last known location (age <= 2 mins)");
                lat = location.getLatitude();
                lon = location.getLongitude();
                AppConfig.getInstance().setLatitude(lat);
                AppConfig.getInstance().setLongitude(lon);
                Log.d("DENNIS_B", "FragmentMain.onStart(): last known location lat/lon " + lat + "/" + lon);
                if(AppConfig.getInstance().hasConnectionOnStartup()) {// connection must be there
                    progressBar.setVisibility(View.VISIBLE);
                    imageViewNoNetworkLocal.setVisibility(View.INVISIBLE);
                    RetrofitLibrary.getWeatherDataLocal(lat, lon, rating, weatherData, imageViewIcon, getContext());
                    RetrofitLibrary.getWeatherForecastData(lat, lon, rvForecastHour, getContext());
                }
                else {
                    progressBar.setVisibility(View.INVISIBLE);
                }

            }
            else {
                Log.d("DENNIS_B", "FragmentMain.onStart(): last known location unusable (age), request current location");
                getCurrentLocation();
            }
            Log.d("DENNIS_B", "FragmentMain.onStart(): setup listener to check every 500m by 50m");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 50, locationListener); // set op the listener parameters
        } else { // stop loading, something went wrong
            stopProgressBarLocal("error");
        }
    }


    private IntentFilter getFilter(){
        IntentFilter intentFilter = new IntentFilter();
        Log.d("DENNIS_B", "FragmentMain.getFilter(): Registering for broadcast action LOCATION_PERMISSION_GRANTED, LOCAL_WEATHER_DATA_ERROR and STOP_PROGRESS_BAR");
        intentFilter.addAction("LOCAL_WEATHER_DATA_ERROR"); // only register receiver for this event
        intentFilter.addAction("STOP_PROGRESS_BAR");
        intentFilter.addAction("LOCATION_PERMISSION_GRANTED");
        intentFilter.addAction("NETWORK_CONNECTION_LOST");
        intentFilter.addAction("NETWORK_CONNECTION_AVAILABLE");
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
        imageViewHumidityLocal = view.findViewById(R.id.imageViewHumidityLocal);
        imageViewNoNetworkLocal = view.findViewById(R.id.imageViewNoNetworkLocal);
        viewIbeam = view.findViewById(R.id.viewIBeam);

        rvForecastHour = view.findViewById(R.id.rvHours);
        rvForecastHour.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        setupWeatherData(view);
        setupRating(view);
        setupWeatherLabels(view);
        ApplicationLibrary.hideRating(rating);
        return view;

    }

    private void setupWeatherData(View view){

        TextView textHumidity, textMaxTemp, textMinTemp, textPressure,
                 textWind, textCity, textTemp, textCondition, textApi;

        textCity = view.findViewById(R.id.textViewIBeam);
        textCondition = view.findViewById(R.id.textViewWeaterCondition);
        textHumidity = view.findViewById(R.id.textViewHumidity);
        textWind = view.findViewById(R.id.textViewWind);
        textMaxTemp = view.findViewById(R.id.textViewMaxTemp);
        textMinTemp = view.findViewById(R.id.textViewMinTemp);
        textPressure = view.findViewById(R.id.textViewPressure);
        textTemp = view.findViewById(R.id.textViewTemperature);
        textApi = view.findViewById(R.id.textViewApi);

        textCity.setText("");
        textCondition.setText("");
        textHumidity.setText("");
        textWind.setText("");
        textMaxTemp.setText("");
        textMinTemp.setText("");
        textPressure.setText("");
        textTemp.setText("");
        textApi.setText("none");

        weatherData.put("city", textCity);
        weatherData.put("condition", textCondition);
        weatherData.put("humidity", textHumidity);
        weatherData.put("wind", textWind);
        weatherData.put("pressure", textPressure);
        weatherData.put("temp", textTemp);

        ApplicationLibrary.setColorTextView(weatherData);

        weatherData.put("maxtemp", textMaxTemp);
        weatherData.put("mintemp", textMinTemp);
        weatherData.put("timestamp", textApi);

    }

    private void setupWeatherLabels(View view){

        TextView textLabelHumidity, textViewIBeam,
                 textLabelPressure, textLabelWind, textLabelAirQuality;

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
                AppConfig.getInstance().setLatitude(lat);
                AppConfig.getInstance().setLongitude(lon);
                Log.d("DENNIS_B", "FragmentMain.setupLocationListener().onLocationChanged(): latitude " + lat + " longitude " + lon);
                if(AppConfig.getInstance().hasConnectionOnStartup()) {
                    imageViewNoNetworkLocal.setVisibility(View.INVISIBLE);
                    RetrofitLibrary.getWeatherDataLocal(lat, lon, rating, weatherData, imageViewIcon, getContext());
                    RetrofitLibrary.getWeatherForecastData(lat, lon, rvForecastHour, getContext());
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                }

            }
        };

        Log.d("DENNIS_B", "FragmentMain.setupLocationListener() end - setup listeners completed");

    }

    @Override
    public void showErrorMessage(String text, String type) {
        // create a dialog here to show an error message
        Log.d("DENNIS_B", "FragmentMain.showErrorMessage() receiver reached for type: " + type);
        if(type.equals("main")) {  // listeners are the same in all fragments type makes sure
            // the correct dialog is shown, and only once (bit tricky)
            progressBar.setVisibility(View.INVISIBLE);
            ApplicationLibrary.getErrorAlertDialog(text, getActivity()).show();
        }
    }

    @Override
    public void stopProgressBar(String type) {
        Log.d("DENNIS_B", "FragmentMain.stopProgressBar() receiver reached with type: " + type);
        progressBar.setVisibility(View.INVISIBLE);

        ApplicationLibrary.showTextViews(weatherLabels);
        ApplicationLibrary.showRating(rating);
        imageViewHumidityLocal.setVisibility(View.VISIBLE);
        viewIbeam.setVisibility(View.VISIBLE);

    }

    @Override
    public void callComplete(String type) {

    }

    public void stopProgressBarLocal(String state) {
        Log.d("DENNIS_B", "FragmentMain.stopProgressBar() receiver reached with state " + state);
        progressBar.setVisibility(View.INVISIBLE);
        switch(state){
            case "error":
                if(!AppConfig.getInstance().hasConnectionOnStartup()) { // no internet
                    imageViewNoNetworkLocal.setVisibility(View.VISIBLE);
                } else { //some other error

                }
                break;
        }
    }

    @Override
    public void afterPermissionGranted() {
        // permission granted so get data
        Log.d("DENNIS_B", "FragmentMain.afterPermissionGranted() receiver reached");
        setupListenersAndInitData();
    }

    @Override
    public void networkStateChanged(String state) {
        Log.d("DENNIS_B", "FragmentMain.networkStateChanged(state) receiver reached with " + state);
        switch(state){
            case "NETWORK_CONNECTION_LOST":
                break;
            case "NETWORK_CONNECTION_AVAILABLE":
                AppConfig.getInstance().setConnectionOnStartup(true);
                setupListenersAndInitData();
                break;
        }
    }
}