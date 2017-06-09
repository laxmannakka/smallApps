package com.fees;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.examination.helper.AnimatedExpandableListView;
import com.resources.erp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ExpandableFeeAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
    private LayoutInflater inflater;

    private List<String> items;
    public double totalAmount;
    public Boolean showCheckBox;
    public ExpandableFeeAdapter(Context context,Boolean showCheck) {
        inflater = LayoutInflater.from(context);
        this.showCheckBox = showCheck;
    }

    public void setData(List<String> items) {
        this.items = items;
    }

    @Override
    public String getGroup(int groupPosition) {
        return items.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return items.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        FeeHolder holder;
        String item = getGroup(groupPosition);
        if (convertView == null) {
            holder = new FeeHolder();
            convertView = inflater.inflate(R.layout.expandable_fee_group_item, parent, false);
            holder.month = (TextView) convertView.findViewById(R.id.textMonthGroup);
            holder.monthCheckBox = (CheckBox) convertView.findViewById(R.id.textFeeCheckBoxGroup);

            convertView.setTag(holder);
        } else {
            holder = (FeeHolder) convertView.getTag();
        }
        if(showCheckBox == false){
            holder.monthCheckBox.setVisibility(View.GONE);
        }else
            holder.monthCheckBox.setVisibility(View.VISIBLE);
        String monthName = item;
        holder.month.setText(monthName);
        /*if(item.getGrade() != null){
            holder.textGradeTitle.setVisibility(View.VISIBLE);
            holder.textGradeTitle.setText(item.getGrade());
        }
        else{
            holder.textGradeTitle.setVisibility(View.GONE);
        }*/

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        return true;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getRealChildView(int groupPosition,
                                 int childPosition, boolean isLastChild,
                                 View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        return 0;
    }
}

class FeeChildHolder
{
    RelativeLayout cardView;
    TextView text_label_fineAmount;
    TextView text_label_concession;
    TextView text_label_total;
    TextView text_label_remarks;
    TextView text_label_dueDate;
    TextView text_value_feeType;
    TextView text_value_instalType;
    TextView text_value_amount;
    TextView text_value_fineAmount;
    TextView text_value_concession;
    TextView text_value_total;
    TextView text_value_dueDate;
    CheckBox check;
}

class FeeHolder
{
    TextView month;
    CheckBox monthCheckBox;
}