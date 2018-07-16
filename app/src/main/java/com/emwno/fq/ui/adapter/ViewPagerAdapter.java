package com.emwno.fq.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.emwno.fq.ui.CameraFragment;
import com.emwno.fq.ui.QuoteFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) return new QuoteFragment();
        else return new CameraFragment();
    }

    @Override
    public int getCount() {
        return 2;
    }

}