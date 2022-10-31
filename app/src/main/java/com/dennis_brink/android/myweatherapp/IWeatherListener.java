package com.dennis_brink.android.myweatherapp;

public interface IWeatherListener {
    void showErrorMessage(String text);
    void stopProgressBar();
}
