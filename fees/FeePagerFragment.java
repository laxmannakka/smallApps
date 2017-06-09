package com.fees;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.calenderEvents.CalendarFragmentPagerAdapter;
import com.helper.BaseFragment;
import com.resources.erp.R;
import com.utils.ERPModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by swatig on 1/31/2017.
 */
public class FeePagerFragment extends BaseFragment implements TabLayout.OnTabSelectedListener,ViewPager.OnPageChangeListener
{
    TabLayout tabs;
    ViewPager pager;
    int moduleId = 3;
    Integer[] featureIdArray;
    String []  featureNameArray ;
    LinearLayout errorLayout;
    TextView textError;
    public static View pagerView;
    public static enum feeFeature {
        Dues(9),
        Payments(11);
        private Integer value = null;
        private feeFeature(int value) {
            this.value = value;
        }
        public static Map<Integer, String> lookup = new HashMap<Integer, String>();
        static {
            for (feeFeature e : feeFeature.values()) {
                lookup.put(e.label(), e.name());
            }
        }
        Integer label() {
            return value;
        }
        public static String get(Integer label) {
            return lookup.get(label);
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        pagerView = inflater.inflate(R.layout.fee_custom_pager_fragment,null);
        errorLayout = (LinearLayout) pagerView.findViewById(R.id.errorLayout);
        textError = (TextView) pagerView.findViewById(R.id.error);
        pager = (ViewPager) pagerView.findViewById(R.id.pager);
        tabs = (TabLayout) pagerView.findViewById(R.id.tabLayout);
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);
        tabs.setOnTabSelectedListener(this);

        getEnableFeatureList();
        if (featureIdArray.length> 0)
        {

            FeeFragmentPagerAdapter adapter = new FeeFragmentPagerAdapter(getChildFragmentManager(),featureIdArray,featureNameArray,pagerView);
            pager.setAdapter(adapter);
            tabs.setupWithViewPager(pager);
            if (featureIdArray.length == 1)
            {
                tabs.setVisibility(View.GONE);
            }
        }else{
            showErrorLayout("Library Details not found");
        }
        return pagerView;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        _activity.getSupportActionBar().setTitle(_activity.getResources().getString(R.string.fee_title));
    }

    public void showErrorLayout(String error) {
        errorLayout.setVisibility(View.VISIBLE);
        textError.setText(error);
    }

    private void getEnableFeatureList() {
        List<Integer> restrictedModuleList = ERPModel.parentLoggedIn.getModuleConfig();
        List<Integer> restrictedFeatureList = ERPModel.parentLoggedIn.getFeatureConfig();

        List<Integer> enablefeaturesIdList = new ArrayList<>();
        List<String> enablefeaturesNameList = new ArrayList<>();


        if(restrictedModuleList!=null && !(restrictedModuleList.contains(moduleId))) {
            for (feeFeature feature : feeFeature.values()) {
                if (restrictedFeatureList != null && !(restrictedFeatureList.contains(feature.label()))) {
                    enablefeaturesNameList.add(feature.name());
                    enablefeaturesIdList.add(feature.label());
                } else if(restrictedFeatureList == null){
                    enablefeaturesIdList.add(feature.label());
                    enablefeaturesNameList.add(feature.name());
                }
            }
        }
        else if(restrictedFeatureList!=null) {
            int i = 0;
            for (feeFeature feature : feeFeature.values()) {
                if (!restrictedFeatureList.contains(feature.label())) {
                    enablefeaturesNameList.add(feature.name());
                    enablefeaturesIdList.add(feature.label());
                }
            }
        }else{
            for (feeFeature feature : feeFeature.values()) {
                enablefeaturesNameList.add(feature.name());
                enablefeaturesIdList.add(feature.label());
            }
        }
        String[] featureNameArr = new String[enablefeaturesNameList.size()];
        featureNameArray = enablefeaturesNameList.toArray(featureNameArr);

        Integer[] featureIdArr = new Integer[enablefeaturesIdList.size()];
        featureIdArray = enablefeaturesIdList.toArray(featureIdArr);

    }
    @Override
    public void onTabSelected(TabLayout.Tab tab)
    {
       pager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab)
    {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab)
    {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {

    }

    @Override
    public void onPageSelected(int position)
    {

    }

    @Override
    public void onPageScrollStateChanged(int state)
    {

    }
}
