package com.example.jiraiya.myapplication;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;




import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

import static com.example.jiraiya.myapplication.CustomerSelectCategory.dateSelected;
import static com.example.jiraiya.myapplication.Service_Selected_Adapter.selectedItems;
import static com.example.jiraiya.myapplication.Service_Selected_Adapter.total;


public class Frag2 extends Fragment {

    String category;
    Context context;
    TextView tv_category;
    public static TextView tv_total;
    private RecyclerView oder_items_recycler;
    private RecyclerView.Adapter ordersAdapter;
    static List<GetSetServicesSelected> orders;
    ProgressDialog dialog;
    private ImageButton date;
    private Button submit;

    ArrayAdapter<String> adapter;
    SpinnerDialog spinnerDialog;
    ArrayList<String> items;
    LinearLayout ll;

    static TextView finalcost;
    static TextView currentdate;


    public Frag2() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v2= inflater.inflate(R.layout.fragment_frag2, container, false);
        tv_category = (TextView)v2.findViewById(R.id.frag2_category);
        tv_total = (TextView)v2.findViewById(R.id.total_cost);

        date = (ImageButton) v2.findViewById(R.id.date_pick);
        submit =(Button)v2.findViewById(R.id.submit);
        ll =(LinearLayout) v2.findViewById(R.id.ll);
        currentdate=(TextView)v2.findViewById(R.id.currentdate);
        finalcost=(TextView)v2.findViewById(R.id.finalcost);

        //Disable Icon


        items=new ArrayList<>();


        category = getArguments().getString("Category");
        tv_category.setText(category);

        dialog = new ProgressDialog(context);
        CustomerSelectCategory.currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        dateSelected = true;

        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerDialog.showSpinerDialog();
            }
        });


        int magId = getResources().getIdentifier("android:id/search_mag_icon", null, null);
        Toast.makeText(context, "Id "+magId, Toast.LENGTH_SHORT).show();


        //Attach XML with Code
        oder_items_recycler = (RecyclerView)v2.findViewById(R.id.frag_recycler_items);
        oder_items_recycler.setHasFixedSize(true);
        oder_items_recycler.setLayoutManager(new LinearLayoutManager(context));

        orders = new ArrayList<>();
        ordersAdapter = new Service_Selected_Adapter(orders, context);
        oder_items_recycler.setAdapter(ordersAdapter);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DialogFragment();
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selectedItems.size() < 1){
                    Toast.makeText(context, "You must Select at least 1 Item", Toast.LENGTH_SHORT).show();
                }
                else if(!dateSelected){
                    Toast.makeText(context, "Invalid Date Selected", Toast.LENGTH_SHORT).show();
                }
                else {

                    Intent i = new Intent(getActivity(), Maps.class);
                    i.putExtra("category",category);
                    i.putExtra("date",CustomerSelectCategory.currentDate);
                    i.putExtra("total",String.valueOf(Service_Selected_Adapter.total));
                    startActivity(i);
                    getActivity().finish();
                }
            }
        });

        //Here
        spinnerDialog=new SpinnerDialog(getActivity(),items,"Search for Service","Close");

        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                View v = oder_items_recycler.getLayoutManager().findViewByPosition(position);
                CheckBox ck = v.findViewById(R.id.checkBox);
                TextView tv = v.findViewById(R.id.service_charge);
                String str= tv.getText().toString();
                if(!ck.isChecked()){
                    selectedItems.add(item);
                    total +=Long.parseLong(str.substring(1,str.length()));

                    ck.setChecked(true);
                }
                else {
                    selectedItems.remove(item);
                    total -=Long.parseLong(str.substring(1,str.length()));

                    ck.setChecked(false);
                }
                tv_total.setText("₹"+String.valueOf(total));
                finalcost.setText("₹"+String.valueOf(total+((5*total)/100)));

            }
        });



        return v2;
    }



    @Override
    public void onStart() {
        super.onStart();

        get_Items();
    }

    private void get_Items() {
        dialog.setMessage("Fetching Services");
        dialog.show();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("services").child(category);

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dshot: dataSnapshot.getChildren()) {
                    GetSetServicesSelected gsss = new GetSetServicesSelected(dshot.getValue(Long.class)
                            ,dshot.getKey());
                    items.add(dshot.getKey());
                    orders.add(gsss);
                }
                ordersAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }


    @Override
    public void onResume() {
        super.onResume();
        orders.clear();
        items.clear();
    }


}


