package com.example.jiraiya.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Thread th =new Thread(){

            @Override
            public void run() {
                super.run();

                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                Boolean isloggedin = preferences.getBoolean("isloggedin", false);

                if(isloggedin)
                {
                    Intent signup = new Intent(MainActivity.this,CustomerSelectCategory.class);
                    startActivity(signup);
                    finish();
                }
                else
                {
                    Intent signup = new Intent(MainActivity.this,LoginFirst.class);
                    startActivity(signup);
                    finish();
                }
            }
        };

        th.start();


    }
}
