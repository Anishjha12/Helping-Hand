package com.example.jiraiya.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

 class Service_Selected_Adapter extends RecyclerView.Adapter<Service_Selected_Adapter.OrderHolder> {

    private List<GetSetServicesSelected> orders;
    private Context context;
    static long total=0;
    static ArrayList<String> selectedItems = new ArrayList<String>();



    //Constructor for initializations

    public Service_Selected_Adapter(List<GetSetServicesSelected> orders, Context context) {
        this.orders = orders;
        this.context = context;
    }

    //Creating a new ViewHolder(OrderHolder) suppling it with the required params and returning
    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.service_type,parent,false);

        return new OrderHolder(v);
    }

    //Called after the ViewHolder(OrderHolder) Object is created and populates with the data
    @Override
    public void onBindViewHolder(@NonNull final OrderHolder holder, int position) {

        final GetSetServicesSelected order = orders.get(position);

        holder.checkbox.setText(order.getService_type());
        holder.cost.setText("₹"+order.getCost());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(holder.checkbox.isChecked()){
                    holder.checkbox.setChecked(false);
                    total-= order.getCost();
                    selectedItems.remove(order.getService_type());
                    Frag2.tv_total.setText("₹"+String.valueOf(total));
                    Frag2.finalcost.setText("₹"+String.valueOf(total+((5*total)/100)));

                }else {
                    holder.checkbox.setChecked(true);
                    total+= order.getCost();
                    selectedItems.add(order.getService_type());

                    Frag2.tv_total.setText("₹"+String.valueOf(total));
                    Frag2.finalcost.setText("₹"+String.valueOf(total+((5*total)/100)));
                }

            }
        });


    }


    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class OrderHolder extends RecyclerView.ViewHolder{

        public CheckBox checkbox;
        public TextView cost;


        public OrderHolder(View itemView) {
            super(itemView);

            //Attaching XML with Code
            checkbox = (CheckBox)itemView.findViewById(R.id.checkBox);
            cost =(TextView)itemView.findViewById(R.id.service_charge);

        }

    }


}


