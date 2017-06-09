package com.fees;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.examination.helper.AnimatedExpandableListView;
import com.fees.model.ERPCartObject;
import com.fees.model.FeeDue;
import com.resources.erp.R;
import com.utils.ERPModel;
import com.utils.ERPUtil;

import java.util.ArrayList;
import java.util.List;

public class ExpandableFeeChildAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter{
    private LayoutInflater inflater;

    private List<FeeDue> items;

    private List<String> months;
    public Boolean showCheckBox ;
    public double totalAmount;
    public ExpandableFeeChildAdapter(Context context,Boolean showCheck) {
        inflater = LayoutInflater.from(context);
        this.showCheckBox = showCheck;
    }

    public void setData(List<FeeDue> items,List<String> months) {
        this.items = items;
        this.months = months;

    }

    @Override
    public FeeDue getChild(int groupPosition, int childPosition) {
        return items.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
        FeeChildHolder holder;
        final FeeDue feeDue = getChild(groupPosition, childPosition);
        if (view == null) {
            holder = new FeeChildHolder();
            view = inflater.inflate(R.layout.expandable_fee_list_item, parent, false);
            holder.cardView = (RelativeLayout) view.findViewById(R.id.relativeLayout1);
            holder.text_label_fineAmount = (TextView) view.findViewById(R.id.fineAmtId);
            holder.text_label_concession = (TextView) view.findViewById(R.id.concesAmtId);
            holder.text_label_total = (TextView) view.findViewById(R.id.totalId);
            holder.text_label_dueDate = (TextView) view.findViewById(R.id.dueOnDateId);

            holder.text_value_feeType = (TextView) view.findViewById(R.id.feeTypeId);
            holder.text_value_instalType = (TextView) view.findViewById(R.id.instalTypeId);
            holder.text_value_amount = (TextView) view.findViewById(R.id.amount);
            holder.text_value_fineAmount = (TextView) view.findViewById(R.id.fineAmtValId);
            holder.text_value_concession = (TextView) view.findViewById(R.id.concesAmtValId);
            holder.text_value_total = (TextView) view.findViewById(R.id.totalValId);
            holder.text_value_dueDate = (TextView) view.findViewById(R.id.dueOnDateValueId);
            holder.check = (CheckBox) view.findViewById(R.id.payCheck);

            view.setTag(holder);
        } else {
            holder = (FeeChildHolder) view.getTag();
        }
        if(showCheckBox == false){
            holder.check.setVisibility(View.GONE);
        }
        if(feeDue.getDueDate() != null)
        {
            holder.text_value_dueDate.setText(ERPUtil.changeDateFormat(feeDue.getDueDate()));

        }
        else {
            holder.text_label_dueDate.setVisibility(View.GONE);
            holder.text_value_dueDate.setVisibility(View.GONE);
        }

        holder.text_value_amount.setText(ERPModel.rupeeSymbol + ERPUtil.formatDoubleAmount(feeDue.getAmount()));
        if(feeDue.getAmount() != 0)
            holder.text_value_feeType.setText(feeDue.getFeeType());
        else {}
        if(feeDue.getInstalType() != null){
            holder.text_value_instalType.setVisibility(View.VISIBLE);
            holder.text_value_instalType.setText("(" + feeDue.getInstalType() + ") ");
        }else{
            holder.text_value_instalType.setText("");
            holder.text_value_instalType.setVisibility(View.INVISIBLE);
        }
        if(feeDue.getFineAmount() == 0 || ERPModel.selectedKid.getFeeDueDetail().isInstallmentBasedFine())
        {
            holder.text_label_fineAmount.setVisibility(View.GONE);
            holder.text_value_fineAmount.setVisibility(View.GONE);
        }
        else
        {
            holder.text_value_fineAmount.setText( ERPModel.rupeeSymbol + ERPUtil.formatDoubleAmount(feeDue.getFineAmount()));
            holder.text_label_fineAmount.setVisibility(View.VISIBLE);
            holder.text_value_fineAmount.setVisibility(View.VISIBLE);
        }

        // concession
        if(feeDue.getConcession() == 0 || ERPModel.selectedKid.getFeeDueDetail().isInstallBasedConcession())
        {
            holder.text_label_concession.setVisibility(View.GONE);
            holder.text_value_concession.setVisibility(View.GONE);
        }
        else
        {
            holder.text_value_concession.setText(ERPModel.rupeeSymbol + ERPUtil.formatDoubleAmount(feeDue.getConcession()));
            holder.text_label_concession.setVisibility(View.VISIBLE);
            holder.text_value_concession.setVisibility(View.VISIBLE);
        }

        // Total amount
        if(feeDue.getTotal() == 0)
        {
            holder.text_label_total.setVisibility(View.GONE);
            holder.text_value_total.setVisibility(View.GONE);
        }
        else
        {
            holder.text_value_total.setText(ERPModel.rupeeSymbol + ERPUtil.formatDoubleAmount(feeDue.getTotal()));
            holder.text_label_total.setVisibility(View.VISIBLE);
            holder.text_value_total.setVisibility(View.VISIBLE);
        }

        /*holder.check.setOnCheckedChangeListener(contactchecklistener);
        holder.check.setTag(childPosition);*/
        //holder.check.setChecked(array.get(groupPosition).getContacts().get(childPosition).isCheck());
        /*holder.cardView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(ERPModel.selectedKid.getFeeDueDetail().isDisableFeeTypeInParentLogin()){
                    CheckBox cb = (CheckBox) view.findViewById(R.id.payCheck);
                    if (cb.isChecked()) {
                        feeDue.setChecked(true);
                        cb.setChecked(true);
                    }
                }
                else if (ERPModel.ischeckbox) {
                    CheckBox cb = (CheckBox) view.findViewById(R.id.payCheck);
                    if (!cb.isChecked()) {
                        cb.toggle();
                        feeDue.setChecked(true);
                        cb.setChecked(true);
                        (ERPModel.feeDueCardSelectionCount)++;
                        //viewHolder.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        addFeeEntry(feeDue);
                    } else {
                        cb.toggle();
                        feeDue.setChecked(false);
                        cb.setChecked(false);
                        (ERPModel.feeDueCardSelectionCount)--;
                        //viewHolder.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        //deleteFeeEntry(feeDue);
                    }
//						calling this method for updating the contextual actionbar
                    //recyclerViewClickListener.onClick(feeDue);
                }
            }
        });*/
        return view;
    }
    /* private CompoundButton.OnCheckedChangeListener contactchecklistener = new CompoundButton.OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(CompoundButton checkboxView, boolean isChecked) {

             FeeDue feeDue = (FeeDue) checkboxView.getTag();
             feeDue.setCheck(isChecked);

         }

     };*/
   /* public void addFeeEntry(FeeDue feeDue){
        if(feeDue.getSubTypeId() == 1)
        {
            totalAmount = totalAmount - feeDue.getTotal();
        }
        else
        {
            totalAmount = totalAmount + feeDue.getTotal();
        }
        List<ERPCartObject> checkedCardValues = null;
        if (ERPModel.paymentBagMap.get(feeDue.getFeeTypeId()) != null)
        {
            checkedCardValues = ERPModel.paymentBagMap.get(feeDue.getFeeTypeId());
        }
        else
        {
            checkedCardValues = new ArrayList<ERPCartObject>();
        }
        ERPCartObject erpCartObject = new ERPCartObject();
        erpCartObject.setFeeTypeName(feeDue.getFeeType());
        erpCartObject.setFeeDueId(feeDue.getFeeDueId());
        erpCartObject.setSubTypeId(feeDue.getSubTypeId());
        erpCartObject.setFeeTypeId(feeDue.getFeeTypeId());
        erpCartObject.setTotal(feeDue.getTotal());
        erpCartObject.setConcessionAmount(feeDue.getConcession());
        erpCartObject.setInstallmentName(feeDue.getInstalType());
        erpCartObject.setFineAmount(feeDue.getFineAmount());
        erpCartObject.setAmount(feeDue.getAmount());
        erpCartObject.setDueDate(feeDue.getDueDate());
        checkedCardValues.add(erpCartObject);
        ERPModel.paymentBagMap.put(feeDue.getFeeTypeId(), checkedCardValues);

    }*/
    @Override
    public int getRealChildrenCount(int groupPosition) {
        return items.size();
    }

    @Override
    public int getGroupCount() {
        return months.size();
    }

    @Override
    public String getGroup(int groupPosition) {
        return months.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        FeeHolder holder;
        final String item = getGroup(groupPosition);
        if (convertView == null)
        {
            holder = new FeeHolder();
            convertView = inflater.inflate(R.layout.expandable_fee_group_item, parent, false);
            holder.month = (TextView) convertView.findViewById(R.id.textMonthGroup);
            holder.monthCheckBox = (CheckBox) convertView.findViewById(R.id.textFeeCheckBoxGroup);
            if(showCheckBox == false){
                holder.monthCheckBox.setVisibility(View.GONE);
            }else
                holder.monthCheckBox.setVisibility(View.VISIBLE);





            holder.monthCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    CheckBox cb = (CheckBox) buttonView.findViewById(R.id.textFeeCheckBoxGroup);
                    if (!cb.isChecked()) {
                        cb.toggle();
                        cb.setChecked(true);
                        //(ERPModel.feeDueCardSelectionCount)++;
                        //viewHolder.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        //addFeeEntry(feeDue);
                    } else {
                        cb.toggle();
                        cb.setChecked(false);
                        /*feeDue.setChecked(false);
                        cb.setChecked(false);
                        (ERPModel.feeDueCardSelectionCount)--;*/
                        //viewHolder.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        //deleteFeeEntry(feeDue);
                    }

                    /*if (ERPModel.ischeckbox) {
                        CheckBox cb = (CheckBox) view.findViewById(R.id.payCheck);
                        if (!cb.isChecked()) {
                            cb.toggle();
                            feeDue.setChecked(true);
                            cb.setChecked(true);
                            (ERPModel.feeDueCardSelectionCount)++;
                            //viewHolder.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                            //addFeeEntry(feeDue);
                        } else {
                            cb.toggle();
                            feeDue.setChecked(false);
                            cb.setChecked(false);
                            (ERPModel.feeDueCardSelectionCount)--;
                            //viewHolder.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                            //deleteFeeEntry(feeDue);
                        }
//						calling this method for updating the contextual actionbar
                        //recyclerViewClickListener.onClick(feeDue);
                    }*/
                }
            });

            convertView.setTag(holder);

        }
        else {
            holder = (FeeHolder) convertView.getTag();
        }
        String monthName = item;
        holder.month.setText(monthName);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }



}
