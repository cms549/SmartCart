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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MoreInfoScreen extends AppCompatActivity {

    TextView tvName;
    TextView tvPrice;
    TextView tvType;
    TextView tvDesc;
    EditText etAmt;


    String rfid;
    String barcode;
    String name;
    double price;
    int x;
    int y;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info_screen);

        tvName = (TextView) findViewById(R.id.tvName);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvType = (TextView) findViewById(R.id.tvType);
        tvDesc = (TextView) findViewById(R.id.tvDescription);
        etAmt = (EditText) findViewById(R.id.etAmt);



        Intent intent=getIntent();
        String barcode=intent.getStringExtra("barcode");
        sendRequest(barcode);

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
        String url ="https://fast-plateau-72318.herokuapp.com/oneitem?barcode="+id;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("THE RESPONSE IS "+response);
                //set the name, price for both variable and for text view
                if (!(response.equals(""))){
                    String[] fields = response.split(",");

                    for (int j=0;j<fields.length;j++) {
                        String st = fields[j];
                        int f = st.indexOf("'");
                        int se = st.indexOf("'", f + 1);
                        int th = st.indexOf("'", se + 1);
                        int fo = st.indexOf("'", th + 1);
                        String kw = st.substring(f + 1, se);
                        String value = st.substring(th + 1, fo);
                        if (kw.equals("name")) {
                            name = value;
                        } else if (kw.equals("price")) {
                            price = Double.parseDouble(value);
                        } else if (kw.equals("barcode")) {
                            barcode = (value);
                        } else if (kw.equals("type")) {
                            tvType.setText(value);
                        } else if (kw.equals("dsc")) {
                            tvDesc.setText(value);
                        } else if (kw.equals("rfid")) {
                            rfid = value;
                        }

                    }


                    tvName.setText(name);
                    tvPrice.setText("$"+price);

                    sendRFIDrequest();

                }else {
                    makeToast();

                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void makeToast() {
        Toast.makeText(this,"No response from server",Toast.LENGTH_LONG).show();
    }

    private void sendRFIDrequest() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://fast-plateau-72318.herokuapp.com/onelocation?rfid="+rfid;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //set the name, price for both variable and for text view
                System.out.println(response);
                response = response.replace("}","");
                String[] fields = response.split(",");
                for (int j=0;j<fields.length;j++) {
                    String st = fields[j];
                    int f = st.indexOf("'");
                    int se = st.indexOf("'", f + 1);
                    int th = st.indexOf(":");
                    String kw = st.substring(f + 1, se);
                    String value = st.substring(th + 1).trim();
                    if (kw.equals("x")) {
                        x = Integer.parseInt(value);
                    } else if (kw.equals("y")) {
                        y = Integer.parseInt(value);
                    }


                }




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
        if(!isOnline()){
            Toast.makeText(this, "Error: Cannot Connect To Server", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent=new Intent(getApplicationContext(),MapActivity.class);
        intent.putExtra("ItemName",name);
        System.out.println("X="+x);
        intent.putExtra("x",x);
        intent.putExtra("y",y);
        //System.out.println(price);
        startActivity(intent);

    }

    /**
     * Adds the item to the cart and updates the balance, if edit text for the amount is not a number it
     * @param view
     */
    public void addToCart(View view) {
        //Grab amount
        String samt = etAmt.getText().toString();
        if (samt.equals("")){
            Toast.makeText(this,"Error: Must enter a number to add to cart",Toast.LENGTH_LONG).show();
            return;
        }
        int quant = Integer.parseInt(samt);
        if(quant==-1){
            Toast.makeText(this, "Error: Must Specify Positive Number to Add to Cart", Toast.LENGTH_LONG).show();
            return;
        }
        //Need to save the name, price, barcode, and amount for each item-> make 4 arrays where indexes match
        SharedPreferences myPref = getSharedPreferences("SmartCart", 0);
        SharedPreferences.Editor editor = myPref.edit();
        //SHARED PREFERENCES ARRAYS - HOW WE SAVED TO FILE
        String[] cnames= myPref.getString("cnames", "").split(",");
        String[] cprices= myPref.getString("cprices", "").split(",");
        String[] cbarcdoes= myPref.getString("cbarcodes", "").split(",");
        String[] camts= myPref.getString("camts", "").split(",");
        //search through all the names to see if repeat
        boolean found = false;
        for(int i=0; i<cnames.length; i++){
            if(name.equals(cnames[i].trim())){
                //increment the amount
                int lastamt = Integer.parseInt(camts[i]);
                camts[i]= (lastamt +quant)+"";
                //save the amount again
                String ca = "";
                for(int j=0; j<camts.length; j++){
                    ca= ca+camts[j]+",";
                }
                System.out.println("Orginal= "+myPref.getString("camts", ""));
                System.out.println("Newone=" +ca);
                editor.putString("camts", ca);
                editor.apply();
                editor.commit();
                found =true;
                break;
            }
        }
        if(!found){
            // add to all arrays
            String ns= myPref.getString("cnames", "")+","+name;
            String ps = myPref.getString("cprices", "")+","+price;
            String bs= myPref.getString("cbarcodes", "")+","+barcode;
            String cs= myPref.getString("camts", "")+","+samt;
            editor.putString("cnames", ""+ns);
            editor.putString("cprices", ""+ps);
            editor.putString("cbarcodes", ""+bs);
            editor.putString("camts", ""+cs);
            editor.apply();
            editor.commit();

        }



        Intent intent=new Intent(getApplicationContext(),MyCartScreen.class);
        startActivity(intent);




    }




}
