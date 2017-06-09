package com.fees;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.fees.model.ERPCartObject;
import com.fees.model.FeeDue;
import com.interfaces.RecyclerViewClickListener;
import com.resources.erp.R;
import com.utils.ERPModel;
import com.utils.ERPUtil;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("NewApi")
public class FeeDueSummaryAdapter extends RecyclerView.Adapter<FeeDueSummaryAdapter.CustomViewHolder >
{

	List<FeeDue> feeDueSummaryList;
	RecyclerViewClickListener recyclerViewClickListener;
	public double totalAmount;
	public Boolean showCheckBox ;
	public FeeDueSummaryAdapter(List<FeeDue> feeDueSummaryList,RecyclerViewClickListener recyclerViewClickListener,Boolean showCheck)
	{
		this.feeDueSummaryList = feeDueSummaryList;
		this.recyclerViewClickListener = recyclerViewClickListener;
		this.showCheckBox = showCheck;
	}

	static class CustomViewHolder extends RecyclerView.ViewHolder
	{
		TextView text_label_scholarShipName;
		TextView text_label_fineAmount;
		TextView text_label_concession;
		TextView text_label_total;
		TextView text_label_remarks;
		TextView text_label_dueDate;

		TextView text_value_feeType;
		TextView text_value_instalType;
		TextView text_value_amount;
		TextView text_value_scholarShipName;
		TextView text_value_fineAmount;
		TextView text_value_concession;
		TextView text_value_total;
		TextView text_value_remarks;
		TextView text_value_dueDate;
		TextView install_name;
		CardView cardView;
		CheckBox check;
		public CustomViewHolder(View view)
		{
			super(view);
			cardView = (CardView) view.findViewById(R.id.card_view);
//			text_label_scholarShipName = (TextView) view.findViewById(R.id.scholarId);
			text_label_fineAmount = (TextView) view.findViewById(R.id.fineAmtId);
			text_label_concession = (TextView) view.findViewById(R.id.concesAmtId);
			text_label_total = (TextView) view.findViewById(R.id.totalId);
//			text_label_remarks = (TextView) view.findViewById(R.id.remarkId);
			text_label_dueDate = (TextView) view.findViewById(R.id.dueOnDateId);

			text_value_feeType = (TextView) view.findViewById(R.id.feeTypeId);
			text_value_instalType = (TextView) view.findViewById(R.id.instalTypeId);
			text_value_amount = (TextView) view.findViewById(R.id.amount);
//			text_value_scholarShipName = (TextView) view.findViewById(R.id.scholarValId);
			text_value_fineAmount = (TextView) view.findViewById(R.id.fineAmtValId);
			text_value_concession = (TextView) view.findViewById(R.id.concesAmtValId);
			text_value_total = (TextView) view.findViewById(R.id.totalValId);
//			text_value_remarks = (TextView) view.findViewById(R.id.remarkValId);
			text_value_dueDate = (TextView) view.findViewById(R.id.dueOnDateValueId);
			//install_name = (TextView) view.findViewById(R.id.installName);
			check = (CheckBox) view.findViewById(R.id.payCheck);
		}

	}

	@Override
	public int getItemCount()
	{
		return feeDueSummaryList.size();
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
			totalAmount = totalAmount - feeDue.getTotal();
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
	}

	@Override
	public void onBindViewHolder(CustomViewHolder customViewHolder, int position)
	{
		final CustomViewHolder viewHolder = customViewHolder;

		final FeeDue feeDue = feeDueSummaryList.get(position);

//		Setting the visibility of the checkbox . ERPMODEL.ischeckbox will be true when user clicks on PayNow button
		if(ERPModel.ischeckbox)
		{
			customViewHolder.check.setVisibility(View.VISIBLE);
//			if(customViewHolder.check.isChecked())
//			{
//				customViewHolder.check.setChecked(true);
//				customViewHolder.cardView.setCardBackgroundColor(Color.parseColor("#2600FFFF"));
//			}
//			else{
//				customViewHolder.check.setChecked(false);
//				customViewHolder.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
//			}
			if(feeDue.isChecked())
			{
				customViewHolder.check.setChecked(true);
//				customViewHolder.cardView.setCardBackgroundColor(Color.parseColor("#1A00FFFF"));
				customViewHolder.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
			}
			else{
				customViewHolder.check.setChecked(false);
				customViewHolder.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
			}
		}
		else
		{
			customViewHolder.check.setVisibility(View.GONE);
			customViewHolder.check.setChecked(false);
			customViewHolder.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
		}

		if(showCheckBox == false){
			customViewHolder.check.setVisibility(View.GONE);
		}
//		onclickListener for CardView . will be enabled only if ERPMODEL.ischeckbox is true
		customViewHolder.cardView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if (ERPModel.ischeckbox) {
					CheckBox cb = (CheckBox) view.findViewById(R.id.payCheck);
					if (!cb.isChecked()) {
						cb.toggle();
						feeDue.setChecked(true);
						cb.setChecked(true);
						(ERPModel.feeDueCardSelectionCount)++;
//							viewHolder.cardView.setCardBackgroundColor(Color.parseColor("#1A00FFFF"));
						viewHolder.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
						addFeeEntry(feeDue);
					} else {
						cb.toggle();
						feeDue.setChecked(false);
						cb.setChecked(false);
						(ERPModel.feeDueCardSelectionCount)--;
						viewHolder.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
						deleteFeeEntry(feeDue);
					}
//						calling this method for updating the contextual actionbar
					recyclerViewClickListener.onClick(feeDue);
				}
			}
		});
		if(feeDue.getDueDate() != null)
		{
			customViewHolder.text_value_dueDate.setText(ERPUtil.changeDateFormat(feeDue.getDueDate()));

		}
		else {
			customViewHolder.text_label_dueDate.setVisibility(View.GONE);
			customViewHolder.text_value_dueDate.setVisibility(View.GONE);
		}

		customViewHolder.text_value_amount.setText(ERPModel.rupeeSymbol + ERPUtil.formatDoubleAmount(feeDue.getAmount()));
		if(feeDue.getAmount() != 0)
			customViewHolder.text_value_feeType.setText(feeDue.getFeeType());
		else {
			/*feeDueSummaryList.remove(position);
			notifyItemRemoved(position);*/
			/*customViewHolder.text_label_dueDate.setVisibility(View.GONE);
			customViewHolder.text_value_dueDate.setVisibility(View.GONE);
			customViewHolder.text_value_feeType.setVisibility(View.GONE);
			customViewHolder.text_value_amount.setVisibility(View.GONE);*/
		}


		/*if(feeDue.getInstalType() != null || feeDue.getSubTypeId() == ERPConstants.TYPE_SCHOLARSHIP)
		  {
		   if(feeDue.getDueDate() == null)
		    customViewHolder.text_value_instalType.setText(" : " + feeDue.getInstalType());
			    if(feeDue.getSubTypeId() == ERPConstants.TYPE_SCHOLARSHIP && feeDue.getFeeTypeId() != -1)
			    {
				     FeeDue fd = ERPModel.scholarshipFilteredMap.get(feeDue.getFeeTypeId());
				     customViewHolder.text_value_instalType.setText(" : " + fd.getFeeType());
			    }
			    else
			    {
				     customViewHolder.text_value_instalType.setText(" : " + feeDue.getInstalType());
			    }
			    customViewHolder.text_value_instalType.setVisibility(View.VISIBLE);
		   }
		  else
		  {
		   customViewHolder.text_value_instalType.setVisibility(View.GONE);
		  }


		if( feeDue.getInstalType() != null && feeDue.getFineAmount() != 0 && feeDue.getConcession() != 0)
		  {
			customViewHolder.text_value_instalType.setText(" : " + feeDue.getInstalType());
			customViewHolder.text_value_instalType.setVisibility(View.VISIBLE);
		  }
		  else
		  {
		   customViewHolder.text_value_instalType.setVisibility(View.GONE);
		  }*/
		// fine amount
		if(feeDue.getInstalType() != null){
			customViewHolder.text_value_instalType.setVisibility(View.VISIBLE);
			customViewHolder.text_value_instalType.setText("(" + feeDue.getInstalType() + ") ");
		}else{
			customViewHolder.text_value_instalType.setText("");
			customViewHolder.text_value_instalType.setVisibility(View.INVISIBLE);
		}
		if(feeDue.getFineAmount() == 0)
		{
			customViewHolder.text_label_fineAmount.setVisibility(View.GONE);
			customViewHolder.text_value_fineAmount.setVisibility(View.GONE);
		}
		else
		{
			customViewHolder.text_value_fineAmount.setText( ERPModel.rupeeSymbol + ERPUtil.formatDoubleAmount(feeDue.getFineAmount()));
			customViewHolder.text_label_fineAmount.setVisibility(View.VISIBLE);
			customViewHolder.text_value_fineAmount.setVisibility(View.VISIBLE);
		}

		// concession
		if(feeDue.getConcession() == 0)
		{
			customViewHolder.text_label_concession.setVisibility(View.GONE);
			customViewHolder.text_value_concession.setVisibility(View.GONE);
		}
		else
		{
			customViewHolder.text_value_concession.setText(ERPModel.rupeeSymbol + ERPUtil.formatDoubleAmount(feeDue.getConcession()));
			customViewHolder.text_label_concession.setVisibility(View.VISIBLE);
			customViewHolder.text_value_concession.setVisibility(View.VISIBLE);
		}

		// Total amount
		if(feeDue.getTotal() == 0)
		{
			customViewHolder.text_label_total.setVisibility(View.GONE);
			customViewHolder.text_value_total.setVisibility(View.GONE);
		}
		else
		{
			customViewHolder.text_value_total.setText(ERPModel.rupeeSymbol + ERPUtil.formatDoubleAmount(feeDue.getTotal()));
			customViewHolder.text_label_total.setVisibility(View.VISIBLE);
			customViewHolder.text_value_total.setVisibility(View.VISIBLE);
		}
		/*if(feeDue.getInstalType() != null) {
			customViewHolder.install_name.setText(feeDue.getInstalType());
			customViewHolder.install_name.setVisibility(View.VISIBLE);
		} else {
			customViewHolder.install_name.setVisibility(View.GONE);
		}*/

	}

	@Override
	public CustomViewHolder onCreateViewHolder(ViewGroup parent, int type)
	{
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fee_due_summary_list_adapter, parent, false);
		CustomViewHolder customViewHolder = new CustomViewHolder(view);
		return customViewHolder;
	}

}
