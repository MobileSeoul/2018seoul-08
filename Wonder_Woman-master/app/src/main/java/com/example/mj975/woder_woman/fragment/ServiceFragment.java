package com.example.mj975.woder_woman.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mj975.woder_woman.R;
import com.example.mj975.woder_woman.adpater.ServicePageAdapter;

public class ServiceFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_service, container, false);

        tabLayout = v.findViewById(R.id.tab);
        tabLayout.addTab(tabLayout.newTab().setText("#안심 택배함"));
        tabLayout.addTab(tabLayout.newTab().setText("#지킴이 집"));
        tabLayout.addTab(tabLayout.newTab().setText("#안심 스카우트"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        viewPager = v.findViewById(R.id.viewPager);


        ServicePageAdapter pagerAdapter = new ServicePageAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return v;
    }
}