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
import com.dennis_brink.android.myweatherapp.MainActivity;
import com.dennis_brink.android.myweatherapp.R;
import com.dennis_brink.android.myweatherapp.Receiver;
import com.dennis_brink.android.myweatherapp.RetrofitLibrary;

public class FragmentForecast extends Fragment implements ILocationListener, IWeatherListener, INetworkStateListener {

    RecyclerView rvForecast;
    Receiver receiver = null;
    private ImageView imageViewNoNetworkForecast;
    private ProgressBar progressBarForecast;
    private boolean lConnectionLost = false;
    private TextView txtApiTimeStamp;

    //LocationLibrary locationLibrary = new LocationLibrary();;

    public static FragmentForecast newInstance() {
        return new FragmentForecast();
    }

    @Override
    public void onResume() { // load data here for continued fresh data
        super.onResume();
        /*
        Log.d("DENNIS_B", "FragmetForecast.onResume(): Connection available " + AppConfig.getInstance().hasConnectionOnStartup());
        if(AppConfig.getInstance().hasConnectionOnStartup() && !lConnectionLost) {
            progressBarForecast.setVisibility(View.VISIBLE);
            RetrofitLibrary.getWeatherForecastDataByDay(rvForecast, getActivity());
        } else {
            try {
                if (rvForecast!=null && rvForecast.getAdapter().getItemCount() == 0) { // empty
                    imageViewNoNetworkForecast.setVisibility(View.VISIBLE);
                }
            }catch(Exception e){
                // no recyclerview is shown yet
                imageViewNoNetworkForecast.setVisibility(View.VISIBLE);
            }
        }
        */
    }

    @Override
    public void onStart() {

        super.onStart();

        if(receiver == null){
            Log.d("DENNIS_B", "FragmentForecast.onStart(): registering receiver and filters");
            receiver = new Receiver();
            receiver.setWeatherListener(this);
            receiver.setLocationListener(this);
            receiver.setNetworkStateListener(this);
        }

        getActivity().registerReceiver(receiver, getFilter());

        //locationLibrary.setupLocationListener(Application.getContext());
        //locationLibrary.getCurrentLocation(Application.getContext());
        ((MainActivity) getActivity()).getCurrentLocation();
        Log.d("DENNIS_B", "FragmentForecast.onStart(): done");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forecast, container, false);

        imageViewNoNetworkForecast = view.findViewById(R.id.imageViewNoNetworkForecast);
        progressBarForecast = view.findViewById(R.id.progressBarForecast);
        txtApiTimeStamp = view.findViewById(R.id.textViewApi20);

        rvForecast = view.findViewById(R.id.rvDays);
        rvForecast.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        return view;

    }

    private IntentFilter getFilter(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("LOCAL_WEATHER_DATA_ERROR"); // only register receiver for this event
        intentFilter.addAction("CALL_COMPLETE");
        intentFilter.addAction("LOCATION_CHANGED");
        intentFilter.addAction("NETWORK_CONNECTION_LOST");
        intentFilter.addAction("NETWORK_CONNECTION_AVAILABLE");
        return intentFilter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null){
            Log.d("DENNIS_B", "FragmentForecast.onDestroy(): unregistering receiver");
            getActivity().unregisterReceiver(receiver);
            receiver = null;
        }
    }

    @Override
    public void networkStateChanged(String state) {
        Log.d("DENNIS_B", "FragmentForecast.networkStateChanged(state) receiver reached with " + state);
        switch(state){
            case "NETWORK_CONNECTION_LOST":
                lConnectionLost = true;
                break;
            case "NETWORK_CONNECTION_AVAILABLE":
                AppConfig.getInstance().setConnectionOnStartup(true);
                imageViewNoNetworkForecast.setVisibility(View.INVISIBLE);
                progressBarForecast.setVisibility(View.VISIBLE);
                //RetrofitLibrary.getWeatherForecastDataByDay(rvForecast, getActivity(), 0, 0);
                //locationLibrary.getCurrentLocation(Application.getContext());
                ((MainActivity) getActivity()).getCurrentLocation();
                break;
        }
    }

    @Override
    public void showErrorMessage(String text, String type) {
        Log.d("DENNIS_B", "FragmentForecast.showErrorMessage() receiver reached for type: " + type);
        if(type.equals("fcast")) {  // listeners are the same in all fragments type makes sure
            // the correct dialog is shown, and only once (bit tricky)
            ApplicationLibrary.getErrorAlertDialog(text, getActivity()).show();
        }
    }
/*
    @Override
    public void stopProgressBar(String type) {
        Log.d("DENNIS_B", "FragmentForecast.stopProgressBar() receiver reached with type: " + type);

        progressBarForecast.setVisibility(View.INVISIBLE);
        rvForecast.setVisibility(View.VISIBLE);
        txtApiTimeStamp.setText(ApplicationLibrary.getTodayDateTime());

    }
*/
    @Override
    public void callComplete(String type) {
        Log.d("DENNIS_B", "FragmentForecast.callComplete() receiver reached with type: " + type);

        progressBarForecast.setVisibility(View.INVISIBLE);
        rvForecast.setVisibility(View.VISIBLE);
        txtApiTimeStamp.setText(ApplicationLibrary.getTodayDateTime());

    }

    @Override
    public void locationChanged(double lat, double lon) {
        Log.d("DENNIS_B", "FragmentForecast.locationChanged receiver reached --> getWeatherForecastDataByDay()");
        progressBarForecast.setVisibility(View.VISIBLE);
        RetrofitLibrary.getWeatherForecastDataByDay(rvForecast, getActivity(), lat, lon);
    }
}