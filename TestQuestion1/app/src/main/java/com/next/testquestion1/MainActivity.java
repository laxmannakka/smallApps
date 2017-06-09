package com.next.testquestion1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setData();


    }
private List<Student> setData()
{
    ArrayList<Student> studentList = new ArrayList<>();
    String name;
    for(int i=0;i<10;i++)
    {
        Student student = new Student();
        for (int j = 0; j < 3; j++)
        {

        }
    }

return null;

}



}
