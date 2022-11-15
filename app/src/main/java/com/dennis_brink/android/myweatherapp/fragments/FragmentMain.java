package com.dennis_brink.android.myweatherapp.fragments;

import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dennis_brink.android.myweatherapp.AppConfig;
import com.dennis_brink.android.myweatherapp.Application;
import com.dennis_brink.android.myweatherapp.ApplicationLibrary;
import com.dennis_brink.android.myweatherapp.ILocationListener;
import com.dennis_brink.android.myweatherapp.INetworkStateListener;
import com.dennis_brink.android.myweatherapp.IWeatherListener;
import com.dennis_brink.android.myweatherapp.LocationLibrary;
import com.dennis_brink.android.myweatherapp.MainActivity;
import com.dennis_brink.android.myweatherapp.R;
import com.dennis_brink.android.myweatherapp.Receiver;
import com.dennis_brink.android.myweatherapp.RetrofitLibrary;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentMain extends Fragment implements IWeatherListener, INetworkStateListener, ILocationListener {

    Receiver receiver = null;
    RecyclerView rvForecastHour;
    TextView txtBusy;

    private ImageView imageViewIcon, imageViewHumidityLocal, imageViewNoNetworkLocal,imageViewSunRise,imageViewSunSet;
    private ProgressBar progressBar;
    private View viewIbeam;

    private ArrayList<ImageView> rating = new ArrayList<>();
    private Map<String, TextView> weatherData = new HashMap<>();
    private Map<String, TextView> weatherLabels = new HashMap<>();

    public static FragmentMain newInstance() {
        return new FragmentMain();
    }

    @Override
    public void onStart() {

        super.onStart();

        try {
            if (receiver == null) {
                Log.d("DENNIS_B", "FragmentMain.onStart(): registering receiver and filters");
                receiver = new Receiver();
                receiver.setWeatherListener(this);
                receiver.setNetworkStateListener(this);
                receiver.setLocationListener(this);
            }
            getActivity().registerReceiver(receiver, getFilter());
            if(((MainActivity) getActivity()).isNetworkAvailable()) { // this will take 10 sec. without network.
                                                                      // Only execute on startup with network
                ((MainActivity) getActivity()).getCurrentLocation();
            } else {
                ApplicationLibrary.setDrawableBackground(imageViewNoNetworkLocal); // day/night
                imageViewNoNetworkLocal.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }

        } catch (Exception e){
            Log.d("DENNIS_B", "FragmentMain.onStart() Exception: "+ e.getLocalizedMessage());
        }

    }

    private IntentFilter getFilter(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("LOCAL_WEATHER_DATA_ERROR");
        intentFilter.addAction("NETWORK_CONNECTION_LOST");
        intentFilter.addAction("NETWORK_CONNECTION_AVAILABLE");
        intentFilter.addAction("LOCATION_CHANGED");
        intentFilter.addAction("CALL_COMPLETE");
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // do stuff here like in an Activity's onCreate
        progressBar = view.findViewById(R.id.progressBar);
        txtBusy = view.findViewById(R.id.textViewBusyGetting);
        imageViewIcon = view.findViewById(R.id.imageViewIcon);
        imageViewHumidityLocal = view.findViewById(R.id.imageViewHumidityLocal);
        imageViewNoNetworkLocal = view.findViewById(R.id.imageViewNoNetworkLocal);
        imageViewSunRise = view.findViewById(R.id.imageViewSunRise);
        imageViewSunSet = view.findViewById(R.id.imageViewSunSet);
        viewIbeam = view.findViewById(R.id.viewIBeam);

        rvForecastHour = view.findViewById(R.id.rvHours);
        rvForecastHour.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        setupWeatherData(view);
        setupRating(view);
        setupWeatherLabels(view);
        return view;

    }

    private void setupWeatherData(View view){

        TextView textHumidity, textMaxTemp, textMinTemp, textPressure,
                 textWind, textCity, textTemp, textCondition, textApi,
                 textSunRise, textSunSet;

        textCity = view.findViewById(R.id.textViewIBeam);
        textCondition = view.findViewById(R.id.textViewWeaterCondition);
        textHumidity = view.findViewById(R.id.textViewHumidity);
        textWind = view.findViewById(R.id.textViewWind);
        textMaxTemp = view.findViewById(R.id.textViewMaxTemp);
        textMinTemp = view.findViewById(R.id.textViewMinTemp);
        textPressure = view.findViewById(R.id.textViewPressure);
        textTemp = view.findViewById(R.id.textViewTemperature);
        textApi = view.findViewById(R.id.textViewApi);
        textSunRise = view.findViewById(R.id.textViewSunRise);
        textSunSet = view.findViewById(R.id.textViewSunSet);

        textCity.setText("");
        textCondition.setText("");
        textHumidity.setText("");
        textWind.setText("");
        textMaxTemp.setText("");
        textMinTemp.setText("");
        textPressure.setText("");
        textTemp.setText("");
        textSunRise.setText("");
        textSunSet.setText("");
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
        weatherData.put("sunset", textSunSet);
        weatherData.put("sunrise", textSunRise);

        ApplicationLibrary.hideTextViews(weatherData);

        weatherData.put("timestamp", textApi);

    }

    private void setupWeatherLabels(View view){

        TextView textLabelHumidity,
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

        ApplicationLibrary.hideTextViews(weatherLabels);

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

        ApplicationLibrary.hideRating(rating);

    }

    private void getWeatherData(double lat, double lon){

        Log.d("DENNIS_B", "FragmentMain.getWeatherData()");
        if(((MainActivity) getActivity()).isNetworkAvailable()) {
            // network available
            imageViewNoNetworkLocal.setVisibility(View.INVISIBLE);
            //progressBar.setVisibility(View.VISIBLE);
            if(lat == 0 && lon == 0){
                // we do not have a location yet; inform the user
                Log.d("DENNIS_B", "ok3");
                txtBusy.setVisibility(View.VISIBLE);
            }
            else {
                txtBusy.setVisibility(View.INVISIBLE);
                RetrofitLibrary.getWeatherDataLocal(lat, lon, rating, weatherData, imageViewIcon, getContext());
                RetrofitLibrary.getWeatherForecastData(lat, lon, rvForecastHour, getContext());
            }
        } else {
            // no network <-- dit zal niet helemaal goed zijn vermoed ik, na laden moet er niets meer gebeuren dus ook niet dit
            ApplicationLibrary.setDrawableBackground(imageViewNoNetworkLocal); // day/night
            imageViewNoNetworkLocal.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            txtBusy.setVisibility(View.INVISIBLE);
        }

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
    public void callComplete(String type) {

        Log.d("DENNIS_B", "FragmentMain.callComplete() receiver reached with type: " + type);
        if (type.equals("main")) {
            progressBar.setVisibility(View.INVISIBLE);
            ApplicationLibrary.showTextViews(weatherData);
            ApplicationLibrary.showTextViews(weatherLabels);
            ApplicationLibrary.showRating(rating);
            imageViewHumidityLocal.setVisibility(View.VISIBLE);
            imageViewSunRise.setVisibility(View.VISIBLE);
            imageViewSunSet.setVisibility(View.VISIBLE);
            ApplicationLibrary.setDrawableBackground(imageViewIcon);
            ApplicationLibrary.setColorIbeam(viewIbeam);
            viewIbeam.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void networkStateChanged(String state) {
        Log.d("DENNIS_B", "FragmentMain.networkStateChanged(state) receiver reached with " + state);
        switch(state){
            case "NETWORK_CONNECTION_LOST":
                break;
            case "NETWORK_CONNECTION_AVAILABLE":
                ((MainActivity) getActivity()).getCurrentLocation();
                break;
        }
    }

    @Override
    public void locationChanged(double lat, double lon) {
        Log.d("DENNIS_B", "FragmentMain.locationChanged receiver reached --> getWeatherData()");
        getWeatherData(lat, lon);
    }


}