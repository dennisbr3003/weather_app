package com.dennis_brink.android.myweatherapp;

import com.dennis_brink.android.myweatherapp.model_airquality.OpenWeatherAirQuality;
import com.dennis_brink.android.myweatherapp.model_forecast.OpenWeatherForecast;
import com.dennis_brink.android.myweatherapp.model_weather.OpenWeatherMap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {

    // annotation is taken from this query:
    // https://api.openweathermap.org/data/2.5/weather?lat=51.45702&lon=5.63827&appid=?&units=metric
    // First take the part after the base url:
    // weather?lat=51.45702&lon=5.63827&appid=?&units=metric
    // Remove the parts you think should be queryable and add them to the call
    // lat and lon will be variables. The metric system will not be variable
    @GET("weather?&units=metric")
    Call<OpenWeatherMap>getWeatherWithLocation(@Query("lat")double lat, @Query("lon")double lon, @Query("appid")String key);

    // Getting the query fro city name from this URL
    // https://api.openweathermap.org/data/2.5/weather?q=Helmond,NL&appid=?&units=metric
    // we use weather?q=Helmond,NL&appid=?&units=metric
    // cityname and country will be variables but will be put in one string value if applicable
    @GET("weather?&units=metric")
    Call<OpenWeatherMap>getWeatherWithCityName(@Query("q")String name, @Query("appid")String key);

    // getting current air quality data from this URL
    // https://api.openweathermap.org/data/2.5/air_pollution?lat=51.45702&lon=5.63827&appid=?
    // lat and lon will be variables.
    // Air Quality Index. Possible values: 1, 2, 3, 4, 5. Where 1 = Good, 2 = Fair, 3 = Moderate, 4 = Poor, 5 = Very Poor.
    @GET("air_pollution?")
    Call<OpenWeatherAirQuality>getWeatherAirQuality(@Query("lat")double lat, @Query("lon")double lon, @Query("appid")String key);
    /*
        components.co Сoncentration of CO (Carbon monoxide), μg/m3
        components.no Сoncentration of NO (Nitrogen monoxide), μg/m3
        components.no2 Сoncentration of NO2 (Nitrogen dioxide), μg/m3
        components.o3 Сoncentration of O3 (Ozone), μg/m3
        components.so2 Сoncentration of SO2 (Sulphur dioxide), μg/m3
        components.pm2_5 Сoncentration of PM2.5 (Fine particles matter), μg/m3
        components.pm10 Сoncentration of PM10 (Coarse particulate matter), μg/m3
        components.nh3 Сoncentration of NH3 (Ammonia), μg/m3
    */

    @GET("forecast?&units=metric")
    Call<OpenWeatherForecast>getWeatherForecast(@Query("lat")double lat, @Query("lon")double lon, @Query("appid")String key);

}
