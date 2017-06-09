package com.next.matchthefollowing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by next on 25/4/17.
 */
public class ListViewAnsAdapter extends BaseAdapter
{
    List<Answer> arrayList;
    LayoutInflater inflater;

    public ListViewAnsAdapter(List<Answer> arrayList, Context context )
    {
        this.arrayList = arrayList;
        inflater =LayoutInflater.from(context);
    }

    @Override
    public int getCount()
    {

        return arrayList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent)
    {
        TextView textView;
        RadioButton radioButton;
        View view=null;
        view = inflater.inflate(R.layout.ans, null);
        textView = (TextView) view.findViewById(R.id.ans_txt);
        radioButton = (RadioButton) view.findViewById(R.id.radio_but);


        textView.setText(arrayList.get(position).getAnswer());
        return view;
    }
}
