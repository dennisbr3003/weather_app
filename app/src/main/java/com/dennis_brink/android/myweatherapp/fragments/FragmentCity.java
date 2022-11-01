package com.dennis_brink.android.myweatherapp.fragments;

import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dennis_brink.android.myweatherapp.AppConfig;
import com.dennis_brink.android.myweatherapp.IWeatherListener;
import com.dennis_brink.android.myweatherapp.R;
import com.dennis_brink.android.myweatherapp.Receiver;
import com.dennis_brink.android.myweatherapp.RetrofitLibrary;
import com.dennis_brink.android.myweatherapp.RetrofitWeather;
import com.dennis_brink.android.myweatherapp.WeatherApi;
import com.dennis_brink.android.myweatherapp.model_weather.OpenWeatherMap;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentCity extends Fragment implements IWeatherListener {

    private EditText editTextSearch;
    private ImageView imageViewSearch, imageViewIconCity, imgStar1, imgStar2, imgStar3, imgStar4, imgStar5;

    private ArrayList<ImageView> imgList = new ArrayList<>();
    private ArrayList<ImageView> rating = new ArrayList<>();
    private Map<String, TextView> weatherData = new HashMap<>();

    Receiver receiver = null;

    public static FragmentCity newInstance() {
        return new FragmentCity();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("DENNIS_B", "FragmentCity onStart()");
        RetrofitLibrary.getWeatherDataCity("city", editTextSearch.getText().toString(), rating, weatherData, imageViewIconCity, getContext());
        Log.d("DENNIS_B", "FragmentCity onStart() completed");

    }

    private IntentFilter getFilter(){
        IntentFilter intentFilter = new IntentFilter();
        Log.d("DENNIS_B", "FragmentCity.getFilter(): Registering for broadcast action LOCAL_WEATHER_DATA_ERROR and STOP_PROGRESS_BAR");
        intentFilter.addAction("WEATHER_DATA_ERROR"); // only register receiver for this event
        intentFilter.addAction("STOP_PROGRESS_BAR");
        return intentFilter;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(receiver == null){
            Log.d("DENNIS_B", "FragmentCity.onResume(): registering receiver");
            receiver = new Receiver();
            receiver.setWeatherListener(this);
        }
        getActivity().registerReceiver(receiver, getFilter());
    }

    @Override
    public void onPause() {
        super.onPause();
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

        // do stuff here like in an Activity's onCreate

        editTextSearch = view.findViewById(R.id.editTextSearch);
        imageViewSearch = view.findViewById(R.id.imageViewIconCitySearch);
        imageViewIconCity = view.findViewById(R.id.imageViewIconCity);

        setupWeatherData(view);
        setupRating(view);

        imageViewSearch.setOnClickListener(view1 -> {
            if(!(editTextSearch.getText().toString().isEmpty() || editTextSearch.getText().toString() == "City")){
                RetrofitLibrary.getWeatherDataCity("city", editTextSearch.getText().toString(), rating, weatherData, imageViewIconCity, getContext());

            }
            editTextSearch.setText("");
        });

        return view;
    }

    private void setupWeatherData(View view){

        TextView textHumidityCity, textMaxTempCity, textMinTempCity, textPressureCity,
                 textWindCity, textCityCity, textTempCity, textConditionCity;

        textCityCity = view.findViewById(R.id.textViewCityCity);
        textConditionCity = view.findViewById(R.id.textViewWeaterConditionCity);
        textHumidityCity = view.findViewById(R.id.textViewHumidityCity);
        textWindCity = view.findViewById(R.id.textViewWindCity);
        textMaxTempCity = view.findViewById(R.id.textViewMaxTempCity);
        textMinTempCity = view.findViewById(R.id.textViewMinTempCity);
        textPressureCity = view.findViewById(R.id.textViewPressureCity);
        textTempCity = view.findViewById(R.id.textViewTemperatureCity);

        textCityCity.setText("");
        textConditionCity.setText("");
        textHumidityCity.setText("");
        textWindCity.setText("");
        textMaxTempCity.setText("");
        textMinTempCity.setText("");
        textPressureCity.setText("");
        textTempCity.setText("");

        weatherData.put("city", textCityCity);
        weatherData.put("condition", textConditionCity);
        weatherData.put("humidity", textHumidityCity);
        weatherData.put("wind", textWindCity);
        weatherData.put("maxtemp", textMaxTempCity);
        weatherData.put("mintemp", textMinTempCity);
        weatherData.put("pressure", textPressureCity);
        weatherData.put("temp", textTempCity);

    }

    private void setupRating(View view){

        ImageView imgStar1, imgStar2, imgStar3, imgStar4, imgStar5;

        imgStar1 = view.findViewById(R.id.star1city);
        imgStar2 = view.findViewById(R.id.star2city);
        imgStar3 = view.findViewById(R.id.star3city);
        imgStar4 = view.findViewById(R.id.star4city);
        imgStar5 = view.findViewById(R.id.star5city);

        rating.add(imgStar1);
        rating.add(imgStar2);
        rating.add(imgStar3);
        rating.add(imgStar4);
        rating.add(imgStar5);

    }

    /*
    private void getWeatherData(String city){

        WeatherApi weatherApi = RetrofitWeather.getClient().create(WeatherApi.class);

        Log.d("DENNIS_B", AppConfig.getInstance().getLocation());

        Call<OpenWeatherMap> call = weatherApi.getWeatherWithCityName(city, AppConfig.getInstance().getApi_key());
        call.enqueue(new Callback<OpenWeatherMap>() {
            @Override
            public void onResponse(Call<OpenWeatherMap> call, Response<OpenWeatherMap> response) {

                if(response.isSuccessful()) {
                    try {
                        Log.d("DENNIS_B", response.toString());
                        Log.d("DENNIS_B", response.body().toString());
                        textCityCity.setText(response.body().getName() + ", " + response.body().getSys().getCountry());
                        textTempCity.setText(response.body().getMain().getTemp() + " °C");
                        textConditionCity.setText(response.body().getWeather().get(0).getDescription());
                        textHumidityCity.setText(response.body().getMain().getHumidity() + "%");
                        textMaxTempCity.setText(response.body().getMain().getTempMax() + " °C");
                        textMinTempCity.setText(response.body().getMain().getTempMin() + " °C");
                        textPressureCity.setText("" + response.body().getMain().getPressure());
                        textWindCity.setText("" + response.body().getWind().getSpeed());

                        AppConfig.getInstance().setSlat(response.body().getCoord().getLat());
                        AppConfig.getInstance().setSlon(response.body().getCoord().getLon());
                        AppConfig.getInstance().setSlocation(response.body().getName());

                        Log.d("DENNIS_B", AppConfig.getInstance().toString());

                        RetrofitLibrary.getPollutionData("city_search", imgList, getContext());

                        String iconCode = response.body().getWeather().get(0).getIcon();
                        Log.d("DENNIS_B", "Icon: " + "https://openweathermap.org/img/wn/" + iconCode + "@2x.png");
                        Picasso.get().load("https://openweathermap.org/img/wn/" + iconCode + "@2x.png").placeholder(R.mipmap.image870).into(imageViewIconCity, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });

                    } catch (Exception e) {
                        Log.d("DENNIS_B", "Error loading data: " + e.getLocalizedMessage());
                    }
                } else {
                    Toast.makeText(getActivity(), "City " + city + " could not be found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OpenWeatherMap> call, Throwable t) {
                Log.d("DENNIS_B", "Retrofit did not return any data");
            }
        });
    }
*/
    @Override
    public void showErrorMessage(String text) {

    }

    @Override
    public void stopProgressBar() {
        Log.d("DENNIS_B", "FragmentCity.stopProgressBar() receiver reached");
        weatherData.get("city").setVisibility(View.VISIBLE);
        weatherData.get("temp").setVisibility(View.VISIBLE);
        weatherData.get("condition").setVisibility(View.VISIBLE);
        imageViewIconCity.setVisibility(View.VISIBLE);
    }
}