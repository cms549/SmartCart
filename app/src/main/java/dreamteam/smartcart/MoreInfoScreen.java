package dreamteam.smartcart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Set;

public class MoreInfoScreen extends AppCompatActivity {

    TextView tvName;
    TextView tvPrice;
    TextView tvAisle;
    TextView tvDesc;
    EditText etAmt;

    String name;
    double price;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info_screen);

        tvName = (TextView) findViewById(R.id.tvName);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvAisle = (TextView) findViewById(R.id.tvAisle);
        tvDesc = (TextView) findViewById(R.id.tvDescription);
        etAmt = (EditText) findViewById(R.id.etAmt);


        //Grab the input
        Intent intent = getIntent();
        String id = (String) intent.getSerializableExtra("itemID");
        if(isOnline()){
            sendRequest(id);
        }
        else{
            tvName.setText("Cannot connect to server.");
        }

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
     * Sends server a request for all items
     */
    private void sendRequest(String id) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://www.google.com/id/"+id;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String[] list = response.split(",");
                //set the name, price for both variable and for text view


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void findInStore(View view) {
        //open up the map view for it
        if(isOnline()){
            Toast.makeText(this, "Error: Cannot Connect To Server", Toast.LENGTH_LONG).show();
            return;
        }

    }

    /**
     * Adds the item to the cart and updates the balance, if edit text for the amount is not a number it
     * @param view
     */
    public void addToCart(View view) {
        //Grab amount
        String samt = etAmt.getText().toString();
        int quant = Integer.parseInt(samt);
        if(quant==-1){
            Toast.makeText(this, "Error: Must Specify Positive Number to Add to Cart", Toast.LENGTH_LONG).show();
            return;
        }
        //Add to cart

        //Update shared pref - balance and list
        Gson gson = new Gson();
        // Look at preferences
        SharedPreferences myPref = getSharedPreferences("SmartCart", 0);
        SharedPreferences.Editor editor = myPref.edit();
        String bal= myPref.getString("cartbalance", "0.00");
        double balance = Double.parseDouble(bal);
        balance = balance + price*quant;
        ArrayList<String> itemsAsJSON = (ArrayList<String>) myPref.getStringSet("itemsList",null);
        if(itemsAsJSON==null){
            itemsAsJSON= new ArrayList<String>();
        }
        Item c = new Item(name,quant,price);
        String json = gson.toJson(c);
        itemsAsJSON.add(json);
        editor.putStringSet("itemsList", (Set<String>) itemsAsJSON);
        editor.putString("cartbalance", ""+balance);
        editor.commit();


    }



}
