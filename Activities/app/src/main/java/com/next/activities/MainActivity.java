package com.next.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.next.activities.fragments.Fragment1;
import com.next.activities.fragments.Fragment2;

public class MainActivity extends AppCompatActivity
{
    FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button);
         frameLayout = (FrameLayout) findViewById(R.id.framelayoit);

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                   createFragment();
                 createFragment1();
                FragmentManager fm = getSupportFragmentManager();

                fm.isDestroyed();
            int i =   fm.getBackStackEntryCount();
                Log.i("FRAGMENTCOUNT",""+i);
            }
        });
    }


    public void createFragment()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.framelayoit,new Fragment1(),"ll");
        fragmentTransaction.commit();
    }
    public void createFragment1()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayoit,new Fragment2(),"lad");
        fragmentTransaction.commit();
    }
}
