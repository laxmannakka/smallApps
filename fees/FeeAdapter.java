package com.fees;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fees.model.FeePayment;
import com.interfaces.RecyclerViewClickListener;
import com.resources.erp.R;
import com.utils.ERPModel;
import com.utils.ERPUtil;

import java.util.List;

public class FeeAdapter extends RecyclerView.Adapter<FeeAdapter.CustomViewHolder>
{

	List<FeePayment> feePaymentDataList;
	RecyclerViewClickListener recyclerViewClickListener;
	String uri;
	
	public FeeAdapter(List<FeePayment> feePaymentDataList,RecyclerViewClickListener recyclerViewClickListener)
	{
		this.feePaymentDataList = feePaymentDataList;
		this.recyclerViewClickListener = recyclerViewClickListener;
	}

	static class CustomViewHolder extends RecyclerView.ViewHolder
	{
		TextView text_amount;
		TextView text_collectionDate;
		TextView text_receiptNo;
		TextView text_payMode;
		TextView text_label_receiptLabel;
		TextView view_receipt;
		
		public CustomViewHolder(View view) 
		{
			super(view);
			text_amount = (TextView) view.findViewById(R.id.amountValue);
			text_collectionDate = (TextView) view.findViewById(R.id.dateValue);
			//text_receiptNo = (TextView) view.findViewById(R.id.receiptNoValue);
			//text_label_receiptLabel = (TextView) view.findViewById(R.id.receiptNo);
			text_payMode = (TextView) view.findViewById(R.id.payModeValue);
			view_receipt = (TextView) view.findViewById(R.id.view);
		}
		
	}

	@Override
	public int getItemCount() 
	{
		return feePaymentDataList.size();
	}

	@Override
	public void onBindViewHolder(CustomViewHolder customViewHolder, int position) 
	{
		final FeePayment feePayment = feePaymentDataList.get(position);
	
		customViewHolder.text_amount.setText(ERPModel.rupeeSymbol+ ERPUtil.formatAmount(feePayment.getAmount()));
		customViewHolder.text_collectionDate.setText(ERPUtil.changeDateFormat(feePayment.getPaymentDate()));
		if(feePayment.getReceiptId() != null)
		{
			
			//customViewHolder.text_label_receiptLabel.setVisibility(View.VISIBLE);
			//customViewHolder.text_receiptNo.setVisibility(View.VISIBLE);
			//customViewHolder.text_receiptNo.setText(String.valueOf(feePayment.getReceiptId()));
		}
		else
		{
		//	customViewHolder.text_label_receiptLabel.setVisibility(View.GONE);
		//	customViewHolder.text_receiptNo.setVisibility(View.GONE);
		}
		customViewHolder.text_payMode.setText(feePayment.getPaymentMode());
		customViewHolder.view_receipt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ERPModel.feeReceiptId = feePayment.getReceiptId();
				uri = ERPModel.parentLoggedIn.getBranch().getBranchId() + "/fee/student/" + ERPModel.selectedKid.getId() + "/getReceipt/" + feePayment.getFeeCollectionId();
				recyclerViewClickListener.onClick(uri);
			}
		});
		
	}

	@Override
	public CustomViewHolder onCreateViewHolder(ViewGroup parent, int type) 
	{
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feepay_list_adapter, parent, false);
		CustomViewHolder customViewHolder = new CustomViewHolder(view);
		return customViewHolder;
	}
}
