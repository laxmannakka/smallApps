package com.next.matchthefollowing;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
{


    int[] qnsposition = new int[2];
    int[] ansposition = new int[2];
    LinearLayout parentLineraLayout;
    int[] parentLayoutPosition = new int[2];
    int q_x, q_y, a_x, a_y;
    CustomView customView;
    int question_id;
    int answer_id;
    RadioButton question_radioButton;
    RadioButton answer_radioButton;
    boolean isAnswerClicked = false, isQuestionClicked = false;
    final List<Answer> ansList = new ArrayList<>();
    int itemPosition = 0;

    TextView selectedQuestionTextview,selectedAnsweTextview;
    RadioButton q_radioButton,a_radioButton;
    View ansView,queView;

    ArrayList<Point> arrayList = new ArrayList<>();
    int selectedItemPostion;
    ArrayList<Integer> selectedAnswerPostions= new ArrayList<>();
    ArrayList<Integer> SelectedQuestionPositions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ListView questionListview = (ListView) findViewById(R.id.question_list);
        final ListView ansListView = (ListView) findViewById(R.id.ans_list);
        parentLineraLayout = (LinearLayout) findViewById(R.id.parentLayout);
        customView = (CustomView) findViewById(R.id.drawview);

        final List<Question> questionList = new ArrayList<>();
        questionList.add(new Question(1, "1+2"));
        questionList.add(new Question(2, "4*4"));
        questionList.add(new Question(3, "2-1"));

        ansList.add(new Answer("16", 1, 2));
        ansList.add(new Answer("1", 2, 3));
        ansList.add(new Answer("3", 3, 1));
        ListViewAdapter qnsAdpter = new ListViewAdapter(questionList, MainActivity.this);
        questionListview.setAdapter(qnsAdpter);
        ListViewAnsAdapter adapter = new ListViewAnsAdapter(ansList, MainActivity.this);
        ansListView.setAdapter(adapter);

        customView.post(new Runnable()
        {
            @Override
            public void run()
            {
                if(arrayList.size() > 0)
                {
                    for(Point point : arrayList)
                    {
                        if(point.getStartPoint_A() != 0)
                            point.setStartPoint_A(customView.getWidth());
                        customView.addObjects(point);
                    }
                }
            }
        });



        questionListview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3)
            {
               if(SelectedQuestionPositions !=null&& SelectedQuestionPositions.contains(arg2))
               {
                   return;
               }

                question_radioButton = (RadioButton) v.findViewById(R.id.radio_button);
                isQuestionClicked = true;
                question_radioButton.setChecked(true);
                question_id = questionList.get(arg2).getQ_id();
                int viewhight[] = new int[2];
                v.getLocationOnScreen(viewhight);
                parentLineraLayout.getLocationOnScreen(parentLayoutPosition);
                q_x = 0;
                q_y = viewhight[1] - parentLayoutPosition[1];

                checkAnswerDrawLine(v, arg2, false);
            }
        });


        ansListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3)
            {

                if(selectedAnswerPostions!=null&& selectedAnswerPostions.contains(arg2))
                {
                    return;
                }

                answer_radioButton = (RadioButton) v.findViewById(R.id.radio_but);
                answer_radioButton.setChecked(true);
                isAnswerClicked = true;

               /* if (ansList.get(arg2).getKeyset() == question_id)
                {*/
                parentLineraLayout.getLocationOnScreen(parentLayoutPosition);
                int viewhight[] = new int[2];
                v.getLocationOnScreen(viewhight);
                a_x = customView.getWidth();
                a_y = viewhight[1] - parentLayoutPosition[1];

                checkAnswerDrawLine(v, arg2, true);

            }
        });
    }


    private void checkAnswerDrawLine(View view, int postion, boolean isfromAnswer)
    {
        if (isfromAnswer)
        {
            itemPosition = postion;
        }
        else
        {
            selectedItemPostion= postion;
        }

        if(isfromAnswer)
        {
            ansView = view;
            selectedAnsweTextview  = (TextView) view.findViewById(R.id.ans_txt);
            a_radioButton = (RadioButton) view.findViewById(R.id.radio_but);
            LayerDrawable bgDrawable_answer = (LayerDrawable) selectedAnsweTextview.getBackground();
            GradientDrawable shape1 = (GradientDrawable) (bgDrawable_answer.findDrawableByLayerId(R.id.top_border));
            shape1.setStroke(10,ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
        else
        {
            queView = view;
            selectedQuestionTextview  = (TextView) view.findViewById(R.id.qns_txt);
            LayerDrawable bgDrawable_question = (LayerDrawable) selectedQuestionTextview.getBackground();
            GradientDrawable shape2 = (GradientDrawable) (bgDrawable_question.findDrawableByLayerId(R.id.top_border));
            shape2.setStroke(10,ContextCompat.getColor(this, R.color.colorPrimaryDark));
            q_radioButton = (RadioButton) view.findViewById(R.id.radio_button);
        }


        if (isAnswerClicked && isQuestionClicked)
        {
            if (ansList.get(itemPosition).getKeyset() == question_id)
            {
                isAnswerClicked = false;
                isQuestionClicked = false;
                LayerDrawable bgDrawable_answer = (LayerDrawable) selectedAnsweTextview.getBackground();
                GradientDrawable shape1 = (GradientDrawable) (bgDrawable_answer.findDrawableByLayerId(R.id.top_border));
                shape1.setStroke(10,ContextCompat.getColor(this, R.color.yellow));

                LayerDrawable bgDrawable_question = (LayerDrawable) selectedQuestionTextview.getBackground();
                GradientDrawable shape2 = (GradientDrawable) (bgDrawable_question.findDrawableByLayerId(R.id.top_border));
                shape2.setStroke(10,ContextCompat.getColor(this, R.color.yellow));
                int textColor = Color.parseColor("#00ff00");
                //set textcolor to radioButton
                answer_radioButton.setChecked(true);
                question_radioButton.setChecked(true);
                q_radioButton.setButtonTintList(ColorStateList.valueOf(textColor));
                a_radioButton.setButtonTintList(ColorStateList.valueOf(textColor));
                customView.addObjects(new Point(q_x, q_y, a_x, a_y));
                arrayList.add(new Point(q_x,q_y,a_x,a_y));
                selectedAnswerPostions.add(itemPosition);
                SelectedQuestionPositions.add(selectedItemPostion);

            }
            else
            {

                isAnswerClicked = false;
                isQuestionClicked = false;
                TextView textView = (TextView) ansView.findViewById(R.id.ans_txt);
                LayerDrawable bgDrawable_answer = (LayerDrawable) textView.getBackground();
                GradientDrawable shape1 = (GradientDrawable) (bgDrawable_answer.findDrawableByLayerId(R.id.top_border));
                shape1.setStroke(10,ContextCompat.getColor(getApplicationContext(), R.color.red));

                TextView qtextView = (TextView) queView.findViewById(R.id.qns_txt);
                LayerDrawable bgDrawable_qeu = (LayerDrawable) qtextView.getBackground();
                GradientDrawable shape2 = (GradientDrawable) (bgDrawable_qeu.findDrawableByLayerId(R.id.top_border));
                shape2.setStroke(10,ContextCompat.getColor(getApplicationContext(), R.color.red));

              Timer  myTimer = new Timer();
                myTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        makeViewsRedcolour();
                    }

                }, 2000);

                int textColor = Color.parseColor("#FF0000");
                q_radioButton.setButtonTintList(ColorStateList.valueOf(textColor));
                a_radioButton.setButtonTintList(ColorStateList.valueOf(textColor));
                answer_radioButton.setChecked(false);
                question_radioButton.setChecked(false);

            }

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
                        int textColor = Color.parseColor("#000000");
                        TextView textView = (TextView) ansView.findViewById(R.id.ans_txt);
                        LayerDrawable bgDrawable_answer = (LayerDrawable) textView.getBackground();
                        GradientDrawable shape1 = (GradientDrawable) (bgDrawable_answer.findDrawableByLayerId(R.id.top_border));
                        shape1.setStroke(10,ContextCompat.getColor(getApplicationContext(), R.color.top_border));
                        RadioButton radioButton = (RadioButton) ansView.findViewById(R.id.radio_but);
                        radioButton.setButtonTintList(ColorStateList.valueOf(textColor));

                        TextView qtextView = (TextView) queView.findViewById(R.id.qns_txt);
                        LayerDrawable bgDrawable_qeu = (LayerDrawable) qtextView.getBackground();
                        GradientDrawable shape2 = (GradientDrawable) (bgDrawable_qeu.findDrawableByLayerId(R.id.top_border));
                        shape2.setStroke(10,ContextCompat.getColor(getApplicationContext(), R.color.top_border));
                        RadioButton radioButton1 = (RadioButton) queView.findViewById(R.id.radio_button);
                        radioButton1.setButtonTintList(ColorStateList.valueOf(textColor));
                    }
                });
            }
        }.start();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        arrayList = savedInstanceState.getParcelableArrayList("pointsArray");


    }

    @Override
    protected void onSaveInstanceState(Bundle bundle)
    {
        super.onSaveInstanceState(bundle);

    bundle.putParcelableArrayList("pointsArray",arrayList);

    }
}
