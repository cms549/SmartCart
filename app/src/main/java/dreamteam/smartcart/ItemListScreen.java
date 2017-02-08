package dreamteam.smartcart;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;


/**
 * Result of  a query -> shows list of possible items you want
 */
public class ItemListScreen extends AppCompatActivity {

    ListView lvSearch;
    ArrayList<String> arr;
    boolean gotResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        lvSearch = (ListView) findViewById(R.id.lvSearch);
        arr = new ArrayList<String>();
        gotResponse = false;
        lvSearch.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arr));

        if(!isOnline()){
            arr.add("Must connect to the internet to use this feature");
            return;
        }

        //Grab the input
        Intent intent = getIntent();
        String name = (String) intent.getSerializableExtra("itemName");
        if(name==null) {

            String type = (String) intent.getSerializableExtra("type");
            if(type==null){
                //grab everything from server
                sendRequestAll();
            }
            else{
                //http request for type
                sendRequestType(type);
            }

        }
        else{
            //http request for name
            sendRequestName(name);
        }

        //DELETE SOMETHING FROM LIST
        lvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> taskList, View v, int pos, long id) {
                if(gotResponse) {
                    //open up specific info for this item
                    Intent nextScreen = new Intent(getApplication(), MoreInfoScreen.class);
                    nextScreen.putExtra("itemID", arr.get(pos));
                    //start next screen
                    startActivity(nextScreen);
                }

            }
        });

    }


    /**
     * Sends server a request for all items
     */
    private void sendRequestAll() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://127.0.0.1:5000/topics";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                arr.clear();
                String[] list = response.split(",");
                if(list==null){
                    return;
                }
                for(int i=0; i<list.length; i++){
                    if(list[i]==""){
                        continue;
                    }
                    arr.add(list[i]);
                }
                gotResponse = true;

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                arr.add("N/A");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /**
     * Sends server a request for all items in type type
     */
    private void sendRequestType(String type) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://www.google.com/type/"+type;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                arr.clear();
                String[] list = response.split(",");
                if(list==null){
                    return;
                }
                for(int i=0; i<list.length; i++){
                    if(list[i]==""){
                        continue;
                    }
                    arr.add(list[i]);
                }
                gotResponse = true;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                arr.add("N/A");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /**
     * Sends server a request for all items
     */
    private void sendRequestName(String name) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://www.google.com/name/"+name;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                arr.clear();
                String[] list = response.split(",");
                if(list==null){
                    return;
                }
                for(int i=0; i<list.length; i++){
                    if(list[i]==""){
                        continue;
                    }
                    arr.add(list[i]);
                }
                gotResponse = true;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                arr.add("N/A");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /**
     * Returns true if connected to internet (by data or wifi)
     * @return
     */
    private boolean isOnline(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            return true;
        }
        return false;
    }

}
