package com.next.nawigationdrawer;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class SecondActivity extends AppCompatActivity {
    ImageView mImageview;
    Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        mImageview = (ImageView) findViewById(R.id.imageview);
        mButton =(Button) findViewById(R.id.buton1);
        mImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButton.setVisibility(View.INVISIBLE);
                Bundle bundle = new Bundle();
                bundle.putInt("key", 1);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                FragmnetOne obj = new FragmnetOne();
                obj.setArguments(bundle);
                ft.add(R.id.framelayout, obj);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
       finish();
    }
}
