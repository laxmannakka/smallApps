package com.next.matchthefollowing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by next on 6/5/17.
 */
public class CustomView extends View
{

    Paint paint = new Paint();
    ArrayList<Point> listOfCoOrdinates = new ArrayList<>();

    public CustomView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        paint.setColor(context.getResources().getColor(R.color.colorAccent));
        paint.setStrokeWidth(10);
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        paint.setColor(getResources().getColor(R.color.yellow));
        paint.setStrokeWidth(3);
        Iterator it = listOfCoOrdinates.iterator();

        while (it.hasNext())
        {

            Point point = (Point) it.next();
            canvas.drawLine(point.getStartPoint_Q(), point.getEndPoint_Q(), point.startPoint_A, point.endPoint_A, paint);

        }

    }

    public void addObjects(Point point)
    {
        listOfCoOrdinates.add(point);
        Log.i("in draw", "" + point.startPoint_Q + "-" + point.endPoint_Q + "-" + point.startPoint_A + "-" + point.endPoint_A);
        invalidate();

    }


    @Override
    protected Parcelable onSaveInstanceState()
    {
        return super.onSaveInstanceState();

    }

    @Override
    protected void onRestoreInstanceState(Parcelable state)
    {
        super.onRestoreInstanceState(state);


    }
}
