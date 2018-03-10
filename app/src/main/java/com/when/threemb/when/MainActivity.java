package com.when.threemb.when;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    int roll=0;
   int status;


    //http://9c418de8.ngrok.io/website/login

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toast.makeText(this,"call func",Toast.LENGTH_SHORT).show();

        FirebaseInstanceId.getInstance().getToken();
        /*finish();*/
        SharedPreferences sp=getSharedPreferences("Userinfo",MODE_PRIVATE);
        status=sp.getInt("Status",0);
        roll=sp.getInt("Roll",0);
        //Toast.makeText(MainActivity.this, "Status:"+status+" Roll:"+roll, Toast.LENGTH_SHORT).show();
        if(status==0 && laaDo()) {
            Intent login = new Intent(this, LoginActivity.class);
            startActivity(login);
            //finish();
        }
        else if(status==1 && laaDo())
        {
            getAttendanceVolley();
            Intent TimeTable=new Intent(this,TimeTable.class);
            startActivity(TimeTable);
            //finish();
        }
        else if(status==1)
        {
            Intent TimeTable=new Intent(this,TimeTable.class);
            startActivity(TimeTable);
            //finish();
        }
        else
        {
            Toast.makeText(MainActivity.this, "RETRY LATER ", Toast.LENGTH_SHORT).show();
        }
    }




    public Boolean laaDo(/*View view*/)//Check internet connection
    {

        //Toast.makeText(MainActivity.this, "Please Wait !!", Toast.LENGTH_SHORT).show();
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // fetch data
            //volley();
            return true;

        } else {
            // display error
            Toast.makeText(MainActivity.this, "NO INTERNET CONNECTION !!", Toast.LENGTH_SHORT).show();
            return false;

        }

    }

    public void getAttendanceVolley(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //TODO change get url
        String urli="http://31f375db.ngrok.io/website/apiattendance.php";//?ROLL=
        urli+="?roll="+roll;

        //Toast.makeText(MainActivity.this, "Att url :"+urli, Toast.LENGTH_SHORT).show();

        final JsonObjectRequest kor=new JsonObjectRequest(Request.Method.GET, urli, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject ob) {

                        //Toast.makeText(MainActivity.this, "ATT: "+ob.toString(), Toast.LENGTH_SHORT).show();
                        if(ob!=null){
                            JSONArray subject= ob.optJSONArray("Ppr_Codes");
                            JSONArray attend= ob.optJSONArray("Attend");
                            //TODO add total
                            JSONArray total= ob.optJSONArray("total");
                            DatabaseHandler dbhandler=new DatabaseHandler(MainActivity.this);
                            for(int i=0;i<subject.length() && i<attend.length() && i<total.length();i++)
                            {
                               /* *//*if(false)//TODO add status condition*/
                                    dbhandler.updateAttendance(subject.optString(i),attend.optInt(i),total.optInt(i));
                                /*else*//*
                                dbhandler.addAttendance(subject.optString(i),attend.optInt(i),10*//*total.optInt(i)*//*);*/
                            }



                        }
                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley","Error");
                        Toast.makeText(MainActivity.this, "No data recieved", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(kor);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
