package com.example.jiraiya.myapplication;

public class CustomerLocations {

    double lat;
    double longi;

    CustomerLocations(double lat, double longi){
            this.lat=lat;
            this.longi=longi;
        }

        double getlat(){
            return lat;
        }

        double getLongi(){
            return longi;
        }

}
