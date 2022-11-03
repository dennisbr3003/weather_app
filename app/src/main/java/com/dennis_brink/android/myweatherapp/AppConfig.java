package com.dennis_brink.android.myweatherapp;

import static android.content.res.Configuration.UI_MODE_NIGHT_MASK;
import static android.content.res.Configuration.UI_MODE_NIGHT_NO;
import static android.content.res.Configuration.UI_MODE_NIGHT_UNDEFINED;
import static android.content.res.Configuration.UI_MODE_NIGHT_YES;

import android.util.Log;

public class AppConfig {

    private static AppConfig instance = new AppConfig();

    private double lon;
    private double lat;
    private double slon;
    private double slat;
    private String location;
    private String slocation;
    private String api_key;
    private boolean darkThemeActive = false;

    private AppConfig() {
        darkThemeActive = getDarkThemeActive();
    }

    public static AppConfig getInstance(){
        return instance;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getSlon() {
        return slon;
    }

    public void setSlon(double slon) {
        this.slon = slon;
    }

    public double getSlat() {
        return slat;
    }

    public void setSlat(double slat) {
        this.slat = slat;
    }

    public String getSlocation() {
        return slocation;
    }

    public void setSlocation(String slocation) {
        this.slocation = slocation;
    }

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public boolean isDarkThemeActive() {
        return darkThemeActive;
    }

    public void setDarkThemeActive(boolean darkThemeActive) {
        this.darkThemeActive = darkThemeActive;
    }

    private boolean getDarkThemeActive(){

        Log.d("DENNIS_B", "Determine if dark theme is active");

        int nightModeFlags;

        nightModeFlags = Application.getContext().getResources().getConfiguration().uiMode & UI_MODE_NIGHT_MASK;

        switch (nightModeFlags) {
            case UI_MODE_NIGHT_YES:
                Log.d("DENNIS_B", "UI_MODE_NIGHT_YES");
                return true;
            case UI_MODE_NIGHT_NO:
                Log.d("DENNIS_B", "UI_MODE_NIGHT_NO");
                return false;
            case UI_MODE_NIGHT_UNDEFINED:
                Log.d("DENNIS_B", "UI_MODE_NIGHT_UNDEFINED");
                return false;
            default:
                throw new IllegalStateException("Unexpected value: " + nightModeFlags);
        }

    }

    @Override
    public String toString() {
        return "AppConfig{" +
                "api_key='" + api_key + '\'' +
                ", darkThemeActive=" + darkThemeActive +
                '}';
    }



}
