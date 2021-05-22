package com.example.jiraiya.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginFirst extends AppCompatActivity {

    Button varify;
    EditText phone;
    TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_first);

        phone = (EditText)findViewById(R.id.LoginFirstPhoneNo);
        varify = (Button)findViewById(R.id.LoginFirstVarify);
        error=(TextView)findViewById(R.id.loginerror);
    }

    public void verify(View view)
    {
        if(phone.getText().toString().length()!=10)
        {
            error.setVisibility(View.VISIBLE);
            Toast.makeText(LoginFirst.this, "Invalid Phone number",
                    Toast.LENGTH_SHORT).show();
        }
        else
        {
            SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(LoginFirst.this);
            SharedPreferences.Editor editor = preference.edit();
            editor.putString("UserPhone",phone.getText().toString());
            editor.apply();

            error.setVisibility(View.INVISIBLE);

            Intent intent=new Intent(LoginFirst.this,MobCustomerLogin.class);
            startActivity(intent);
        }
    }
}
