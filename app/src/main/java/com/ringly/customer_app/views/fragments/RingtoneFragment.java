package com.ringly.customer_app.views.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.ringly.customer_app.R;
import com.ringly.customer_app.entities.FragmentLifecycle;
import com.ringly.customer_app.views.adapters.RingtoneTabsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;


public class RingtoneFragment extends Fragment {

    private static final String TAG = RingtoneFragment.class.getSimpleName();

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private RingtoneTabsPagerAdapter adapter;

    public RingtoneFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ringtone, container, false);
        setRetainInstance(true);

        tabLayout = view.findViewById(R.id.tabs);
        viewPager = view.findViewById(R.id.view_pager);
        /*FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
         */
        adapter = new RingtoneTabsPagerAdapter(getChildFragmentManager(),BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int currentPosition = 0;
            @Override
            public void onPageSelected(int newPosition) {

                FragmentLifecycle fragmentToShow = (FragmentLifecycle)adapter.getItem(newPosition);
                fragmentToShow.onResumeFragment();

                FragmentLifecycle fragmentToHide = (FragmentLifecycle)adapter.getItem(currentPosition);
                fragmentToHide.onPauseFragment();

                currentPosition = newPosition;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) { }

            public void onPageScrollStateChanged(int arg0) { }
        });

        return view;
    }
}

