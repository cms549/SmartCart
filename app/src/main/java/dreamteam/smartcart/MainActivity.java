package dreamteam.smartcart;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    /**
     * Current Occupancy of store -> use wifi to grab this from database
     */
    TextView tvocc;

    /**
     * Running total of your cart -> grab this from shared preferences
     */
    TextView tvtot;


    MapFragment mapfrag;

    SearchFragment searchfrag;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvocc = (TextView) findViewById(R.id.tvocc);
        tvtot = (TextView) findViewById(R.id.tvtot);

        if(!isOnline()){
            TextView tv = (TextView) findViewById(R.id.tvmap);
            tv.setText("No internet connection.");
        }
        else if(isConnectedToCart()){
            mapfrag = new MapFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragMap, mapfrag).commit();
        }

        searchfrag = new SearchFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragsearch, searchfrag).commit();



        setOccupancyField();
        setMyCartBalance();
    }

    /**
     * Checks if you are already connected to a cart
     * @return
     */
    private boolean isConnectedToCart() {
        // Look at preferences
        SharedPreferences myPref = getSharedPreferences("SmartCart", 0);
        Boolean isconnected= myPref.getBoolean("cartconnected", false);

        //check shared pref
        if(isconnected){
           return true;
        }
        return false;
    }

    /**
     * Sets the balance text
     */
    private void setMyCartBalance() {
        String s = "$";
        // Look at preferences
        SharedPreferences myPref = getSharedPreferences("SmartCart", 0);
        String bal= myPref.getString("cartbalance", "0.00");
        tvtot.setText(s+bal);
    }

    /**
     * Sets the occupancy text
     */
    private void setOccupancyField() {
        //check if you have internet access
        if(isOnline()) {
            sendARequest();
        }
        else{
            tvocc.setText("N/A");
        }
    }

    /**
     * Sends server a request for occupancy
     */
    private void sendARequest() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        //String url ="http://www.google.com";
        String url ="http://127.0.0.1:5000/occupancy";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //String s = "Response is: "+ response.substring(0,500);
                        tvocc.setText(response.substring(0,10).trim());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        tvocc.setText("N/A");
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

    /**
     * Jumps to my cart screen -> get all info about cart from shared preferences
     * @param view
     */
    public void goToMyCart(View view) {
        Intent nextScreen = new Intent(getApplicationContext(), MyCartScreen.class);
        nextScreen.putExtra("searchfrag", searchfrag);
        //start next screen
        startActivity(nextScreen);
    }

    /**
     * Jumps to sync cart screen -> set up communication with bluetooth
     * @param view
     */
    public void goToSyncCart(View view) {
        Intent nextScreen = new Intent(getApplicationContext(), SyncCartScreen.class);
        //start next screen
        startActivity(nextScreen);

    }

    /**
     * Jumps to shooping list screen -> grab info from shared pref
     * @param view
     */
    public void goToShoppingList(View view) {
        Intent nextScreen = new Intent(getApplicationContext(), MyShoppingListScreen.class);
        //start next screen
        startActivity(nextScreen);
    }



}
