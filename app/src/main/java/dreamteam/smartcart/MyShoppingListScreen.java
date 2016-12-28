package dreamteam.smartcart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

/**
 *  Allows you to add anything to shopping list -> each value has a amount associated with it
 */
public class MyShoppingListScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvocc = (TextView) findViewById(R.id.tvocc);
        tvtot = (TextView) findViewById(R.id.tvtot);
        etitem = (EditText) findViewById(R.id.etitem);
    }
}
