package com.next.jsonproblem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity
{

    ArrayList<Student> studentArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String responce = JsonReader.jsonreader(this);
        formListOfStudents(responce);
        sortByName();
        sortById();
        sortByroolNo();
    }

    public void formListOfStudents(String jsonresponce)
    {
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;

        try
        {
            jsonObject = new JSONObject(jsonresponce);
            jsonArray = jsonObject.getJSONArray("studentdetails");

            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject json = jsonArray.getJSONObject(i);
                Student student = new Student();
                student.setName(json.getString("name"));
                student.setId(json.getString("id"));
                student.setRoolNo(json.getInt("roolNo"));
                studentArrayList.add(student);
            }

        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    private  void sortByName()
    {
        ArrayList<Student> tempList = studentArrayList;

        Collections.sort(tempList, new Comparator<Student>()
        {
            @Override
            public int compare(Student s1, Student s2)
            {
                return s1.getName().compareToIgnoreCase(s2.getName());
            }

        });

        for(int i=0;i<tempList.size();i++)
        {
            Student student = tempList.get(i);
            Log.i("Sorted String",""+student.getName());
        }

    }
    private void sortByroolNo()
    {
        ArrayList<Student> tempList = studentArrayList;
        Collections.sort(tempList, new Comparator<Student>()
        {
            @Override
            public int compare(Student p1, Student p2)
            {
                return p1.roolNo - p2.roolNo;
            }
        });

        for(int i=0;i<tempList.size();i++)
        {
            Student student = tempList.get(i);
            Log.i("Sorted String",""+student.getRoolNo());
        }
    }


    private void sortById()
    {
        ArrayList<Student> tempList = studentArrayList;
        Collections.sort(tempList, new Comparator<Student>()
        {
            @Override
            public int compare(Student o1, Student o2)
            {
                String string1 = o1.getId();
                String string2 = o2.getId();
                String id1 = string1.substring(string1.indexOf("_")+1);
                String id2 = string2.substring(string2.indexOf("_")+1);
                return id1.compareToIgnoreCase(id2);
            }
        });
        for(int i=0;i<tempList.size();i++)
        {
            Student student = tempList.get(i);
            Log.i("Sorted String",""+student.getId());
        }


    }
}

