package dreamteam.smartcart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Displays current cart, the items in it, the total
 * Allows you to delete items and modify the amount of each
 * Buttons to search for an item -> by barcode, by search or by categories
 */
public class MyCartScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

}
