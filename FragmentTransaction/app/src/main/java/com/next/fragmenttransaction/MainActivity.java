package com.next.fragmenttransaction;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{

    FrameLayout frameLayout;
    RecyclerView recyclerView;
    RecyclerviewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // frameLayout = (FrameLayout) findViewById(R.id.frameLayout);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
       // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
     //  recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this,4);
        recyclerView.setLayoutManager(mLayoutManager);
         adapter = new RecyclerviewAdapter(this);
        recyclerView.setAdapter(adapter);


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
            recyclerView.invalidate();
            recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this,4);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setHasFixedSize(true);
            adapter = new RecyclerviewAdapter(this);
            recyclerView.setAdapter(adapter);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();


        }

    }
}
