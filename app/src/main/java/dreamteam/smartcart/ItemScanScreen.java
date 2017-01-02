package dreamteam.smartcart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * THIS WAS JUST COPY AND PASTED FROM SYNC CART
 * Item scanning screen -> needs to use sql to look at what item it is
 */
public class ItemScanScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_item);


    }


    public void scanItem(View view){
        //Recoginze the Barcode
        int barcode=-1;

        if(barcode==-1){
            //Error with reading code
            Toast.makeText(this, "Error: Cannot recognize barcode. Please take another picture and try again." , Toast.LENGTH_LONG).show();
            return;
        }

        //Look it up in Carts table
        String btaddr = findItemByBarcode(barcode);
        if(btaddr.equals("-1")){
            Toast.makeText(this, "Error: Cart not recognized in system. Please take another picture and try again." ,Toast.LENGTH_LONG).show();
            //Error with look up
            return;
        }


        //Return to previous screen
        finish();
    }

    private String findItemByBarcode(int barcode) {
        //look it up in sql table

        return "-1";
    }

    public void back(View v){
        finish();
    }

}
