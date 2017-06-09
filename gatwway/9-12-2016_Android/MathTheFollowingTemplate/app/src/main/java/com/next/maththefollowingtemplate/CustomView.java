package com.next.maththefollowingtemplate;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by next on 5/5/17.
 */
public class CustomView extends View
{

    Paint paint = new Paint();
    List<Coordinates> mXYCordinates;

    public CustomView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mXYCordinates =new ArrayList<>();
        paint.setColor(context.getResources().getColor(R.color.colorAccent));
        paint.setStrokeWidth(10);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        Iterator it = mXYCordinates.iterator();

        while (it.hasNext())
        {
           Coordinates coordinates = (Coordinates) it.next();
            canvas.drawLine(coordinates.startX, coordinates.startY, coordinates.endX,
                    coordinates.endY, paint);
        }
    }

    public void drawLine(Coordinates lineCoordinate)
    {
        mXYCordinates.add(lineCoordinate);
        invalidate();
    }
}
