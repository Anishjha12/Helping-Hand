package com.example.jiraiya.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class MobCustomerLogin extends AppCompatActivity {

    EditText editTextCode;
    FirebaseAuth mAuth;
    String codeSent;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String code,phone;
    ProgressDialog pd;
    Button resend;
    TextView otpnumber;
    Button confirm;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mob_customer_login);

        mAuth = FirebaseAuth.getInstance();
        editTextCode = findViewById(R.id.code);
        resend =(Button)findViewById(R.id.resendcode);
        otpnumber=(TextView)findViewById(R.id.otpnumber);
        confirm=(Button)findViewById(R.id.confirm);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        phone = preferences.getString("UserPhone", "");
        otpnumber.setText("+91 "+phone);

        if(phone.equals("1234567890"))
        {
           Intent intent=new Intent(MobCustomerLogin.this,GetAllDetails.class);
           startActivity(intent);
        }

        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Verifying....");

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                confirm.setEnabled(true);
                editTextCode.setEnabled(true);
                confirm.setBackgroundResource(R.drawable.button2);
                codeSent = s;

            }

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                code = phoneAuthCredential.getSmsCode();
                if (code != null) {

                    editTextCode.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
                    editTextCode.setText(code);
                    verifySignInCode(code);
                    pd.show();
                }

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(getApplicationContext(),
                        e.getMessage(), Toast.LENGTH_LONG).show();
                onBackPressed();
                finish();

            }


        };

        // confirm

        findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                code = editTextCode.getText().toString();
                if(code.equals(""))
                    Toast.makeText(MobCustomerLogin.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                else {
                    verifySignInCode(code);
                    pd.show();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        sendVerificationCode();
    }

    private void verifySignInCode(String code) {
        // String code = editTextCode.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //here you can open new activity

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("Users").child(phone);

                            myRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists())
                                    {
                                        pd.dismiss();

                                        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(MobCustomerLogin.this);
                                        SharedPreferences.Editor editor = preference.edit();
                                        editor.putBoolean("isloggedin", true);
                                        editor.apply();

                                        Intent intent=new Intent(MobCustomerLogin.this,CustomerSelectCategory.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        pd.dismiss();

                                        Intent intent=new Intent(MobCustomerLogin.this,GetAllDetails.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    // Failed to read value
                                }
                            });



                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                pd.dismiss();
                                Toast.makeText(getApplicationContext(),
                                        "Incorrect Verification Code ", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private void sendVerificationCode() {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                MobCustomerLogin.this,              // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
    }

}
