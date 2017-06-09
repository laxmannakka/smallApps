package com.next.maththefollowingtemplate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
        // for questions
        LinearLayout mQuestionLayout;
        // for answers
        LinearLayout mAnswerLayout;

       // drawing line in this view
        CustomView mDraw;
        // srarting x and y co ordinates
        int mStartLayoutCoordinates[] = new int[2];
        int question_id = 100;
        int answer_id = 200;
        final String TAG = MainActivity.class.getSimpleName();
        ArrayList<Integer> mSelectedViewItems = new ArrayList<>();
        List<View> mSelectedViews = new ArrayList<>();
        List<String> answers = new ArrayList<>();
        int mStartLIneCoordinates[] = new int[2];
        int mEndLineCoordinates[] = new int[2];

    @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            mQuestionLayout = (LinearLayout) findViewById(R.id.que_layout);
            mAnswerLayout = (LinearLayout) findViewById(R.id.ans_layout);
            mDraw = (CustomView) findViewById(R.id.draw_view);
            setQuestionAndViews();
            setAnswerAndViews();
            setAnswers();
        }



    private void setQuestionAndViews()
    {
        List<String> questions = new ArrayList<>();
        questions.add("Bed");
        questions.add("Tooth");
        questions.add("Home");
        questions.add("Break");

        Iterator it = questions.iterator();
        while (it.hasNext())
        {
            String question = (String) it.next();
            View view = LayoutInflater.from(this).inflate(R.layout.qns_item, null);
            view.setId(question_id++);
            TextView textView = (TextView) view.findViewById(R.id.text);
            textView.setText(question);
            view.setOnClickListener(this);
            mQuestionLayout.addView(view);
        }
    }

    private void setAnswerAndViews()
    {
        List<String> answers = new ArrayList<>();
        answers.add("brush");
        answers.add("fast");
        answers.add("room");
        answers.add("work");

        Iterator it = answers.iterator();
        while (it.hasNext())
        {
            String answer = (String) it.next();
            View view = LayoutInflater.from(this).inflate(R.layout.ans_iten, null);
            view.setId(answer_id++);
            TextView textView = (TextView) view.findViewById(R.id.text);
            textView.setText(answer);
            view.setOnClickListener(MainActivity.this);
            mAnswerLayout.addView(view);
        }
    }

    private  void setAnswers()
    {
        answers.add("Bedroom");
        answers.add("Toothbrush");
        answers.add("Homework");
        answers.add("Breakfast");
    }

    @Override
    public void onClick(View v)
    {
        mQuestionLayout.getLocationOnScreen(mStartLayoutCoordinates);

        ImageView circleImage = (ImageView) v.findViewById(R.id.circle_image);
        int xy[] = new int[2];
        circleImage.getLocationInWindow(xy);

        if(!mSelectedViewItems.contains(v.getId()))
        {
            mSelectedViews.add(v);
            mSelectedViewItems.add(v.getId());
            if(mSelectedViews.size() == 2)
            {
                if(v.getId() >= 1 && v.getId() <= question_id)
                {
                    mEndLineCoordinates[0] = 0;
                    mEndLineCoordinates[1] = (xy[1] - mStartLayoutCoordinates[1] + (circleImage.getHeight() / 2));
                }
                else
                {
                    mEndLineCoordinates[0] = mDraw.getWidth();
                    mEndLineCoordinates[1] = (xy[1] - mStartLayoutCoordinates[1] + (circleImage.getHeight() / 2));
                }
                checkAnswers();
            }
            else
            {
                setItemSelected(v);
                if(v.getId() >= 1 && v.getId() <= question_id)
                {
                    mStartLIneCoordinates[0] = 0;
                    mStartLIneCoordinates[1] = (xy[1] - mStartLayoutCoordinates[1] + (circleImage.getHeight() / 2));
                }
                else
                {
                    mStartLIneCoordinates[0] = mDraw.getWidth();
                    mStartLIneCoordinates[1] = (xy[1] - mStartLayoutCoordinates[1] + (circleImage.getHeight() / 2));
                }
            }
        }


    }
    private void setItemSelected(View view)
    {
        ImageView border = (ImageView) view.findViewById(R.id.border);
        ImageView node = (ImageView) view.findViewById(R.id.circle_image);
        border.setImageResource(R.drawable.selected_border);
        node.setImageResource(R.drawable.selected_view_circle);
    }

    private void checkAnswers()
    {
        String text1, text2;
        TextView textView1 = (TextView) mSelectedViews.get(0).findViewById(R.id.text);
        TextView textView2 = (TextView) mSelectedViews.get(1).findViewById(R.id.text);
        text1 = textView1.getText().toString();
        text2 = textView2.getText().toString();
        if(answers.contains(text1+text2) || answers.contains(text2+text1))
        {
            for(View view : mSelectedViews)
            {
                ImageView border = (ImageView) view.findViewById(R.id.border);
                ImageView node = (ImageView) view.findViewById(R.id.circle_image);
                border.setImageResource(R.drawable.right_answer_border);
                node.setImageResource(R.drawable.right_answer_circle);
            }
            mDraw.drawLine(new Coordinates(mStartLIneCoordinates[0], mStartLIneCoordinates[1], mEndLineCoordinates[0], mEndLineCoordinates[1]));
            mSelectedViews.clear();
        }
        else
        {
            for(View view : mSelectedViews)
            {
                ImageView border = (ImageView) view.findViewById(R.id.border);
                ImageView node = (ImageView) view.findViewById(R.id.circle_image);
                border.setImageResource(R.drawable.wrong_answer_border);
                node.setImageResource(R.drawable.wrong_answer_circle);
            }

            new Timer().schedule(new TimerTask() {
                @Override
                public void run()
                {
                    makeViewsRedcolour();
                }
            }, 2000);
        }
    }

    private void makeViewsRedcolour()
    {
        new Thread(){
            @Override
            public void run()
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        for(View view : mSelectedViews)
                        {
                            ImageView border = (ImageView) view.findViewById(R.id.border);
                            ImageView node = (ImageView) view.findViewById(R.id.circle_image);
                            border.setImageResource(R.drawable.outer_border_shape);
                            node.setImageResource(R.drawable.ring_shape);
                            mSelectedViewItems.remove((Object)view.getId());
                        }
                        mSelectedViews.clear();
                    }
                });
            }
        }.start();
    }

}


