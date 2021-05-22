package com.example.jiraiya.myapplication;

public class GetSetServicesSelected {
    long cost;
    String service_type;

    public GetSetServicesSelected() {
    }

    public GetSetServicesSelected(long cost, String service_type) {
        this.cost = cost;
        this.service_type = service_type;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }
}
