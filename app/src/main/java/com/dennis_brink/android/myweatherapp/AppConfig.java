package com.dennis_brink.android.myweatherapp;

import static android.content.res.Configuration.UI_MODE_NIGHT_MASK;
import static android.content.res.Configuration.UI_MODE_NIGHT_NO;
import static android.content.res.Configuration.UI_MODE_NIGHT_UNDEFINED;
import static android.content.res.Configuration.UI_MODE_NIGHT_YES;

import android.content.res.AssetManager;
import android.util.Log;

import com.dennis_brink.android.myweatherapp.model_config.OpenWeatherConfig;
import com.dennis_brink.android.myweatherapp.model_wind.Direction;
import com.dennis_brink.android.myweatherapp.model_wind.WindDirection;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AppConfig {

    private static AppConfig instance = new AppConfig();
    private static String TAG = "DENNIS_B";

    private String api_key = "";
    private static boolean darkThemeActive = false;

    private List<Direction> directions;

    private double lat;
    private double lon;

    private AppConfig() {

    }

    public static AppConfig getInstance() {
        darkThemeActive = getDarkThemeActive();
        return instance;
    }

    public String getApi_key() {
        if (this.api_key.equals("")) {
            this.api_key = getApiKey();
        }
        return api_key;
    }

    public List<Direction> getWindDirections() {
        if (this.directions==null) {
            this.directions = loadWindDirections();
        }
        return this.directions;
    }

    public String getWindDirection(int degrees) {
        if (this.directions==null) {
            this.directions = loadWindDirections();
        }
        return getDirection(degrees);
    }

    public void setLocation(double lat, double lon){
        this.lat = lat;
        this.lon = lon;
        Log.d(TAG, "MainActivity.setLocation(): lat/lon : " + lat + "/" + lon);
    }

    public boolean isLocationChanged(double lat, double lon){

        /*
            2023-01-12 01:46:48.107 10124-10124 DENNIS_B
            MainActivity.isLocationChanged(): this.lat 0.0
            MainActivity.isLocationChanged(): this.lon 0.0
            MainActivity.isLocationChanged(): lat 51.457395
            MainActivity.isLocationChanged(): lon 5.637906666666667
            MainActivity.isLocationChanged(): lon diff (bigdecimal) 51.457395
            MainActivity.isLocationChanged(): lan diff (bigdecimal) 5.637906666666667
            MainActivity.isLocationChanged(): lon diff > 0.0005 1
            MainActivity.isLocationChanged(): lan diff > 0.0005 1

            2023-01-12 01:46:48.246 10124-10124 DENNIS_B
            MainActivity.isLocationChanged(): this.lat 51.457395
            MainActivity.isLocationChanged(): this.lon 5.637906666666667
            MainActivity.isLocationChanged(): lat 51.4574859
            MainActivity.isLocationChanged(): lon 5.6377886
            MainActivity.isLocationChanged(): lon diff (bigdecimal) 0.0000909
            MainActivity.isLocationChanged(): lan diff (bigdecimal) 0.000118066666667
            MainActivity.isLocationChanged(): lon diff > 0.0005 -1
            MainActivity.isLocationChanged(): lan diff > 0.0005 -1
        */

        BigDecimal c = BigDecimal.valueOf(this.lat).subtract(BigDecimal.valueOf(lat));
        BigDecimal d = BigDecimal.valueOf(this.lon).subtract(BigDecimal.valueOf(lon));
        
        Log.d(TAG, "MainActivity.isLocationChanged(): this.lat " + this.lat);
        Log.d(TAG, "MainActivity.isLocationChanged(): this.lon " + this.lon);
        Log.d(TAG, "MainActivity.isLocationChanged(): lat " + lat);
        Log.d(TAG, "MainActivity.isLocationChanged(): lon " + lon);
        Log.d(TAG, "MainActivity.isLocationChanged(): lon diff (bigdecimal) " + c.abs());
        Log.d(TAG, "MainActivity.isLocationChanged(): lan diff (bigdecimal) " + d.abs());
        Log.d(TAG, "MainActivity.isLocationChanged(): lon diff > 0.0005 " + c.abs().compareTo(BigDecimal.valueOf(0.0005)));
        Log.d(TAG, "MainActivity.isLocationChanged(): lan diff > 0.0005 " + d.abs().compareTo(BigDecimal.valueOf(0.0005))); // -1 = LT, 0 = EQ, 1 = GT

        this.lat = lat;
        this.lon = lon;

        // check if lat or lon is changed more then the default deviation allowed
        if ((c.abs().compareTo(BigDecimal.valueOf(0.0005)) > 0) || (d.abs().compareTo(BigDecimal.valueOf(0.0005)) > 0) ){
            return true;
        } else {
            return false;
        }
    }

    private String getDirection(int degrees) {
        String direction = "";
        Collections.sort(this.directions, Comparator.comparing(Direction::getDegrees));
        for(int i=0; i< this.directions.size(); i++ ){
            if(directions.get(i).getDegrees() >= degrees){
                try {
                    if ((Math.abs(directions.get(i-1).getDegrees() - degrees)) < (Math.abs(directions.get(i).getDegrees() - degrees))) {
                        direction = directions.get(i-1).getDirection();
                    } else {
                        direction = directions.get(i).getDirection();
                    }
                } catch(Exception e){
                    Log.d(TAG, "MainActivity.getWindDirection(): error (Exception) : " + e.getLocalizedMessage());
                }
                break;
            }
        }
        return direction;
    }

    public boolean isDarkThemeActive() {
        return darkThemeActive;
    }

    private static List<Direction> loadWindDirections(){

        String line;
        InputStream in;
        WindDirection windDirection = null;

        try {
            AssetManager am = Application.getContext().getAssets();
            in = am.open("wind.directions.json");
        } catch (IOException e) {
            Log.d(TAG, "MainActivity.loadWindDirections(): error (IOException) : " + e.getLocalizedMessage());
            return null;
        } catch(Exception ee){
            Log.d(TAG, "MainActivity.loadWindDirections(): error (Exception) : " + ee.getLocalizedMessage());
            return null;
        }

        StringBuilder str = new StringBuilder();

        try(BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            while((line = br.readLine()) != null){
                str.append(line.replaceAll("\\s", ""));
            }
        } catch(Exception e) {
            Log.d(TAG, "MainActivity.loadWindDirections(): error (Exception) : " + e.getLocalizedMessage() + e.getMessage());
            return null;
        }

        Log.d(TAG, "MainActivity.loadWindDirections(): string : " + str);

        ObjectMapper mapper = new ObjectMapper();
        try {
            windDirection = mapper.readValue(str.toString(), WindDirection.class);
        } catch (JsonProcessingException e) {
            Log.d(TAG, "MainActivity.loadWindDirections(): error (JsonProcessingException) : " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.d(TAG, "MainActivity.loadWindDirections(): error (Exception) : " + e.getLocalizedMessage());
        }

        Log.d(TAG, String.format("MainActivity.loadWindDirections(): number of registered wind directions %d", windDirection.getDirections().size()));
        return windDirection.getDirections();
    }

    private static String getApiKey(){
        String line;
        InputStream in;

        AssetManager am = Application.getContext().getAssets();
        try {
            in = am.open("datasource.config.json");
        } catch (IOException e) {
            Log.d(TAG, "MainActivity.getApiKey(): error (IOException) : " + e.getLocalizedMessage());
            return "";
        }

        StringBuilder str = new StringBuilder();

        try(BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            while((line = br.readLine()) != null){
                str.append(line.replaceAll("\\s", ""));
            }
        } catch(Exception e) {
            Log.d(TAG, "MainActivity.getApiKey(): error (Exception) : " + e.getLocalizedMessage() + e.getMessage());
            return "";
        }

        Log.d(TAG, "MainActivity.getApiKey(): string : " + str);

        ObjectMapper mapper = new ObjectMapper();
        try {
            OpenWeatherConfig openWeatherConfig = mapper.readValue(str.toString(), OpenWeatherConfig.class);
            //AppConfig.getInstance().setApi_key(openWeatherConfig.getDatasource().getKey());
            return openWeatherConfig.getDatasource().getKey();
        } catch (JsonProcessingException e) {
            Log.d(TAG, "MainActivity.getApiKey(): error (JsonProcessingException) : " + e.getLocalizedMessage());
            return "";
        } catch (Exception e) {
            Log.d(TAG, "MainActivity.getApiKey(): error (Exception) : " + e.getLocalizedMessage());
            return "";
        }
    }

    private static boolean getDarkThemeActive() {

        int nightModeFlags;

        nightModeFlags = Application.getContext().getResources().getConfiguration().uiMode & UI_MODE_NIGHT_MASK;

        switch (nightModeFlags) {
            case UI_MODE_NIGHT_YES:
                return true;
            case UI_MODE_NIGHT_NO:
                return false;
            case UI_MODE_NIGHT_UNDEFINED:
                return false;
            default:
                throw new IllegalStateException("Unexpected value: " + nightModeFlags);
        }

    }

    @Override
    public String toString() {
        return "AppConfig{" +
                "api_key='" + api_key + '\'' +
                '}';
    }
}
