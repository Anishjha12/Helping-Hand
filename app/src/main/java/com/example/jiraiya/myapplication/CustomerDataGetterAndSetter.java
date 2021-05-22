package com.example.jiraiya.myapplication;

public class CustomerDataGetterAndSetter {
    String profile_uri;
    String name;
    String email;
    String phone;

    public CustomerDataGetterAndSetter() {
    }

    public CustomerDataGetterAndSetter(String profile_uri, String name, String email, String phone) {
        this.profile_uri = profile_uri;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getProfile_uri() {
        return profile_uri;
    }

    public void setProfile_uri(String profile_uri) {
        this.profile_uri = profile_uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
