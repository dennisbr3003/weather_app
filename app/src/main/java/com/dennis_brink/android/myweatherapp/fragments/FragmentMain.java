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
import com.dennis_brink.android.myweatherapp.ApplicationLibrary;
import com.dennis_brink.android.myweatherapp.ILocationListener;
import com.dennis_brink.android.myweatherapp.INetworkStateListener;
import com.dennis_brink.android.myweatherapp.IWeatherListener;
import com.dennis_brink.android.myweatherapp.R;
import com.dennis_brink.android.myweatherapp.Receiver;
import com.dennis_brink.android.myweatherapp.RetrofitLibrary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentMain extends Fragment implements IWeatherListener, INetworkStateListener, ILocationListener {

    Receiver receiver = null;
    RecyclerView rvForecastHour;
    TextView txtBusy;

    private ImageView imageViewIcon, imageViewHumidityLocal, imageViewNoNetworkLocal;
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

        if(receiver == null){
            Log.d("DENNIS_B", "FragmentMain.onStart(): registering receiver and filters");
            receiver = new Receiver();
            receiver.setWeatherListener(this);
            receiver.setNetworkStateListener(this);
            receiver.setLocationListener(this);
        }
        getActivity().registerReceiver(receiver, getFilter());

        Log.d("DENNIS_B", "FragmentMain.onStart(): done");
    }

    @Override
    public void onResume() {
        super.onResume();
        getWeatherData(); // try to load on startup (when using last known location or current coarse location)
    }

    private IntentFilter getFilter(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("LOCAL_WEATHER_DATA_ERROR"); // only register receiver for this event
        intentFilter.addAction("STOP_PROGRESS_BAR");
        intentFilter.addAction("NETWORK_CONNECTION_LOST");
        intentFilter.addAction("NETWORK_CONNECTION_AVAILABLE");
        intentFilter.addAction("LOCATION_CHANGED");
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

    private void getWeatherData(){

        Log.d("DENNIS_B", "FragmentMain.getWeatherData() receiver reached ");
        if(AppConfig.getInstance().hasConnectionOnStartup()) {
            // network available
            imageViewNoNetworkLocal.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            if(AppConfig.getInstance().getLatitude() == 0 && AppConfig.getInstance().getLongitude() == 0){
                // we do not have a location yet; inform the user
                txtBusy.setVisibility(View.VISIBLE);
            }
            else {
                txtBusy.setVisibility(View.INVISIBLE);
                RetrofitLibrary.getWeatherDataLocal(AppConfig.getInstance().getLatitude(), AppConfig.getInstance().getLongitude(), rating, weatherData, imageViewIcon, getContext());
                RetrofitLibrary.getWeatherForecastData(AppConfig.getInstance().getLatitude(), AppConfig.getInstance().getLongitude(), rvForecastHour, getContext());
            }
        } else {
            // no network <-- dit zal niet helemaal goed zijn vermoed ik, na laden moet er niets meer gebeuren dus ook niet dit
            imageViewNoNetworkLocal.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
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

    @Override
    public void networkStateChanged(String state) {
        Log.d("DENNIS_B", "FragmentMain.networkStateChanged(state) receiver reached with " + state);
        switch(state){
            case "NETWORK_CONNECTION_LOST":
                break;
            case "NETWORK_CONNECTION_AVAILABLE":
                AppConfig.getInstance().setConnectionOnStartup(true);
                //setupListenersAndInitData();
                break;
        }
    }

    @Override
    public void locationChanged() {
        Log.d("DENNIS_B", "FragmentMain.locationChanged receiver reached --> get weatherdata");
        getWeatherData();
    }


}