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
import com.dennis_brink.android.myweatherapp.model_day.Day;
import com.dennis_brink.android.myweatherapp.model_forecast.OpenWeatherForecast;
import com.dennis_brink.android.myweatherapp.model_weather.OpenWeatherMap;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentForecast extends Fragment {

    RecyclerView rvForecast;
    List<com.dennis_brink.android.myweatherapp.model_forecast.List> data;
    List<Day> days = new ArrayList<>();
    ArrayList<Integer>imageList = new ArrayList<>();
    ForecastAdapter adapter;

    int intdate;
    Calendar xdate = Calendar.getInstance();
    String sdate="";
    String cdate="";
    String stime="";
    int day_num=0;
    Day day=null;

    public static FragmentForecast newInstance() {
        return new FragmentForecast();
    }

    @Override
    public void onStart() {
        super.onStart();
        getWeatherForecastData();
        loadImageList();
    }

    private void loadImageList(){

        imageList.add(R.drawable.item00);
        imageList.add(R.drawable.item0);
        imageList.add(R.drawable.item1);
        imageList.add(R.drawable.item2);
        imageList.add(R.drawable.item3);
        imageList.add(R.drawable.item4);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forecast, container, false);

        rvForecast = view.findViewById(R.id.rvDays);
        rvForecast.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        return view;

    }

    private void getWeatherForecastData(){

        WeatherApi weatherApi = RetrofitWeather.getClient().create(WeatherApi.class);

        Log.d("DENNIS_B", "getWeatherForecastData() lat/lon: 51.4574544 / 5.6377686");

        Call<OpenWeatherForecast> call = weatherApi.getWeatherForecast(51.4574544, 5.6377686, "eda35914aa73c5ac26a4d657718b1dbc");
        call.enqueue(new Callback<OpenWeatherForecast>() {
            @Override
            public void onResponse(Call<OpenWeatherForecast> call, Response<OpenWeatherForecast> response) {


                try {
                    Log.d("DENNIS_B", response.toString());
                    Log.d("DENNIS_B", response.body().toString());

                    data = response.body().getList(); // data is declared as List<ModelClass> globally
                    days = processForecast(data);
                    Log.d("DENNIS_B", days.toString());
                    adapter = new ForecastAdapter(days, imageList, getActivity()); // create adapter and move data in as parameter
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

    private List<Day> processForecast(List<com.dennis_brink.android.myweatherapp.model_forecast.List> data) {


        for(int i=0; i < data.size(); i++ ){

            //Log.d("DENNIS_B", "iteration i " + i);

            intdate = data.get(i).getDt();
            xdate.setTimeInMillis(intdate * 1000L);
            sdate = String.format("%02d-%02d-%04d",
                    xdate.get(Calendar.DAY_OF_MONTH),
                    xdate.get(Calendar.MONTH) + 1,
                    xdate.get(Calendar.YEAR));
            stime = String.format("%02d:%02d",
                    xdate.get(Calendar.HOUR_OF_DAY),
                    xdate.get(Calendar.MINUTE));
            //Log.d("DENNIS_B", "sdate " + sdate);
            //Log.d("DENNIS_B", "cdate " + cdate);

            if(cdate.equals("") || (!cdate.equals("") && !cdate.equals(sdate))){

                if(day!=null){
                    Log.d("DENNIS_B", day.toString());
                    days.add(day);
                }

                // create new day
                cdate = sdate;
                day = new Day();
                day.setId(day_num);
                day.setDayofweek(xdate.get(Calendar.DAY_OF_WEEK));
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
                        day.setRain_time(stime);
                    }
                }
            } catch(Exception e){
                Log.d("DENNIS_B", "rain not found");
            }
        }
        days.add(day);
        return days;
    }
}