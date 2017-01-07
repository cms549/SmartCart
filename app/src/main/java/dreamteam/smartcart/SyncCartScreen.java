package dreamteam.smartcart;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

/**
 *
 * it will connect to Bluetooth.
 * Once connected it will update SharedPreferences. "cartconnected" = true
 */
public class SyncCartScreen extends AppCompatActivity {

    /**
     * Data structure to hold bt devices
     */
    private ArrayList<String> btDevices;
    ListView lvSC;
    ArrayList<String> list;

    private BluetoothAdapter BA;
    private Set<BluetoothDevice> pairedDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_cart);
        lvSC = (ListView) findViewById(R.id.lvSC);
        BA = BluetoothAdapter.getDefaultAdapter();
        if(BA==null){
            list = new ArrayList<String>();
            list.add("CANNOT ACCESS BLUETOOTH ADAPTER");
            final ArrayAdapter adapter = new  ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
            lvSC.setAdapter(adapter);
            return;
        }
        if (!BA.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            Toast.makeText(getApplicationContext(), "Turned on BT",Toast.LENGTH_LONG).show();
        }
        BA.startDiscovery();
        pairedDevices = BA.getBondedDevices();
        list = new ArrayList<String>();

        for(BluetoothDevice bt : pairedDevices) list.add(bt.getName());
        Toast.makeText(getApplicationContext(), "Showing Paired Devices",Toast.LENGTH_SHORT).show();

        final ArrayAdapter adapter = new  ArrayAdapter(this,android.R.layout.simple_list_item_1, list);

        lvSC.setAdapter(adapter);
        if(pairedDevices.size()==0){
            list.add("NO PAIRED DEVICES AVAILABLE");
            return;
        }

        lvSC.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> taskList, View v, int pos, long id) {
                lvSC.invalidateViews();
                String name = list.get(pos);
                for(BluetoothDevice bt : pairedDevices) {
                    if( bt.getName().equals(name)){
                        connectWithBT(bt);
                    }
                }

            }
        });



    }


    private void connectWithBT(BluetoothDevice bt) {
        //Establish a connection
        //bt.connectGatt();

        //update shared pref
        SharedPreferences myPref = getSharedPreferences("SmartCart", 0);
        SharedPreferences.Editor editor = myPref.edit();
        editor.putBoolean("cartconnected", true);
        editor.putString("btcode", bt.getAddress());
        editor.commit();
        finish();

    }

}
