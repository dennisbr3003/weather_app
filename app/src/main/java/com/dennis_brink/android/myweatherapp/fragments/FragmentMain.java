package com.dennis_brink.android.myweatherapp.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dennis_brink.android.myweatherapp.AppConfig;
import com.dennis_brink.android.myweatherapp.R;
import com.dennis_brink.android.myweatherapp.RetrofitLibrary;
import com.dennis_brink.android.myweatherapp.RetrofitWeather;
import com.dennis_brink.android.myweatherapp.WeatherApi;
import com.dennis_brink.android.myweatherapp.model_weather.OpenWeatherMap;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Query;

public class FragmentMain extends Fragment {

    private TextView textHumidity, textMaxTemp, textMinTemp, textPressure,
            textWind, textCity, textTemp, textCondition;
    private ImageView imageViewIcon, imgStar1, imgStar2, imgStar3, imgStar4, imgStar5;
    LocationManager locationManager;
    LocationListener locationListener;
    ProgressBar progressBar;
    double lat, lon;

    private ArrayList<ImageView> imgList = new ArrayList<>();

    public static FragmentMain newInstance() {
        return new FragmentMain();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("DENNIS_B", "FragmentMain start");
        setupLocationListener();
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            Log.d("DENNIS_B", "Request current location start");
            //final LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location != null && location.getTime() > Calendar.getInstance().getTimeInMillis() - 2 * 60 * 1000) {
                // Do something with the recent location fix
                //  otherwise wait for the update below
                Log.d("DENNIS_B", "We will use the last know location because it is not that old (2 mins)");

                lat = location.getLatitude();
                lon = location.getLongitude();
                Log.d("DENNIS_B", "Latitude " + lat + " Longitude " + lon);
                AppConfig.getInstance().setLat(lat);
                AppConfig.getInstance().setLon(lon);
                Log.d("DENNIS_B", "Secured values: " + AppConfig.getInstance().toString());

                getWeatherData();
                RetrofitLibrary.getPollutionData("local", imgList);

            }
            else {
                getCurrentLocation(); // get a new location directly
            }
            Log.d("DENNIS_B", "Request current location end");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 50, locationListener); // set op the listener parameters
        }
        Log.d("DENNIS_B", "Einde FragmentMain start");
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation(){
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);

        // Now create a location manager
        final LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

        // This is the Best And IMPORTANT part
        final Looper looper = null;

        locationManager.requestSingleUpdate(criteria, locationListener, looper);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // do stuff here like in an Activity's onCreate
        textCity = view.findViewById(R.id.textViewCity);
        textCondition = view.findViewById(R.id.textViewWeaterCondition);
        textHumidity = view.findViewById(R.id.textViewHumidity);
        textWind = view.findViewById(R.id.textViewWind);
        textMaxTemp = view.findViewById(R.id.textViewMaxTemp);
        textMinTemp = view.findViewById(R.id.textViewMinTemp);
        textPressure = view.findViewById(R.id.textViewPressure);
        textTemp = view.findViewById(R.id.textViewTemperature);
        progressBar = view.findViewById(R.id.progressBar);
        imageViewIcon = view.findViewById(R.id.imageViewIcon);

        imgStar1 = view.findViewById(R.id.star1);
        imgStar2 = view.findViewById(R.id.star2);
        imgStar3 = view.findViewById(R.id.star3);
        imgStar4 = view.findViewById(R.id.star4);
        imgStar5 = view.findViewById(R.id.star5);

        setupImageViewList();

        progressBar.setVisibility(View.VISIBLE);
        textCity.setVisibility(View.INVISIBLE);
        return view;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1 && permissions.length > 0 && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 50, locationListener);
        } else {
            // no permission
        }

    }

    private void setupImageViewList(){
        imgList.add(imgStar1);
        imgList.add(imgStar2);
        imgList.add(imgStar3);
        imgList.add(imgStar4);
        imgList.add(imgStar5);
    }

    private void getWeatherData(){

        WeatherApi weatherApi = RetrofitWeather.getClient().create(WeatherApi.class);

        Log.d("DENNIS_B", "getWeatherData() lat/lon: " + AppConfig.getInstance().getLat() + " " + AppConfig.getInstance().getLon());

        Call<OpenWeatherMap> call = weatherApi.getWeatherWithLocation(AppConfig.getInstance().getLat(), AppConfig.getInstance().getLon(),AppConfig.getInstance().getApi_key());
        call.enqueue(new Callback<OpenWeatherMap>() {
            @Override
            public void onResponse(Call<OpenWeatherMap> call, Response<OpenWeatherMap> response) {

                progressBar.setVisibility(View.INVISIBLE);
                textCity.setVisibility(View.VISIBLE);

                try {
                    Log.d("DENNIS_B", response.toString());
                    Log.d("DENNIS_B", response.body().toString());

                    textCity.setText(response.body().getName() + ", " + response.body().getSys().getCountry());
                    textTemp.setText(response.body().getMain().getTemp() + " °C");
                    textCondition.setText(response.body().getWeather().get(0).getDescription());
                    textHumidity.setText(response.body().getMain().getHumidity() + "%");
                    textMaxTemp.setText(response.body().getMain().getTempMax() + " °C");
                    textMinTemp.setText(response.body().getMain().getTempMin() + " °C");
                    textPressure.setText("" + response.body().getMain().getPressure());
                    textWind.setText("" + response.body().getWind().getSpeed());

                    AppConfig.getInstance().setLocation(response.body().getName());

                    String iconCode = response.body().getWeather().get(0).getIcon();
                    Log.d("DENNIS_B", "Icon: " + "https://openweathermap.org/img/wn/" + iconCode + "@2x.png");
                    Picasso.get().load("https://openweathermap.org/img/wn/" + iconCode + "@2x.png").placeholder(R.mipmap.image870).into(imageViewIcon, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });

                }catch(Exception e){
                    Log.d("DENNIS_B", "Error loading weather data: " + e.getLocalizedMessage());
                }

            }

            @Override
            public void onFailure(Call<OpenWeatherMap> call, Throwable t) {
                Log.d("DENNIS_B", "Retrofit did not return any weather data");
            }
        });
    }
    private void setupLocationListener(){

        Log.d("DENNIS_B", "setupLocationListener() start");

        // dit moet naar een aparte proc, lon en lat moet overal beschikbaar zijn
        locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onProviderEnabled(@NonNull String provider) {
                LocationListener.super.onProviderEnabled(provider);
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                LocationListener.super.onProviderDisabled(provider);
            }

            @Override
            public void onLocationChanged(@NonNull Location location) { // user location
                lat = location.getLatitude();
                lon = location.getLongitude();
                Log.d("DENNIS_B", "Latitude " + lat + " Longitude " + lon);
                AppConfig.getInstance().setLat(lat);
                AppConfig.getInstance().setLon(lon);
                Log.d("DENNIS_B", "Secured values: " + AppConfig.getInstance().toString());

                getWeatherData();
                RetrofitLibrary.getPollutionData("local", imgList);

            }
        };

        Log.d("DENNIS_B", "setupLocationListener() end - setup listeners completed");

    }

}