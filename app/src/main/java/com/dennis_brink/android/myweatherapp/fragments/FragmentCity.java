package com.dennis_brink.android.myweatherapp.fragments;

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
import com.dennis_brink.android.myweatherapp.R;
import com.dennis_brink.android.myweatherapp.RetrofitLibrary;
import com.dennis_brink.android.myweatherapp.RetrofitWeather;
import com.dennis_brink.android.myweatherapp.WeatherApi;
import com.dennis_brink.android.myweatherapp.model_weather.OpenWeatherMap;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentCity extends Fragment {

    private TextView textHumidityCity, textMaxTempCity, textMinTempCity, textPressureCity,
            textWindCity, textCityCity, textTempCity, textConditionCity;
    private EditText editTextSearch;
    private ImageView imageViewSearch, imageViewIconCity, imgStar1, imgStar2, imgStar3, imgStar4, imgStar5;

    private ArrayList<ImageView> imgList = new ArrayList<>();

    public static FragmentCity newInstance() {
        return new FragmentCity();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("DENNIS_B", "FragmentCity onStart()");
        getWeatherData(AppConfig.getInstance().getLocation());
        Log.d("DENNIS_B", "FragmentCity onStart() completed");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_city, container, false);

        // do stuff here like in an Activity's onCreate
        textCityCity = view.findViewById(R.id.textViewCityCity);
        textConditionCity = view.findViewById(R.id.textViewWeaterConditionCity);
        textHumidityCity = view.findViewById(R.id.textViewHumidityCity);
        textWindCity = view.findViewById(R.id.textViewWindCity);
        textMaxTempCity = view.findViewById(R.id.textViewMaxTempCity);
        textMinTempCity = view.findViewById(R.id.textViewMinTempCity);
        textPressureCity = view.findViewById(R.id.textViewPressureCity);
        textTempCity = view.findViewById(R.id.textViewTemperatureCity);

        editTextSearch = view.findViewById(R.id.editTextSearch);
        imageViewSearch = view.findViewById(R.id.imageViewIconCitySearch);
        imageViewIconCity = view.findViewById(R.id.imageViewIconCity);

        imgStar1 = view.findViewById(R.id.star1city);
        imgStar2 = view.findViewById(R.id.star2city);
        imgStar3 = view.findViewById(R.id.star3city);
        imgStar4 = view.findViewById(R.id.star4city);
        imgStar5 = view.findViewById(R.id.star5city);

        setupImageViewList();

        imageViewSearch.setOnClickListener(view1 -> {
            if(!(editTextSearch.getText().toString().isEmpty() || editTextSearch.getText().toString() == "City")){
                getWeatherData(editTextSearch.getText().toString());
            }
            editTextSearch.setText("");
        });

        return view;
    }

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

    private void setupImageViewList(){
        imgList.add(imgStar1);
        imgList.add(imgStar2);
        imgList.add(imgStar3);
        imgList.add(imgStar4);
        imgList.add(imgStar5);
    }

}