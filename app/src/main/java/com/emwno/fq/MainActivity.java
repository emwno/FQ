package com.emwno.fq;

import android.animation.ArgbEvaluator;
import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emwno.fq.network.FQ;
import com.emwno.fq.network.FQFactory;
import com.emwno.fq.network.FQService;
import com.emwno.fq.network.Field;
import com.emwno.fq.network.Fuck;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, FuckFragment.OnFuckSelectedListener {

    private ViewPager mPager;
    private ViewPagerAdapter mAdapter;
    private RelativeLayout mQuoteLayout;
    private FQBottomSheetFragment mBottomSheet;
    private View mBottomSheetSwipeView;
    private TextView mQuoteTitle;
    private TextView mQuoteSubtitle;
    private ImageView mQuoteIcon;
    private FQService mService;
    private ArgbEvaluator mEvaluator;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPager = findViewById(R.id.viewPager);
        mQuoteLayout = findViewById(R.id.fqLayout);
        mBottomSheetSwipeView = findViewById(R.id.fqSwipeView);
        mQuoteTitle = findViewById(R.id.fqTitle);
        mQuoteSubtitle = findViewById(R.id.fqSubtitle);
        mQuoteIcon = findViewById(R.id.imageView);

        mBottomSheet = new FQBottomSheetFragment();
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mService = FQFactory.getRetrofitInstance().create(FQService.class);
        mEvaluator = new ArgbEvaluator();

        mPager.addOnPageChangeListener(this);
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(1);
        mPager.setOffscreenPageLimit(2);

        GestureDetector detector = new GestureDetector(this, new SwipeUpListener() {
            @Override
            public void onSwipeUp() {
                mBottomSheet.show(getSupportFragmentManager(), "BottomSheetFragment");
            }
        });

        mBottomSheetSwipeView.setOnTouchListener((v, event) -> !detector.onTouchEvent(event));
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
            float deltaWidth = mPager.getWidth() - positionOffset * mPager.getWidth();
            mQuoteLayout.setTranslationX((-mPager.getWidth() * position + deltaWidth) * 0.75f);

            // Integers = hex colors as integers = dark | light
            int color = (int) mEvaluator.evaluate(positionOffset, -15198184, -460552);
            mQuoteLayout.setAlpha(positionOffset);
            mQuoteIcon.getDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onFuckSelected(Fuck fuck) {
        for (int i = 0; i < fuck.getFields().size(); i++) {
            Field field = fuck.getFields().get(i);
            fuck.getFields().get(i).setData("~" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + "|");
        }
        fetchFQ(fuck);
        mPager.setCurrentItem(1);
    }

    private void fetchFQ(Fuck fuck) {
        String url = fuck.getUrl();

        for (Field field : fuck.getFields()) {
            url = url.replace(":" + field.getField(), field.getData());
        }

        Call<FQ> call = mService.getFuck(url);
        Log.e("king", call.request().url().toString());

        call.enqueue(new Callback<FQ>() {
            @Override
            public void onResponse(Call<FQ> call, Response<FQ> response) {

                String title = response.body().getMessage();
                title = title.replaceAll("~", " <u>");
                title = title.replaceAll("\\|", "</u>");

                String subtitle = response.body().getSubtitle();
                subtitle = subtitle.replaceAll("~", "<u>");
                subtitle = subtitle.replaceAll("\\|", "</u>");

                mQuoteTitle.setText(Util.fromHtml(title));
                mQuoteSubtitle.setText(Util.fromHtml(subtitle));
            }

            @Override
            public void onFailure(Call<FQ> call, Throwable t) {

            }
        });
    }

}