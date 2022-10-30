package com.dennis_brink.android.myweatherapp;

public class AppConfig {

    private static AppConfig instance = new AppConfig();

    private double lon;
    private double lat;
    private double slon;
    private double slat;
    private String location;
    private String slocation;

    private AppConfig() {
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

    @Override
    public String toString() {
        return "AppConfig{" +
                "lon=" + lon +
                ", lat=" + lat +
                ", slon=" + slon +
                ", slat=" + slat +
                ", location='" + location + '\'' +
                ", slocation='" + slocation + '\'' +
                '}';
    }
}
