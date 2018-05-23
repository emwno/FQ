package com.emwno.fq;

import android.animation.ArgbEvaluator;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.emwno.fq.network.FQFactory;
import com.emwno.fq.network.FQService;
import com.emwno.fq.network.Fuck;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private ViewPager mPager;
    private ViewPagerAdapter mAdapter;
    private LinearLayout mQuote;
    private ImageView mQuoteIcon;
    private FQService mService;
    private ArgbEvaluator mEvaluator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPager = findViewById(R.id.viewPager);
        mQuote = findViewById(R.id.quoteText);
        mQuoteIcon = findViewById(R.id.imageView);

        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mService = FQFactory.getRetrofitInstance().create(FQService.class);
        mEvaluator = new ArgbEvaluator();

        mPager.setPageTransformer(true, new ViewPagerTransformer());
        mPager.addOnPageChangeListener(this);
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(1);

        Call<Fuck> call = mService.getFuck("awesome", "me");
        Log.e("king", call.request().url().toString());

        call.enqueue(new Callback<Fuck>() {
            @Override
            public void onResponse(Call<Fuck> call, Response<Fuck> response) {
                Log.e("king", response.body().getMessage() + " " + response.body().getSubtitle());
            }

            @Override
            public void onFailure(Call<Fuck> call, Throwable t) {

            }
        });
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

            // Integers = hex colors as integers = dark | light
            int color = (int) mEvaluator.evaluate(positionOffset, -15198184, -460552);
            mQuote.setAlpha(positionOffset);
            mQuoteIcon.getDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
