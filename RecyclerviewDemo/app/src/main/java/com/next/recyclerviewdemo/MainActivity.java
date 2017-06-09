package com.next.recyclerviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements CheckboxChangeListener
{
    RecyclerView mRecyclerview;
    ArrayList<TeacherSubject> list = new ArrayList<>();
    boolean checkBoxMainScreenClick = true;
    RecyclerViewAdapter adapter;
    private Set<Long> mCurrentSelectedMarks = new HashSet<>();
    CheckBox mCheckBox;
    EditText mEditext;
    Button mButton;
ArrayList<TeacherSubject> checkMarked = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerview = (RecyclerView) findViewById(R.id.grid_recycler_view);
        LinearLayoutManager lLayout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mCheckBox = (CheckBox) findViewById(R.id.checkbox);
        mEditext = (EditText)findViewById(R.id.edittextmain);
        mButton =(Button)findViewById(R.id.done);


        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (checkBoxMainScreenClick)
                {
                    setcheckedData(isChecked);
                }
            }
        });



        mButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String value = mEditext.getText().toString();
                float ll = Float.parseFloat(value);
                getSelectedList(ll);



            }
        });
        // setting the layout
        mRecyclerview.setLayoutManager(lLayout);
        setdata();
        adapter = new RecyclerViewAdapter(list, this, this);
        mRecyclerview.setAdapter(adapter);


    }

    public ArrayList<TeacherSubject>  getSelectedList(float value)
    {

        List<Long> pos = new ArrayList<>(mCurrentSelectedMarks);
        for(int i=0;i<pos.size();i++)
        {
         long postion=   pos.get(i);

          TeacherSubject subject = list.get((int)postion);
            subject.setMarks(String.valueOf(value));
        }
        adapter.notifyDataSetChanged();
        return checkMarked;
    }


    public void setcheckedData(boolean isChecked)
    {
        checkBoxMainScreenClick = true;
        long i=0;

        for (TeacherSubject subject : list)
        {

            if(isChecked)
            {
                subject.setCheckstatus(isChecked);
                mCurrentSelectedMarks.add( i);
                i++;
            }
            else
            {
                subject.setCheckstatus(isChecked);
                mCurrentSelectedMarks.remove(i);
                i++;
            }
        }
        adapter.notifyDataSetChanged();
    }


    public void setdata()
    {
        for (int i = 0; i < 100; i++)
        {
            TeacherSubject obj = new TeacherSubject();
            obj.setName("English");
            list.add(obj);
        }
    }

    @Override
    public void setCheckbox(boolean isChecked, int position)
    {
        if (isChecked)
        {
            mCurrentSelectedMarks.add(Long.valueOf(position));
        } else
        {
            mCurrentSelectedMarks.remove(Long.valueOf(position));
        }

        if (mCurrentSelectedMarks.size() == list.size())
        {
            if (!mCheckBox.isChecked())
            {
                checkBoxMainScreenClick = false;
                mCheckBox.setChecked(true);
                checkBoxMainScreenClick = true;
            }
        }
        else
        {
            if (mCheckBox.isChecked())
            {
                checkBoxMainScreenClick = false;
                mCheckBox.setChecked(false);
                checkBoxMainScreenClick = true;
            }
        }
    }
}
