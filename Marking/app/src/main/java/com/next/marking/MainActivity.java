package com.next.marking;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements OnClickLisner
{

    int currentId=0;
    float startx;
    float starty;
    float endx;
    float endy;
    boolean startselected=false,endselected=false;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView questionListview = (ListView) findViewById(R.id.question_list);
        ListView ansListView = (ListView) findViewById(R.id.ans_list);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        List<String> questionList = new ArrayList<>();
        questionList.add("1+2");
        questionList.add("4*4");
        questionList.add("2-1");
        List<String> ansList = new ArrayList<>();
        ansList.add("1");
        ansList.add("16");
        ansList.add("3");
        ListViewAdapter qnsAdpter = new ListViewAdapter(questionList,MainActivity.this,false,this);
        questionListview.setAdapter(qnsAdpter);
        ListViewAnsAdapter adapter = new ListViewAnsAdapter(ansList,MainActivity.this,this);
        ansListView.setAdapter(adapter);
        Canvas canvas = new Canvas();
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.colorAccent));
        paint.setStrokeWidth(10);

        int endx = 150;
        int endy = 360;
        canvas.drawLine(startx, starty, endx, endy, paint);
    }

    @Override
    public void onclickRadioButton(float x, float y,int id,boolean flag)
    {
       ;
        if(flag)
        {
             startx = x;
             starty = y;
            startselected=true;
            Log.i("in if",""+startx+starty);
        }
        else
        {
            endx =x;
            endy=y;
            endselected=true;
            Log.i("in else",""+endx+endy);

        }
        if(startselected && endselected)
        {
            Bitmap bitmap = Bitmap.createBitmap(
                    500, // Width
                    300, // Height
                    Bitmap.Config.ARGB_8888) ;
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(Color.LTGRAY);

            Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStrokeWidth(10);
            paint.setStyle(Paint.Style.STROKE);
            // Line width in pixels
            paint.setStrokeWidth(8);
            paint.setAntiAlias(true);

            Path path = new Path();
            path.moveTo(startx,starty);
            path.lineTo(startx, starty);
            canvas.drawPath(path,paint);

           // canvas.drawBitmap(bitmap, 50, 100, paint);

        }
    }
}
