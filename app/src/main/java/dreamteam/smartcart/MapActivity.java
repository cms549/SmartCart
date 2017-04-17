package dreamteam.smartcart;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by AthiraHaridas on 1/13/17.
 */

public class MapActivity extends Activity {

    private int stx;
    private int sty;
    private int endx;
    private int endy;
    BluetoothChatService btservice;
    private BluetoothAdapter BA;
    private BluetoothDevice device;
    private Handler handler;
    private CustomView cv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Grab the Bluetooth device to connect to
        //BA = BluetoothAdapter.getDefaultAdapter();
        //Set<BluetoothDevice> pairedDevices = BA.getBondedDevices();
        //device = pairedDevices.iterator().next();

        //handler = new Handler();
        //Start the service
        //btservice = new BluetoothChatService(this, handler);
        //btservice.connect(device, false);


        Intent intent=getIntent();
        endx = intent.getIntExtra("x",0);
        endy = intent.getIntExtra("y",0);
        String itemName = intent.getStringExtra("ItemName");

        stx =3;//get from bluetooth -> maybe have it saved in a file???
        sty=13;

        cv = new CustomView(this,stx,sty,endx,endy, itemName);
        setContentView(cv);

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                Timer timer=new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        cv.eraseCells();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cv.invalidate();
                            }
                        });

                    }
                },1000,10000);


            }
        });
        thread.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        btservice.stop();
    }



}
