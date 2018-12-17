package com.example.mj975.woder_woman.adpater;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.mj975.woder_woman.fragment.ServiceDeliveryFragment;
import com.example.mj975.woder_woman.fragment.ServiceSafeHouseFragment;
import com.example.mj975.woder_woman.fragment.ServiceScoutFragment;

public class ServicePageAdapter extends FragmentStatePagerAdapter {

    private int tabCount;

    public ServicePageAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new ServiceDeliveryFragment();
                return fragment;
            case 1:
                fragment = new ServiceSafeHouseFragment();
                return fragment;
            case 2:
                fragment = new ServiceScoutFragment();
                return fragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
