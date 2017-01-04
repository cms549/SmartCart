package dreamteam.smartcart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

/**
 * Give user option categories to look in
 */
public class ItemTypesScreen extends AppCompatActivity {

    /**
     * Data structure to hold types
     */
    private ArrayList<String> listOfTypes;
    private ListView lvType;
    private boolean gotTypes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_type);

        lvType = (ListView) findViewById(R.id.lvType);
        gotTypes=false;

        listOfTypes = new ArrayList<String>();
        if(isOnline()) {
            loadTypes();
        }
        else{
            listOfTypes.add("Must connect to internet to use this service.");
        }

        ArrayAdapter<String> ad = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listOfTypes);

        //Set adapter
        lvType.setAdapter(ad);

        //DELETE SOMETHING FROM LIST
        lvType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> taskList, View v, int pos, long id) {
                if(gotTypes) {
                    //open up list view for this specific type
                    Intent nextScreen = new Intent(getApplication(), ItemListScreen.class);
                    nextScreen.putExtra("type", listOfTypes.get(pos));
                    //start next screen
                    startActivity(nextScreen);
                }

            }
        });




    }

    private void loadTypes() {
        listOfTypes.add("Loading types...");
        // http request
        sendARequest();
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

    /**
     * Sends server a request for occupancy
     */
    private void sendARequest() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://www.google.com";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                gotTypes=true;
                listOfTypes.clear();
                String[] list = response.split(",");
                if(list==null){
                    return;
                }
                for(int i=0; i<list.length; i++){
                    if(list[i]==""){
                        continue;
                    }
                    listOfTypes.add(list[i]);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listOfTypes.add("N/A");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }





}
