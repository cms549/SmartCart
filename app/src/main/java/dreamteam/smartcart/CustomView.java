package dreamteam.smartcart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by AthiraHaridas on 1/13/17.
 */

public class CustomView extends View {

    private Paint paint;

    public CustomView (Context context){
        super(context);
        paint=new Paint();
        paint.setColor(Color.BLUE);

    }
    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawRect(40,50,1000,120,paint);
        canvas.drawRect(40,155,1000,225,paint);
        canvas.drawRect(40,260,1000,330,paint);
        canvas.drawRect(40,365,1000,435,paint);
        canvas.drawRect(40,470,1000,540,paint);




    }
}
