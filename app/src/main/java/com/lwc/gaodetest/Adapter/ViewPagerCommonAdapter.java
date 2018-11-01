package com.lwc.gaodetest.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by lingwancai on
 * 2018/11/1 14:27
 */
public class ViewPagerCommonAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragentsList;

    public ViewPagerCommonAdapter(FragmentManager fm, List<Fragment> fragmentsList) {
        super(fm);
        this.mFragentsList = fragmentsList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragentsList.get(position);
    }

    @Override
    public int getCount() {
        return mFragentsList.size();
    }
}
