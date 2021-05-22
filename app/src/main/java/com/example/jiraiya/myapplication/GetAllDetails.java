package com.example.jiraiya.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import de.hdodenhof.circleimageview.CircleImageView;

public class GetAllDetails extends AppCompatActivity {

    CircleImageView users_image;
    EditText getname;
    EditText getemail;
    Button save_and_continue;
    String phone;
    ProgressBar photoprogress;

    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_all_details);

        users_image = (CircleImageView) findViewById(R.id.GetAllDerailsImage);
        getname = (EditText) findViewById(R.id.getname);
        getemail = (EditText) findViewById(R.id.getemail);
        save_and_continue = (Button) findViewById(R.id.GetAllDetailsButton);
        photoprogress=(ProgressBar)findViewById(R.id.photoprogress);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        phone = preferences.getString("UserPhone", "");


    }

    public void save(View view)
    {
        if(getname.getText().toString().length()==0||getemail.getText().toString().length()==0)
        {
            Toast.makeText(GetAllDetails.this, "Enter valid information",
                    Toast.LENGTH_SHORT).show();
        }

        else {

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Users").child(phone).child("Name");
            myRef.setValue(getname.getText().toString());

            DatabaseReference myRef1 = database.getReference("Users").child(phone).child("Email");
            myRef1.setValue(getemail.getText().toString());

            DatabaseReference myRef2 = database.getReference("Users").child(phone).child("Phone");
            myRef2.setValue(phone);

            SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preference.edit();
            editor.putBoolean("isloggedin", true);
            editor.apply();

            Intent intent = new Intent(GetAllDetails.this, CustomerSelectCategory.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
    }

    public void choosepic(View view)
    {
        photoprogress.setVisibility(View.VISIBLE);

        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 0);

        save_and_continue.setBackgroundResource(R.drawable.button3);
        save_and_continue.setEnabled(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            Uri targetUri = data.getData();
            Bitmap bitmap;
            try {

                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,70,byteArrayOutputStream);
                byte[] data1=byteArrayOutputStream.toByteArray();

                users_image.setImageBitmap(bitmap);

                StorageReference profilepic = mStorageRef.child("Users").child(phone).child("profilepic");

                profilepic.putBytes(data1)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content

                                save_and_continue.setBackgroundResource(R.drawable.button2);
                                save_and_continue.setEnabled(true);

                                photoprogress.setVisibility(View.INVISIBLE);
                                save_and_continue.setEnabled(true);
                                save_and_continue.setBackgroundResource(R.drawable.button2);

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                // ...
                                photoprogress.setVisibility(View.INVISIBLE);
                                save_and_continue.setEnabled(true);
                                save_and_continue.setBackgroundResource(R.drawable.button2);
                                Toast.makeText(GetAllDetails.this, "photo not uploaded",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else
        {
            photoprogress.setVisibility(View.GONE);
            save_and_continue.setEnabled(true);
            save_and_continue.setBackgroundResource(R.drawable.button2);
        }
    }

}
