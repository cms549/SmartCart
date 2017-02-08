package dreamteam.smartcart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import java.util.HashMap;

import java.util.ArrayList;


import static android.R.attr.keyBackground;
import static android.R.attr.left;
import static android.R.attr.right;




/**
 * Created by AthiraHaridas on 1/13/17.
 */

public class CustomView extends View {
    HashMap<Integer, Integer> xVals;
    HashMap<Integer, Integer> yVals;

    private int radius=30;
    int h,w;


    private Paint paint;
    private Paint linePaint;


    public CustomView (Context context){
        super(context);
        paint=new Paint();
        linePaint=new Paint();
        paint.setColor(Color.BLUE);
        linePaint.setColor(Color.RED);
        xVals=new HashMap<>();
        yVals=new HashMap<>();


        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int height = metrics.heightPixels;
        int width = metrics.widthPixels;

        System.out.println("height is: "+ height); //1776
        System.out.println("width is: "+width); //1080

        h=(int)(Math.floor(height/10))*10;//1770
        w=(int)(Math.floor(width/10))*10;//1080

        System.out.println("height is: "+ h);
        System.out.println("width is: "+w);

    }
    @Override
    protected void onDraw(Canvas canvas){



        //10 cols 15 rows-- w is x coord, h is y coord
        int numCols=w/100;//10
        int numRows=h/100;//17
        System.out.println("rows: "+ numRows+" cols: "+numCols);
        int k=0; int m=0;
        for (int i=10; i<h; i+=100){
            canvas.drawLine(10,i,w,i,linePaint);
            yVals.put(k,i);
            System.out.println(yVals.get(k));
            k++;


        }

        for (int j=10;j<w;j+=100){
            canvas.drawLine(j,10,j,h,linePaint);
            xVals.put(m,j);
            System.out.println(xVals.get(m));
            m++;

        }


        //10x15
        /**
         * 0000000000
         * 0000000000
         * 0011111110
         * 0000000000
         * 0000000000
         * 0011111110
         * 0000000000
         * 0000000000
         * 0011111110
         * 0000000000
         * 0000000000
         * 0011111110
         * 0000000000
         * 0000000000
         * 0000000000
         *
         *
         */

        ArrayList<Integer> grid=new ArrayList<>();
        for (int i=0;i<22;i++){
            grid.add(0);
        }
        for (int j=22;j<29;j++){
            grid.add(1);
        }
        for (int i=29;i<52;i++){
            grid.add(0);
        }
        for (int j=52;j<59;j++){
            grid.add(1);
        }
        for (int i=59;i<82;i++){
            grid.add(0);
        }
        for (int j=82;j<89;j++){
            grid.add(1);
        }
        for (int i=89;i<112;i++){
            grid.add(0);
        }

        for (int j=112;j<119;j++){
            grid.add(1);
        }
        for (int i=119;i<150;i++){
            grid.add(0);
        }

        for (int i=0;i<grid.size();i++){
            System.out.print(grid.get(i));
            if (i%10==0){
                System.out.println();
            }

            if (grid.get(i)==1){
                //22
                int y=i/10; //2
                int x=i%10; //2
                System.out.println("x= "+x+" y="+y);
                canvas.drawRect(xVals.get(x),yVals.get(y),xVals.get(x)+100,yVals.get(y)+100,paint);

            }

        }


    }

}
