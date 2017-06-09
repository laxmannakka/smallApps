package com.fees;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.billdesk.sdk.PaymentOptions;
import com.fees.interfaces.PaymentListener;
import com.fees.model.Concession;
import com.fees.model.ERPCartObject;
import com.fees.model.PaymentStatus;
import com.fees.model.TransactionDetails;
import com.helper.BaseFragment;
import com.helper.CustomLogger;
import com.helper.ERPConstants;
import com.helper.ServerRequestTask;
import com.interfaces.ServerRequestListener;
import com.resources.erp.R;
import com.resources.erp.util.ApplicationGlobal;
import com.utils.ERPModel;
import com.utils.ERPUtil;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PaymentSummaryFragment extends Fragment implements ServerRequestListener{

	View view;

	PaymentListener paymentListener;
	String paymntJson ;
	int resultCode;
	LinearLayout errorLayout;
	CardView cardLayout ;
	ImageView paymentStatusIcon;
	TextView paymentStatusMsg;
	TextView transxId;
	TextView paymentExtraText;
	Button actionBtn ;
	TextView transxAmt;
	boolean transxFailed = false;
	public PaymentSummaryFragment(){

	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.clear();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.payment_summary_layout, container, false);
		errorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);
		cardLayout = (CardView) view.findViewById(R.id.card_view);

		paymentStatusIcon = (ImageView) view.findViewById(R.id.payment_remark_icon);
		paymentStatusMsg = (TextView) view.findViewById(R.id.payment_remark);
		transxId = (TextView) view.findViewById(R.id.order_id);
		paymentExtraText = (TextView) view.findViewById(R.id.payment_status_more_text);
		actionBtn  = (Button) view.findViewById(R.id.actionButton);
		transxAmt = (TextView) view.findViewById(R.id.order_total_amt);
		setHasOptionsMenu(false);
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(false);
		cardLayout.setVisibility(View.GONE);
		if(getArguments()!=null)
		{
			paymntJson = getArguments().getString("payment_json");
		}
		HashMap<String, Object> reqParamMap = new HashMap<String, Object>();

		reqParamMap.put("KEY", paymntJson);
		String uri = ERPModel.parentLoggedIn.getBranch().getBranchId() + "/onlinePayment/student/" + ERPModel.selectedKid.getId() + "/getPostPaymentDetails";
		new ServerRequestTask(this, ERPModel.SERVER_URL, uri, reqParamMap, true, "post").execute();
	}

	public void showPaymentStatusLayout(final PaymentStatus paymentStatus)
	{
		errorLayout.setVisibility(View.GONE);
		cardLayout.setVisibility(View.VISIBLE);
		if(paymentStatus.getCode().equals("success"))
		{
			ERPUtil.sendEventHit("Payment", "Success", ApplicationGlobal.getDefaultTracker());
			paymentStatusIcon.setBackgroundResource(R.drawable.success);
			paymentStatusMsg.setText("Payment is successfully done.");
			ERPModel.updateFeedueList = true;
			ERPModel.updateFeepaymentsList = true;
			actionBtn.setVisibility(View.VISIBLE);
			actionBtn.setText("View Receipt");
			if(paymentStatus.getTransId()==null)
				transxId.setVisibility(View.GONE);
			else
				transxId.setText("Transaction Id : "+paymentStatus.getTransId());
			if(paymentStatus.getAmount()==null)
				transxAmt.setVisibility(View.GONE);
			else
				transxAmt.setText("Total Amount : "+ERPModel.rupeeSymbol+ERPUtil.truncateDecimal(Double.parseDouble(paymentStatus.getAmount())));
			actionBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ERPModel.payNowButtonClicked = true;
					String openPDFUri = ERPModel.parentLoggedIn.getBranch().getBranchId() + "/fee/student/" + ERPModel.selectedKid.getId() + "/getReceipt/" + paymentStatus.getFeeCollectionId();
					String contentUrl = ERPModel.SERVER_URL + openPDFUri;
					ERPUtil.openPdf(getActivity(), contentUrl , "Fee Receipt");
				}
			});
		}
		else if(paymentStatus.getCode().equals("error"))
		{
			ERPUtil.sendEventHit("Payment", "Failed", ApplicationGlobal.getDefaultTracker());
			paymentStatusIcon.setBackgroundResource(R.drawable.failure);
			paymentStatusMsg.setText("Transaction failed. Please try again.");
			paymentExtraText.setVisibility(View.GONE);
			actionBtn.setVisibility(View.VISIBLE);
			ERPModel.updateFeedueList = false;
			actionBtn.setText("Retry");
			transxId.setVisibility(View.VISIBLE);
			transxAmt.setVisibility(View.VISIBLE);
			if(paymentStatus.getTransId()==null)
				transxId.setVisibility(View.GONE);
			else
				transxId.setText("Transaction Id : "+paymentStatus.getTransId());
			if(paymentStatus.getAmount()==null)
				transxAmt.setVisibility(View.GONE);
			else
				transxAmt.setText("Total Amount : "+ERPModel.rupeeSymbol+ERPUtil.truncateDecimal(Double.parseDouble(paymentStatus.getAmount())));
			actionBtn.setEnabled(true);
			actionBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					/*if(getFragmentManager().getBackStackEntryCount()>0)

					{
						getFragmentManager().popBackStack();
						getActivity().
					}*/
					ERPModel.payNowButtonClicked = true;
					sendSelectedFeeMap(true);
				}
			});
		}
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
			CustomLogger.e("PaymentSummaryfragment", "sendSelectedFeeMap", e);
		} catch (JsonMappingException e)
		{
			CustomLogger.e("PaymentSummaryfragment", "sendSelectedFeeMap", e);
		} catch (IOException e)
		{
			CustomLogger.e("PaymentSummaryfragment", "sendSelectedFeeMap", e);
		}


		final byte[] data = ((ByteArrayOutputStream) out).toByteArray();
		System.out.println(new String(data));

		HashMap<String, Object> reqParamMap = new HashMap<String, Object>();
		reqParamMap.put("KEY", new String(data));
		String uri = ERPModel.parentLoggedIn.getBranch().getBranchId() + "/onlinePayment/student/" + ERPModel.selectedKid.getId() + ERPConstants.URI_PRE_PAYMENT;
		new ServerRequestTask(this, ERPModel.SERVER_URL, uri, reqParamMap, true, "post").execute();
	}

	@Override
	public void onDetach() {
		super.onDetach();

	}

	private void setPaymentStatusDetail(String uri, String response)
	{
		try
		{
			JSONObject jsonObject = new JSONObject(response);
			ObjectMapper objectMapper = new ObjectMapper();
			System.out.println("================>"+response);
			ERPModel.paymentDetails = objectMapper.readValue(response, PaymentStatus.class);
			if (ERPModel.paymentDetails != null)
			{
				showPaymentStatusLayout(ERPModel.paymentDetails);
			}

		} catch (Exception jsone)
		{
			//currentFragment.showPaymentStatusLayout("");
			CustomLogger.e("MarksActivity", "inside setPaymentStatusDetail()", jsone);
		}
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
			if (jsonObject.has("transactionInfo") && !jsonObject.isNull("transactionInfo"))
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
			CustomLogger.e("PaymentSummaryFragment", "inside setTransactionDetails()", jsone);
		}
	}

	public void openPaymentGateway() {
		if(ERPModel.currentTransactionDetails != null){
			//createTechProcessCheckoutObject(ERPModel.currentTransactionDetails);
			openBillDeskGateway(ERPModel.currentTransactionDetails);
		}else{
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

	}

	@Override
	public void onResponseReceived(String uri, String response)
	{
		if(getView() != null && isAdded())
		{
			if (uri.indexOf("getPostPaymentDetails") != -1)
			{
				ERPModel.responseMap.put(uri, true);
				setPaymentStatusDetail(uri, response);
			}else if (uri.indexOf("getPrePaymentDetails") != -1)
			{
				ERPModel.responseMap.put(uri, true);
				setTransactionDetails(uri, response);
			}
		}
	}

	@Override
	public void onExceptionOccured(String uri, String msg) {

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