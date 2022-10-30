package com.dennis_brink.android.myweatherapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dennis_brink.android.myweatherapp.AppConfig;
import com.dennis_brink.android.myweatherapp.ForecastAdapter;
import com.dennis_brink.android.myweatherapp.R;
import com.dennis_brink.android.myweatherapp.RetrofitWeather;
import com.dennis_brink.android.myweatherapp.WeatherApi;
import com.dennis_brink.android.myweatherapp.model_forecast.OpenWeatherForecast;
import com.dennis_brink.android.myweatherapp.model_weather.OpenWeatherMap;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentForecast extends Fragment {

    RecyclerView rvForecast;
    List<com.dennis_brink.android.myweatherapp.model_forecast.List> data;
    ForecastAdapter adapter;
    public static FragmentForecast newInstance() {
        return new FragmentForecast();
    }

    @Override
    public void onStart() {
        super.onStart();
        getWeatherForecastData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forecast, container, false);

        rvForecast = view.findViewById(R.id.rvDays);
        rvForecast.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        return view;

    }

    private void getWeatherForecastData(){

        WeatherApi weatherApi = RetrofitWeather.getClient().create(WeatherApi.class);

        Log.d("DENNIS_B", "getWeatherForecastData() lat/lon: " + AppConfig.getInstance().getLat() + " " + AppConfig.getInstance().getLon());

        Call<OpenWeatherForecast> call = weatherApi.getWeatherForecast(AppConfig.getInstance().getLat(), AppConfig.getInstance().getLon(), "eda35914aa73c5ac26a4d657718b1dbc");
        call.enqueue(new Callback<OpenWeatherForecast>() {
            @Override
            public void onResponse(Call<OpenWeatherForecast> call, Response<OpenWeatherForecast> response) {


                try {
                    Log.d("DENNIS_B", response.toString());
                    Log.d("DENNIS_B", response.body().toString());

                    data = response.body().getList(); // data is declared as List<ModelClass> globally
                    adapter = new ForecastAdapter(data); // create adapter and move data in as parameter
                    rvForecast.setAdapter(adapter);

                }catch(Exception e){
                    Log.d("DENNIS_B", "Error loading weather forecast data: " + e.getLocalizedMessage());
                }

            }

            @Override
            public void onFailure(Call<OpenWeatherForecast> call, Throwable t) {
                Log.d("DENNIS_B", "Retrofit did not return any weather forecast data " + t.getLocalizedMessage());
            }
        });
    }

}