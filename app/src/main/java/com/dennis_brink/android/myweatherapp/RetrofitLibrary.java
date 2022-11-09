package com.dennis_brink.android.myweatherapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dennis_brink.android.myweatherapp.model_airquality.OpenWeatherAirQuality;
import com.dennis_brink.android.myweatherapp.model_day.Day;
import com.dennis_brink.android.myweatherapp.model_forecast.List;
import com.dennis_brink.android.myweatherapp.model_forecast.OpenWeatherForecast;
import com.dennis_brink.android.myweatherapp.model_weather.OpenWeatherMap;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitLibrary {

    private static String getPollutionData(double lat, double lon, ArrayList<ImageView>rating, Context context) {

        WeatherApi weatherApi = RetrofitWeather.getClient().create(WeatherApi.class);

        final String[] error = {""};

        Log.d("DENNIS_B", "RetrofitLibrary.getPollutionData() lat/lon: " + lat + " " + lon);
        try {
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
                        int t = 0;
                        for (int i = rating.size(); i != 0; i--) {
                            if (i >= response.body().getList().get(0).getMain().getAqi()) {
                                rating.get(t).setImageResource(R.drawable.star_yellow);
                            } else {
                                rating.get(t).setImageResource(R.drawable.star_grey);
                            }
                            t++;
                        }
                    } catch (Exception e) {
                        Log.d("DENNIS_B", "RetrofitLibrary.getPollutionData() Error loading air quality data: " + e.getLocalizedMessage());
                        error[0] ="RetrofitLibrary.getPollutionData() Error loading air quality data: " + e.getLocalizedMessage();
                    }
                }

                @Override
                public void onFailure(Call<OpenWeatherAirQuality> call, Throwable t) {
                    Log.d("DENNIS_B", "RetrofitLibrary.getPollutionData() Retrofit did not return any pollution data");
                    error[0] ="RetrofitLibrary.getPollutionData() Retrofit did not return any pollution data" + t.getLocalizedMessage();
                }
            });
        }
        finally{
            return error[0];
        }

    }

    public static void getWeatherDataLocal(double lat, double lon, ArrayList<ImageView> rating, Map<String, TextView> weatherData, ImageView icon, Context context){

        WeatherApi weatherApi = RetrofitWeather.getClient().create(WeatherApi.class);

        Log.d("DENNIS_B", "RetrofitLibrary.getWeatherDataLocal() lat/lon: " + lat + " " + lon);

        Call<OpenWeatherMap> call = weatherApi.getWeatherWithLocation(lat, lon, AppConfig.getInstance().getApi_key());
        call.enqueue(new Callback<OpenWeatherMap>() {
            @Override
            public void onResponse(Call<OpenWeatherMap> call, Response<OpenWeatherMap> response) {

                try {
                    if(response.isSuccessful()) {
                        String error = RetrofitLibrary.getPollutionData(lat, lon, rating, context);
                        if(!error.equals("")){
                            broadcastErrorAlert(context, error, "main");
                        }

                        Log.d("DENNIS_B", response.toString());
                        Log.d("DENNIS_B", response.body().toString());

                        loadValuesIntoViews(weatherData, response, icon, context, "main");
                    } else {
                        Log.d("DENNIS_B", "RetrofitLibrary.getWeatherDataLocal() : call is not successful; no data found");
                        broadcastErrorAlert(context, String.format("No new data found using latitude '%s' and longitude '%s'", lat, lon), "main");
                    }

                }catch(Exception e){
                    Log.d("DENNIS_B", "RetrofitLibrary.getWeatherDataLocal(): error loading weather data: " + e.getLocalizedMessage());
                    broadcastErrorAlert(context, String.format("Error (exception) loading weather data '%s'", e.getLocalizedMessage()), "main");
                }

            }

            @Override
            public void onFailure(Call<OpenWeatherMap> call, Throwable t) {
                Log.d("DENNIS_B", "RetrofitLibrary.getWeatherDataLocal(): Retrofit did not return any weather data" + t.getLocalizedMessage());
                broadcastErrorAlert(context, String.format("Error (onFailure) loading weather data '%s'", t.getLocalizedMessage()), "main");
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
                    if(response.isSuccessful()) {
                        String error = RetrofitLibrary.getPollutionData(response.body().getCoord().getLat(), response.body().getCoord().getLon(), rating, context);
                        if(!error.equals("")){
                            broadcastErrorAlert(context, error, "city");
                        }

                        Log.d("DENNIS_B", response.toString());
                        Log.d("DENNIS_B", response.body().toString());

                        loadValuesIntoViews(weatherData, response, icon, context, "city");

                        broadcastCallCompleteAlert(context, "city");  // this will let the fragment now the call ended correctly,
                                                                           // stop the progressbar and show the views -- city only

                    } else {
                        Log.d("DENNIS_B", "RetrofitLibrary.getWeatherDataCity() : call is not successful; no data found");
                        broadcastErrorAlert(context, String.format("No new data found using '%s'", city), "city");
                    }

                }catch(Exception e){
                    Log.d("DENNIS_B", "RetrofitLibrary.getWeatherDataCity(): error loading weather data: " + e.getLocalizedMessage());
                    broadcastErrorAlert(context, String.format("Error (exception) loading weather data '%s'", e.getLocalizedMessage()), "city");
                }

            }

            @Override
            public void onFailure(Call<OpenWeatherMap> call, Throwable t) {
                Log.d("DENNIS_B", "RetrofitLibrary.getWeatherDataCity(): Retrofit did not return any weather data" + t.getLocalizedMessage());
                broadcastErrorAlert(context, String.format("Error (onFailure) loading weather data '%s'", t.getLocalizedMessage()), "city");
            }
        });

    }

    private static void loadValuesIntoViews(Map<String, TextView> weatherData, Response<OpenWeatherMap> response, ImageView icon, Context context, String type){

        weatherData.get("city").setText(response.body().getName() + ", " + response.body().getSys().getCountry());
        weatherData.get("condition").setText(response.body().getWeather().get(0).getDescription());
        weatherData.get("humidity").setText(response.body().getMain().getHumidity() + "%");
        weatherData.get("maxtemp").setText(response.body().getMain().getTempMax() + " °C");
        weatherData.get("mintemp").setText(response.body().getMain().getTempMin() + " °C");
        weatherData.get("pressure").setText("" + response.body().getMain().getPressure());
        weatherData.get("wind").setText("" + response.body().getWind().getSpeed());
        weatherData.get("temp").setText(response.body().getMain().getTemp() + " °C");

        String sdate = ApplicationLibrary.getDate(response.body().getDt());
        String stime = ApplicationLibrary.getTime(response.body().getDt());

        weatherData.get("timestamp").setText(String.format("%s %s", sdate, stime));

        String iconCode = response.body().getWeather().get(0).getIcon();

        Log.d("DENNIS_B", "RetrofitLibrary.getWeatherDataLocal() icon: " + "https://openweathermap.org/img/wn/" + iconCode + "@2x.png");

        Picasso.get().load("https://openweathermap.org/img/wn/" + iconCode + "@2x.png")
                .into(icon, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d("DENNIS_B", "RetrofitLibrary.getWeatherDataLocal() weather icon loaded: " + "https://openweathermap.org/img/wn/" + iconCode + "@2x.png");
                        broadcastProgressBarAlert(context, type);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d("DENNIS_B", "RetrofitLibrary.getWeatherDataLocal(): error loading weather icon: " + e.getLocalizedMessage());
                        icon.setImageResource(R.mipmap.image870);
                        broadcastProgressBarAlert(context, type);
                    }
                });
    }

    public static void getWeatherForecastData(double lat, double lon, RecyclerView recyclerView, Context context){

        WeatherApi weatherApi = RetrofitWeather.getClient().create(WeatherApi.class);

        Log.d("DENNIS_B", "getWeatherForecastData() lat/lon: " + lat + "/" + lon);

        Call<OpenWeatherForecast> call = weatherApi.getWeatherForecast(lat, lon, AppConfig.getInstance().getApi_key());
        call.enqueue(new Callback<OpenWeatherForecast>() {
            @Override
            public void onResponse(Call<OpenWeatherForecast> call, Response<OpenWeatherForecast> response) {


                try {
                    if(response.isSuccessful()) {
                        Log.d("DENNIS_B", response.toString());
                        Log.d("DENNIS_B", response.body().toString());

                        response.body().getList(); // data is declared as List<ModelClass> globally
                        ForecastHourAdapter adapter = new ForecastHourAdapter(response.body().getList()); // create adapter and move data in as parameter
                        recyclerView.setAdapter(adapter);
                    } else {
                        Log.d("DENNIS_B", "RetrofitLibrary.getWeatherDataLocal() : call is not successful; no data found");
                        broadcastErrorAlert(context, String.format("No forecast data found using latitude '%s' and longitude '%s'", lat, lon), "main");
                    }

                }catch(Exception e){
                    Log.d("DENNIS_B", "Error loading weather forecast data: " + e.getLocalizedMessage());
                    broadcastErrorAlert(context, String.format("Error (exception) loading weather forecast data '%s'", e.getLocalizedMessage()), "main");
                }

            }

            @Override
            public void onFailure(Call<OpenWeatherForecast> call, Throwable t) {
                Log.d("DENNIS_B", "Retrofit did not return any weather forecast data " + t.getLocalizedMessage());
                broadcastErrorAlert(context, String.format("Error (onFailure) loading forecast weather data '%s'", t.getLocalizedMessage()), "main");
            }
        });
    }

    public static void getWeatherForecastDataByDay(RecyclerView recyclerView, Context context) {

        WeatherApi weatherApi = RetrofitWeather.getClient().create(WeatherApi.class);

        double lat = AppConfig.getInstance().getLatitude(); // contains values from main page (location = here)
        double lon = AppConfig.getInstance().getLongitude();

        Log.d("DENNIS_B", "getWeatherForecastDataByDay() lat/lon: " + lat + "/" + lon);

        if (lat==0 && lon==0){
            broadcastErrorAlert(context, String.format("Error. No location was determined yet (lat = 0, lon = 0). Unable to get correct local forecast data"), "fcast");
        }

        Call<OpenWeatherForecast> call = weatherApi.getWeatherForecast(lat, lon, AppConfig.getInstance().getApi_key());
        call.enqueue(new Callback<OpenWeatherForecast>() {

            @Override
            public void onResponse(Call<OpenWeatherForecast> call, Response<OpenWeatherForecast> response) {

                try {
                    if(response.isSuccessful()) {
                        Log.d("DENNIS_B", response.toString());
                        Log.d("DENNIS_B", response.body().toString());

                        java.util.List<Day> days = new ArrayList<>();

                        days = processForecast(response.body().getList(), days);
                        Log.d("DENNIS_B", days.toString());
                        ForecastAdapter adapter = new ForecastAdapter(days, context); // create adapter and move data in as parameter
                        recyclerView.setAdapter(adapter);

                        broadcastProgressBarAlert(context, "fcast");

                    } else {
                        Log.d("DENNIS_B", "RetrofitLibrary.getWeatherForecastDataByDay() : call is not successful; no data found");
                        broadcastErrorAlert(context, String.format("No forecast data found using latitude '%s' and longitude '%s'", lat, lon), "fcast");
                    }

                }catch(Exception e){
                    Log.d("DENNIS_B", "Error loading weather forecast data: " + e.getLocalizedMessage());
                    broadcastErrorAlert(context, String.format("Error (exception) loading weather forecast data '%s'", e.getLocalizedMessage()), "fcast");
                }

            }

            @Override
            public void onFailure(Call<OpenWeatherForecast> call, Throwable t) {
                Log.d("DENNIS_B", "Retrofit did not return any weather forecast data " + t.getLocalizedMessage());
                broadcastErrorAlert(context, String.format("Error (onFailure) loading forecast weather data '%s'", t.getLocalizedMessage()), "fcast");
            }
        });
    }

    private static java.util.List<Day> processForecast(java.util.List<List> data,  java.util.List<Day> days) {

        String cdate="";
        int day_num=0;
        Day day=null;

        for(int i=0; i < data.size(); i++ ){

            String sdate = ApplicationLibrary.getDate(data.get(i).getDt());
            String stime = ApplicationLibrary.getTime(data.get(i).getDt());

            if(cdate.equals("") || (!cdate.equals("") && !cdate.equals(sdate))){

                if(day!=null){
                    Log.d("DENNIS_B", day.toString());
                    days.add(day);
                }

                // create new day
                cdate = sdate;
                day = new Day();
                day.setId(day_num);
                day.setDayofweek(ApplicationLibrary.getDayOfWeek(data.get(i).getDt()));
                day.setDate(cdate);
                day_num++;
            }
            // day is aangemaakt --> vullen
            day.setHumidity(data.get(i).getMain().getHumidity());
            day.setMaxtemp(data.get(i).getMain().getTempMax());
            day.setMintemp(data.get(i).getMain().getTempMin());
            day.setPressure(data.get(i).getMain().getPressure());
            day.setTemp(data.get(i).getMain().getTemp());
            day.setIcon(data.get(i).getWeather().get(0).getIcon());
            day.setWind(data.get(i).getWind().getSpeed());

            day.setCondition(data.get(i).getWeather().get(0).getDescription());
            day.setMeasurements(1);
            try {
                if(day.getRain_time().isEmpty()) {
                    if (data.get(i).getRain().get3h() != 0) {
                        Log.d("DENNIS_B", "rain found coming at " + stime);
                        day.setRain_time(stime);
                    }
                }
            } catch(Exception e){
                Log.d("DENNIS_B", "rain not found");
            }
        }
        days.add(day);
        Log.d("DENNIS_B", "number of days created " + days.size());
        return days;
    }

    private static void broadcastErrorAlert(Context context, String text, String type) {

        Log.d("DENNIS_B", String.format("RetrofitLibrary.broadcastProgressBarAlert(): sending 'LOCAL_WEATHER_DATA_ERROR' for type " + type));
        Intent i = new Intent();
        i.setAction("LOCAL_WEATHER_DATA_ERROR");
        i.putExtra("text", text);
        i.putExtra("type", type);
        context.sendBroadcast(i);

    }

    private static void broadcastProgressBarAlert(Context context, String type) {

        Log.d("DENNIS_B", String.format("RetrofitLibrary.broadcastProgressBarAlert(): sending 'STOP_PROGRESS_BAR' for type " + type));
        Intent i = new Intent();
        i.setAction("STOP_PROGRESS_BAR");
        i.putExtra("type", type);
        context.sendBroadcast(i);

    }

    private static void broadcastCallCompleteAlert(Context context, String type) {

        Log.d("DENNIS_B", String.format("RetrofitLibrary.broadcastCallCompleteAlert(): sending 'CALL_COMPLETE' for type " + type));
        Intent i = new Intent();
        i.setAction("CALL_COMPLETE");
        i.putExtra("type", type);
        context.sendBroadcast(i);

    }

}