package athiraharidas.example.com.canvastest;

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
import android.view.MotionEvent;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import java.util.ArrayList;


import static android.R.attr.keyBackground;
import static android.R.attr.left;
import static android.R.attr.right;
import static android.R.attr.switchMinWidth;

//upload a picture of a map and then put the isles in when clicked on a grid spot



/**
 * Created by AthiraHaridas on 1/13/17.
 */

public class CustomView extends View {
    HashMap<Integer, Integer> xVals;
    HashMap<Integer, Integer> yVals;
    ArrayList<Integer>grid;

    int h,w,numRows,numCols,boxSize,xPos,yPos;
    Cell[][] cells=new Cell[10][15];


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
        grid=new ArrayList<>();
        boxSize=50;


        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int height = metrics.heightPixels;
        int width = metrics.widthPixels;


        //getting them divisible by 100
        h=(int)(Math.floor(height/10))*10;//1770
        w=(int)(Math.floor(width/10))*10;//1080

        //10 cols 15 rows-- w is x coord, h is y coord
        numRows=w/boxSize;
        numCols=h/boxSize;
        System.out.println("got here");
        String s=readFromAssets(getContext(),"grid.txt");
        fillArrayList(s);
        System.out.println("returned from method");
        System.out.println(s+ "s is empty");

    }

    public void fillArrayList(String s){
        for (int i=0;i<s.length();i++){
            char x= s.charAt(i);
            //System.out.println(x);
            int z=Character.getNumericValue(x);
            //System.out.println(z);
            if (z>=0){
                grid.add(z);
            }

        }

    }

    public static String readFromAssets(Context context,String filename){
        try{
            InputStream is=context.getAssets().open(filename);
            int size=is.available();
            byte buffer[]=new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer);
        }catch (Exception e){
            e.printStackTrace();
            return " ";
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                xPos=(int) event.getX();
                yPos=(int) event.getY();
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas){

        Drawable d= getResources().getDrawable(R.drawable.store);
        d.setBounds(0,0,1000,1500);
        d.draw(canvas);

       // canvas.drawRect(xPos,yPos,xPos+boxSize,yPos+boxSize,paint);
        int k=0; int m=0;
        for (int i=10; i<h; i+=100){
           // canvas.drawLine(10,i,w,i,linePaint);
            yVals.put(k,i);
            k++;
        }
        for (int j=10;j<w;j+=100){
            //canvas.drawLine(j,10,j,h,linePaint);
            xVals.put(m,j);
            m++;

        }




        for (int i=0;i<grid.size();i++){
            int y=i/10; //2
            int x=i%10; //2
            Cell c=new Cell(x,y);

            if (grid.get(i)==0){
                c.cellType='0';
                canvas.drawRect(xVals.get(x),yVals.get(y),xVals.get(x)+boxSize,yVals.get(y)+boxSize,paint);
            }
            cells[x][y]=c;

        }
        //go from [2,13] to [5,4]


        LocationAlgo algo=new LocationAlgo(cells);
        ArrayList<Cell> result=algo.AStar(cells[2][13],cells[5][4]);
        paint.setColor(Color.RED);
       // canvas.drawRect(xVals.get(2),yVals.get(13),xVals.get(2)+100,yVals.get(13)+100,paint);
        //canvas.drawRect(xVals.get(5),yVals.get(4),xVals.get(5)+100,yVals.get(4)+100,paint);


        for (int i=0;i<result.size();i++){
            int x=result.get(i).x;
            int y=result.get(i).y;
            canvas.drawRect(xVals.get(x),yVals.get(y),xVals.get(x)+boxSize,yVals.get(y)+boxSize,paint);


        }


    }

}
