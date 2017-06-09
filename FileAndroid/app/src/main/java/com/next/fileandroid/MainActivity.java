package com.next.fileandroid;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity
{

    Thread thread;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress);
//        File file = new File("laxman");
        final TextView     textView = (TextView) findViewById(R.id.coutdown);

        thread = new Thread(new Mythread());
        thread.start();

        handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);

                progressBar.setProgress(msg.arg1);
                textView.setText(String.valueOf(msg.arg1));
            }
        };
    }


    class  Mythread implements   Runnable{

        @Override
        public void run()
        {
          for(int i=0;i<1000;i++)
          {
              Message messge = Message.obtain();
              messge.arg1 =i;
              handler.handleMessage(messge);
              try
              {
                  Thread.sleep(100);
              } catch (InterruptedException e)
              {
                  e.printStackTrace();
              }
          }

        }
    }

    public void run(){
        System.out.println("thread is running...");
    }

}
