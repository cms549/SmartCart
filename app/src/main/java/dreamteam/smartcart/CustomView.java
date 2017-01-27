package dreamteam.smartcart;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import java.util.HashMap;

import java.util.ArrayList;


import static android.R.attr.left;
import static android.R.attr.right;




/**
 * Created by AthiraHaridas on 1/13/17.
 */

public class CustomView extends View {
    HashMap<Integer, Integer> xVals;
    HashMap<Integer, Integer> yVals;
    HashMap<Point,Moves> pointMovesHashMap;
    private int radius=30;


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

        pointMovesHashMap=new HashMap<>();

    }
    @Override
    protected void onDraw(Canvas canvas){

        Drawable d = getResources().getDrawable(R.drawable.store,null);
        d.setBounds(0, 100, 1000, 1000);
        d.draw(canvas);

        initHashMap();

        initPointMoves();

        for (int i=0;i<xVals.size();i++){
            for (int j=0; j<yVals.size();j++){
                canvas.drawCircle(xVals.get(i),yVals.get(j),radius,paint);
            }
        }
        canvas.drawLine(xVals.get(0),yVals.get(0),xVals.get(0),yVals.get(3),linePaint);

    /*
       // getCoords(new Point(0,0), new Point(2,2));

        Point start=new Point(0,0);
        Point end=new Point(0,3);
        int startX=start.x;
        int startY=start.y;
        int endX=end.x;
        int endY=end.y;
        Moves startMoves=pointMovesHashMap.get(start); //0,0
        //check if they are in the same col --> x coords match
        if (startX==endX){
            //if start is below end [0,0]--[0,3]
            if (startY<endY){
                l:while (startY<endY){
                    //go up if possible
                    Point p=new Point(startX,startY);
                    if (pointMovesHashMap.get(p).up){
                        canvas.drawLine(xVals.get(startX),yVals.get(startY),xVals.get(startX),yVals.get(startY+1),paint);
                        startY++;
                    }
                    else{
                        break l;
                    }
                }
                //get here either if startY==endY so we found the end OR you can't move up OR startY>endY
            }
            else if (startY>endY){
                //if end is below start [0,3]--[0,0]
                m: while(startY>endY){
                    // go down
                    Point p=new Point(startX,startY);
                    if(pointMovesHashMap.get(p).down){
                        canvas.drawLine(xVals.get(startX),yVals.get(startY),xVals.get(startX),yVals.get(startY-1),paint);
                        startY--;
                    }
                    else {
                        break m;
                    }

                }
                //get here either if startY==endY so we found end OR you can't move down

            }

        }

        //check if they are in the same row
        if (startY==endY){
            //start before end [0,0]--[3,0], move right
            if (startX<endX){
                n: while (startX<endX){
                    Point p=new Point(startX,startY);
                    if (pointMovesHashMap.get(p).right){
                        canvas.drawLine(xVals.get(startX),yVals.get(startY),xVals.get(startX+1),yVals.get(startY),paint);
                        startX++;
                    }
                    else{
                        break n;
                    }
                }
            }
            //get here if startX==endX found OR startX>endX
            else if (startX>endX){
                o: while (startX>endX){
                    Point p=new Point(startX,startY);
                    if (pointMovesHashMap.get(p).left){
                        canvas.drawLine(xVals.get(startX),yVals.get(startY),xVals.get(startX-1),yVals.get(startY),paint);
                        startX--;
                    }
                    else{
                        break o;
                    }
                }
            }
        }

/**
        canvas.drawCircle(285,225,30,paint);//top left [0,3]
        canvas.drawCircle(285,435,30,paint);//[0,2]
        canvas.drawCircle(285,635,30,paint);//[0,1]
        canvas.drawCircle(290,865,30,paint);//[0,0]

        canvas.drawCircle(450,225,30,paint);//[1,3]
        canvas.drawCircle(620,225,30,paint);//[2,3]
        canvas.drawCircle(785,225,30,paint);//[3,3]

        canvas.drawCircle(785,435,30,paint);//[3,2]
        canvas.drawCircle(620,435,30,paint);//[2,2]
        canvas.drawCircle(450,435,30,paint);//[1,2]

        canvas.drawCircle(450,635,30,paint);//[1,1]
        canvas.drawCircle(620,635,30,paint);//[2,1]
        canvas.drawCircle(785,635,30,paint);//[3,1]

        canvas.drawCircle(450,865,30,paint);//[1,0]
        canvas.drawCircle(620,865,30,paint);//[2,0]
        canvas.drawCircle(785,865,30,paint);//[3,0]



        //Step 1: determine which edges are possible,
        /**
         * EDges that are possible from
         *      [0,0]--[0,1], [1,0]
         *      [1,0]--[0,0], [2,0]
         *      [2,0]--[1,0], [3,0]
         *      [3,0]--[2,0], [3,1]
         *      [0,1]--[1,1], [0,2], [0,0]
         *      [1,1]--[0,1], [2,1]
         *      [2,1]--[1,1], [3,1]
         *      [3,1]--[2,1], [3,2], [3,0]
         *      [0,2]--[0,1], [0,3], [1,2]
         *      [1,2]--[0,2], [2,2]
         *      [2,2]--[1,2], [3,2]
         *      [0,3]--[0,2], [1,3]
         *      [1,3]--[0,3], [2,3]
         *      [2,3]-- [1,3], [3,3]
         *      [3,3]--[2,3], [3,2]
         *
         *
         *
         *
         */



    }

    public void initHashMap(){
        xVals.put(0,325);
        xVals.put(1,500);
        xVals.put(2,690);
        xVals.put(3,885);

        yVals.put(0,865);
        yVals.put(1,635);
        yVals.put(2,435);
        yVals.put(3,225);
    }

    public void initPointMoves(){
        //[up, down, left, right]
        pointMovesHashMap.put(new Point(0,0),new Moves(true,false,false,true));
        pointMovesHashMap.put(new Point(0,1), new Moves(true,true,false,true));
        pointMovesHashMap.put(new Point(0,2), new Moves(true,true,false,true));
        pointMovesHashMap.put(new Point(0,3), new Moves(false,true,false,true));
        pointMovesHashMap.put(new Point(1,0), new Moves(false,false,true,true));
        pointMovesHashMap.put(new Point(1,1), new Moves(false,false,true,true));
        pointMovesHashMap.put(new Point(1,2), new Moves(false,false,true,true));
        pointMovesHashMap.put(new Point(1,3), new Moves(false,false,true,true));
        pointMovesHashMap.put(new Point(2,0), new Moves(false,false,true,true));
        pointMovesHashMap.put(new Point(2,1), new Moves(false,false,true,true));
        pointMovesHashMap.put(new Point(2,2), new Moves(false,false,true,true));
        pointMovesHashMap.put(new Point(2,3), new Moves(false,false,true,true));
        pointMovesHashMap.put(new Point(3,0), new Moves(true,false,true, false));
        pointMovesHashMap.put(new Point(3,1), new Moves(true,true,true,false));
        pointMovesHashMap.put(new Point(3,2), new Moves(true,true,true,false));
        pointMovesHashMap.put(new Point(3,3), new Moves(false,true,true,false));

    }









}
