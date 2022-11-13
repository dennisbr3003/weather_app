package com.dennis_brink.android.myweatherapp;

import static android.content.res.Configuration.UI_MODE_NIGHT_MASK;
import static android.content.res.Configuration.UI_MODE_NIGHT_NO;
import static android.content.res.Configuration.UI_MODE_NIGHT_UNDEFINED;
import static android.content.res.Configuration.UI_MODE_NIGHT_YES;

import android.content.res.AssetManager;
import android.util.Log;

import com.dennis_brink.android.myweatherapp.model_config.OpenWeatherConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AppConfig {

    private static AppConfig instance = new AppConfig();
    private static String TAG = "DENNIS_B";

    private String api_key = "";
    private static boolean darkThemeActive = false;

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

    public boolean isDarkThemeActive() {
        return darkThemeActive;
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
