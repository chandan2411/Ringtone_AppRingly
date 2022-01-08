package com.ringly.customer_app.views.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ringly.customer_app.views.fragments.CategoryRingtoneFragment;
import com.ringly.customer_app.views.fragments.FeatureRingtoneFragment;
import com.ringly.customer_app.views.fragments.PopularRingtoneFragment;

public class RingtoneTabsPagerAdapter extends FragmentPagerAdapter {



    private static final int FRAGMENT_COUNT = 3;


    public RingtoneTabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public RingtoneTabsPagerAdapter(FragmentManager childFragmentManager, int behaviorResumeOnlyCurrentFragment) {
        super(childFragmentManager, behaviorResumeOnlyCurrentFragment);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new FeatureRingtoneFragment("");
            case 1:
                return new CategoryRingtoneFragment();
            case 2:
                return new PopularRingtoneFragment();

        }
        return null;
    }


    @Override
    public int getCount() {
        return FRAGMENT_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Home";
            case 1:
                return "Categories";
            case 2:
                return "Popular";

        }
        return null;
    }

}
