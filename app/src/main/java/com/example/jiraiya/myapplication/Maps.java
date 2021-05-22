package com.example.jiraiya.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Maps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    double latitude;
    double longitude;

    LatLng currentLocation;
    String myphone;
    String ProfessionalType;


    Map<String,Object> map=new HashMap<>();

    String date;
    String total;

   // ProgressDialog pd=new ProgressDialog(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        ProfessionalType = preferences.getString("ServiceCategory", "");
        myphone = preferences.getString("UserPhone", "");

        for(int i=0;i<Frag2.orders.size();i++)
        {
            map.put(String.valueOf(i),Frag2.orders.get(i));
        }

        date = getIntent().getStringExtra("date");
        total = getIntent().getStringExtra("total");

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }

        mMap.setMyLocationEnabled(true);

        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location!=null) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            currentLocation = new LatLng(latitude, longitude);
          //  mMap.addMarker(new MarkerOptions().position(currentLocation).title("You"));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(currentLocation));

            // show professionals on map
            FetchProfessionals();

        }


    }

    public void FetchProfessionals()
    {

        final ArrayList<Node> list=new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("ProfessionalLocation").child(ProfessionalType);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot item_snapshot : dataSnapshot.getChildren())
                    {
                        Node node=new Node();
                        node.id=item_snapshot.getKey();

                        for (DataSnapshot child_snapshot : item_snapshot.getChildren())
                        {

                            if(child_snapshot.getKey().equals("Latitude"))
                            {
                                node.latitude=Double.parseDouble(child_snapshot.getValue(String.class));
                            }
                            else if(child_snapshot.getKey().equals("Longitude"))
                            {
                                node.longitude=Double.parseDouble(child_snapshot.getValue(String.class));
                            }
                            else if(child_snapshot.getKey().equals("online"))
                            {
                                node.online=child_snapshot.getValue(String.class);
                            }
                            else
                            {
                                node.tasks=Integer.parseInt(child_snapshot.getValue(String.class));
                            }
                        }

                        LatLng location=new LatLng(node.latitude,node.longitude);
                        float[] results=new float[1];
                        Location.distanceBetween(currentLocation.latitude,currentLocation.longitude,
                                location.latitude, location.longitude, results);

                        Toast.makeText(Maps.this, String.valueOf(results[0]/1000)+"  KM",
                                Toast.LENGTH_SHORT).show();

                        mMap.addMarker(new MarkerOptions().position(location).title(ProfessionalType));

                        if(node.online.equals("online"))
                        {
                            list.add(node);
                        }

                    }

                    // assign professional . . .

                    assignProfessional(list);

                    }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    public void assignProfessional(ArrayList<Node> list)
    {
        int min=1000000;

        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).tasks<min)
                min=list.get(i).tasks;
        }

        // send request . . .

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).tasks==min)
            {
                DatabaseReference myRef = database.getReference("Professionals").child(list.get(i).id).child("Requests");
                myRef.push().setValue(myphone);

            }
        }


        DatabaseReference myRef = database.getReference("RequestCenter").child(myphone);
        myRef.child("phone").setValue(myphone);
        myRef.child("Date").setValue(date);
        myRef.child("Status").setValue("pending");
        myRef.child("Assigned").setValue("false");
        myRef.child("Total").setValue(total);
        myRef.child("ServicesRequired").setValue(map);

        DatabaseReference myRef1 = database.getReference(myphone).child("MyRequests");
        myRef1.push().setValue(myphone);


    }

}

class Node
{
    String id;
    Double latitude;
    Double longitude;

    String online;

    int tasks;
}
