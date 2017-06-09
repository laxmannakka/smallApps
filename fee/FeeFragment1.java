package com.fees;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fees.interfaces.TotalListener;
import com.fees.model.Concession;
import com.fees.model.ERPCartObject;
import com.fees.model.FeeDue;
import com.fees.model.FeeDueDetail;
import com.helper.BaseFragment;
import com.helper.CustomLogger;
import com.helper.ServerRequestTask;
import com.interfaces.RecyclerViewClickListener;
import com.interfaces.ServerRequestListener;
import com.resources.erp.R;
import com.utils.ERPModel;
import com.utils.ERPUtil;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FeeFragment1 extends BaseFragment implements RecyclerViewClickListener,TotalListener,ServerRequestListener
{
	ExpandListAdapter adapter;
	LinearLayout loadingLayout;
	LinearLayout listLayout;
	String currentTab;
	View view;
	Button goToBagButton;
	LinearLayout proceedLayout;
	LinearLayout errorLayout;
	TextView textError;
	String uri;
	ExpandableListView listView;
	List<String> feeMonthList = new ArrayList<>();
	Map<String, List<FeeDue>> filteredFeeList ;
	RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
			RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

	public FeeFragment1(){}

	/*@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		try
		{
			this.studentInfoListener = (StudentInfoListener) activity;
		}
		catch(ClassCastException cce)
		{
			throw new ClassCastException(activity.toString() + " must implement StudentInfoListener.");
		}
	}*/

	@Override
	public void onDetach()
	{
		super.onDetach();
		ERPModel.ischeckbox = false;
		ERPModel.feeDueCardSelectionCount = 0;
		ERPModel.paymentBagMap.clear();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		view = inflater.inflate(R.layout.fragment_fee_due_summary_new, container, false);

		listLayout = (LinearLayout) view.findViewById(R.id.listLayout);
		listView = (ExpandableListView) view.findViewById(R.id.listView);


		/*if((getActivity()) instanceof HomeActivity){
			paymentLayout = (LinearLayout) (getActivity()).findViewById(R.id.layout_dashboard_payment);
			kidImageLayout = (LinearLayout) (getActivity()).findViewById(R.id.layout_dashboard_kidImage);
			totalValueFeeDue = (TextView) (getActivity()).findViewById(R.id.value_totalFee);
		}*/

		goToBagButton = (Button) view.findViewById(R.id.goToBagButton);
		proceedLayout = (LinearLayout) view.findViewById(R.id.proceedLayout);


		goToBagButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ERPModel.isGoToBagButtonClicked = true;
				//feeDueSummaryAdapter.notifyDataSetChanged();
				//setCheckedBoxesToFalse();
				if (ERPModel.paymentBagMap != null) {
					if (ERPModel.paymentBagMap.keySet().size() == 0) {
						ERPUtil.showToast(getActivity(), "Please select at least month/Installment.");
						return;
					} else if (adapter.totalAmount <= 0 || adapter.totalAmount == 0.00) {
						ERPUtil.showToast(getActivity(), "Amount should be greater than zero.");
						adapter.totalAmount = 0;
						//ERPModel.feeDueCardSelectionCount = 0;
						//((MarksActivity)getActivity()).refreshTheFeeDuesList();
						return;
					}
					sendFeeDetailsTOserver();
				}

			}
		});

		loadingLayout = (LinearLayout) view.findViewById(R.id.loadingLayout);
		errorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);
		textError = (TextView) view.findViewById(R.id.error);
		return view;
	}

	private void sendFeeDetailsTOserver() {
		HashMap<String, Object> hashMap = new HashMap<>();
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();

		Set<Long> keys = ERPModel.paymentBagMap.keySet();
		for(Long key : keys) {
			List<ERPCartObject> cartList = ERPModel.paymentBagMap.get(key);
			if (cartList != null) {
				for (ERPCartObject object : cartList){
					if(object.getDueDate()!=null) {
						Date dueDate = object.getDueDate();
						String monthName = getDueDateInFormat(dueDate);
						if(!jsonArray.toString().contains(monthName))
							jsonArray.put(monthName);
					}

				}
			}
		}
		try {
			jsonObject.put("dueMonthList", jsonArray);
			hashMap.put("KEY", jsonObject.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String uri = ERPModel.parentLoggedIn.getBranch().getBranchId() + "/onlinePayment/student/" + ERPModel.selectedKid.getId() + "/getInstallmentBasedFineAndConcession";
		new ServerRequestTask(this, ERPModel.SERVER_URL, uri, hashMap, true, "post").execute();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		uri = ERPModel.parentLoggedIn.getBranch().getBranchId() + "/profile/" + ERPModel.selectedKid.getId() + "/fee";

		listLayout.setVisibility(View.GONE);
		List<FeeDue> feeDueList = null;
		if(ERPModel.selectedKid != null && ERPModel.selectedKid.getFeeDueDetail() != null)
		{
			feeDueList= ERPModel.selectedKid.getFeeDueDetail().getFeeDueList();
		}
		if(feeDueList != null)
		{
			setInfo(ERPModel.selectedKid.getFeeDueDetail().getFeeDueList());
		}
		else if( ERPModel.responseMap.get(uri) != null && ERPModel.responseMap.get(uri) == true)
		{
			showErrorLayout("Fee Due Details not found");
		}

		/* if(ERPModel.updateFeedueList.equals(true)){
			studentInfoListener.fetchInfo(uri);
		}

		if(feeDueList != null)
		{
			setInfo(ERPModel.selectedKid.getFeeDueSummaryList());
		}
		else if( ERPModel.responseMap.get(uri) != null && ERPModel.responseMap.get(uri) == true)
		{
			showErrorLayout("Fee Due Details not found");
		}
		else
		{
			studentInfoListener.fetchInfo(uri);
		}*/
		enableGoToBagButton();
		layoutParams.setMargins(0, 0, 0, 100);
		listLayout.setLayoutParams(layoutParams);
		ERPModel.ischeckbox = true;

		//((HomeActivity)getActivity()).settingTheTitleToFeeDue();
		//feeDueSummaryAdapter.notifyDataSetChanged();
		proceedLayout.setVisibility(View.VISIBLE);
		goToBagButton.setVisibility(View.VISIBLE);
	}

	public void setInfo(List<FeeDue> feeDueSummaryList)
	{
		filteredFeeList = putFeeDueListInMap(feeDueSummaryList);
		List<String> monthslist = feeMonthList;

		/*feeDueSummaryAdapter = new ExpandableFeeAdapter(getActivity(),true);
		feeDueSummaryAdapter.setData(monthslist);

		listView.setAdapter(feeDueSummaryAdapter);*/
		sortFilteredFeeList(filteredFeeList);
		adapter = new ExpandListAdapter(getActivity(),filteredFeeList);
		adapter.setmListener(this);
		listView.setAdapter(adapter);


		listLayout.setVisibility(View.VISIBLE);
		/*listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int position, long id) {
				currentClickedGroupPosition = position;
				String monthKey = feeMonthList.get(position);
				List<FeeDue> feeDueList = filteredFeeList.get(monthKey);

				// check is previous group is expanded or not
				if (listView.isGroupExpanded(previousClickedGroupPosition)) {
					listView.collapseGroupWithAnimation(previousClickedGroupPosition);
					previousClickedGroupPosition = -1;
				}

				// expand or fetch the list if previous and current position are not equal
				// and if current clicked position is not expanded.
				if (previousClickedGroupPosition != currentClickedGroupPosition &&
						!(listView.isGroupExpanded(currentClickedGroupPosition))) {
					// check if list is already in map or not
					if (feeDueList != null ) {
						expandChildList(feeDueList);
					}
				}
				return true;
			}
		});*/
		listLayout.setVisibility(View.VISIBLE);
		loadingLayout.setVisibility(View.GONE);


	}

	private void sortFilteredFeeList(Map<String, List<FeeDue>> filteredFeeList) {
		List<String> monthList  = new ArrayList<String>();
		for (Map.Entry<String, List<FeeDue>> entry : filteredFeeList.entrySet()) {
			monthList.add(entry.getKey());
		}
		Collections.sort(monthList, new Comparator<String>() {
			DateFormat f = new SimpleDateFormat("MMM yyyy");

			@Override
			public int compare(String o1, String o2) {
				try {
					return f.parse(o1).compareTo(f.parse(o2));
				} catch (ParseException e) {
					throw new IllegalArgumentException(e);
				}
			}
		});
		Map<String, List<FeeDue>> treeMap = new LinkedHashMap<>();

		for (String month : monthList) {
			treeMap.put(month,filteredFeeList.get(month));
		}

		/*Map<String, List<FeeDue>> valueComparator = new TreeMap<String, List<FeeDue>>(
				new Comparator<String>() {

					@Override
					public int compare(String o1, String o2) {
						DateFormat format = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
						try {
							Date date1 = format.parse(o1);
							Date date2 = format.parse(o2);
							return date1.compareTo(date2);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						return o2.compareTo(o1);
					}

				});

		Map<String, List<FeeDue>> treeMap = new TreeMap<String, List<FeeDue>>(valueComparator);
		treeMap.putAll(filteredFeeList);*/
		this.filteredFeeList = treeMap;
	}


	/*public List<FeeDue> putFeeDueListInMap(List<FeeDue> feeDueSummaryList)
	{
		List<FeeDue> filteredList = new ArrayList<>();
		for (FeeDue feeDue : feeDueSummaryList)
		{
			if (feeDue.getSubTypeId() != 1 && feeDue.getAmount() != 0.0)
			{
				filteredList.add(feeDue);
				totalFeeDue += feeDue.getTotal();
				ERPModel.scholarshipFilteredMap.put(feeDue.getFeeTypeId(), feeDue);
			} else if (feeDue.getSubTypeId() == 1 && feeDue.getAmount() != 0.0)
			{
				filteredList.add(feeDue);
				totalScholarShip += feeDue.getTotal();
			}
		}
		totalFeeDue = totalFeeDue - totalScholarShip;

		return filteredList;
	}*/
	public Map<String,List<FeeDue>> putFeeDueListInMap(List<FeeDue> feeDueSummaryList)
	{
		Map<String,List<FeeDue>> filteredList = new HashMap<>();
		for (FeeDue feeDue : feeDueSummaryList)
		{
			String month = getMonthName(feeDue.getDueDate());
			if(filteredList.containsKey(month)){
				List<FeeDue> monthFeeDueList = filteredList.get(month);
				if(monthFeeDueList == null) {
					monthFeeDueList = new ArrayList<>();
					if(feeDue.getAmount()!=0.0)
						monthFeeDueList.add(feeDue);
				}else {
					if (feeDue.getAmount() != 0.0) {
						monthFeeDueList.add(feeDue);
					}
				}
				if(feeDue.getAmount()!=0.0)
					filteredList.put(month, monthFeeDueList);
			}else{
				List<FeeDue> monthFeeDueList = new ArrayList<>();
				if(feeDue.getAmount()!=0.0) {
					monthFeeDueList.add(feeDue);
					filteredList.put(month, monthFeeDueList);
					feeMonthList.add(month);
				}
			}
		}

		return filteredList;
	}

	private String getMonthName(Date selectedMonth) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(selectedMonth);
		int month = cal.get(Calendar.MONTH);
		String monthName = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
		int yearName = cal.get(Calendar.YEAR);
		return monthName+" "+yearName;
	}
	public void showErrorLayout(String error)
	{
		listLayout.setVisibility(View.GONE);
		loadingLayout.setVisibility(View.GONE);
		errorLayout.setVisibility(View.VISIBLE);
		textError.setText(error);
	}

	private String getDueDateInFormat(Date selectedMonth) {
		final Calendar c = Calendar.getInstance();
		c.setTime(selectedMonth);
		int month = c.get(Calendar.MONTH);
		return (++month+"-"+c.get(Calendar.YEAR));
	}
	@Override
	public void onClick(Object object) {
//		FeeDue feeDue = (FeeDue) object;
//		changeActionTitleAndCount() method will be called for updating the contextual action bar and for the first time it will call onCreateActionMode
		//((MarksActivity)getActivity()).changeActionTitleAndCount();
	}

	public void enableGoToBagButton()
	{
		goToBagButton.setVisibility(View.VISIBLE);
		proceedLayout.setVisibility(View.VISIBLE);
		/*if(feeDueSummaryAdapter != null)
		{
			layoutParams.setMargins(0, 0, 0, 100);
			listLayout.setLayoutParams(layoutParams);
		}*/
	}

	public void hideGoToBagButton()
	{
		goToBagButton.setVisibility(View.GONE);
		proceedLayout.setVisibility(View.VISIBLE);
		/*if(feeDueSummaryAdapter != null)
		{
			layoutParams.setMargins(0, 0, 0, 0);
			listLayout.setLayoutParams(layoutParams);
		}*/
	}

	public void enablePayNowButton()
	{


	}

	public void disablePayNowButton()
	{
	}

	@Override
	public void onResume() {
		super.onResume();


	}
	@Override/*if(feeDueSummaryAdapter!=null && (getActivity()) instanceof DashBoardActivity)
		{
	 		*//*feeDueSummaryAdapter.totalAmount=0.0;
			ERPModel.ischeckbox = false;
			feeDueSummaryAdapter.notifyDataSetChanged();
			layoutParams.setMargins(0, 0, 0, 0);
			listLayout.setLayoutParams(layoutParams);
			((DashBoardActivity)getActivity()).settingTheTitleToFeeDue();*//*
		}else if(feeDueSummaryAdapter!=null && (getActivity()) instanceof MarksActivity){
			setCheckedBoxesToFalse();
		}*/
	public void onDestroy() {
		super.onDestroy();
		System.out.println("destroy ");
	}

	public void setCheckedBoxesToFalse()
	{
		for(FeeDue feeDue : ERPModel.selectedKid.getFeeDueDetail().getFeeDueList())
		{
			feeDue.setChecked(false);
		}
	}

	public void setCheckedBoxesToTrue()
	{
		/*for(FeeDue feeDue : ERPModel.selectedKid.getFeeDueDetail().getFeeDueList())
		{
			*//*feeDue.setChecked(true);
			(ERPModel.feeDueCardSelectionCount)++;
			feeDueSummaryAdapter.addFeeEntry(feeDue);*//*
		}
		feeDueSummaryAdapter.notifyDataSetChanged();*/
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				System.out.println("jsdjsa");
				return true;
		}
		return false;
	}


	public void expandChildList(List<FeeDue> list)
	{
		/*feeDueChildAdapter = new ExpandableFeeChildAdapter(getActivity(),true);
		feeDueChildAdapter.setData(list, feeMonthList);
		listView = (AnimatedExpandableListView) view.findViewById(R.id.listView);
		listView.setAdapter(feeDueChildAdapter);
		listView.expandGroupWithAnimation(currentClickedGroupPosition);
		previousClickedGroupPosition = currentClickedGroupPosition;*/
	}


	@Override
	public void onTotalChanged(double sum) {

	}

	@Override
	public void expandGroupEvent(int groupPosition, boolean isExpanded) {
		if(isExpanded) {
			listView.collapseGroup(groupPosition);
		}
		else {
			listView.expandGroup(groupPosition);
			listView.setSelectionFromTop(groupPosition, 0);
		}
	}

	private void setFeeBagDetails(String response) {
		try {
			DecimalFormat df = new DecimalFormat("#.##");
			List<Concession> concessionList = null;
			ObjectMapper objectMapper = new ObjectMapper();
			JSONObject jsonObject = new JSONObject(response);
			FeeDueDetail currentFeeDueDetail = ERPModel.selectedKid.getFeeDueDetail();
			if (jsonObject.has("fineAndConcessionMap")) {
				JSONObject jsonMap = jsonObject.getJSONObject("fineAndConcessionMap");
				Double fineAmount = Double.valueOf(df.format(jsonMap.getDouble("fineAmount")));
				currentFeeDueDetail.setFineAmount(fineAmount);

				if (jsonMap.has("concessionMapList")) {
					JSONArray jsonArray = jsonMap.getJSONArray("concessionMapList");
					if (jsonArray.length() > 0) {
						if (concessionList == null) {
							concessionList = new ArrayList<Concession>();
						}
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObject1 = jsonArray.getJSONObject(i);
							Concession concession = new Concession();
							concession.setAmount(Double.valueOf(df.format(jsonObject1.getDouble("concessionAmount"))));
							concession.setConcessionName(jsonObject1.getString("concessionName"));
							concessionList.add(concession);
						}
					}
				}
				currentFeeDueDetail.setConcessionList(concessionList);
				/*FeeDuesFragment fragment = (FeeDuesFragment) getSupportFragmentManager().findFragmentByTag(FeeDuesFragment.class.getName());
				if (fragment != null)
				{
					fragment.OpenfeeBagFragment();
				}*/
				FeeBagFragment feeBagFragment = new FeeBagFragment();
				_onNavigationListener.onShowFragment(feeBagFragment,_activity.getResources().getString(R.string.feeDueSummaryTitle), true, false, false);
			}

		} catch (Exception jsone) {
			CustomLogger.e("DashBoardActivity", "inside setFeeInfo()", jsone);
		}
	}

	@Override
	public void onResponseReceived(String uri, String response)
	{
		if(getView() != null && isAdded())
		{
			if (uri.indexOf("getInstallmentBasedFineAndConcession") != -1)
			{
				ERPModel.responseMap.put(uri, true);
				setFeeBagDetails(response);
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