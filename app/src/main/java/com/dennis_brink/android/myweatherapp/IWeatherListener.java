package com.dennis_brink.android.myweatherapp;

public interface IWeatherListener {
    void showErrorMessage(String text, String type);
    void stopProgressBar(String type);
    void callComplete(String type);
}
