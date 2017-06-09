package com.fees;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.calenderEvents.HolidayEventFragment;
import com.calenderEvents.HolidayVacationsFragment;

/**
 * Created by swatig on 1/31/2017.
 */
public class FeeFragmentPagerAdapter extends FragmentStatePagerAdapter
{
    int tabCount;
    View view;
    String []  tabTitlesArr ;
    Integer [] enableFeatureIdArr;
    public FeeFragmentPagerAdapter(FragmentManager fm, Integer [] featureIdArr,String []  tabTitles, View view)
    {
        super(fm);
        tabCount = featureIdArr.length;
        enableFeatureIdArr = featureIdArr;
        this.tabTitlesArr = tabTitles;
        this.view = view;
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (enableFeatureIdArr[position])
        {
            case 9:
                return new FeeFragment(view);
            case 11:
                return new FeePaymentFragment();
        }
        return null;
    }

    @Override
    public int getCount()
    {
        return tabCount;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return tabTitlesArr[position];
    }


}