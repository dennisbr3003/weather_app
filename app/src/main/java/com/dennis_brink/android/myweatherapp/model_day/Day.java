package com.dennis_brink.android.myweatherapp.model_day;

import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
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
    double wind;
    int pressure;
    int humidity;
    int airquality;
    String day_display;
    String day_display_abbreviated;
    String rain_time="";

    private Map<String, Integer> weather_icon = new HashMap<>();
    private Map<String, Integer> weather_condition = new HashMap<>();

    DateFormatSymbols symbols = new DateFormatSymbols(new Locale("en"));

    String dayNames[] = symbols.getWeekdays();
    String dayShortNames[] = symbols.getShortWeekdays();

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
        return mintemp;
    }

    public void setMintemp(double mintemp) {
        if(this.mintemp == 0){
            this.mintemp = mintemp;
        } else {
            if(mintemp < this.mintemp) {
                this.mintemp = mintemp;
            }
        }
    }

    public double getMaxtemp() {
       return maxtemp;
    }

    public void setMaxtemp(double maxtemp) {
        if(this.maxtemp == 0){
            this.maxtemp = maxtemp;
        } else {
            if (maxtemp > this.maxtemp) {
                this.maxtemp = maxtemp;
            }
        }
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

    public double getWind() {
        if (this.measurements == 0){return wind;}
        return formatToOneDecimal(wind / this.measurements);
    }

    public void setWind(double wind) {
        this.wind += wind;
    }

    public String getCondition() {
        String conditionx = Collections.max(weather_condition.entrySet(), Map.Entry.comparingByValue()).getKey();
        return conditionx;
    }

    public void setCondition(String condition) {
        if(weather_condition.containsKey(condition)){
            weather_condition.put(condition, weather_condition.get(condition) + 1);
        } else {
            weather_condition.put(condition, 1);
        }
    }

    public String getRain_time() {
        return rain_time;
    }

    public void setRain_time(String rain_time) {
        this.rain_time = rain_time;
    }

    private double formatToOneDecimal(double x){

        String sx = String.format("%.1f", x);
        sx = sx.replaceAll(",", ".");
        double d = Double.parseDouble(sx);
        return d;

    }

    private double formatDecimals(double x){

        String sx = String.format("%.2f", x);
        sx = sx.replaceAll(",", ".");
        double d = Double.parseDouble(sx);
        return d;

    }

    @Override
    public String toString() {
        return "Day{" +
                "id=" + id +
                ", measurements=" + measurements +
                ", date='" + date + '\'' +
                ", dayofweek=" + dayofweek +
                ", date_display='" + date_display + '\'' +
                ", temp=" + temp +
                ", mintemp=" + mintemp +
                ", maxtemp=" + maxtemp +
                ", wind=" + wind +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                ", airquality=" + airquality +
                ", day_display='" + day_display + '\'' +
                ", day_display_abbreviated='" + day_display_abbreviated + '\'' +
                ", rain_time='" + rain_time + '\'' +
                ", weather_icon=" + weather_icon +
                ", weather_condition=" + weather_condition +
                ", symbols=" + symbols +
                ", dayNames=" + Arrays.toString(dayNames) +
                ", dayShortNames=" + Arrays.toString(dayShortNames) +
                '}';
    }
}
