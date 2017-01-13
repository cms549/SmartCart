package dreamteam.smartcart;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

/**
 * Created by AthiraHaridas on 1/13/17.
 */

public class MapActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new CustomView(this));

    }
}
