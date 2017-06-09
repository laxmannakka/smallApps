package com.next.javascriptbridge;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

public class PracticeJSActivity extends AppCompatActivity
{


    WebView webView;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_js);
        Button button = (Button) findViewById(R.id.fun_call);
        textView = (TextView) findViewById(R.id.textView);
        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(this, "practice");
        webView.setWebChromeClient(new WebChromeClient());

        webView.loadUrl("file:///android_asset/add.html");
        webView.setBackgroundColor(Color.GREEN);


        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                webView.loadUrl("javascript:diplayJavaMsg('Hello World!')");
            }
        });


    }

    @JavascriptInterface
    public  void fromjavaScript(String message)
    {
        textView.setText(message);
    }

}

