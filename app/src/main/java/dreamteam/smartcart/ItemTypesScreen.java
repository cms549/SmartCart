package dreamteam.smartcart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_type);

        lvType = (ListView) findViewById(R.id.lvType);

        listOfTypes = new ArrayList<String>();
        loadFromSP();

        ArrayAdapter<String> ad = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listOfTypes);

        //Set adapter
        lvType.setAdapter(ad);

        //DELETE SOMETHING FROM LIST
        lvType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> taskList, View v, int pos, long id) {
                //open up list view for this specific type
                Intent nextScreen = new Intent(getApplication(), ItemListScreen.class);

                nextScreen.putExtra("itemName", listOfTypes.get(pos));

                //start next screen
                startActivity(nextScreen);
                return;
            }
        });




    }

    private void loadFromSP() {
        listOfTypes.clear();
        // Look at preferences
        SharedPreferences myPref = getSharedPreferences("SmartCart", 0);
        String sl = myPref.getString("SL","");
        String[] list = sl.split(",");
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

    public void back(View view) {
        finish();
    }


}
