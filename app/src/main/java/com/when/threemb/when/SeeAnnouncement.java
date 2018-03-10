package com.when.threemb.when;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class SeeAnnouncement extends AppCompatActivity {

    String username="asd",message="asdd";
    ListView earthquakeListView= null;
    TextView output ;
//    Button go;
    String data = "";
    String urli="https://31f375db.ngrok.io/userinfo";

    RequestQueue requestQueue;
    ArrayList<MessageObj> earthquakes;

    AnnouncementAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_announcement);
        setTitle("Announcements");

        // Create a fake list of earthquake locations.
        earthquakeListView = (ListView) findViewById(R.id.listView1);
        output = (TextView) findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(output);

        /*ArrayList<MessageObj> */
        earthquakes = new ArrayList<>();


        /*earthquakes.add(new MessageObj(username,message));
        earthquakes.add(new MessageObj("abc",message));
        earthquakes.add(new MessageObj("siaya","sjhdsajd"));*/
        laaDo();


    }

    public void laaDo(/*View view*/)
    {

        Toast.makeText(SeeAnnouncement.this, "Please Wait !!", Toast.LENGTH_SHORT).show();
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // fetch data
            volley();

        } else {
            // display error
            output.setText("NO INTERNET CONNECTIVITY");

        }

    }

    public void volley(){
        requestQueue = Volley.newRequestQueue(this);

        final JsonObjectRequest kor=new JsonObjectRequest(Request.Method.GET, urli, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject ob) {

                        Toast.makeText(SeeAnnouncement.this, "fetching response", Toast.LENGTH_SHORT).show();
                        if(ob!=null){
                            JSONArray allu= ob.optJSONArray("allu");
                            data="";
                            for(int i=0;i<allu.length();i++)
                            {
                                JSONObject alluobj=allu.optJSONObject(i);
                                String username=alluobj.optString("username");
                                String message=alluobj.optString("message");
                                earthquakes.add(new MessageObj(username,message));
                            }
                            adapter=new AnnouncementAdapter(SeeAnnouncement.this,0,earthquakes);
                            earthquakeListView.setAdapter(adapter);
                            output.setText("No Announcements");


                        }
                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley","Error");
                        Toast.makeText(SeeAnnouncement.this, "No data recieved", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(kor);
    }

}
