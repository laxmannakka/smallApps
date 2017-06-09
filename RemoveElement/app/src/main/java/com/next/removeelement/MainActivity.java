package com.next.removeelement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity
{
    ArrayList<Integer> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addElements();
        removeElements();
    }

    private void addElements()
    {
        for (int i = 0; i < 50; i++)
        {
            list.add(i, i + 2);
        }
    }

    private void removeElements()
    {
        while (list.size() > 1)
        {
            for (int i = 0; i < list.size(); i++)
            {
                if (i % 3 == 0)
                {//Every 3rd element should be true
                    list.remove(i);
                }
            }
        }

    }
}

/*
* */