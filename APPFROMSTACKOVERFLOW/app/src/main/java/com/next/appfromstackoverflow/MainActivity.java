package com.next.appfromstackoverflow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity
{

    ArrayAdapter<String> listadapter;
    DrawView draw;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] s1 = { "smiley1", "smiley2", "smiley3" };
        ListView lv = (ListView) findViewById(R.id.text_list);
        ArrayList<String> list = new ArrayList<String>();
        list.addAll(Arrays.asList(s1));
        listadapter = new ArrayAdapter<String>(this, R.layout.row_text, s1);
        lv.setAdapter(listadapter);
        GridView gv = (GridView) findViewById(R.id.image_list);
        gv.setAdapter(new ImageAdapter(this));

        // This should be done in the layout xml
        // I moved it here to do it only once not for every click
        // I don't know how your layout is defined but it seems as this should
        //   be the parent component of the text and image views and it's not.
        //   If it works like this I don't think you should bother with it.
        //   Otherwise post your layout file.
        LinearLayout ll= (LinearLayout) findViewById(R.id.draw_line);
        draw = new DrawView(this);
        ll.addView(draw);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3){
                float x1 = v.getX();
                float y1 = v.getY();
                draw.addSourcePoint(x1, y1);
                Log.d("list","text positions x1:"+x1+" y1:"+y1);
            }
        });

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3){
                float x2 = v.getX();
                float y2 = v.getY();
                draw.addDestinationPoint(x2, y2);
                Log.d("list","image positions x2:"+x2+" y2:"+y2);
                Toast.makeText(getApplicationContext(),"msg",Toast.LENGTH_LONG).show();
            }
        });

    }
}
