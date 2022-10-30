package com.dennis_brink.android.myweatherapp;

import android.util.Log;
import android.widget.ImageView;

import com.dennis_brink.android.myweatherapp.model_airquality.OpenWeatherAirQuality;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitLibrary {

    public static void getPollutionData(String type, ArrayList<ImageView>imgList){

        Log.d("DENNIS_B", "RetrofitLibrary.getPollutionData() type: " + type);

        switch(type){
            case "city_search":
                getPollutionData(AppConfig.getInstance().getSlat(), AppConfig.getInstance().getSlon(), imgList);
                break;
            case "local":
                getPollutionData(AppConfig.getInstance().getLat(), AppConfig.getInstance().getLon(), imgList);
                break;
        }

    }

    private static void getPollutionData(double lat, double lon, ArrayList<ImageView>imgList){

        WeatherApi weatherApi = RetrofitWeather.getClient().create(WeatherApi.class);

        Log.d("DENNIS_B", "RetrofitLibrary.getPollutionData() lat/lon: " + lat + " " + lon);

        Call<OpenWeatherAirQuality> call = weatherApi.getWeatherAirQuality(lat, lon, AppConfig.getInstance().getApi_key());
        call.enqueue(new Callback<OpenWeatherAirQuality>() {
            @Override
            public void onResponse(Call<OpenWeatherAirQuality> call, Response<OpenWeatherAirQuality> response) {

                try {
                    Log.d("DENNIS_B", "RetrofitLibrary.getPollutionData() " + response.toString());
                    Log.d("DENNIS_B", "RetrofitLibrary.getPollutionData() score : " + response.body().getList().get(0).getMain().getAqi());
                    /* 1 = 5 stars
                       2 = 4 stars
                       3 = 3 stars
                       4 = 2 stars
                       5 = 1 stars
                       Air Quality Index. Possible values: 1, 2, 3, 4, 5. Where 1 = Good, 2 = Fair, 3 = Moderate, 4 = Poor, 5 = Very Poor.
                     */
                    int t=0;
                    for(int i = imgList.size();i!=0;i--){
                        if(i >= response.body().getList().get(0).getMain().getAqi()) {
                            imgList.get(t).setImageResource(R.drawable.star_yellow);
                        } else {
                            imgList.get(t).setImageResource(R.drawable.star_grey);
                        }
                        t++;
                    }
                }catch(Exception e){
                    Log.d("DENNIS_B", "RetrofitLibrary.getPollutionData() Error loading air quality data: " + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(Call<OpenWeatherAirQuality> call, Throwable t) {
                Log.d("DENNIS_B", "RetrofitLibrary.getPollutionData() Retrofit did not return any pollution data");
            }
        });

    }

}
