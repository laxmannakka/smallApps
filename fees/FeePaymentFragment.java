package com.fees;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fees.model.FeePayment;
import com.helper.BaseFragment;
import com.helper.CustomLogger;
import com.helper.ServerRequestTask;
import com.interfaces.RecyclerViewClickListener;
import com.interfaces.ServerRequestListener;
import com.interfaces.StudentInfoListener;
import com.resources.erp.R;
import com.splash.DashBoardActivity;
import com.utils.ERPModel;
import com.utils.ERPUtil;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONArray;
import org.json.JSONObject;

public class FeePaymentFragment extends BaseFragment implements RecyclerViewClickListener,ServerRequestListener
{

	StudentInfoListener studentInfoListener;
	RecyclerView recyclerView;
	public FeeAdapter feeAdapter;
	LinearLayout loadingLayout;
	LinearLayout listLayout;
	LinearLayout errorLayout;
	TextView textError;
	String currentTab;
	View view;
	RecyclerView.LayoutManager recyclerVierw_LayoutManager;

	List<FeePayment> feePaymentList;

	String uri;

	LinearLayout paymentLayout;
	LinearLayout kidImageLayout;
	Button payNow ;
	public FeePaymentFragment(String tabTittle)
	{
		this.currentTab = tabTittle;
	}

	public FeePaymentFragment()
	{

	}

	@Override
	public void onStart() {
		super.onStart();
		//new ServerRequestTask(this, ERPModel.SERVER_URL, uri, null, true, "get").execute();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		view = inflater.inflate(R.layout.fragment_feepay, container, false);

		listLayout = (LinearLayout) view.findViewById(R.id.listLayout);
		recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
		recyclerView.setHasFixedSize(true);
		recyclerVierw_LayoutManager = new LinearLayoutManager(getActivity());
		recyclerView.setLayoutManager(recyclerVierw_LayoutManager);

		loadingLayout = (LinearLayout) view.findViewById(R.id.loadingLayout);
		errorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);
		textError = (TextView) view.findViewById(R.id.error);
		//paymentLayout = (LinearLayout) (getActivity()).findViewById(R.id.layout_dashboard_payment);
		//payNow = (Button) (getActivity()).findViewById(R.id.payNow);
		//payNow.setEnabled(false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		listLayout.setVisibility(View.GONE);
		
	/*	paymentLayout.setVisibility(View.GONE);
		kidImageLayout.setVisibility(View.VISIBLE);*/

		uri = ERPModel.parentLoggedIn.getBranch().getBranchId() + "/fee/student/" + ERPModel.selectedKid.getId() + "/getPaidDetails";
		//List<FeePayment> feePaymentList = ERPModel.selectedKid.getFeePaymentDataList();

		if(ERPModel.updateFeepaymentsList.equals(true)){
			new ServerRequestTask(this, ERPModel.SERVER_URL, uri, null, false, "get").execute();
			ERPModel.updateFeepaymentsList = false;
		}
		if(feePaymentList != null)
		{
			setInfo(feePaymentList);
		}
		else if( ERPModel.responseMap.get(uri) != null && ERPModel.responseMap.get(uri) == true)
		{
			showErrorLayout("Student Fee Payment Details not found");
		}
		else
		{
			//studentInfoListener.fetchInfo(uri);
			new ServerRequestTask(this, ERPModel.SERVER_URL, uri, null, false, "get").execute();
		}
	}

	public void setInfo(List<FeePayment> feePaymentDataList)
	{
		List<FeePayment> sortedfeePaymentDataList = new ArrayList<FeePayment>();
		if(feePaymentDataList != null)
		{
			sortedfeePaymentDataList.addAll(feePaymentDataList);
		}
		Collections.reverse(sortedfeePaymentDataList);


		feeAdapter = new FeeAdapter(sortedfeePaymentDataList,this);
		recyclerView.setAdapter(feeAdapter);

		listLayout.setVisibility(View.VISIBLE);
		loadingLayout.setVisibility(View.GONE);

	}

	public void showErrorLayout(String error) {
		listLayout.setVisibility(View.GONE);
		loadingLayout.setVisibility(View.GONE);
		errorLayout.setVisibility(View.VISIBLE);
		textError.setText(error);
	}

	@Override
	public void onClick(Object object) {
		// TODO Auto-generated method stub
		String contentUrl = ERPModel.SERVER_URL + object.toString();
		ERPUtil.openPdf(getActivity(), contentUrl, "Fee Receipt");
//		studentInfoListener.fetchInfo(uri);
	}

	@Override
	public void onResume() {
		super.onResume();
		if(feeAdapter!=null && (getActivity()) instanceof DashBoardActivity)
		{
			feeAdapter.notifyDataSetChanged();
			((DashBoardActivity)getActivity()).settingTheTitleToFeeDue();
		}

	}

	public void refresh(List<FeePayment> feePayments)
	{
		List<FeePayment> feePaymentList = feePayments;

		if(feePaymentList != null && (feePaymentList.size() > 0))
		{
			setInfo(feePaymentList);
		}
		else
		{
			showErrorLayout("Student Fee Payment Details not found");
		}
	}

	private void setFeeInfo(String response) {
		try {
//			ERPModel.responseMap.put(ERPConstants.URI_FEEPAYMENT, true);
			ObjectMapper objectMapper = new ObjectMapper();
			JSONObject jsonObject = new JSONObject(response);
			List<FeePayment> feePaymentDataList = null;
			if (jsonObject.has("collectionDetails") && !jsonObject.isNull("collectionDetails")) {
				JSONArray jsonArray = jsonObject.getJSONArray("collectionDetails");
				feePaymentDataList = objectMapper.readValue(jsonArray.toString(), new TypeReference<ArrayList<FeePayment>>() {
				});
			}
			// ERPModel.selectedKid.setFeePaymentDataList(feePaymentDataList);

			refresh(feePaymentDataList);
			Log.v("screenName", "Refreshing FeePaymentFragment");
			/*int position = returnPositionOfModule("FeePaymentFragment");
			if (position != -1) {
//                FeePaymentFragment feePaymentFragment = (FeePaymentFragment) adapter.fragmentsList.get(position);
				FeePaymentFragment feePaymentFragment = null;
				for(int i = 0; i < getSupportFragmentManager().getFragments().size(); i++) {
					if(getSupportFragmentManager().getFragments().get(i) instanceof  FeePaymentFragment) {
						feePaymentFragment = (FeePaymentFragment) getSupportFragmentManager().getFragments().get(i);
					}
				}
				if (feePaymentFragment != null && feePaymentFragment instanceof FeePaymentFragment) {
					feePaymentFragment.refresh(feePaymentDataList);
					Log.v("screenName", "Refreshing FeePaymentFragment");
				}
			}*/

		} catch (Exception jsone) {
			CustomLogger.e("FeePaymentFragment", "inside setFeeInfo()", jsone);
		}
	}

	@Override
	public void onResponseReceived(String uri, String response)
	{
		if(getView() != null && isAdded())
		{
			if (uri.indexOf("getPaidDetails") != -1) {
				//ERPModel.responseMap.put(uri, true);
				setFeeInfo(response);
			}
		}
	}

	@Override
	public void onExceptionOccured(String uri, String msg)
	{
		if(getView() != null && isAdded())
		{
			if (msg.contains(_activity.getResources().getString(R.string.session_expired)))
				ERPUtil.showSessionExpiredDialog(getActivity(), msg);
			else {
				showErrorLayout(msg);
			}
		}
	}

	@Override
	public Map<String, String> getRequestHeaders(String url) {
		return ERPUtil.getRequestHeaders(url);
	}

	@Override
	public void showLoadingDialog() {
		ERPUtil.showLoadingDialog(getActivity(), "Loading..");
	}

	@Override
	public void dismissLoadingDialog() {
		ERPUtil.dismissLoadingDialog(getActivity());
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