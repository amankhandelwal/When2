package com.when.threemb.when;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TimeTable extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView top,bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "An Initiative of Team 3MB...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        int dow = (calendar.get(Calendar.DAY_OF_WEEK)) - 1;
        String days="Monday";

        switch(dow)
        {
            case 1:days="Monday";
                break;
            case 2:days="Tuesday";
                break;
            case 3:days="Wednesday";
                break;
            case 4:days="Thursday";
                break;
            case 5:days="Friday";
                break;
            default:
                Toast.makeText(TimeTable.this, "HOLIDAY", Toast.LENGTH_SHORT).show();
                finish();
        }
        SharedPreferences sp=getSharedPreferences("Userinfo",MODE_PRIVATE);
        /*top=(TextView)findViewById(R.id.topText);
        bottom=(TextView)findViewById(R.id.bottomText);
        top.setText(sp.getString("Name","When"));
        bottom.setText(sp.getString("Dept","")+" "+sp.getString("Roll","")+" \n Group: "+sp.getString("Group",""));*/
        /*
        if(sp.getInt("Login",0)==0){
            LoginActivity.loginwa.finish();
            SharedPreferences.Editor editor1=sp.edit();
            editor1.putInt("Login",1);
            editor1.apply();
        }*/
        firstfunction(days);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.time_table, menu);
        return true;
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the attendance action
//            Toast.makeText(TimeTable.this, "Under Progress", Toast.LENGTH_SHORT).show();
            secondfunction();
        } else if (id == R.id.nav_gallery) {
            // Handle the monday action
            firstfunction("Monday");

        } else if (id == R.id.nav_slideshow) {
            // Handle the tuesday action
            firstfunction("Tuesday");

        } else if (id == R.id.nav_manage) {
            // Handle the wednesday action
            firstfunction("Wednesday");

        } else if (id == R.id.nav_manage2) {
            // Handle the thursday action
            firstfunction("Thursday");

        }else if (id == R.id.nav_manage3) {
            // Handle the friday action
            firstfunction("Friday");

        }else if (id == R.id.nav_share) {
            Toast.makeText(TimeTable.this, "Under Progress", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(TimeTable.this,OtherDays.class);
            startActivity(intent1);


        } else if (id == R.id.nav_send) {
            Toast.makeText(TimeTable.this, "Under Progress", Toast.LENGTH_SHORT).show();
            /*Intent intent1 = new Intent(TimeTable.this,SeeAnnouncement.class);
            startActivity(intent1);*/
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    TimetableAdapter adapter;
    ListView timetableListView;
    public void firstfunction(String days)
    {
        Toast.makeText(TimeTable.this, days, Toast.LENGTH_SHORT).show();
        DatabaseHandler dbh=new DatabaseHandler(this);

        ArrayList<TimetableObject> periods=dbh.getAllPeriods(days);
        timetableListView = (ListView) findViewById(R.id.list);
        adapter = new TimetableAdapter(this, 0, periods);
        timetableListView.setAdapter(adapter);

    }

    public void secondfunction()
    {
        DatabaseHandler dbh=new DatabaseHandler(this);

        ArrayList<TimetableObject> periods=dbh.getAttendanceView();
        timetableListView = (ListView) findViewById(R.id.list);
        adapter = new TimetableAdapter(this, 0, periods);
        timetableListView.setAdapter(adapter);
    }


}
