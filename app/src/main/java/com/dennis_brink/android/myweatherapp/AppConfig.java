package com.dennis_brink.android.myweatherapp;

import static android.content.res.Configuration.UI_MODE_NIGHT_MASK;
import static android.content.res.Configuration.UI_MODE_NIGHT_NO;
import static android.content.res.Configuration.UI_MODE_NIGHT_UNDEFINED;
import static android.content.res.Configuration.UI_MODE_NIGHT_YES;

import android.util.Log;

public class AppConfig {

    private static AppConfig instance = new AppConfig();

    private String api_key;
    private static boolean darkThemeActive = false;
    private static boolean connectionOnStartup = false;

    private AppConfig() {

    }

    public static AppConfig getInstance(){
        darkThemeActive = getDarkThemeActive();
        return instance;
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

    public boolean isConnectionOnStartup() {
        return connectionOnStartup;
    }

    public void setConnectionOnStartup(boolean connectionOnStartup) {
        AppConfig.connectionOnStartup = connectionOnStartup;
    }

    private static boolean getDarkThemeActive(){

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
                '}';
    }
}
