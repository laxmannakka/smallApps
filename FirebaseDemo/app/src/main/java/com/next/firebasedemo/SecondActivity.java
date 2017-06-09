package com.next.firebasedemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.analytics.FirebaseAnalytics;

public class SecondActivity extends AppCompatActivity {
    String name = "SecondActivity";
    private FirebaseAnalytics mFirebaseAnalytics;
    Button mNextbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(SecondActivity.this);
        mNextbutton= (Button) findViewById(R.id.btn_next);

        Bundle params = new Bundle();
        params.putString("activity_name", name);
        mFirebaseAnalytics.logEvent("SecondActivity", params);

        mNextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this,ThirdActivity.class);
                startActivity(intent);
            }
        });


    }
}
