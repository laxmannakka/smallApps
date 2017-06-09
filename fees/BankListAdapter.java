package com.fees;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fees.model.Bank;
import com.resources.erp.R;

import java.util.List;

public class BankListAdapter extends BaseAdapter {

	private List<Bank> myList;

	private Context parentActivity;
	private LayoutInflater inflater;

	public BankListAdapter(Context parent, List<Bank> l) {
		parentActivity = parent;
		myList = l;
		inflater = (LayoutInflater) parentActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return myList.size();
	}

	@Override
	public Object getItem(int position) {
		return myList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (convertView == null)
			view = inflater.inflate(R.layout.spinner_item_downlaodchallan, null);

		TextView text1 = (TextView) view.findViewById(R.id.text1);
		Bank myObj = myList.get(position);
		text1.setText(String.valueOf(myObj.getName())+"\n(A/C-"+String.valueOf(myObj.getAcc_no())+")");
		return view;
	}

}
