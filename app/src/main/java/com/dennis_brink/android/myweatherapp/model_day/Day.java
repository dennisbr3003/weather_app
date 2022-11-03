package com.dennis_brink.android.myweatherapp.model_day;

import android.util.Log;

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Day {

    int id;
    int measurements;
    String date;
    int dayofweek;
    String date_display;
    double temp;
    double mintemp;
    double maxtemp;
    int pressure;
    int humidity;
    int airquality;
    String day_display;
    String day_display_abbreviated;

    private Map<String, Integer> weather_icon = new HashMap<>();
    private static final DecimalFormat df = new DecimalFormat("0.00");
    String dayNames[] = new DateFormatSymbols().getWeekdays();
    String dayShortNames[] = new DateFormatSymbols().getShortWeekdays();

    public Day() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate_display() {
        return date_display;
    }

    public double getTemp() {
        if (this.measurements == 0){return temp;}
        return formatDecimals(temp / this.measurements);
    }

    public void setTemp(double temp) {
        this.temp += temp;
    }

    public double getMintemp() {
        if (this.measurements == 0){return mintemp;}
        return formatDecimals(mintemp / this.measurements);
    }

    public void setMintemp(double mintemp) {
        this.mintemp += mintemp;
    }

    public double getMaxtemp() {
        if (this.measurements == 0){return maxtemp;}
        return formatDecimals(maxtemp / this.measurements);
    }

    public void setMaxtemp(double maxtemp) {
        this.maxtemp += maxtemp;
    }

    public int getPressure() {
        if (this.measurements == 0){return pressure;}
        return (int) pressure / this.measurements;
    }

    public void setPressure(int pressure) {
        this.pressure += pressure;
    }

    public int getHumidity() {
        if (this.measurements == 0){return humidity;}
        return (int) humidity / this.measurements;
    }

    public void setHumidity(int humidity) {
        this.humidity += humidity;
    }

    public int getAirquality() {
        return airquality;
    }

    public void setAirquality(int airquality) {
        this.airquality = airquality;
    }

    public int getMeasurements() {
        return measurements;
    }

    public void setMeasurements(int measurements) {
        this.measurements += measurements;
    }

    public String getIcon() {
        String iconx = Collections.max(weather_icon.entrySet(), Map.Entry.comparingByValue()).getKey();
        return iconx;
    }

    public void setIcon(String icon) {
        if(weather_icon.containsKey(icon)){
            weather_icon.put(icon, weather_icon.get(icon) + 1);
        } else {
            weather_icon.put(icon, 1);
        }
    }

    public String getDay_display() {
        return day_display;
    }

    public String getDay_display_abbreviated() {
        return day_display_abbreviated;
    }

    public void setDayofweek(int dayofweek) {

        this.dayofweek = dayofweek;
        this.day_display = dayNames[this.dayofweek];
        this.day_display_abbreviated = dayShortNames[this.dayofweek];

    }

    @Override
    public String toString() {
        return "Day{" +
                "id=" + id +
                ", measurements=" + measurements +
                ", date='" + date + '\'' +
                ", dayofweek = '" + dayofweek + '\'' +
                ", date_display='" + date_display + '\'' +
                ", temp=" + temp +
                ", mintemp=" + mintemp +
                ", maxtemp=" + maxtemp +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                ", airquality=" + airquality +
                ", day_display='" + day_display + '\'' +
                ", day_display_abbreviated='" + day_display_abbreviated + '\'' +
                ", weather_icon=" + weather_icon +
                '}';
    }

    private double formatDecimals(double x){

        String sx = String.format("%.2f", (temp / this.measurements));
        sx = sx.replaceAll(",", ".");
        double d = Double.parseDouble(sx);
        return d;

    }

}
