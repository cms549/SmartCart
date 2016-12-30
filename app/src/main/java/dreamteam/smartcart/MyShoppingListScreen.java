package dreamteam.smartcart;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Set;

/**
 *  Allows you to add anything to shopping list
 */
public class MyShoppingListScreen extends AppCompatActivity {

    /**
     * Data structure to hold items
     */
    private ArrayList<String> listOfItems;
    private EditText etSL;
    private ListView lvSL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        lvSL = (ListView) findViewById(R.id.lvSL);
        etSL = (EditText) findViewById(R.id.etSL);

        listOfItems = new ArrayList<String>();
        loadFromSP();

        ArrayAdapter<String> ad = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listOfItems);

        //Set adapter
        lvSL.setAdapter(ad);


        //DELETE SOMETHING FROM LIST
        lvSL.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> taskList, View v, int pos, long id) {
                listOfItems.remove(pos);
                lvSL.invalidateViews();
                saveToSP();
                return true;
            }
        });

    }

    /**
     * Adds item to list
     * @param view
     */
    public void addItemToList(View view) {

        String item = etSL.getText().toString();
        if(item.isEmpty()){
            Toast.makeText(this, "You forgot the task title.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(item.indexOf('\t')>-1 || item.indexOf('\n')>-1){
            Toast.makeText(this, "Title cannot contain tab or new line character! Please try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        listOfItems.add(item);

        etSL.setText("");

        //refresh the page
        lvSL.invalidateViews();

        //save it to SP
        saveToSP();



    }

    /**
     * Saves the task list data to Shared Preferences
     */
    private void saveToSP(){
        if(listOfItems==null){
            return;
        }
        //update shared pref
        SharedPreferences myPref = getSharedPreferences("SmartCart", 0);
        SharedPreferences.Editor editor = myPref.edit();
        editor.putStringSet("SL", (Set<String>) listOfItems);
        editor.commit();

    }

    /**
     * Loads list of items from Shared Preferences
     */
    private void loadFromSP(){
        listOfItems.clear();
        // Look at preferences
        SharedPreferences myPref = getSharedPreferences("SmartCart", 0);
        ArrayList<String> list= (ArrayList<String>) myPref.getStringSet("SL", null);
        if(list==null){
            return;
        }
        for(int i=0; i<list.size(); i++){
            listOfItems.add(list.get(i));
        }
    }


    public void back(View view) {
        finish();
    }
}



