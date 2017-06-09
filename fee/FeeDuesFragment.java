package com.fees;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iconcept.model.IconceptModel;
import com.interfaces.StudentInfoListener;
import com.resources.erp.R;
import com.splash.SlidingTabLayout;
import com.splash.ViewPagerAdapter;
import com.transport.geo.GPSData;
import com.transport.model.BusTracking;
import com.utils.ERPModel;
import com.utils.ERPUtil;

public class FeeDuesFragment extends Fragment implements ActionMode.Callback{

	String currentTab;
	StudentInfoListener studentInfoListener;
	View v;
	ViewPager pager;
	ViewPagerAdapter adapter;
	SlidingTabLayout tabs;
	ActionMode actionMode = null;
	TextView contextualTextView;
	boolean isSelectedAllPressed;
	public FeeDuesFragment(String tabTittle) {
		this.currentTab = tabTittle;
	}

	public FeeDuesFragment() {
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			this.studentInfoListener = (StudentInfoListener) activity;
		}
		catch (ClassCastException cce) {
			throw new ClassCastException(activity.toString()
					+ " must implement studentInfoListener.");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_feedue, container, false);
		tabs = (SlidingTabLayout) v.findViewById(R.id.tabs);
		pager = (ViewPager) v.findViewById(R.id.pager);
		return v;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		List<String> enableFeatureList = getFeeDueList();
		adapter = new ViewPagerAdapter(getFragmentManager(),getActivity(),enableFeatureList, enableFeatureList.size());

		pager.setAdapter(adapter);
		tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true,
		tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
			@Override
			public int getIndicatorColor(int position) {
				return getResources().getColor(R.color.tabsScrollColor);
			}
		});
		tabs.setVisibility(View.GONE);
		tabs.setViewPager(pager);
		pager.setCurrentItem(0);
	}

	private List<String> getFeeDueList() {
		List<String> filterdEnableFeatureList = new ArrayList<String>();

		if(ERPUtil.isClass("com.resources.erp.fragment."+ ERPModel.enableFeatures.get(ERPModel.featureConfig.Dues.getFeatureNo())))
			filterdEnableFeatureList.add(ERPModel.enableFeatures.get(ERPModel.featureConfig.Dues.getFeatureNo())+"1");

		return filterdEnableFeatureList;
	}

	public void setInfo(GPSData data, BusTracking busTracking){
		Fragment currentFragment = adapter.getCurrentFragment();
	}

	public void showErrorLayout(String error) {
		Fragment currentFragment = adapter.getCurrentFragment();
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
			case R.id.action_selectall:
				Fragment currentFragment = adapter.getCurrentFragment();
				if(currentFragment instanceof FeeFragment1)
				{
					ERPModel.feeDueCardSelectionCount = 0;
					FeeFragment1 feeDueSummaryFragment = (FeeFragment1) currentFragment;
					if(!isSelectedAllPressed)
					{
        			/*isSelectedAllPressed = true;
        			item.setIcon(R.drawable.selectedall);
        			feeDueSummaryFragment.feeDueSummaryAdapter.totalAmount = 0;
        			ERPModel.paymentBagMap.clear();
        			feeDueSummaryFragment.setCheckedBoxesToTrue();
        			contextualTextView.setText(ERPModel.feeDueCardSelectionCount + " Selected" + " Payable : " + ERPModel.rupeeSymbol+ ERPUtil.formatAmount(feeDueSummaryFragment.feeDueSummaryAdapter.totalAmount));
        			mode.setCustomView(contextualTextView);*/
					}
					else
					{
						isSelectedAllPressed = false;
						item.setIcon(R.drawable.selectallicon);
						ERPModel.paymentBagMap.clear();
						mode.finish();
					}
				}
				break;
		}
		return true;
	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		// TODO Auto-generated method stub
		System.out.println("hi");
		MenuInflater inflater = getActivity().getMenuInflater();
		Fragment currentFragment = adapter.getCurrentFragment();
		if(currentFragment instanceof FeeFragment1)
		{
			/*FeeFragment1 feeDueSummaryFragment = (FeeFragment1) currentFragment;
			inflater.inflate(R.menu.context, menu);
			contextualTextView = (TextView)getActivity().getLayoutInflater().inflate(R.layout.contextual_title, null);
			contextualTextView.setText(ERPModel.feeDueCardSelectionCount + " Selected" + " Payable : " + ERPModel.rupeeSymbol+ ERPUtil.formatAmount(feeDueSummaryFragment.feeDueSummaryAdapter.totalAmount));
			mode.setCustomView(contextualTextView);
			actionMode = mode;*/


		}

		return true;
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {
		// TODO Auto-generated method stub
		System.out.println("hi");
		if(actionMode != null)
		{
			actionMode = null;
			ERPModel.paymentBagMap.clear();
			Fragment currentFragment = adapter.getCurrentFragment();

			if(currentFragment instanceof FeeFragment)
			{
				/*FeeFragment1 feeDueSummaryFragment = (FeeFragment1) currentFragment;
					if(ERPModel.feeDueCardSelectionCount == 0){
						ERPModel.ischeckbox = true;
						feeDueSummaryFragment.disablePayNowButton();
						feeDueSummaryFragment.enableGoToBagButton();

					}
					else{
						ERPModel.ischeckbox = false;
						feeDueSummaryFragment.enableGoToBagButton();
					}
					feeDueSummaryFragment.setCheckedBoxesToFalse();
					feeDueSummaryFragment.feeDueSummaryAdapter.totalAmount = 0.00;
					feeDueSummaryFragment.feeDueSummaryAdapter.notifyDataSetChanged();*/
			}
			/*
			 * User select fee due to pay and press back ->else part execute
			 */
			else if(currentFragment instanceof FeeFragment1)
			{
				/*if(ERPModel.paymentDetails != null)
				{
					ERPModel.paymentDetails = null;
				}
				FeeFragment1 feeDueSummaryFragment = (FeeFragment1) currentFragment;
					if(ERPModel.feeDueCardSelectionCount == 0){
						ERPModel.ischeckbox = true;
						feeDueSummaryFragment.disablePayNowButton();
						feeDueSummaryFragment.enableGoToBagButton();

					}
					else{
						//ERPModel.ischeckbox = false;
						feeDueSummaryFragment.enableGoToBagButton();
					}
					feeDueSummaryFragment.setCheckedBoxesToFalse();
					feeDueSummaryFragment.feeDueSummaryAdapter.totalAmount = 0.00;
					feeDueSummaryFragment.feeDueSummaryAdapter.notifyDataSetChanged();*/
			}
		}
		ERPModel.feeDueCardSelectionCount = 0;
		//pager.setPagingEnabled(true);
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		// TODO Auto-generated method stub
		System.out.println("hi");
		return false;
	}

	public void changeActionTitleAndCount()
	{
		System.out.println("hi");
		if(actionMode == null)
		{
			actionMode = getActivity().startActionMode(this);
		}
		else
		{
			/*if(ERPModel.feeDueCardSelectionCount == 0)
			{
				actionMode.finish();
				return;
			}
			Fragment currentFragment = adapter.getCurrentFragment();
			if(currentFragment instanceof FeeFragment || currentFragment instanceof FeeFragment1)
			{
				FeeFragment1 feeDueSummaryFragment = (FeeFragment1) currentFragment;
				contextualTextView.setText(ERPModel.feeDueCardSelectionCount + " Selected" + " Payable : " + ERPModel.rupeeSymbol+ ERPUtil.formatAmount(feeDueSummaryFragment.feeDueSummaryAdapter.totalAmount));
				actionMode.setCustomView(contextualTextView);
			}*/
		}
	}
	public void refreshTheFeeDuesList()
	{
		if(actionMode != null)
			actionMode.finish();
	}
	/*
	 * @see android.support.v4.app.Fragment#onResume()
	 * USer Select Fee due to pay ->GO to cart->Press back
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Fragment currentFragment = adapter.getCurrentFragment();
		if(currentFragment instanceof FeeFragment)
		{
			FeeFragment1 feeDueSummaryFragment = (FeeFragment1) currentFragment;
			feeDueSummaryFragment.hideGoToBagButton();
			feeDueSummaryFragment.setCheckedBoxesToFalse();
			ERPModel.feeDueCardSelectionCount = 0;
			if(actionMode != null)
			{
				actionMode.finish();
			}
			/*feeDueSummaryFragment.feeDueSummaryAdapter.notifyDataSetChanged();
			feeDueSummaryFragment.enablePayNowButton();*/
		}else if(currentFragment instanceof FeeFragment1){
			if(ERPModel.payNowButtonClicked != true ){
				FeeFragment1 feeDueSummaryFragment = (FeeFragment1) currentFragment;
				feeDueSummaryFragment.setCheckedBoxesToTrue();
				ERPModel.feeDueCardSelectionCount = 0;
				if(actionMode != null)
				{
					actionMode.finish();
				}
				//feeDueSummaryFragment.feeDueSummaryAdapter.notifyDataSetChanged();
			}
			else{
				ERPModel.paymentDetails = null;
				getActivity().finish();
				ERPModel.payNowButtonClicked = false;
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();  // Always call the superclass
		System.out.println("destroy ");
	    /*if(ERPModel.payNowButtonClicked == true){
			getActivity().finish();
		}*/
		// Stop method tracing that the activity started during onCreate()
	}

	/*public void OpenfeeBagFragment() {
		Intent intent = new Intent(getActivity().getApplicationContext(), MarksActivity.class);
		intent.putExtra("showFeeBag", true);
		getActivity().startActivityForResult(intent, 3);
	}*/
}