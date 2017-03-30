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
        //RequestQueue queue = Volley.newRequestQueue(this);
        //String url ="https://fast-plateau-72318.herokuapp.com/types";
        /////
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "https://fast-plateau-72318.herokuapp.com/types";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("type", type);
            final String mRequestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    System.out.println("Resp="+response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    // As of f605da3 the following should work
                    NetworkResponse response = error.networkResponse;
                    if (error instanceof ServerError && response != null) {
                        try {
                            String res = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                            // Now you can use any deserializer to make sense of data
                            JSONObject obj = new JSONObject(res);
                        } catch (UnsupportedEncodingException e1) {
                            // Couldn't properly decode data to string
                            e1.printStackTrace();
                        } catch (JSONException e2) {
                            // returned data is not JSONObject?
                            e2.printStackTrace();
                        }
                    }
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    System.out.println("H="+response.headers);
                    try {
                        System.out.println("D="+ this.getBody().toString());
                    } catch (AuthFailureError authFailureError) {
                        authFailureError.printStackTrace();
                    }

                    String responseString = "";
                    if (response != null) {
                        System.out.println(response.toString());
                        try {
                            try {
                                responseString =new String(this.getBody(), HttpHeaderParser.parseCharset(response.headers));
                            } catch (AuthFailureError authFailureError) {
                                authFailureError.printStackTrace();
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                    }

                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }

            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ////////
        /*
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String requestBody = jsonBody.toString();
        System.out.println("Body="+requestBody);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("RESP="+response);
                arr.clear();
                String[] list = response.split("u");
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
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {

                    responseString = String.valueOf(response.statusCode);

                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        */
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
