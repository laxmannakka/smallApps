package com.next.marking;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
    List<String> arrayList;
    LayoutInflater inflater;
    OnClickLisner onClickLisner;

    public ListViewAnsAdapter(List<String> arrayList, Context context, OnClickLisner onClickLisner )
    {
        this.arrayList = arrayList;
        inflater =LayoutInflater.from(context);
        this.onClickLisner =onClickLisner;
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
        textView = (TextView) view.findViewById(R.id.qns_txt);
        radioButton = (RadioButton) view.findViewById(R.id.radio_but);

        radioButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    float xposition =  event.getX();
                    float yposition =  event.getX();
                    Log.i("posotion"," "+xposition+yposition+"position of layout"+position+v.getId());

                    onClickLisner.onclickRadioButton(xposition,yposition,v.getId(),false);
                }
                return true;
            }
        });

        textView.setText(arrayList.get(position));
        return view;
    }
}
