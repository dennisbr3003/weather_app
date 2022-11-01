package com.dennis_brink.android.myweatherapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dennis_brink.android.myweatherapp.model_airquality.OpenWeatherAirQuality;
import com.dennis_brink.android.myweatherapp.model_weather.OpenWeatherMap;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitLibrary {

    public static void getPollutionData(String type, ArrayList<ImageView>rating, Context context){

        Log.d("DENNIS_B", "RetrofitLibrary.getPollutionData() type: " + type);

        switch(type){
            case "city_search":
                getPollutionData(AppConfig.getInstance().getSlat(), AppConfig.getInstance().getSlon(), rating, context);
                break;
            case "local":
                getPollutionData(AppConfig.getInstance().getLat(), AppConfig.getInstance().getLon(), rating, context);
                break;
        }

    }

    private static void getPollutionData(double lat, double lon, ArrayList<ImageView>rating, Context context){

        WeatherApi weatherApi = RetrofitWeather.getClient().create(WeatherApi.class);

        Log.d("DENNIS_B", "RetrofitLibrary.getPollutionData() lat/lon: " + lat + " " + lon);

        Call<OpenWeatherAirQuality> call = weatherApi.getWeatherAirQuality(lat, lon, AppConfig.getInstance().getApi_key());
        call.enqueue(new Callback<OpenWeatherAirQuality>() {
            @Override
            public void onResponse(Call<OpenWeatherAirQuality> call, Response<OpenWeatherAirQuality> response) {

                try {
                    Log.d("DENNIS_B", "RetrofitLibrary.getPollutionData() " + response);
                    Log.d("DENNIS_B", "RetrofitLibrary.getPollutionData() score : " + response.body().getList().get(0).getMain().getAqi());
                    /* 1 = 5 stars
                       2 = 4 stars
                       3 = 3 stars
                       4 = 2 stars
                       5 = 1 stars
                       Air Quality Index. Possible values: 1, 2, 3, 4, 5. Where 1 = Good, 2 = Fair, 3 = Moderate, 4 = Poor, 5 = Very Poor.
                     */
                    int t=0;
                    for(int i = rating.size();i!=0;i--){
                        if(i >= response.body().getList().get(0).getMain().getAqi()) {
                            rating.get(t).setImageResource(R.drawable.star_yellow);
                        } else {
                            rating.get(t).setImageResource(R.drawable.star_grey);
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

    public static void getWeatherDataLocal(double lat, double lon, ArrayList<ImageView> rating, Map<String, TextView> weatherData, ImageView icon, Context context){

        WeatherApi weatherApi = RetrofitWeather.getClient().create(WeatherApi.class);

        Log.d("DENNIS_B", "RetrofitLibrary.getWeatherDataLocal() lat/lon: " + lat + " " + lon);

        Call<OpenWeatherMap> call = weatherApi.getWeatherWithLocation(lat, lon, AppConfig.getInstance().getApi_key());
        call.enqueue(new Callback<OpenWeatherMap>() {
            @Override
            public void onResponse(Call<OpenWeatherMap> call, Response<OpenWeatherMap> response) {

                try {

                    RetrofitLibrary.getPollutionData(lat, lon, rating, context);

                    Log.d("DENNIS_B", response.toString());
                    Log.d("DENNIS_B", response.body().toString());

                    loadValuesIntoViews(weatherData, response, icon, context);

                }catch(Exception e){
                    Log.d("DENNIS_B", "RetrofitLibrary.getWeatherDataLocal(): error loading weather data: " + e.getLocalizedMessage());
                }

            }

            @Override
            public void onFailure(Call<OpenWeatherMap> call, Throwable t) {
                Log.d("DENNIS_B", "RetrofitLibrary.getWeatherDataLocal(): Retrofit did not return any weather data" + t.getLocalizedMessage());
            }
        });

    }

    public static void getWeatherDataCity(String city, ArrayList<ImageView> rating, Map<String, TextView> weatherData, ImageView icon, Context context){

        WeatherApi weatherApi = RetrofitWeather.getClient().create(WeatherApi.class);

        Log.d("DENNIS_B", "RetrofitLibrary.getWeatherDataCity() city: " + city);

        Call<OpenWeatherMap> call = weatherApi.getWeatherWithCityName(city, AppConfig.getInstance().getApi_key());
        call.enqueue(new Callback<OpenWeatherMap>() {
            @Override
            public void onResponse(Call<OpenWeatherMap> call, Response<OpenWeatherMap> response) {

                try {

                    RetrofitLibrary.getPollutionData(response.body().getCoord().getLat(), response.body().getCoord().getLon(), rating, context);

                    Log.d("DENNIS_B", response.toString());
                    Log.d("DENNIS_B", response.body().toString());

                    loadValuesIntoViews(weatherData, response, icon, context);

                }catch(Exception e){
                    Log.d("DENNIS_B", "RetrofitLibrary.getWeatherDataCity(): error loading weather data: " + e.getLocalizedMessage());
                }

            }

            @Override
            public void onFailure(Call<OpenWeatherMap> call, Throwable t) {
                Log.d("DENNIS_B", "RetrofitLibrary.getWeatherDataCity(): Retrofit did not return any weather data" + t.getLocalizedMessage());
            }
        });

    }

    private static void loadValuesIntoViews(Map<String, TextView> weatherData, Response<OpenWeatherMap> response, ImageView icon, Context context){

        weatherData.get("city").setText(response.body().getName() + ", " + response.body().getSys().getCountry());
        weatherData.get("condition").setText(response.body().getWeather().get(0).getDescription());
        weatherData.get("humidity").setText(response.body().getMain().getHumidity() + "%");
        weatherData.get("maxtemp").setText(response.body().getMain().getTempMax() + " °C");
        weatherData.get("mintemp").setText(response.body().getMain().getTempMin() + " °C");
        weatherData.get("pressure").setText("" + response.body().getMain().getPressure());
        weatherData.get("wind").setText("" + response.body().getWind().getSpeed());
        weatherData.get("temp").setText(response.body().getMain().getTemp() + " °C");

        String iconCode = response.body().getWeather().get(0).getIcon();

        Log.d("DENNIS_B", "RetrofitLibrary.getWeatherDataLocal() icon: " + "https://openweathermap.org/img/wn/" + iconCode + "@2x.png");

        Picasso.get().load("https://openweathermap.org/img/wn/" + iconCode + "@2x.png")
                .into(icon, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d("DENNIS_B", "RetrofitLibrary.getWeatherDataLocal() weather icon loaded: " + "https://openweathermap.org/img/wn/" + iconCode + "@2x.png");
                        //if(type.equals("local") || type.equals("city")) {
                            broadcastProgressBarAlert(context);
                        //}
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d("DENNIS_B", "RetrofitLibrary.getWeatherDataLocal(): error loading weather icon: " + e.getLocalizedMessage());
                        icon.setImageResource(R.mipmap.image870);
                        //if(type.equals("local") || type.equals("city")) {
                            broadcastProgressBarAlert(context);
                        //}
                    }
                });

    }

    private static void broadcastProgressBarAlert(Context context) {

        Log.d("DENNIS_B", String.format("RetrofitLibrary.broadcastProgressBarAlert(): sending 'STOP_PROGRESS_BAR' "));
        Intent i = new Intent();
        i.setAction("STOP_PROGRESS_BAR");
        context.sendBroadcast(i);

    }

}
