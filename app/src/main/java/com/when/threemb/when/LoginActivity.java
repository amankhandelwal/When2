package com.when.threemb.when;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    //TODO change Post URL
    private static final String REGISTER_URL = "http://31f375db.ngrok.io/website/apilogin.php";
    //TODO change GEt URL
    String urli = "http://31f375db.ngrok.io/website/apitimetable.php";
    RequestQueue requestQueue;

    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";


    private EditText editTextUsername;
    private EditText editTextPassword;

    private Button loginButton;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    int roll,sem,group,year;
    String dept,name;
    public static Activity loginwa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("LOGIN ");
        editTextUsername = (EditText) findViewById(R.id.username);
        editTextPassword = (EditText) findViewById(R.id.password);
    }

    private void registerUser() {
        final String username = editTextUsername.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        if (username.length()!=0 && password.length()!=0) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(LoginActivity.this,"Sent successfully !", Toast.LENGTH_LONG).show();
                            //request roll/sem/group/dept/name
                            if(response.equalsIgnoreCase("error"))
                                Toast.makeText(LoginActivity.this, response+": Incorrect Credentials", Toast.LENGTH_LONG).show();
                            else
                            parseJson(response);
                            //finish();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(LoginActivity.this, "FAILED " + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(KEY_USERNAME, username);
                    params.put(KEY_PASSWORD, password);
                    return params;
                }

            };

            requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
        else
        {
            Toast.makeText(this,"Invalid Username/Password",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void parseJson(String response) {

        try {
            JSONObject k = new JSONObject(response);
            JSONObject j= k.optJSONObject("keyvalue");
            name=j.optString("Stud_Name");
            dept=j.optString("Stud_Dept");
            roll=j.optInt("Stud_Roll_No");
            sem=j.optInt("Stud_Sem");
            year=(sem+1)/2;

            group=j.optInt("Group_No");

            Toast.makeText(this,"Credentials : "+name+" "+dept+" "+roll,Toast.LENGTH_LONG).show();

            sp=getSharedPreferences("Userinfo",MODE_PRIVATE);
            editor=sp.edit();
            editor.putString("Name",name);
            editor.putString("Dept",dept);
            editor.putInt("Roll",roll);
            editor.putInt("Sem",sem);
            editor.putInt("Year",year);
            editor.putInt("Group",group);
            editor.putInt("Status",1);
            editor.apply();

            //Toast.makeText(LoginActivity.this, "Status was:"+sp.getInt("Status",0), Toast.LENGTH_SHORT).show();

            retrieveData(year,group);


        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
    }

    public void loginClick(View v) {
        Toast.makeText(LoginActivity.this, "Logging in...Please Wait !!", Toast.LENGTH_SHORT).show();
        registerUser();
    }

    public void retrieveData(int year,int group){
        urli+="?year="+year+"&group="+group;
        //Toast.makeText(LoginActivity.this, urli, Toast.LENGTH_SHORT).show();

        requestQueue = Volley.newRequestQueue(this);

        final JsonObjectRequest kor=new JsonObjectRequest(Request.Method.GET, urli, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject ob) {

                        //Toast.makeText(LoginActivity.this,ob.toString().substring(2,40), Toast.LENGTH_SHORT).show();


                        if(ob!=null){
                            DatabaseHandler object=new DatabaseHandler(LoginActivity.this);

                            //TODO PARSE JSON DATA
                            try {
                                JSONObject monday = ob.optJSONObject("Monday");
                                JSONObject tuesday = ob.optJSONObject("Tuesday");
                                JSONObject wednesday = ob.optJSONObject("Wednesday");
                                JSONObject thursday = ob.optJSONObject("Thursday");
                                JSONObject friday = ob.optJSONObject("Friday");
                                int i;
                                String Ppr_Code, Fac_Initials, Room_No, dow;
                                int Start_Time, End_Time;

                                //For Monday
                                {
                                    JSONArray APpr_Code = monday.optJSONArray("Ppr_Code");
                                    JSONArray AFac_Initials = monday.optJSONArray("Fac_Initials");
                                    JSONArray ARoom_No = monday.optJSONArray("Room_No");
                                    JSONArray AStart_Time = monday.optJSONArray("Start_Time");
                                    JSONArray AEnd_Time = monday.optJSONArray("End_Time");

                                    for (i = 0; i < APpr_Code.length(); i++) {

                                        Ppr_Code = APpr_Code.optString(i);
                                        Fac_Initials = AFac_Initials.optString(i);
                                        Room_No = ARoom_No.optString(i);
                                        Start_Time = AStart_Time.optInt(i);
                                        End_Time = AEnd_Time.optInt(i);
                                        dow = "Monday";
                                        MyTimetable obj = new MyTimetable(Ppr_Code, Fac_Initials, Room_No, Start_Time, End_Time, dow);
                                        object.addPeriod(obj);
                                    }
                                }

                                //For Tuesday
                                {
                                    JSONArray APpr_Code = tuesday.optJSONArray("Ppr_Code");
                                    JSONArray AFac_Initials = tuesday.optJSONArray("Fac_Initials");
                                    JSONArray ARoom_No = tuesday.optJSONArray("Room_No");
                                    JSONArray AStart_Time = tuesday.optJSONArray("Start_Time");
                                    JSONArray AEnd_Time = tuesday.optJSONArray("End_Time");

                                    for (i = 0; i < APpr_Code.length(); i++) {

                                        Ppr_Code = APpr_Code.optString(i);
                                        Fac_Initials = AFac_Initials.optString(i);
                                        Room_No = ARoom_No.optString(i);
                                        Start_Time = AStart_Time.optInt(i);
                                        End_Time = AEnd_Time.optInt(i);
                                        dow = "Tuesday";
                                        //Toast.makeText(LoginActivity.this, Ppr_Code + dow, Toast.LENGTH_SHORT).show();
                                        MyTimetable obj = new MyTimetable(Ppr_Code, Fac_Initials, Room_No, Start_Time, End_Time, dow);
                                        object.addPeriod(obj);
                                    }
                                }

                                //For Wednesday
                                {
                                    JSONArray APpr_Code = wednesday.optJSONArray("Ppr_Code");
                                    JSONArray AFac_Initials = wednesday.optJSONArray("Fac_Initials");
                                    JSONArray ARoom_No = wednesday.optJSONArray("Room_No");
                                    JSONArray AStart_Time = wednesday.optJSONArray("Start_Time");
                                    JSONArray AEnd_Time = wednesday.optJSONArray("End_Time");

                                    for (i = 0; i < APpr_Code.length(); i++) {

                                        Ppr_Code = APpr_Code.optString(i);
                                        Fac_Initials = AFac_Initials.optString(i);
                                        Room_No = ARoom_No.optString(i);
                                        Start_Time = AStart_Time.optInt(i);
                                        End_Time = AEnd_Time.optInt(i);
                                        dow = "Wednesday";
                                        //Toast.makeText(LoginActivity.this, Ppr_Code + dow, Toast.LENGTH_SHORT).show();
                                        MyTimetable obj = new MyTimetable(Ppr_Code, Fac_Initials, Room_No, Start_Time, End_Time, dow);
                                        object.addPeriod(obj);
                                    }
                                }

                                //For Thursday
                                {
                                    JSONArray APpr_Code = thursday.optJSONArray("Ppr_Code");
                                    JSONArray AFac_Initials = thursday.optJSONArray("Fac_Initials");
                                    JSONArray ARoom_No = thursday.optJSONArray("Room_No");
                                    JSONArray AStart_Time = thursday.optJSONArray("Start_Time");
                                    JSONArray AEnd_Time = thursday.optJSONArray("End_Time");

                                    for (i = 0; i < APpr_Code.length(); i++) {

                                        Ppr_Code = APpr_Code.optString(i);
                                        Fac_Initials = AFac_Initials.optString(i);
                                        Room_No = ARoom_No.optString(i);
                                        Start_Time = AStart_Time.optInt(i);
                                        End_Time = AEnd_Time.optInt(i);
                                        dow = "Thursday";
                                        MyTimetable obj = new MyTimetable(Ppr_Code, Fac_Initials, Room_No, Start_Time, End_Time, dow);
                                        object.addPeriod(obj);
                                    }
                                }

                                //For Friday
                                {
                                    JSONArray APpr_Code = friday.optJSONArray("Ppr_Code");
                                    JSONArray AFac_Initials = friday.optJSONArray("Fac_Initials");
                                    JSONArray ARoom_No = friday.optJSONArray("Room_No");
                                    JSONArray AStart_Time = friday.optJSONArray("Start_Time");
                                    JSONArray AEnd_Time = friday.optJSONArray("End_Time");

                                    for (i = 0; i < APpr_Code.length(); i++) {

                                        Ppr_Code = APpr_Code.optString(i);
                                        Fac_Initials = AFac_Initials.optString(i);
                                        Room_No = ARoom_No.optString(i);
                                        Start_Time = AStart_Time.optInt(i);
                                        End_Time = AEnd_Time.optInt(i);
                                        dow = "Friday";
                                        MyTimetable obj = new MyTimetable(Ppr_Code, Fac_Initials, Room_No, Start_Time, End_Time, dow);
                                        object.addPeriod(obj);
                                    }
                                }


                                //TODO Copy paste for other days
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                            finally {
                                //MainActivity object12=new MainActivity();
                                getAttendanceVolley();
                                /*Intent TimeTable=new Intent(LoginActivity.this,TimeTable.class);
                                startActivity(TimeTable);*/

                            }


                        }else
                            Toast.makeText(LoginActivity.this, "NO OBJECT received", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley","Error");
                        Toast.makeText(LoginActivity.this,error.toString(), Toast.LENGTH_SHORT).show();
                        sp=getSharedPreferences("Userinfo",MODE_PRIVATE);
                        editor=sp.edit();
                        editor.putInt("Status",0);
                        editor.apply();
                    }
                }
        );

        requestQueue.add(kor);
    }

    public void getAttendanceVolley(){
        requestQueue = Volley.newRequestQueue(this);
        //TODO change get url
        String urli="http://31f375db.ngrok.io/website/apiattendance.php";//?ROLL=
        urli+="?roll="+roll;

        //Toast.makeText(LoginActivity.this, "Att url :"+urli, Toast.LENGTH_SHORT).show();

        final JsonObjectRequest kor=new JsonObjectRequest(Request.Method.GET, urli, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject ob) {

                        //Toast.makeText(LoginActivity.this, "ATT: "+ob.toString(), Toast.LENGTH_SHORT).show();
                        if(ob!=null){
                            JSONArray subject= ob.optJSONArray("Ppr_Codes");
                            JSONArray attend= ob.optJSONArray("Attend");
                            //TODO add total
                            JSONArray total= ob.optJSONArray("total");
                            DatabaseHandler dbhandler=new DatabaseHandler(LoginActivity.this);
                            for(int i=0;i<subject.length() && i<attend.length() && i<total.length();i++)
                            {
                                /*if(false)//TODO add status condition
                                    dbhandler.updateAttendance(subject.optString(i),attend.optInt(i),10*//*total.optInt(i)*//*);
                                else*/
                                    dbhandler.addAttendance(subject.optString(i),attend.optInt(i),total.optInt(i));
                            }

                            Intent TimeTable=new Intent(LoginActivity.this,TimeTable.class);
                            startActivity(TimeTable);



                        }
                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley","Error");
                        Toast.makeText(LoginActivity.this, "No data recieved", Toast.LENGTH_SHORT).show();
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


