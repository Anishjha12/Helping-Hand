package com.example.jiraiya.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class NewMap extends FragmentActivity implements OnMapReadyCallback {

    public GoogleMap mMap;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    public static LatLng current_location;
    Marker current_marker;
    CameraUpdate cameraUpdate;
    Button get_prof;
    String category;
    String date;
    String total;
    boolean found =false;
    String my_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        my_id =  Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        //Intents
        category = getIntent().getStringExtra("category");
        date = getIntent().getStringExtra("date");
        total = getIntent().getStringExtra("total");
        Toast.makeText(this, "Cata "+category, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "date "+date, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "total "+total, Toast.LENGTH_SHORT).show();


        get_prof = findViewById(R.id.get_prof);

        get_prof.setOnClickListener(view -> {
            request_prof(category);
            get_prof.setEnabled(false);
        });

    }

    private void request_prof(String categoryy) {
        if(categoryy != null){
            DatabaseReference get_your_proff = FirebaseDatabase.getInstance().getReference()
                    .child("professionalAvailable")
                    .child(categoryy);

            //Get The Location Of the Professional and then Check that if that Professional already has request from the user ID
            //If not send a request

            get_your_proff.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshott) {
                    if(dataSnapshott.getChildrenCount() != 0){
                        //Toast.makeText(NewMap.this, "Professionals Available", Toast.LENGTH_SHORT).show();
                        for(final DataSnapshot dshot : dataSnapshott.getChildren()){

                            if(found)
                                break;

                            final SetLatLong sll = dshot.getValue(SetLatLong.class);
                            Objects.requireNonNull(sll).setKey(dshot.getKey());
                            double dist = distFrom(sll.getLatitude(),sll.getLongitude(),
                                    current_location.latitude,current_location.longitude);

                            if(dist <= 10000.0 && !found){
                                //Check for Pending Request from Same Profess
                                addMarkerProff(sll.getLatitude(),sll.getLongitude());

                               Log.d("GETTIN_VALUES","Key is "+sll.getKey());
                               DatabaseReference pending_req = FirebaseDatabase.getInstance().getReference()
                                       .child("users").child("professionals").child(sll.getKey()).child("Requests");
                               pending_req.addListenerForSingleValueEvent(new ValueEventListener() {
                                   @Override
                                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                       if(!dataSnapshot.hasChild(my_id)){

                                           //Add into his profAvail/catag/id/(Add-Here)
                                           DatabaseReference customer_info = FirebaseDatabase
                                                   .getInstance().getReference().child("users")
                                                   .child("customers").child(my_id);
                                           //Get users Image and other details
                                           customer_info.addListenerForSingleValueEvent(new ValueEventListener() {
                                               @Override
                                               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                   CustomerDataGetterAndSetter cdgs = dataSnapshot.
                                                           getValue(CustomerDataGetterAndSetter.class);
                                                   //Make new obj cdgs+Intents(service total money etc..)
                                                   final Call_Proff_GetSet cpgs = new Call_Proff_GetSet(
                                                           Objects.requireNonNull(cdgs).getProfile_uri(),
                                                           "Default Address for Now",
                                                           category,
                                                           date,
                                                           total,
                                                           cdgs.getName(),
                                                           "Pending",
                                                           current_location.latitude,
                                                           current_location.longitude
                                                   );

                                                   //If No other Requests are there then Only Go

                                                   DatabaseReference check_pending_req = FirebaseDatabase.getInstance().getReference()
                                                           .child("professionalAvailable")
                                                           .child(category).child(sll.getKey())
                                                           .child("Requests");

                                                   check_pending_req.addListenerForSingleValueEvent(new ValueEventListener() {
                                                       @SuppressLint("SetTextI18n")
                                                       @Override
                                                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                           if(dataSnapshot.getChildrenCount() == 0){


                                                               //Add The Value to profAvail/cata/prof_uid/Req
                                                               get_prof.setText("Sending Request");
                                                               FirebaseDatabase.getInstance().getReference()
                                                                       .child("professionalAvailable")
                                                                       .child(category).child(sll.getKey())
                                                                       .child("Requests").child(my_id).setValue(cpgs).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                   @Override
                                                                   public void onSuccess(Void aVoid) {
                                                                       //Listen to the Request Status
                                                                       get_prof.setText("Request Sent");
                                                                       final DatabaseReference request_status=FirebaseDatabase.getInstance().getReference()
                                                                               .child("professionalAvailable")
                                                                               .child(category).child(sll.getKey()).child("Requests")
                                                                               .child(my_id);

                                                                       request_status.addChildEventListener(new ChildEventListener() {
                                                                           @Override
                                                                           public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                                                           }

                                                                           @Override
                                                                           public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                                                               String response = dataSnapshot.getValue(String.class);
                                                                               Toast.makeText(NewMap.this,response, Toast.LENGTH_SHORT).show();

                                                                               assert response != null;
                                                                               if(response.equals("Accepted")){
                                                                                   get_prof.setText("Request Accepted");
                                                                                   found = true;
                                                                                   update_in_proff(cpgs,sll.getKey());
                                                                                   remove_all(category);

                                                                               }
                                                                               else if(response.equals("Denied")){
                                                                                   found = false;
                                                                                   get_prof.setText("Request Denied");
                                                                               }
                                                                               removeReq(category,sll.getKey());

                                                                               request_status.removeEventListener(this);
                                                                           }

                                                                           @Override
                                                                           public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                                                                               found = false;
                                                                               get_prof.setText("Request Denied");
                                                                               request_status.removeEventListener(this);
                                                                           }

                                                                           @Override
                                                                           public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                                                           }

                                                                           @Override
                                                                           public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                           }
                                                                       });

                                                                   }
                                                               });

                                                           }
                                                           else {
                                                               Toast.makeText(NewMap.this, "Has other Pending Requests",
                                                                       Toast.LENGTH_SHORT).show();
                                                               found =false;
                                                           }
                                                       }

                                                       @Override
                                                       public void onCancelled(@NonNull DatabaseError databaseError) {

                                                       }
                                                   });




                                               }

                                               @Override
                                               public void onCancelled(@NonNull DatabaseError databaseError) {

                                               }
                                           });

                                       }
                                       else
                                           found = false;
                                   }

                                   @Override
                                   public void onCancelled(@NonNull DatabaseError databaseError) {

                                   }
                               });
                            }
//                            else
//                                Log.d("GETTIN_VALUES", "Not Near "+ dist);

                        }
                        Toast.makeText(NewMap.this, "No Professionals Found", Toast.LENGTH_SHORT).show();

                    }
                    else
                        Toast.makeText(NewMap.this, "No Professionals", Toast.LENGTH_SHORT).show();


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    private void remove_all(final String category) {
        DatabaseReference del_all = FirebaseDatabase.getInstance().getReference("professionalAvailable")
                .child(category);

        del_all.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dshot:dataSnapshot.getChildren()){

                    final DatabaseReference del = FirebaseDatabase.getInstance().getReference("professionalAvailable")
                            .child(category).child(Objects.requireNonNull(dshot.getKey())).child("Requests").child(my_id);

                    del.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getChildrenCount() != 0)
                                del.removeValue().addOnSuccessListener(aVoid -> Toast.makeText(NewMap.this, "Deleted", Toast.LENGTH_SHORT).show());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void removeReq(String category, String id) {

        final DatabaseReference if_has_than_del = FirebaseDatabase.getInstance().getReference("professionalAvailable")
                .child(category).child(id).child("Requests");
        if_has_than_del.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() != 0)
                    if_has_than_del.removeValue().addOnSuccessListener(aVoid -> Toast.makeText(NewMap.this, "Request Removed", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void update_in_proff(Call_Proff_GetSet cpgs,String proff_id) {
        FirebaseDatabase.getInstance().getReference().child("users").child("professionals")
                .child(proff_id).child("Requests").child(my_id).setValue(cpgs).addOnSuccessListener(aVoid -> Toast.makeText(NewMap.this, "New Request Added ", Toast.LENGTH_LONG).show());
    }


    private void stopLocation() {

        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private void addMarkerProff(double lat,Double lon){
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng( lat,lon))
                .title("Professional")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
    }





    private void buildLocationCallback() {

        locationCallback =new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for(Location mLocation:locationResult.getLocations()){
                    Toast.makeText(NewMap.this, "Updating", Toast.LENGTH_SHORT).show();

                    if(current_marker != null){
                        current_marker.remove();
                    }
                    current_location = new LatLng(mLocation.getLatitude(),mLocation.getLongitude());
                    current_marker = mMap.addMarker(new MarkerOptions().position(current_location).title("Your Location"));
                    cameraUpdate = CameraUpdateFactory.newLatLngZoom(current_location, 16);
                    mMap.animateCamera(cameraUpdate);

                }
            }

        };

    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(3000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setSmallestDisplacement(5);
    }


    private void startLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        buildLocationRequest();
        buildLocationCallback();
        startLocation();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(fusedLocationProviderClient != null)
        stopLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();

        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE );
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(!statusOfGPS){
            buildAlertMessageNoGps();
        }
        else {
            buildLocationRequest();
            buildLocationCallback();
            startLocation();

        }


    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton("No", (dialog, id) -> {
                    dialog.cancel();
                    buildAlertMessageNoGps();
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return (float) (earthRadius * c);
    }
}
