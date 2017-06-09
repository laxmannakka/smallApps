package com.next.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by next on 9/1/17.
 */
public class CanvasView extends View {
    public CanvasView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        // make the entire canvas white
        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);

        // draw blue circle with anti aliasing turned off
        paint.setAntiAlias(false);
        paint.setColor(Color.RED);
        canvas.drawCircle(20, 20, 15, paint);
        canvas.drawRect(100, 5, 200, 30, paint);

        // draw the rotated text
        canvas.rotate(-45);
        paint.setTextSize(55);
        paint.setStyle(Paint.Style.FILL);
        int xPos = (canvas.getWidth() / 2);
        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)) ;
        //((textPaint.descent() + textPaint.ascent()) / 2) is the distance from the baseline to the center.

        canvas.drawText("Hello", xPos, yPos, paint);
      //  canvas.drawText("Graphics Rotation", 20, 100, paint);

    }
}
