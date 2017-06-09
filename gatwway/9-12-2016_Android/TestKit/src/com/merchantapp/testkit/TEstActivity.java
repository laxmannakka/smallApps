package com.merchantapp.testkit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class TEstActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Intent intent = getIntent();

      String  payment_id = intent.getStringExtra("payment_id");
        System.out.println("Success and Failure response to merchant app..." + " " + payment_id);

    }
}
