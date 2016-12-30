package dreamteam.smartcart;

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
        searchfrag = (SearchFragment) savedInstanceState.getSerializable("searchfrag");
        if(searchfrag==null){
            searchfrag = new SearchFragment();
        }
        getSupportFragmentManager().beginTransaction().add(R.id.fragsearch, searchfrag).commit();

        tvbal = (TextView) findViewById(R.id.tvbal);

        itemsList = new ArrayList<Item>();
        loadFromSP();


        lvCart = (ListView) findViewById(R.id.lvCart);

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



    }

    /**
     * Loads all three array lists and the balance
     */
    private void loadFromSP(){

        itemsList.clear();
        String s = "Total: $";
        Gson gson = new Gson();
        // Look at preferences
        SharedPreferences myPref = getSharedPreferences("SmartCart", 0);
        String bal= myPref.getString("cartbalance", "0.00");
        balance = Double.parseDouble(bal);
        tvbal.setText(s+bal);
        ArrayList<String> a = (ArrayList<String>) myPref.getStringSet("itemsList",null);
        if(a==null){
            return;
        }

        for(int i=0; i<a.size(); i++){
            String st = a.get(i);
            Item it = gson.fromJson(st, Item.class);
            itemsList.add(it);
        }

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
        ArrayList<String> itemsAsJSON = new ArrayList<String>(itemsList.size());
        Gson gson = new Gson();
        for(int i=0; i<itemsList.size(); i++){
            Item c = itemsList.get(i);
            String json = gson.toJson(c);
            itemsAsJSON.add(json);
        }


        editor.putStringSet("itemsList", (Set<String>) itemsAsJSON);
        editor.putString("cartbalance", ""+balance);
        editor.commit();

    }






    public void back(View view) {
        finish();
    }
}
