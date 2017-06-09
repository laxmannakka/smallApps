package com.next.nativeproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{


    LinearLayout contentView;
    CustomWebview customWebView;
    Button btn;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        this.contentView = new LinearLayout(this);
        this.contentView.setOrientation(LinearLayout.VERTICAL);

        this.btn = new Button(this);
        this.btn.setText("Android Btn\nClick Me");
        this.btn.setOnClickListener(this);

        this.textView = new TextView(this);

        this.customWebView = new CustomWebview(this);

        LinearLayout.LayoutParams customWebViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0);
        customWebViewParams.weight = 4;

        LinearLayout.LayoutParams textViewViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0);
        textViewViewParams.weight = 1;


        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0);
        btnParams.weight = 1;

        this.contentView.addView(this.customWebView,customWebViewParams);
        this.contentView.addView(this.textView,textViewViewParams);
        this.contentView.addView(this.btn,btnParams);

        setContentView(this.contentView);
    }


    int x  = 0;
    @Override
    public void onClick(View view) {

        this.customWebView.loadUrl("file:///android_asset/ll.htmlcode");
        x++;

    }
}
