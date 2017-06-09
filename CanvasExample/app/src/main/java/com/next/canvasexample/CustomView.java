package com.next.canvasexample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

/**
 * Created by next on 1/4/17.
 */
public class CustomView extends SurfaceView
{
    public CustomView(Context context)
    {
        super(context);
    }



    @Override
    protected void onDraw(final Canvas canvas)
    {
        super.onDraw(canvas);
        float width = (float) getWidth();
        float height = (float) getHeight();
        float radius;

        if (width > height) {
            radius = height / 4;
        } else {
            radius = width / 4;
        }

        Path path = new Path();
        path.addCircle(width / 2,
                height / 2, radius,
                Path.Direction.CW);

      final   Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.FILL);

        float center_x, center_y;
        final RectF oval = new RectF();
        paint.setStyle(Paint.Style.STROKE);

        center_x = width / 2;
         center_y = height / 2;

        oval.set(center_x - radius,
                center_y - radius,
                center_x + radius,
                center_y + radius);
       canvas.drawArc(oval, 90, 180, false, paint);
        //invalidate();

/*

       final Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                handler.postDelayed(this, 1000);
                canvas.drawCircle(100,100,50,paint);
            }
        };
Thread thread = new Thread()
{
    @Override
    public void run()
    {
        super.run();
        try
        {
            sleep(1000);
            Toast.makeText(getContext(),"Toast",Toast.LENGTH_LONG).show();
          //  invalidate();
            canvas.drawArc(oval, 90, 180, false, paint);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
};
*/


        canvas.drawLine(0,25,100,100,paint);


    }
}
