package com.next.javascriptbridge;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.TextView;

/**
 * Created by next on 8/5/17.
 */
public class JsHandler
{


    Activity activity;
    String TAG = "JsHandler";
    WebView webView;
    TextView mTextView;

    public JsHandler(Activity activity, WebView webView, TextView textView)
    {
        this.activity = activity;
        this.webView = webView;
        this.mTextView = textView;
    }

    /**
     * This function handles call from JS
     */
    @JavascriptInterface
    public void jsFnCall(String s)
    {
        Log.i(TAG, "jsFnCall: ");
        showDialog(s);
    }

    /**
     * This function handles call from Android-Java
     */
    public void javaFnCall(String message)
    {
        final String webUrl = "javascript:diplayJavaMsg('" + message + "')";
        webView.loadUrl(webUrl);

    }
    @JavascriptInterface
    public  void fromjavaScript(String message)
    {
        showDialog(message);
    }

    /**
     * function shows Android-Native Alert Dialog
     */
    private void showDialog(final String message)
    {

        // Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();

        if (!activity.isFinishing())
        {
            activity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    mTextView.setText(message);
                }
            });
        }

    }
}


