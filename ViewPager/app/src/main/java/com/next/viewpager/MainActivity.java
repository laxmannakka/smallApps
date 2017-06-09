package com.next.viewpager;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.next.viewpager.fragments.ViewPagerWithFragments;

public class MainActivity extends AppCompatActivity {

    ViewPager mViewpager;
    Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewpager= (ViewPager) findViewById(R.id.viewpager);
        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ViewPagerWithFragments.class);
                startActivity(intent);
            }
        });
        CustomAdapter adapter = new CustomAdapter(this);
        mViewpager.setAdapter(adapter);

    }
}
