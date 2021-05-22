package com.example.jiraiya.myapplication;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerSelectCategory extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    Spinner sp;
    TextView item;
    ArrayAdapter <String> adapter;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> mySerivce = new ArrayList<>();
    CheckBox checkBox;
    String text;
    Integer totalCost = 0;
    static Boolean dateSelected = false;
    static String currentDate="";
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    TextView nav_name;
    TextView nav_mob;
    CircleImageView nav_image;



    private SearchView.SearchAutoComplete   mSearchAutoComplete;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_select_category);



        navigationView =(NavigationView)findViewById(R.id.nav_view);
        drawerLayout =(DrawerLayout)findViewById(R.id.drawer);
        toggle =new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        View hView =  navigationView.getHeaderView(0);
        nav_name = (TextView)hView.findViewById(R.id.nav_name);
        nav_mob =(TextView)hView.findViewById(R.id.number);
        nav_image =(CircleImageView)hView.findViewById(R.id.nav_image);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                switch (id) {
                    case R.id.book_a_service:
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        Toast.makeText(CustomerSelectCategory.this, "Book a Service", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.requests:
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        Toast.makeText(CustomerSelectCategory.this, "See Requests", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.notification:
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        Toast.makeText(CustomerSelectCategory.this, "See Notification", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.contact:
                        Toast.makeText(CustomerSelectCategory.this, "Contact", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.about:
                        Toast.makeText(CustomerSelectCategory.this, "About" , Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.settings:
                        Toast.makeText(CustomerSelectCategory.this, "Settings", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.logout:
                        Toast.makeText(CustomerSelectCategory.this, "Logout", Toast.LENGTH_SHORT).show();
                        break;
                }

                return false;
            }
        });



    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);

    }



    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
     Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, day);
            currentDate = DateFormat.getDateInstance(DateFormat.SHORT).format(c.getTime());
            Frag2.currentdate.setText(currentDate);

        final Calendar cal = Calendar.getInstance();
        String today =  DateFormat.getDateInstance(DateFormat.SHORT).format(cal.getTime());

        if (currentDate.compareTo(today) < 0) {
            Toast.makeText(CustomerSelectCategory.this,"Inappropriate Date Selected", Toast.LENGTH_SHORT).show();
            Frag2.currentdate.setText(".. Invalid ..");
            dateSelected = false;
        }else{
            Frag2.currentdate.setText(currentDate);
            Toast.makeText(CustomerSelectCategory.this,"Date "+currentDate, Toast.LENGTH_SHORT).show();
            dateSelected = true;
        }



    }



}
