package dreamteam.smartcart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

/**
 * Created by AthiraHaridas on 1/13/17.
 */

public class MapActivity extends Activity {

    private int stx;
    private int sty;
    private int endx;
    private int endy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomView cv = new CustomView(this);
        setContentView(cv);

        Intent intent=getIntent();
        endx=Integer.parseInt(intent.getStringExtra("x"));
        endy=Integer.parseInt(intent.getStringExtra("y"));

        stx =0;//get from bluetooth -> maybe have it saved in a file???
        sty=0;

        //Call a funciton that finds path from st to end
        //cv.drawPath();

    }
}
