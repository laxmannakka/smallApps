package com.example.next.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button mCamera,mGallary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCamera = (Button) findViewById(R.id.camera);
        mGallary =(Button) findViewById(R.id.gallery);
        mCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera = new Intent(MainActivity.this,CameraActivity.class);
                startActivity(camera);
            }
        });

        mGallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallary = new Intent(MainActivity.this,GallaryActivity.class);
                startActivity(gallary);
            }
        });
    }
}
