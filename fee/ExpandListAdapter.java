package com.fees;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fees.interfaces.TotalListener;
import com.fees.model.ERPCartObject;
import com.fees.model.FeeDue;
import com.resources.erp.R;
import com.utils.ERPModel;
import com.utils.ERPUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ExpandListAdapter extends BaseExpandableListAdapter {

    private Map<String, List<FeeDue>> mGroupList;
    public double totalAmount;    /*
     *  Raw Data
     */
    String[] testChildData =  {"10","20","30", "40", "50"};
    //String[] testgroupData =  {"Apple","Banana","Mango", "Orange", "Pineapple", "Strawberry"};
    Context mContext;
    ArrayList<ArrayList<Boolean>> selectedChildCheckBoxStates = new ArrayList<>();
    ArrayList<Boolean> selectedParentCheckBoxesState = new ArrayList<>();
    TotalListener mListener;

    public void setmListener(TotalListener mListener) {
        this.mListener = mListener;
    }

    public void setmGroupList(Map<String, List<FeeDue>> mGroupList) {
        this.mGroupList = mGroupList;
    }

    class ViewHolder {
        public CheckBox groupName;
        public TextView dummyTextView; // View to expand or shrink the list
        public CheckBox childCheckBox;
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
        RelativeLayout relativeLayout;
    }

    public ExpandListAdapter(Context context, Map<String, List<FeeDue>> filteredFeeList) {
        mContext = context;

        //Add raw data into Group List Array
        /*for(int i = 0; i < testgroupData.length; i++){
            ArrayList<String> prices = new ArrayList<>();
            for(int j = 0; j < testChildData.length; j++) {
                prices.add(testChildData[j]);
        }
            mGroupList.add(i, prices);
        }*/
        mGroupList=filteredFeeList;
        //initialize default check states of checkboxes
        initCheckStates(false);
    }

    /**
     * Called to initialize the default check states of items
     * @param defaultState : false
     */
    private void initCheckStates(boolean defaultState) {
        Iterator entries = mGroupList.entrySet().iterator();
        int i =0;
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            String key = (String)entry.getKey();
            selectedParentCheckBoxesState.add(i, defaultState);

            ArrayList<Boolean> childStates = new ArrayList<>();
            List<FeeDue> value = (List<FeeDue>)entry.getValue();
            for (FeeDue feeDue:value) {
                childStates.add(defaultState);
            }
            selectedChildCheckBoxStates.add(i, childStates);
            i++;
        }
        /*for(int i1 = 0 ; i < mGroupList.size(); i++){
            selectedParentCheckBoxesState.add(i, defaultState);
            ArrayList<Boolean> childStates = new ArrayList<>();
            for(int j1 = 0; j1 < mGroupList.get(i).size(); j++){
                childStates.add(defaultState);
            }

            selectedChildCheckBoxStates.add(i, childStates);
        }*/
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mGroupList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    @Override
    public int getChildrenCount(int groupPosition) {
        Iterator entries = mGroupList.entrySet().iterator();
        int i =0;
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            if(i==groupPosition){
                List<FeeDue> value = (List<FeeDue>)entry.getValue();
                return value.size();
            }else i++;
        }
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroupList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return mGroupList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.expandable_fee_group_item, null);
            holder = new ViewHolder();
            holder.groupName = (CheckBox) convertView.findViewById(R.id.textFeeCheckBoxGroup);
            holder.dummyTextView = (TextView) convertView.findViewById(R.id.textMonthGroup);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        int i=0;
        Object groupName = null;
        Iterator entries = mGroupList.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            if(i==groupPosition){
                groupName = entry.getKey();break;
            }else i++;
        }
        holder.dummyTextView.setText(groupName.toString());
        if(selectedParentCheckBoxesState.size() <= groupPosition){
            selectedParentCheckBoxesState.add(groupPosition, false);
        }else {
            holder.groupName.setChecked(selectedParentCheckBoxesState.get(groupPosition));
        }



        holder.groupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //Callback to expansion of group item
                if(!isExpanded)
                    mListener.expandGroupEvent(groupPosition, isExpanded);

                boolean state = selectedParentCheckBoxesState.get(groupPosition);
                Log.d("TAG", "STATE = " + state);
                selectedParentCheckBoxesState.remove(groupPosition);
                selectedParentCheckBoxesState.add(groupPosition, state ? false : true);
                Iterator entries = mGroupList.entrySet().iterator();
                int i =0;
                List<FeeDue> feeDueList = null;
                while (entries.hasNext()) {
                    Map.Entry entry = (Map.Entry) entries.next();
                    if(i==groupPosition){
                        feeDueList = (List<FeeDue>)entry.getValue();
                        break;

                    }else i++;
                }
                int j = 0;
                for (FeeDue fee :feeDueList) {
                    selectedChildCheckBoxStates.get(groupPosition).remove(j);
                    selectedChildCheckBoxStates.get(groupPosition).add(j, state ? false : true);
                    j++;

                    //showTotal(groupPosition);
                }
                notifyDataSetChanged();
                showTotal(groupPosition);
            }
        });


        //callback to expand or shrink list view from dummy text click
        holder.dummyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //Callback to expansion of group item
                mListener.expandGroupEvent(groupPosition, isExpanded);
            }
        });

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.expandable_fee_list_item, null);
            holder = new ViewHolder();
            holder.childCheckBox = (CheckBox) convertView.findViewById(R.id.payCheck);

            holder.text_label_fineAmount = (TextView) convertView.findViewById(R.id.fineAmtId);
            holder.text_label_concession = (TextView) convertView.findViewById(R.id.concesAmtId);
            holder.text_label_total = (TextView) convertView.findViewById(R.id.totalId);
            holder.text_label_dueDate = (TextView) convertView.findViewById(R.id.dueOnDateId);

            holder.text_value_feeType = (TextView) convertView.findViewById(R.id.feeTypeId);
            holder.text_value_instalType = (TextView) convertView.findViewById(R.id.instalTypeId);
            holder.text_value_amount = (TextView) convertView.findViewById(R.id.amount);
            holder.text_value_fineAmount = (TextView) convertView.findViewById(R.id.fineAmtValId);
            holder.text_value_concession = (TextView) convertView.findViewById(R.id.concesAmtValId);
            holder.text_value_total = (TextView) convertView.findViewById(R.id.totalValId);
            holder.text_value_dueDate = (TextView) convertView.findViewById(R.id.dueOnDateValueId);
            holder.relativeLayout = (RelativeLayout) convertView.findViewById(R.id.relativeLayout1);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
//mGroupList.get(groupPosition).get(childPosition);

        //holder.childCheckBox.setText((CharSequence) mGroupList.get(groupPosition).get(childPosition));
        int i =0;
        List<FeeDue> feeDueList = null;
        Iterator entries = mGroupList.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            if(i==groupPosition){
                feeDueList = (List<FeeDue>)entry.getValue();
                break;
            }else i++;
        }
        FeeDue feeDue = null;
        if(feeDueList!=null){
            int j=0;
            for (FeeDue feedue:feeDueList) {
                if(j==childPosition){
                    feeDue = feedue;
                    break;
                }else j++;
            }
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
        if(selectedChildCheckBoxStates.size() <= groupPosition) {
            ArrayList<Boolean> childState = new ArrayList<>();
            int j=0;
            for (FeeDue feedue:feeDueList ) {
                if(childState.size() > childPosition) {
                    childState.add(childPosition, false);
                    j++;
                }
                else {
                    childState.add(false);
                    j++;
                }
            }
                   /* for(int i= 0; i < mGroupList.get(groupPosition).size(); i++){
                        if(childState.size() > childPosition)
                        childState.add(childPosition, false);
                        else
                            childState.add(false);
                    }*/
            if(selectedChildCheckBoxStates.size() > groupPosition) {
                selectedChildCheckBoxStates.add(groupPosition, childState);
            }else
                selectedChildCheckBoxStates.add(childState);
        }else{
            holder.childCheckBox.setChecked(selectedChildCheckBoxStates.get(groupPosition).get(childPosition));
        }

        /*if(!ERPModel.selectedKid.getFeeDueDetail().isDisableFeeTypeInParentLogin()) {
            holder.childCheckBox.setTextAppearance(mContext, R.style.BrandedCheckBox);
            int id = Resources.getSystem().getIdentifier("BrandedCheckBox", "styles", "android");
            holder.childCheckBox.setButtonDrawable(id);
        }*/


        holder.childCheckBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!ERPModel.selectedKid.getFeeDueDetail().isDisableFeeTypeInParentLogin()) {
                    boolean state = selectedChildCheckBoxStates.get(groupPosition).get(childPosition);
                    selectedChildCheckBoxStates.get(groupPosition).remove(childPosition);
                    selectedChildCheckBoxStates.get(groupPosition).add(childPosition, state ? false : true);

                    showTotal(groupPosition, childPosition);
                }else{
                    if(selectedParentCheckBoxesState.get(groupPosition)){
                        holder.childCheckBox.setChecked(true);
                    }else
                        holder.childCheckBox.setChecked(false);
                }

            }
        });


        return convertView;
    }

    /**
     * Called to reflect the sum of checked prices
     * @param groupPosition : group position of list
     */
    private void showTotal(int groupPosition) {
        double sum = 0;
        for (int i = 0; i < selectedChildCheckBoxStates.get(groupPosition).size(); i++) {
            Log.d("TAG", "I = " + i);

            if (selectedChildCheckBoxStates.get(groupPosition).get(i)) {
                FeeDue feeDue = getChildFeeDueAmount(i,groupPosition);
                addFeeEntry(feeDue);
            }else{
                FeeDue feeDue = getChildFeeDueAmount(i,groupPosition);
                deleteFeeEntry(feeDue);
            }
        }
        mListener.onTotalChanged(sum);
    }



    private void showTotal(int groupPosition, int childPosition) {
        //Below code is to get the sum of checked prices
        double sum = 0;
        for (int i = 0; i <= selectedChildCheckBoxStates.get(groupPosition).size()-1; i++) { // 3
            Log.d("TAG", "I = " + i);
            if(i == childPosition) {
                if (selectedChildCheckBoxStates.get(groupPosition).get(i)) {
                    FeeDue feeDue = getChildFeeDueAmount(i, groupPosition);
                    addFeeEntry(feeDue);
                } else {
                    FeeDue feeDue = getChildFeeDueAmount(i, groupPosition);
                    deleteFeeEntry(feeDue);
                }
            }
        }
        mListener.onTotalChanged(sum);
    }




    public void deleteFeeEntry(FeeDue feeDue) {

        if (ERPModel.paymentBagMap.get(feeDue.getFeeTypeId()) != null
                && feeDue.getSubTypeId() == 1) {
            totalAmount = totalAmount + feeDue.getTotal();
            // ERPModel.paymentBagMap.get(feeDue.getFeeTypeId()).clear();
            if (ERPModel.paymentBagMap.get(feeDue.getFeeTypeId()) != null) {
                List<ERPCartObject> list = ERPModel.paymentBagMap.get(feeDue
                        .getFeeTypeId());
                if (list.size() == 1) {
                    ERPModel.paymentBagMap.get(feeDue.getFeeTypeId()).clear();
                } else {
                    for (ERPCartObject object : list) {
                        if (object.getFeeDueId() == feeDue.getFeeDueId()) {
                            list.remove(object);
                            ERPModel.paymentBagMap.put(feeDue.getFeeTypeId(),
                                    list);
                            return;
                        }
                    }
                }
            }
        } else if (ERPModel.paymentBagMap.get(feeDue.getFeeTypeId()) != null) {
            totalAmount = totalAmount - feeDue.getTotal(); // -1000-1000 = -2000
            // ERPModel.paymentBagMap.get(feeDue.getFeeTypeId()).clear();
            if (ERPModel.paymentBagMap.get(feeDue.getFeeTypeId()) != null) {
                List<ERPCartObject> list = ERPModel.paymentBagMap.get(feeDue
                        .getFeeTypeId());
                if (list.size() == 1) {
                    ERPModel.paymentBagMap.get(feeDue.getFeeTypeId()).clear();
                } else {
                    for (ERPCartObject object : list) {
                        if (object.getFeeDueId() == feeDue.getFeeDueId()) {
                            list.remove(object);
                            ERPModel.paymentBagMap.put(feeDue.getFeeTypeId(),
                                    list);
                            return;
                        }
                    }
                }
            }
        }
        System.out.println("Total amount = "+totalAmount);
    }
    public void addFeeEntry(FeeDue feeDue){
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
        Boolean toAdd = true;
        if(checkedCardValues.size()>0){
            for (ERPCartObject object:checkedCardValues) {
                if(object.getFeeDueId()==feeDue.getFeeDueId()) {
                    toAdd = false;
                    break;
                }
            }
        }
        if(toAdd) {
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
        }

        System.out.println("Total amount = "+totalAmount);

    }

    private FeeDue getChildFeeDueAmount(int childPosition, int groupPosition) {
        List<FeeDue> feeDueList = null;
        int i =0;
        Iterator entries = mGroupList.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            if(i==groupPosition){
                feeDueList = (List<FeeDue>)entry.getValue();
                break;
            }else i++;
        }
        FeeDue feeDue = null;
        if(feeDueList!=null){
            int j=0;
            for (FeeDue feedue:feeDueList) {
                if(j==childPosition){
                    feeDue = feedue;
                    break;
                }else j++;
            }
        }
        return feeDue;
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
