package dreamteam.smartcart;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


/**
 * Result of  a query -> shows list of possible items you want
 */
public class ItemListScreen extends AppCompatActivity {

    ListView lvSearch;
    ArrayList<String> arr;
    boolean gotResponse;
    ArrayList<ItemType>typeList;
    MyListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);




        lvSearch = (ListView) findViewById(R.id.lvSearch);
        arr = new ArrayList<String>();
        typeList=new ArrayList<ItemType>();
        gotResponse = false;

        //MyListAdapter
        listAdapter=new MyListAdapter(this,R.layout.row_itemlist,typeList);

        lvSearch.setAdapter(listAdapter);

       // lvSearch.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arr));

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
                    nextScreen.putExtra("itemID", typeList.get(pos).barcode);
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
        String url ="https://fast-plateau-72318.herokuapp.com/allitems";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                arr.clear();

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
    private void sendRequestType(final String type) {
        // Instantiate the RequestQueue.

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://fast-plateau-72318.herokuapp.com/onetype?type="+type;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("RESPONSE : "+response);
                //name,price,barcode
                String[] itts= response.split("\\}");
                for (int i=0;i<itts.length;i++){//for each item
                    ItemType item=new ItemType();
                    String[] fields = itts[i].split(",");
                    for (int j=0;j<fields.length;j++){
                        String st = fields[j];
                        int f = st.indexOf("'");
                        int se =st.indexOf("'",f+1);
                        int th= st.indexOf("'",se+1);
                        int fo= st.indexOf("'",th+1);
                        String kw = st.substring(f+1,se);
                        String value = st.substring(th+1,fo);
                        if(kw.equals("name")){
                            item.name=value;
                        }
                        else if(kw.equals("price")){
                            item.price=Double.parseDouble(value);
                        }
                        else if(kw.equals("barcode")){
                            item.barcode=value;
                        }
                        else if(kw.equals("type")){
                            item.type=value;
                        }

                    }
                    typeList.add(item);
                }
                listAdapter.notifyDataSetChanged();




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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
        String url ="https://fast-plateau-72318.herokuapp.com/searchitem?keyword="+name;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.println(response);

                String[] stuff=response.split("\\}");
                for (int i=0;i<stuff.length;i++) {
                    System.out.println(stuff[i]);
                    ItemType item = new ItemType();
                    String[] fields = stuff[i].split(",");
                    for (int j=0;j<fields.length;j++){
                        String st = fields[j];
                        int f = st.indexOf("'");
                        int se =st.indexOf("'",f+1);
                        int th= st.indexOf("'",se+1);
                        int fo= st.indexOf("'",th+1);
                        String kw = st.substring(f+1,se);
                        String value = st.substring(th+1,fo);
                        if(kw.equals("name")){
                            item.name=value;
                        }
                        else if(kw.equals("price")){
                            item.price=Double.parseDouble(value);
                        }
                        else if(kw.equals("barcode")){
                            item.barcode=value;
                        }
                        else if(kw.equals("type")){
                            item.type=value;
                        }

                    }
                    typeList.add(item);
                }
                listAdapter.notifyDataSetChanged();



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
