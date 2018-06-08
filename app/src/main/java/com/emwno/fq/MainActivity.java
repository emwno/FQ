package com.emwno.fq;

import android.animation.ArgbEvaluator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.emwno.fq.network.FQ;
import com.emwno.fq.network.FQFactory;
import com.emwno.fq.network.FQService;
import com.emwno.fq.network.Field;
import com.emwno.fq.network.Fuck;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,
        FuckFragment.OnFuckSelectedListener, FQBottomSheetFragment.OnBlanksFilledListener,
        QuoteFragment.OnAdjustFQListener, GestureListener, CameraFragment.onCapturePictureListener {

    private Fuck mFuck;
    private float mStatusBarHeight;
    private int mQuoteStyle = 0;
    private float currentSize = 25f;

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
    private GestureDetector mGestureDetector;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        mPager = findViewById(R.id.viewPager);
        mQuoteLayout = findViewById(R.id.fqLayout);
        mBottomSheetSwipeView = findViewById(R.id.fqSwipeView);
        mQuoteTitle = findViewById(R.id.fqTitle);
        mQuoteSubtitle = findViewById(R.id.fqSubtitle);
        mQuoteIcon = findViewById(R.id.imageView);

        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mService = FQFactory.getRetrofitInstance().create(FQService.class);
        mEvaluator = new ArgbEvaluator();

        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        mStatusBarHeight = getResources().getDimension(resourceId);

        mGestureDetector = new GestureDetector(this, this);

        mPager.addOnPageChangeListener(this);
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(1);
        mPager.setOffscreenPageLimit(2);

        mBottomSheetSwipeView.setOnTouchListener((v, event) -> !mGestureDetector.onTouchEvent(event));
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
        } else if (position == 1) {
            mQuoteIcon.setTranslationY(-mStatusBarHeight * positionOffset);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (position == 2) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float yDiff = Math.abs(e1.getY() - e2.getY());

        if (Math.abs(velocityY) > 0 && yDiff > 100) {
            if (e1.getY() > e2.getY()) {
                if (mBottomSheet != null)
                    mBottomSheet.show(getSupportFragmentManager(), "BottomSheetFragment");
            }
        }

        return true;
    }

    @Override
    public void onFuckSelected(Fuck fuck) {
        for (int i = 0; i < fuck.getFields().size(); i++) {
            fuck.getFields().get(i).setData("~|");
        }

        if (mBottomSheet != null) {
            getSupportFragmentManager().beginTransaction().remove(mBottomSheet).commit();
            mBottomSheet = null;
        }

        mFuck = fuck;
        mBottomSheet = new FQBottomSheetFragment();

        Bundle b = new Bundle();
        b.putParcelable("fuck", fuck);
        mBottomSheet.setArguments(b);

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
                title = title.replace("~|", " <u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u>");

                String subtitle = response.body().getSubtitle();
                subtitle = subtitle.replace("~|", " <u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u>");

                mQuoteTitle.setText(Util.fromHtml(title));
                mQuoteSubtitle.setText(Util.fromHtml(subtitle));
            }

            @Override
            public void onFailure(Call<FQ> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBlanksFilled(List<String> fuckBlanks) {
        for (int i = 0; i < mFuck.getFields().size(); i++) {
            String blank = fuckBlanks.get(i);

            if (blank.contains("/")) {
                Toast.makeText(this, "Hey! Do you want to fuck up the app?\nBlanks can't contain a '/'", Toast.LENGTH_LONG).show();
                return;
            }

            if (!blank.isEmpty())
                mFuck.getFields().get(i).setData(fuckBlanks.get(i));
            else
                mFuck.getFields().get(i).setData("~|");
        }

        fetchFQ(mFuck);
    }

    @Override
    public void onAdjustFQSize(int direction, float scale) {
//        float currentSize = mQuoteTitle.getTextSize();
        if (direction == 0) {
            currentSize += 1 + scale;
        } else {
            currentSize -= 1 + scale;
        }

        if (currentSize > 150) {
            currentSize = 150;
        } else if (currentSize < 25) {
            currentSize = 25;
        }

        mQuoteTitle.setTextSize(currentSize);
    }

    @Override
    public void onAdjustFQStyle() {
        if (mQuoteStyle == 3) {
            mQuoteStyle = 0;
            mQuoteTitle.setTypeface(Typeface.DEFAULT, mQuoteStyle);
            mQuoteSubtitle.setTypeface(Typeface.DEFAULT, mQuoteStyle);
        } else {
            mQuoteStyle++;
            mQuoteTitle.setTypeface(Typeface.DEFAULT, mQuoteStyle);
            mQuoteSubtitle.setTypeface(Typeface.DEFAULT, mQuoteStyle);
        }
    }

    @Override
    public void onCapturePicture(byte[] picture) {
        Intent intent = new Intent(getBaseContext(), PreviewActivity.class);
        intent.putExtra("image", picture);
        intent.putExtra("fqTitle", mQuoteTitle.getText().toString());
        intent.putExtra("fqSubTitle", mQuoteSubtitle.getText().toString());
        intent.putExtra("textSize", currentSize);
        intent.putExtra("textStyle", mQuoteStyle);
        startActivity(intent);
    }
}