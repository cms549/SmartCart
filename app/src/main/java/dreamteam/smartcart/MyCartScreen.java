package dreamteam.smartcart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Set;

/**
 * Displays current cart, the items in it, the total
 * Allows you to delete items and modify the amount of each
 * Buttons to search for an item -> by barcode, by search or by categories (in search fragment)
 */
public class MyCartScreen extends AppCompatActivity {

    SearchFragment searchfrag;
    TextView tvbal;
    ListView lvCart;
    double balance;
    ArrayList<Item> itemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);



        balance=0;
        //set up search bar
        Intent intent = getIntent();
        searchfrag = (SearchFragment) intent.getSerializableExtra("searchfrag");
        if(searchfrag==null){
            searchfrag = new SearchFragment();
        }
        getSupportFragmentManager().beginTransaction().add(R.id.fragsearch, searchfrag).commit();

        tvbal = (TextView) findViewById(R.id.tvbal);

        itemsList = new ArrayList<Item>();

        loadFromSP();


        lvCart = (ListView) findViewById(R.id.lvSearch);

        //CREATE ADPATER
        MyCartAdapter ad = new MyCartAdapter(this, R.layout.row_mycart, itemsList);

        //Set adapter
        lvCart.setAdapter(ad);


        //DELETE SOMETHING FROM LIST
        lvCart.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> taskList, View v, int pos, long id) {
                if(pos==0){
                    //can't delete the header row
                    return true;
                }
                Item i = itemsList.get(pos);
                double pi = i.price *i.quant;
                balance = balance-pi;
                tvbal.setText("Total: $"+balance);
                itemsList.remove(pos);
                lvCart.invalidateViews();
                saveToSP();
                return true;
            }
        });


        lvCart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> taskList, View v, int pos, long id) {
                Item i = itemsList.get(pos);
                //open up list view for this specific type
                Intent nextScreen = new Intent(getApplication(), MoreInfoScreen.class);
                nextScreen.putExtra("barcode", i.barcode);
                //start next screen
                startActivity(nextScreen);


            }
        });


    }

    /**
     * Loads all three array lists and the balance
     */
    private void loadFromSP(){

        itemsList.clear();
        String s = "Total: $";
        // Look at preferences
        SharedPreferences myPref = getSharedPreferences("SmartCart", 0);
        balance = 0;
        String[] cnames= myPref.getString("cnames", "").split(",");
        String[] cprices= myPref.getString("cprices", "").split(",");
        String[] cbarcdoes= myPref.getString("cbarcodes", "").split(",");
        String[] camts= myPref.getString("camts", "").split(",");


        for(int i=0; i<cnames.length; i++){
            if (!(cnames[i].equals(""))){
                Item it = new Item(cnames[i], Integer.parseInt(camts[i]), Double.parseDouble(cprices[i]), cbarcdoes[i]);
                itemsList.add(it);
                balance= balance+it.price*it.quant;
            }

        }

        tvbal.setText(s+balance);

    }


    /**
     * Saves the lists and balance
     */
    private void saveToSP(){
        if(itemsList==null){
            return;
        }
        //update shared pref
        SharedPreferences myPref = getSharedPreferences("SmartCart", 0);
        SharedPreferences.Editor editor = myPref.edit();
        String cnames="", cprices="", camts="", cbarcodes="";
        for(int i=0; i<itemsList.size(); i++){
            Item c = itemsList.get(i);
            cnames = cnames+ ","+c.item;
            cprices= cprices + ","+c.price;
            camts= camts+","+c.quant;
            cbarcodes=cbarcodes+","+c.barcode;
        }

        editor.putString("cnames", ""+cnames);
        editor.putString("cprices", ""+cprices);
        editor.putString("cbarcodes", ""+cbarcodes);
        editor.putString("camts", ""+camts);
        editor.apply();

        editor.commit();

    }

}
