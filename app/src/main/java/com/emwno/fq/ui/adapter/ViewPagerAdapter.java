package com.emwno.fq.ui.adapter;

import com.emwno.fq.ui.CameraFragment;
import com.emwno.fq.ui.QuoteFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

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