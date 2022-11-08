package com.dennis_brink.android.myweatherapp.fragments;

import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dennis_brink.android.myweatherapp.AppConfig;
import com.dennis_brink.android.myweatherapp.ApplicationLibrary;
import com.dennis_brink.android.myweatherapp.INetworkStateListener;
import com.dennis_brink.android.myweatherapp.IWeatherListener;
import com.dennis_brink.android.myweatherapp.R;
import com.dennis_brink.android.myweatherapp.Receiver;
import com.dennis_brink.android.myweatherapp.RetrofitLibrary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentCity extends Fragment implements IWeatherListener, INetworkStateListener {

    private ImageView imageViewIconCity, imageViewHumidity, imageViewSearch;
    private EditText editTextSearch;
    private View viewIbeam;
    Receiver receiver = null;

    private ArrayList<ImageView> rating = new ArrayList<>();
    private Map<String, TextView> weatherData = new HashMap<>();
    private Map<String, TextView> weatherLabels = new HashMap<>();

    public static FragmentCity newInstance() {
        return new FragmentCity();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("DENNIS_B", "FragmentCity onStart()");
        if(receiver == null){
            Log.d("DENNIS_B", "FragmentCity.onResume(): registering receiver for weather and network state listeners");
            receiver = new Receiver();
            receiver.setWeatherListener(this);
            receiver.setNetworkStateListener(this);
        }
        getActivity().registerReceiver(receiver, getFilter());

        if(!AppConfig.getInstance().hasConnectionOnStartup()){
            disableSearchField();
        }

    }

    private IntentFilter getFilter(){
        IntentFilter intentFilter = new IntentFilter();
        Log.d("DENNIS_B", "FragmentCity.getFilter(): Registering for broadcast action LOCAL_WEATHER_DATA_ERROR, STOP_PROGRESS_BAR");
        Log.d("DENNIS_B", "FragmentCity.getFilter(): Registering for broadcast action NETWORK_CONNECTION_LOST, NETWORK_CONNECTION_AVAILABLE");
        intentFilter.addAction("LOCAL_WEATHER_DATA_ERROR"); // only register receiver for this event
        intentFilter.addAction("STOP_PROGRESS_BAR");
        intentFilter.addAction("CALL_COMPLETE");
        intentFilter.addAction("NETWORK_CONNECTION_LOST");
        intentFilter.addAction("NETWORK_CONNECTION_AVAILABLE");
        return intentFilter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null){
            Log.d("DENNIS_B", "FragmentCity.onPause(): unregistering receiver");
            getActivity().unregisterReceiver(receiver);
            receiver = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_city, container, false);

        imageViewIconCity = view.findViewById(R.id.imageViewIconCity);
        imageViewHumidity = view.findViewById(R.id.imageViewHumidityCity);
        imageViewSearch = view.findViewById(R.id.imageViewIconCitySearch);
        editTextSearch = view.findViewById(R.id.editTextSearch);
        viewIbeam = view.findViewById(R.id.viewIBeam0);

        setupWeatherData(view);
        setupRating(view);
        setupWeatherLabels(view);
        ApplicationLibrary.hideRating(rating);
        setupSearchText(view);

        return view;
    }

    private void setupWeatherData(View view){

        TextView textHumidityCity, textMaxTempCity, textMinTempCity, textPressureCity,
                 textWindCity, textCityCity, textTempCity, textConditionCity, textViewApi12;

        textCityCity = view.findViewById(R.id.textViewCityCity);
        textConditionCity = view.findViewById(R.id.textViewWeaterConditionCity);
        textHumidityCity = view.findViewById(R.id.textViewHumidityCity);
        textWindCity = view.findViewById(R.id.textViewWindCity);
        textMaxTempCity = view.findViewById(R.id.textViewMaxTempCity);
        textMinTempCity = view.findViewById(R.id.textViewMinTempCity);
        textPressureCity = view.findViewById(R.id.textViewPressureCity);
        textTempCity = view.findViewById(R.id.textViewTemperatureCity);
        textViewApi12 = view.findViewById(R.id.textViewApi12);

        textCityCity.setText("");
        textConditionCity.setText("");
        textHumidityCity.setText("");
        textWindCity.setText("");
        textMaxTempCity.setText("");
        textMinTempCity.setText("");
        textPressureCity.setText("");
        textTempCity.setText("");
        textViewApi12.setText("none");

        weatherData.put("city", textCityCity);
        weatherData.put("condition", textConditionCity);
        weatherData.put("humidity", textHumidityCity);
        weatherData.put("wind", textWindCity);
        weatherData.put("pressure", textPressureCity);
        weatherData.put("temp", textTempCity);

        ApplicationLibrary.setColorTextView(weatherData);

        weatherData.put("maxtemp", textMaxTempCity);
        weatherData.put("mintemp", textMinTempCity);
        weatherData.put("timestamp", textViewApi12);

    }

    private void setupWeatherLabels(View view){

        TextView textLabelHumidityCity,
                 textLabelPressureCity, textLabelWindCity, textLabelAirQualityCity;

        textLabelHumidityCity = view.findViewById(R.id.textViewLabelHumidityCity);
        textLabelWindCity = view.findViewById(R.id.textViewLabelWindCity);
        textLabelPressureCity = view.findViewById(R.id.textViewLabelPressureCity);
        textLabelAirQualityCity = view.findViewById(R.id.textViewLabelAirqualityCity);

        weatherLabels.put("humidity", textLabelHumidityCity);
        weatherLabels.put("wind", textLabelWindCity);
        weatherLabels.put("pressure", textLabelPressureCity);
        weatherLabels.put("airquality", textLabelAirQualityCity);

        ApplicationLibrary.setColorTextView(weatherLabels);

    }

    private void setupSearchText(View view){

        imageViewSearch.setTag(R.drawable.ic_baseline_search_24);

        imageViewSearch.setOnClickListener(view1 -> {
            if(!(editTextSearch.getText().toString().isEmpty() || editTextSearch.getText().toString().equals("City"))){
                RetrofitLibrary.getWeatherDataCity(editTextSearch.getText().toString(), rating, weatherData, imageViewIconCity, getContext());
                // if all went well we should end up in "stopProgressBar()". The screen will be set up from there

            }
            editTextSearch.setText("");
        });

        ApplicationLibrary.setColorView(editTextSearch);
        ApplicationLibrary.setColorDrawableBackgroundStroke(editTextSearch);

    }

    private void setupRating(View view){

        ImageView imgStar1, imgStar2, imgStar3, imgStar4, imgStar5;

        imgStar1 = view.findViewById(R.id.star1City);
        imgStar2 = view.findViewById(R.id.star2City);
        imgStar3 = view.findViewById(R.id.star3City);
        imgStar4 = view.findViewById(R.id.star4City);
        imgStar5 = view.findViewById(R.id.star5City);

        rating.add(imgStar1);
        rating.add(imgStar2);
        rating.add(imgStar3);
        rating.add(imgStar4);
        rating.add(imgStar5);

    }

    @Override
    public void showErrorMessage(String text, String type) {
        Log.d("DENNIS_B", "FragmentCity.showErrorMessage() receiver reached for type: " + type);
        // if a data error occurs do nothing. Just show there is no data found.
        if(type.equals("city")) {  // listeners are the same in all fragments type makes sure
                                   // the correct dialog is shown, and only once (bit tricky)
            ApplicationLibrary.getErrorAlertDialog(text, getActivity()).show();
        }

    }

    @Override
    public void stopProgressBar(String type) {
        Log.d("DENNIS_B", "FragmentCity.stopProgressBar() receiver reached for type " + type);
    }

    @Override
    public void callComplete(String type) {
        Log.d("DENNIS_B", "FragmentCity.callComplete(type) receiver reached with " + type);
        if(type.equals("city")){
            ApplicationLibrary.showTextViews(weatherLabels);
            ApplicationLibrary.showRating(rating);
            imageViewHumidity.setVisibility(View.VISIBLE);
            viewIbeam.setVisibility(View.VISIBLE);
        }
    }

    private void disableSearchField(){
        imageViewIconCity.setClickable(false);
        editTextSearch.setEnabled(false);
        ApplicationLibrary.setColorDrawable(imageViewSearch, R.color.Gray);
        editTextSearch.setHint("You appear to be offline...");
    }

    private void enableSearchField(){
        imageViewIconCity.setClickable(true);
        ApplicationLibrary.setColorDrawable(imageViewSearch, R.color.searchOrange);
        editTextSearch.setEnabled(true);
        editTextSearch.setHint("Enter city name here...");
    }

    @Override
    public void networkStateChanged(String state) {
        Log.d("DENNIS_B", "FragmentCity.networkStateChanged(state) receiver reached with " + state);
        switch(state){
            case "NETWORK_CONNECTION_LOST":
                disableSearchField();
                break;
            case "NETWORK_CONNECTION_AVAILABLE":
                AppConfig.getInstance().setConnectionOnStartup(true);
                enableSearchField();
                break;
        }
    }
}