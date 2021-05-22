package com.example.jiraiya.myapplication;

public class Call_Proff_GetSet {

    String profile_uri;
    String address;
    String category;
    String date;
    String total;
    String name;
    String status;
    double latitude;
    double longitude;

    public Call_Proff_GetSet() {
    }

    public Call_Proff_GetSet(String profile_uri, String address, String category, String date, String total, String name, String status, double latitude, double longitude) {
        this.profile_uri = profile_uri;
        this.address = address;
        this.category = category;
        this.date = date;
        this.total = total;
        this.name = name;
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getProfile_uri() {
        return profile_uri;
    }

    public void setProfile_uri(String profile_uri) {
        this.profile_uri = profile_uri;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
