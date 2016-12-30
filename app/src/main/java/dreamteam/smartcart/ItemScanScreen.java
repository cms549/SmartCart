package dreamteam.smartcart;

import android.bluetooth.BluetoothDevice;
import android.content.SharedPreferences;
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
        setContentView(R.layout.activity_sync_cart);


    }


    public void syncCart(View view){
        //Recoginze the Barcode
        int barcode=-1;

        if(barcode==-1){
            //Error with reading code
            Toast.makeText(this, "Error: Cannot recognize barcode. Please take another picture and try again." , Toast.LENGTH_LONG).show();
            return;
        }

        //Look it up in Carts table
        String btaddr = findCartByBarcode(barcode);
        if(btaddr.equals("-1")){
            Toast.makeText(this, "Error: Cart not recognized in system. Please take another picture and try again." ,Toast.LENGTH_LONG).show();
            //Error with look up
            return;
        }

        //Establish a connection with BT device
        if(connectWithBT(btaddr)){
            //update shared pref
            SharedPreferences myPref = getSharedPreferences("SmartCart", 0);
            SharedPreferences.Editor editor = myPref.edit();
            editor.putBoolean("cartconnected", true);
            editor.putString("btcode", btaddr);
            editor.commit();
        }
        else{
            Toast.makeText(this, "Error: Bluetooth cannot connect. Please try again." ,Toast.LENGTH_LONG).show();
            return;
        }

        //Return to previous screen
        finish();
    }

    private String findCartByBarcode(int barcode) {
        //look it up in sql table

        return "-1";
    }

    private boolean connectWithBT(String addr) {
        //Establish a connection
        String deviceName = "My_Device_Name";

        BluetoothDevice result = null;
/*
        Set<BluetoothDevice> device = adapter.getBondedDevices();
        if (devices != null) {
            for (BluetoothDevice device : devices) {
                if (deviceName.equals(device.getName())) { //if (addr.equals(device.getAddress()))
                    result = device;
                    break;
                }
            }
        }

*/

        return false;

    }

    public void back(View v){
        finish();
    }

}
