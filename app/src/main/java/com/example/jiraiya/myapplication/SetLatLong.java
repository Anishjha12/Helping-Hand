package com.example.jiraiya.myapplication;

public class SetLatLong {
    double latitude;
    double longitude;
    String key;

    public SetLatLong() {
    }

    public SetLatLong(double latitude, double longitude, String key) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.key = key;
    }


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
