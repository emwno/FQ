package com.emwno.fq;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private ViewPager mPager;
    private ViewPagerAdapter mAdapter;
    private LinearLayout mQuote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQuote = findViewById(R.id.quoteText);
        mPager = findViewById(R.id.viewPager);

        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        mPager.setPageTransformer(true, new ViewPagerTransformer());
        mPager.addOnPageChangeListener(this);
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(1);
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) super.onBackPressed();
        else mPager.setCurrentItem(0);
    }

    public void toggleFucks(View v) {
        if (mPager.getCurrentItem() == 0) {
            mPager.setCurrentItem(1);
        } else {
            mPager.setCurrentItem(0);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (position == 0) {
            mQuote.setScaleX(positionOffset);
            mQuote.setScaleY(positionOffset);
            mQuote.setPivotX(mPager.getWidth() * 0.5f);
            mQuote.setPivotY(mPager.getHeight() * 0.5f);
            float deltaWidth = mPager.getWidth() - positionOffset * mPager.getWidth();
            mQuote.setTranslationX((-mPager.getWidth() * position + deltaWidth) * 0.75f);
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
