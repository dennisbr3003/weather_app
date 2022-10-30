package com.dennis_brink.android.myweatherapp;

import com.dennis_brink.android.myweatherapp.model_airquality.OpenWeatherAirQuality;
import com.dennis_brink.android.myweatherapp.model_weather.OpenWeatherMap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {

    // annotation is taken from this query:
    // https://api.openweathermap.org/data/2.5/weather?lat=51.45702&lon=5.63827&appid=eda35914aa73c5ac26a4d657718b1dbc&units=metric
    // First take the part after the base url:
    // weather?lat=51.45702&lon=5.63827&appid=eda35914aa73c5ac26a4d657718b1dbc&units=metric
    // Remove the parts you think should be queryable and add them to the call
    // lat and lon will be variables. The metric system and the API key will not be variable
    @GET("weather?appid=eda35914aa73c5ac26a4d657718b1dbc&units=metric")
    Call<OpenWeatherMap>getWeatherWithLocation(@Query("lat")double lat, @Query("lon")double lon);

    // Getting the query fro city name from this URL
    // https://api.openweathermap.org/data/2.5/weather?q=Helmond,NL&appid=eda35914aa73c5ac26a4d657718b1dbc&units=metric
    // we use weather?q=Helmond,NL&appid=eda35914aa73c5ac26a4d657718b1dbc&units=metric
    // cityname and country will be variables but will be put in one string value if applicable
    @GET("weather?appid=eda35914aa73c5ac26a4d657718b1dbc&units=metric")
    Call<OpenWeatherMap>getWeatherWithCityName(@Query("q")String name);

    // getting current air quality data from this URL
    // https://api.openweathermap.org/data/2.5/air_pollution?lat=51.45702&lon=5.63827&appid=eda35914aa73c5ac26a4d657718b1dbc
    // lat and lon will be variables.
    // Air Quality Index. Possible values: 1, 2, 3, 4, 5. Where 1 = Good, 2 = Fair, 3 = Moderate, 4 = Poor, 5 = Very Poor.
    @GET("air_pollution?appid=eda35914aa73c5ac26a4d657718b1dbc")
    Call<OpenWeatherAirQuality>getWeatherAirQuality(@Query("lat")double lat, @Query("lon")double lon);
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

}
