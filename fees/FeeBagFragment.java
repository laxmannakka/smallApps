package com.fees;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.billdesk.sdk.PaymentOptions;
import com.fees.interfaces.PaymentListener;
import com.fees.model.Concession;
import com.fees.model.ERPCartObject;
import com.fees.model.TransactionDetails;
import com.helper.BaseFragment;
import com.helper.CustomLogger;
import com.helper.ERPConstants;
import com.helper.ServerRequestTask;
import com.interfaces.ServerRequestListener;
import com.resources.erp.R;
import com.utils.ERPModel;
import com.utils.ERPUtil;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;


public class FeeBagFragment extends BaseFragment implements ServerRequestListener{

	View view;
	LinearLayout bagContentsLayout;
	public static Map<Long,List<ERPCartObject>> subTypeMap= new HashMap<Long,List<ERPCartObject>>() ;
	LinearLayout summaryLayout;
	TextView totalValueLabel;
	Button checkoutButton;
	Button goBackButton;
	Toolbar toolbar;
	List<ERPCartObject> selectedFeeDuesList = new ArrayList<ERPCartObject>();
	Map<String, Object> jsonFeeDueStructure = new HashMap<String, Object>();
	PaymentListener paymentListener;
	String uri;
	Map<String,Double> scholarshipMap = new HashMap<String,Double>() ;
	boolean transxFailed = false;
	private static final String TAG = FeeBagFragment.class.getName();
	public FeeBagFragment(){}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.bag_layout, container, false);
		bagContentsLayout = (LinearLayout) view.findViewById(R.id.bagContentsLayout);
		checkoutButton = (Button) view.findViewById(R.id.checkoutButton);
		summaryLayout = (LinearLayout) view.findViewById(R.id.summaryLinearLayout);
		checkoutButton = (Button) view.findViewById(R.id.checkoutButton);

		checkoutButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ERPModel.payNowButtonClicked = true;
				sendSelectedFeeMap(false);
				/*String strMsg = "AIRMTST|ARP1459915746514|NA|2.00|NA|NA|NA|INR|NA|R|airmtst|NA|NA|F|NA|NA|NA|NA|NA|NA|NA|http://122.169.118.65/billdesk/pg_resp.php|1640343085";// pg_msg

				String strEmail = "abc@def.ghi";
				String strMobile = "9800000000";
				BillDeskCallBack callbackObj = new BillDeskCallBack(); // callback
				// instance

				System.out.println("msg:- " + strMsg);

				Intent intent = new Intent(getActivity(),PaymentOptions.class);

				intent.putExtra("msg", strMsg); // pg_msg
//				intent.putExtra("token", strToken);
				intent.putExtra("user-email", strEmail);
				intent.putExtra("user-mobile", strMobile);
				intent.putExtra("callback", callbackObj);
				startActivity(intent);*/
			}
		});
		return view;
	}

	public void sendSelectedFeeMap(boolean b)
	{
		transxFailed = b;
		List<ERPCartObject> selectedFeeDuesList = new ArrayList<ERPCartObject>();
		Map<String, Object> jsonFeeDueStructure = new HashMap<String, Object>();
		Set<Long> keys = ERPModel.paymentBagMap.keySet();
		for (Long key : keys)
		{
			List<ERPCartObject> cartList = ERPModel.paymentBagMap.get(key);
			for (ERPCartObject object : cartList)
			{
				selectedFeeDuesList.add(object);
			}
		}
		jsonFeeDueStructure.put("feeDues", selectedFeeDuesList);
		List<Map<String, Object>> fineList = new ArrayList<>();
		Map<String, Object> fineMap = new HashMap<String, Object>();
		if(ERPModel.selectedKid.getFeeDueDetail().getFineAmount()==0.0) {
			fineList.add(fineMap);
			jsonFeeDueStructure.put("fineCollectionDetail", fineList);
		}else {

			fineMap.put("concession", 0);
			fineMap.put("fineAmount", ERPModel.selectedKid.getFeeDueDetail().getFineAmount());
			fineMap.put("amount", ERPModel.selectedKid.getFeeDueDetail().getFineAmount());
			fineMap.put("total", ERPModel.selectedKid.getFeeDueDetail().getFineAmount());
			jsonFeeDueStructure.put("fineCollectionDetail", fineList);
			fineList.add(fineMap);
		}

		List<Map<String, Object>> concessionList = new ArrayList<>();
		Map<String, Object> concessionMap = new HashMap<String, Object>();
		if(ERPModel.selectedKid.getFeeDueDetail().getConcessionList()!=null) {
			for (Concession concession : ERPModel.selectedKid.getFeeDueDetail().getConcessionList()) {
				Concession obj = concession;
				concessionMap.put("feeConcessionConfigurationName", obj.getConcessionName());
				concessionMap.put("concession", 0);
				concessionMap.put("fineAmount", 0);
				concessionMap.put("amount", obj.getAmount());
				concessionMap.put("total", obj.getAmount());
				concessionList.add(concessionMap);
			}
		}else{
			concessionList.add(concessionMap);
		}
		jsonFeeDueStructure.put("concessionCollectionDetail",concessionList);

		final OutputStream out = new ByteArrayOutputStream();
		final ObjectMapper mapper = new ObjectMapper();

		try
		{
			mapper.writeValue(out, jsonFeeDueStructure);
		} catch (JsonGenerationException e)
		{
			CustomLogger.e(TAG, "sendSelectedFeeMap", e);
		} catch (JsonMappingException e)
		{
			CustomLogger.e(TAG, "sendSelectedFeeMap", e);
		} catch (IOException e)
		{
			CustomLogger.e(TAG, "sendSelectedFeeMap", e);
		}


		final byte[] data = ((ByteArrayOutputStream) out).toByteArray();
		System.out.println(new String(data));

		HashMap<String, Object> reqParamMap = new HashMap<String, Object>();
		reqParamMap.put("KEY", new String(data));
		String uri = ERPModel.parentLoggedIn.getBranch().getBranchId() + "/onlinePayment/student/" + ERPModel.selectedKid.getId() + ERPConstants.URI_PRE_PAYMENT;
		new ServerRequestTask(this, ERPModel.SERVER_URL, uri, reqParamMap, true, "post").execute();
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		setInfo();
	}
	public void setInfo()
	{
		Set<Long> keys = ERPModel.paymentBagMap.keySet();
		double totalFeeDueAmount = 0 ;
		double scholarshipAmount = 0;
		for(Long key : keys)
		{
//			Handle for cases when subTypeId =2,3,4,5 because the textviews should be created separately
			double typeIdTotal = 0;
			String typeName = "";
			List<ERPCartObject> cartList = ERPModel.paymentBagMap.get(key);
			if(cartList != null)
			{
				for(ERPCartObject object : cartList)
				{
					if(object.getFeeTypeId() == -1 && object.getSubTypeId() > 1)
					{
						List<ERPCartObject> checkedSubTypeCardValues = null;
						if (subTypeMap.get(object.getSubTypeId()) != null)
						{
							checkedSubTypeCardValues = subTypeMap.get(object.getSubTypeId());
						}
						else
						{
							checkedSubTypeCardValues = new ArrayList<ERPCartObject>();
						}
						ERPCartObject erpCartObject = new ERPCartObject();
						erpCartObject.setFeeTypeName(object.getFeeTypeName());
						erpCartObject.setFeeDueId(object.getFeeDueId());
						erpCartObject.setSubTypeId(object.getSubTypeId());
						erpCartObject.setFeeTypeId(object.getFeeTypeId());
						erpCartObject.setTotal(object.getTotal());
						checkedSubTypeCardValues.add(erpCartObject);
						subTypeMap.put(object.getSubTypeId(), checkedSubTypeCardValues);

					}
					else
					{
						if(object.getSubTypeId() != 1)
						{
							typeIdTotal = typeIdTotal + object.getTotal();
							if(!typeName.equals(object.getFeeTypeName()))
							{
								typeName = object.getFeeTypeName();
							}
						}
						else if(object.getSubTypeId() == 1)
						{
							//createTextViews(bagContentsLayout, "Scholarship"+object.getScholarshipName(), object.getTotal());
							createScholarshipMap(object.getFeeTypeName(),object.getTotal());
							scholarshipAmount = scholarshipAmount + object.getTotal();
						}
					}
				}
				if(!typeName.equals(""))
				{
					createTextViews(bagContentsLayout,typeName,typeIdTotal);
				}
				totalFeeDueAmount = totalFeeDueAmount + typeIdTotal;
			}

		}
		Set<Long> subTypeKeys = subTypeMap.keySet();
		for(Long key: subTypeKeys)
		{
			double typeIdTotal = 0;
			String typeName = "";
			List<ERPCartObject> cartList = subTypeMap.get(key);
			if(cartList != null)
			{
				for(ERPCartObject object : cartList)
				{
					if(object.getFeeTypeId() == -1 && object.getSubTypeId() > 1)
					{
						typeIdTotal = typeIdTotal + object.getTotal();
					}
					if(!typeName.equals(object.getFeeTypeName()))
					{
						typeName = object.getFeeTypeName();
					}
				}
				createTextViews(bagContentsLayout,typeName,typeIdTotal);
				totalFeeDueAmount = totalFeeDueAmount + typeIdTotal;
			}
		}
		totalFeeDueAmount = totalFeeDueAmount - scholarshipAmount;
		/*if(scholarshipAmount != 0 )
		{
			createTextViews(bagContentsLayout, "Scholarship", scholarshipAmount);
		}*/
		if(scholarshipMap.size()>0){
			for (Map.Entry<String, Double> entry : scholarshipMap.entrySet()){
				createTextViews(bagContentsLayout, "Scholarship"+entry.getKey(), entry.getValue());
			}
		}
		if( ERPModel.selectedKid.getFeeDueDetail() != null && ERPModel.selectedKid.getFeeDueDetail().getFineAmount()!= 0.0 && ERPModel.selectedKid.getFeeDueDetail().isInstallmentBasedFine()){
			totalFeeDueAmount = totalFeeDueAmount - ERPModel.selectedKid.getFeeDueDetail().getFineAmount();
			createTextViews(bagContentsLayout, "Fine Amount",ERPModel.selectedKid.getFeeDueDetail().getFineAmount() );
		}
		if(ERPModel.selectedKid.getFeeDueDetail() != null && ERPModel.selectedKid.getFeeDueDetail().getConcessionList()!= null && ERPModel.selectedKid.getFeeDueDetail().isInstallBasedConcession()){
			for (Concession concession :ERPModel.selectedKid.getFeeDueDetail().getConcessionList()) {
				createTextViews(bagContentsLayout,concession.getConcessionName(),concession.getAmount());
				totalFeeDueAmount = totalFeeDueAmount - concession.getAmount();
			}
		}
		createTextViews(bagContentsLayout, "Total Amount", totalFeeDueAmount);
	}

	private void createScholarshipMap(String installmentName, double total)
	{
		if(scholarshipMap!=null){
			if(scholarshipMap.get(installmentName)!=null){
				Double amt =scholarshipMap.get(installmentName);
				scholarshipMap.put(installmentName,amt+total);
			}
			else{
				//Double amt =scholarshipMap.get(installmentName);
				scholarshipMap.put(installmentName,total);
			}
		}
	}


	public void createTextViews(LinearLayout parentBagContent,String typeName,double typeIdTotal)
	{
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(0, 0, 0, 10);
		LinearLayout childBagContent = new LinearLayout(getActivity());
		childBagContent.setLayoutParams(layoutParams);
		childBagContent.setOrientation(LinearLayout.HORIZONTAL);
		childBagContent.setWeightSum(3);
		LinearLayout.LayoutParams textParamsText = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 2);
		LinearLayout.LayoutParams textParamsValue = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);

		TextView nameText = new TextView(getActivity());
		nameText.setLayoutParams(textParamsText);

		nameText.setTextSize(16);

		if(typeName.equals("Total Amount"))
			nameText.setTypeface(null, Typeface.BOLD);
		DecimalFormat df = new DecimalFormat("#.##");
		TextView valueText = new TextView(getActivity());
		valueText.setLayoutParams(textParamsValue);
		if(typeName.contains("Scholarship")) {
			typeIdTotal=Double.valueOf(df.format(typeIdTotal));
			valueText.setText("- " + ERPModel.rupeeSymbol + ERPUtil.formatAmount(typeIdTotal));
		}
		else
			valueText.setText(ERPModel.rupeeSymbol+ERPUtil.formatAmount(typeIdTotal));

		if(typeName.contains("Scholarship")) {
			typeName = typeName.replaceFirst("Scholarship","");
			//typeName = typeName.replace("Scholarship", "");
			nameText.setText(typeName);
		}else
			nameText.setText(typeName);
		valueText.setTextSize(16);

		if(typeName.equals("Total Amount"))
			valueText.setTypeface(null, Typeface.BOLD);

		valueText.setTextColor(Color.parseColor("#000000"));
		valueText.setGravity(Gravity.RIGHT);
		childBagContent.addView(nameText);
		childBagContent.addView(valueText);
		summaryLayout.addView(childBagContent);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		if(ERPModel.paymentDetails != null)
		{
			ERPModel.paymentBagMap.clear();
			subTypeMap.clear();
			//ERPModel.paymentDetails = null;
		}
		ERPModel.ischeckbox = true; //  user back press from feebag fragm
		subTypeMap.clear();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();  // Always call the superclass
		System.out.println("destroy ");
	}

	@Override
	public void onResume()
	{
		super.onResume();
		_activity.getSupportActionBar().setTitle(_activity.getResources().getString(R.string.fee_due_summary_title));
	}


	private void setTransactionDetails(String uri, String response)
	{
		try
		{
			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject.getString("code").equals("error"))
			{
				ERPUtil.showToast(getActivity(), jsonObject.getString("msg"));
				return;
			}
			TransactionDetails transacDetails = null;
			if(jsonObject.has("transactionInfo")&& !jsonObject.isNull("transactionInfo"))
			{
				if(!jsonObject.getString("param06").equalsIgnoreCase("BillDesk"))
				{
					ERPUtil.showToast(getActivity(), "Payment is not configured");
					return;
				}
			}
			if (jsonObject.has("transactionInfo") && !jsonObject.isNull("transactionInfo") )
			{
				ObjectMapper objectMapper = new ObjectMapper();
				transacDetails = objectMapper.readValue(jsonObject.getString("transactionInfo").toString(), TransactionDetails.class);
			}
			if (transacDetails != null)
			{
				ERPModel.currentTransactionDetails = transacDetails;
				openPaymentGateway();
			} else if (ERPModel.responseMap.get(uri) != null && ERPModel.responseMap.get(uri) == true)
			{
				ERPUtil.showToast(getActivity(), getString(R.string.preTransxError));
			}

		} catch (Exception jsone)
		{
			CustomLogger.e(TAG, "inside setTransactionDetails()", jsone);
		}
	}

	public void openPaymentGateway() {
		if (ERPModel.currentTransactionDetails != null) {
			//createTechProcessCheckoutObject(ERPModel.currentTransactionDetails);
			openBillDeskGateway(ERPModel.currentTransactionDetails);
		} else {
			ERPUtil.showToast(getActivity(), "Transaction Details Not Found");
			return;
		}
	}

	private void openBillDeskGateway(TransactionDetails currentTransactionDetails) {
		String strMsg = currentTransactionDetails.getParam07();
		//String tokenMsg = currentTransactionDetails.getParam10();
		String strEmail = "";
		String strMobile = "";
		if(currentTransactionDetails.getParam08()!= null && currentTransactionDetails.getParam09() !=null ) {
			strEmail = currentTransactionDetails.getParam08();
			strMobile = currentTransactionDetails.getParam09();
		}
		else {
			strEmail = "";
			strMobile = "";
		}

		BillDeskCallBack callbackObj = new BillDeskCallBack(); // callback instance
		System.out.println("msg:- " + strMsg);

		Intent intent = new Intent(getActivity(),PaymentOptions.class);
		if(transxFailed)
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		intent.putExtra("msg", strMsg); // pg_msg
		//intent.putExtra("token", tokenMsg);
		intent.putExtra("user-email", strEmail);
		intent.putExtra("user-mobile", strMobile);
		intent.putExtra("callback", callbackObj);
		intent.putExtra("orientation", Configuration.ORIENTATION_PORTRAIT);

		startActivity(intent);
		//getActivity().finish();

	}

	@Override
	public void onResponseReceived(String uri, String response)
	{
		if(getView() != null && isAdded())
		{
			if (uri.indexOf("getPrePaymentDetails") != -1)
			{
				ERPModel.responseMap.put(uri, true);
				setTransactionDetails(uri, response);
			}
		}
	}

	@Override
	public void onExceptionOccured(String uri, String msg)
	{

	}

	@Override
	public Map<String, String> getRequestHeaders(String url) {
		return null;
	}

	@Override
	public void showLoadingDialog() {

	}

	@Override
	public void dismissLoadingDialog() {

	}

	@Override
	public void showPaymentLoadingScreen(String msg) {

	}

	@Override
	public void dismissPaymentLoadingScreen(String msg, String response) {

	}

	@Override
	public void showLoadingDialog(String msg) {

	}

}