package com.ringly.customer_app.views.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.ringly.customer_app.R;
import com.ringly.customer_app.views.adapters.RingtoneTabsPagerAdapter;
import com.google.android.material.tabs.TabLayout;


public class WallpaperFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;


    public WallpaperFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ringtone, container, false);

        tabLayout = (TabLayout)view.findViewById(R.id.tabs);
        viewPager = (ViewPager)view.findViewById(R.id.view_pager);

        viewPager.setAdapter(new RingtoneTabsPagerAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        return view;

    }

}