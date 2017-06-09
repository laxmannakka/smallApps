package com.next.recyclerviewdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by next on 17/3/17.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.TeacherViewHoler>
{
    ArrayList<TeacherSubject> list;
    Context context;
    CheckboxChangeListener checkboxChangeListener;


    public RecyclerViewAdapter(ArrayList<TeacherSubject> list, Context context, CheckboxChangeListener checkboxChangeListener)
    {
        this.list = list;
        this.context = context;
        this.checkboxChangeListener = checkboxChangeListener;
    }

    @Override
    public TeacherViewHoler onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.checkboxlayout, parent, false);
        return new TeacherViewHoler(view);
    }

    @Override
    public void onBindViewHolder(TeacherViewHoler holder, final int position)
    {
        final TeacherSubject teacherSubject = list.get(position);
        boolean checkstatus = teacherSubject.isCheckstatus();
        holder.mCheckBox.setChecked(checkstatus);
        if (teacherSubject.getMarks() != null)
        {
            holder.mEditText.setText(teacherSubject.getMarks());
        }
        else
        {
            holder.mEditText.setText("0");
        }
        holder.mCheckBox.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                CheckBox checkBox = (CheckBox) v;
                boolean flag = checkBox.isChecked();
                teacherSubject.setCheckstatus(flag);
                if (checkboxChangeListener != null)
                {
                    checkboxChangeListener.setCheckbox(flag, position);
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class TeacherViewHoler extends RecyclerView.ViewHolder
    {
        TextView subjectName, sectionName;
        CheckBox mCheckBox;
        EditText mEditText;

        public TeacherViewHoler(View itemView)
        {
            super(itemView);
            subjectName = (TextView) itemView.findViewById(R.id.name);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.itemCheckbox);
            mEditText = (EditText) itemView.findViewById(R.id.edittext);
        }
    }
}
