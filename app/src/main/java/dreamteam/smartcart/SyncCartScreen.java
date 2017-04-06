package dreamteam.smartcart;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
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
        list = new ArrayList<String>();
        //pairedDevices = new HashSet<BluetoothDevice>();
        list.add("Before");
        pairedDevices=BA.getBondedDevices();
        list.add("size="+pairedDevices.size());
        BluetoothDevice btd = pairedDevices.iterator().next();
        list.add(btd.toString());
        //list.add(pairedDevices.iterator().next().getName());
        if(pairedDevices.size()==0){
            list.add("NO PAIRED DEVICES AVAILABLE");

            //return;
        }



        final ArrayAdapter adapter = new  ArrayAdapter(this,android.R.layout.simple_list_item_1, list);

        lvSC.setAdapter(adapter);


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

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        BA.startDiscovery();








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


    //The BroadcastReceiver that listens for bluetooth broadcasts
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Toast.makeText(context, "recieved something ", Toast.LENGTH_LONG).show();
            String action = intent.getAction();

            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                Toast.makeText(context, "FOUND BLUETOOTH DEVICE", Toast.LENGTH_LONG).show();
                //Device found
                pairedDevices.add(device);
                list.add(device.getName());
                lvSC.invalidateViews();

            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Toast.makeText(context, "Fini ", Toast.LENGTH_LONG).show();


            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                Toast.makeText(context, "Commence ", Toast.LENGTH_LONG).show();


            }


        }
    };

}




