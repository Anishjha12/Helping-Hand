package com.example.jiraiya.myapplication;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Frag1 extends Fragment {

    Button go_to;
    ProgressDialog pd;
    Context context;
    static List<String> categories;
    static  ArrayAdapter<String> dataAdapter;
    static Spinner spinner;
    static String category_selected="";

    public Frag1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_frag1, container, false);

        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                3);

        go_to = (Button)view.findViewById(R.id.frag1_button_go);
        go_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Frag2 frag2 = new Frag2();

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("ServiceCategory", category_selected);
                editor.apply();

                Bundle args = new Bundle();
                args.putString("Category", category_selected);
                frag2.setArguments(args);
                getFragmentManager().beginTransaction().replace(R.id.output,frag2,"Go").addToBackStack(null).commit();
            }
        });

        spinner = (Spinner)view.findViewById(R.id.frag1_spinner);

        categories = new ArrayList<String>();
        dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                category_selected = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                category_selected = "Beautician";
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        pd = new ProgressDialog(context);
        pd.setMessage("Fetching All Professionals");
        pd.show();

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("services");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dshot: dataSnapshot.getChildren()) {
                    categories.add(dshot.getKey());
                    dataAdapter.notifyDataSetChanged();
                }
                pd.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onResume() {
        categories.clear();
        dataAdapter.notifyDataSetChanged();
        super.onResume();
    }
}
