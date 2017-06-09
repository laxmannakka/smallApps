package com.next.screens;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Button screen1 = (Button) findViewById(R.id.button1);
        Button screen2 = (Button) findViewById(R.id.button2);
        Button screen3 = (Button) findViewById(R.id.button3);

        screen1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent screen = new Intent(Main2Activity.this,Screen1.class);
                startActivity(screen);
            }
        });
        screen2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent screen = new Intent(Main2Activity.this,Screen2.class);
                startActivity(screen);
            }
        });
        screen3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent screen = new Intent(Main2Activity.this,Screen3.class);
                startActivity(screen);
            }
        });
    }
}
