package com.emwno.fq.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.emwno.fq.R;
import com.emwno.fq.model.FQ;
import com.emwno.fq.model.Fuck;
import com.emwno.fq.ui.MainPresenter.State;
import com.emwno.fq.ui.adapter.FuckAdapter;
import com.emwno.fq.ui.adapter.ViewPagerAdapter;
import com.emwno.fq.ui.listerner.GestureListener;
import com.emwno.fq.ui.listerner.RecyclerItemClickListener;
import com.emwno.fq.ui.transition.TextViewSize;
import com.emwno.fq.util.DeviceOrientation;
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.ChangeText;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.util.List;

public class MainActivity extends AppCompatActivity implements
        MainContract.View, GestureListener, QuoteFragment.OnActionListener, CameraFragment.OnCapturePictureListener, FQBottomSheetFragment.OnBlanksFilledListener, RecyclerItemClickListener.OnItemClickListener {

    DeviceOrientation mDeviceOrientation;
    SensorManager mSensorManager;
    Sensor mAccelerometer;
    Sensor mMagnetometer;
    private int mLayoutCompressedHeight;
    private int mLayoutCompressedMargin;
    private int mQuoteStyle = 0;
    private float mCurrentTitleSize = 25f;
    private TransitionSet mTransitionSet;
    private AnimationDrawable mQuoteGradient;
    private RelativeLayout mContainerLayout;
    private FQBottomSheetFragment mBottomSheet;
    private ViewPager mPager;
    private RecyclerView mQuoteList;
    private CardView mQuoteLayout;
    private TextView mQuoteTitle;
    private TextView mQuoteSubtitle;
    private ImageView mQuoteIcon;
    private View mQuoteFucks;
    private ViewPagerAdapter mPagerAdapter;
    private FuckAdapter mQuoteListAdapter;
    private GestureDetector mDetector;
    private MainPresenter mPresenter;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDeviceOrientation = new DeviceOrientation();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        mContainerLayout = findViewById(R.id.containerLayout);
        mPager = findViewById(R.id.viewPager);
        mQuoteList = findViewById(R.id.fqList);
        mQuoteLayout = findViewById(R.id.fqLayout);
        mQuoteTitle = findViewById(R.id.fqTitle);
        mQuoteSubtitle = findViewById(R.id.fqSubtitle);
        mQuoteIcon = findViewById(R.id.fqIcon);
        mQuoteFucks = findViewById(R.id.fqFucks);

        mPresenter = new MainPresenter(this);

        mTransitionSet = new TransitionSet();
        mTransitionSet.addTransition(new Fade(Fade.OUT).setDuration(50))
                .addTransition(new ChangeBounds().setDuration(300))
                .addTransition(new Fade(Fade.IN).setDuration(50))
                .addTransition(new TextViewSize());

        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setItemPrefetchEnabled(false);
        mQuoteList.setLayoutManager(layoutManager);
        mQuoteList.setAdapter(mQuoteListAdapter);
        mQuoteList.addOnItemTouchListener(new RecyclerItemClickListener(this, this));

        mDetector = new GestureDetector(this, this);
        mQuoteFucks.setOnTouchListener((v, event) -> !mDetector.onTouchEvent(event));

        mLayoutCompressedHeight = (int) (150 * getResources().getDisplayMetrics().density);
        mLayoutCompressedMargin = (int) (8 * getResources().getDisplayMetrics().density);

        mQuoteGradient = (AnimationDrawable) findViewById(R.id.fqBackground).getBackground();
        mQuoteGradient.setEnterFadeDuration(3000);
        mQuoteGradient.setExitFadeDuration(3000);
        mQuoteGradient.start();

        mPresenter.getFucks();
    }

    public void handleLoad() {
        TransitionSet set = new TransitionSet();
        set.addTransition(new ChangeBounds())
                .addTransition(new Fade(Fade.IN));
        TransitionManager.beginDelayedTransition(mContainerLayout, set);

        RelativeLayout.LayoutParams paramsIcon = (RelativeLayout.LayoutParams) mQuoteIcon.getLayoutParams();
        paramsIcon.removeRule(RelativeLayout.CENTER_IN_PARENT);
        mQuoteIcon.setLayoutParams(paramsIcon);

        mQuoteGradient = (AnimationDrawable) findViewById(R.id.fqBackground).getBackground();
        mQuoteGradient.setEnterFadeDuration(3000);
        mQuoteGradient.setExitFadeDuration(3000);
        mQuoteGradient.start();

        mPager.setVisibility(View.VISIBLE);
        mQuoteTitle.setVisibility(View.VISIBLE);
        mQuoteSubtitle.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mQuoteGradient != null && !mQuoteGradient.isRunning())
            mQuoteGradient.start();
        mSensorManager.registerListener(mDeviceOrientation.getEventListener(), mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(mDeviceOrientation.getEventListener(), mMagnetometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mQuoteGradient != null && mQuoteGradient.isRunning())
            mQuoteGradient.stop();
        mSensorManager.unregisterListener(mDeviceOrientation.getEventListener());
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) super.onBackPressed();
        else mPager.setCurrentItem(0);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        boolean result = false;
        float diffY = e1.getY() - e2.getY();
        if (Math.abs(diffY) > 100 && Math.abs(velocityY) > 100) {
            if (diffY > 0) {
                onShowFucks();
            } else {
                onHideFucks();
            }
            result = true;
        }

        return result;
    }

    private void onShowFucks() {
        mPresenter.changeState();
        TransitionManager.beginDelayedTransition(mContainerLayout, mTransitionSet);

        animateQuoteLayout(mLayoutCompressedHeight, mLayoutCompressedMargin);
        mQuoteSubtitle.setVisibility(View.GONE);

        RelativeLayout.LayoutParams paramsIcon = (RelativeLayout.LayoutParams) mQuoteIcon.getLayoutParams();
        paramsIcon.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsIcon.setMarginStart(mLayoutCompressedMargin * 3);
        mQuoteIcon.setLayoutParams(paramsIcon);

        RelativeLayout.LayoutParams paramsTitle = (RelativeLayout.LayoutParams) mQuoteTitle.getLayoutParams();
        paramsTitle.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTitle.removeRule(RelativeLayout.ABOVE);
        paramsTitle.addRule(RelativeLayout.RIGHT_OF, R.id.fqIcon);
        mQuoteTitle.setLayoutParams(paramsTitle);
        mQuoteTitle.setTextSize(18);
    }

    private void onHideFucks() {
        mPresenter.changeState();
        TransitionManager.beginDelayedTransition(mContainerLayout, mTransitionSet);

        mQuoteList.stopScroll();

        animateQuoteLayout(RelativeLayout.LayoutParams.MATCH_PARENT, 0);
        mQuoteSubtitle.setVisibility(View.VISIBLE);

        RelativeLayout.LayoutParams paramsIcon = (RelativeLayout.LayoutParams) mQuoteIcon.getLayoutParams();
        paramsIcon.removeRule(RelativeLayout.CENTER_VERTICAL);
        paramsIcon.setMarginStart(mLayoutCompressedMargin * 4);
        mQuoteIcon.setLayoutParams(paramsIcon);

        RelativeLayout.LayoutParams paramsTitle = (RelativeLayout.LayoutParams) mQuoteTitle.getLayoutParams();
        paramsTitle.removeRule(RelativeLayout.CENTER_VERTICAL);
        paramsTitle.addRule(RelativeLayout.ABOVE, R.id.fqSubtitle);
        paramsTitle.removeRule(RelativeLayout.RIGHT_OF);
        mQuoteTitle.setLayoutParams(paramsTitle);
        mQuoteTitle.setTextSize(mCurrentTitleSize);
    }

    @Override
    public void onShowBlanks() {
        if (mBottomSheet != null && mPresenter.getCurrentState() == State.EXPANDED) {
            TransitionManager.beginDelayedTransition(mContainerLayout, mTransitionSet);
            animateQuoteLayout(RelativeLayout.LayoutParams.MATCH_PARENT, mLayoutCompressedMargin);
            mBottomSheet.show(getSupportFragmentManager(), "BottomSheetFragment");
        }
    }

    @Override
    public void onBlanksFilled(List<String> fuckBlanks) {
        TransitionManager.beginDelayedTransition(mContainerLayout, mTransitionSet);
        animateQuoteLayout(RelativeLayout.LayoutParams.MATCH_PARENT, 0);
        mPresenter.handleBlanks(fuckBlanks);
    }

    @Override
    public void onAdjustFQSize(int direction, float scale) {
        if (direction == 0) {
            mCurrentTitleSize += 1 + scale;
        } else {
            mCurrentTitleSize -= 1 + scale;
        }

        if (mCurrentTitleSize > 150) {
            mCurrentTitleSize = 150;
        } else if (mCurrentTitleSize < 25) {
            mCurrentTitleSize = 25;
        }

        mQuoteTitle.setTextSize(mCurrentTitleSize);
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
        intent.putExtra("imageOrientation", mDeviceOrientation.getOrientation());
        intent.putExtra("fqTitle", mQuoteTitle.getText().toString());
        intent.putExtra("fqSubTitle", mQuoteSubtitle.getText().toString());
        intent.putExtra("textSize", mCurrentTitleSize);
        intent.putExtra("textStyle", mQuoteStyle);
        startActivity(intent);
    }

    private void animateQuoteLayout(int height, int margin) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mQuoteLayout.getLayoutParams();
        params.height = height;
        params.setMargins(margin, margin, margin, 0);
        mQuoteLayout.setLayoutParams(params);
    }

    @Override
    public void onFucksReceived(List<Fuck> fuckList) {
        mQuoteListAdapter = new FuckAdapter(MainActivity.this, fuckList);
        mQuoteList.setAdapter(mQuoteListAdapter);
        handleLoad();
    }

    @Override
    public void onFQReceived(FQ fq) {
        String title = fq.getMessage();
        title = title.replace("~|", " _______");

        String subtitle = fq.getSubtitle();
        subtitle = subtitle.replace("~|", " _______");

        TransitionManager.beginDelayedTransition(mContainerLayout,
                new ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN));
        mQuoteTitle.setText(title.trim());
        mQuoteSubtitle.setText(subtitle);
    }

    @Override
    public void onErrorFQ() {
        Toast.makeText(this, "Hey! Do you want to fuck up the app?\nBlanks can't contain a '/'", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onErrorFucks() {
        TransitionManager.beginDelayedTransition(mContainerLayout);
        TextView tv = findViewById(R.id.fqMessage);
        tv.setText("Oh Noes!\nNo Internet Connection Available\nTap to retry");
        tv.setVisibility(View.VISIBLE);
        tv.setOnClickListener(v -> {
            TransitionManager.beginDelayedTransition(mContainerLayout);
            tv.setVisibility(View.GONE);
            mPresenter.getFucks();
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        if (mBottomSheet != null) {
            getSupportFragmentManager().beginTransaction().remove(mBottomSheet).commit();
            mBottomSheet = null;
        }

        mBottomSheet = new FQBottomSheetFragment();

        Bundle b = new Bundle();
        b.putString("fuck", mQuoteListAdapter.getList().get(position).getName());
        mBottomSheet.setArguments(b);

        mPresenter.getFQMessage(mQuoteListAdapter.getList().get(position));

        onHideFucks();
    }

}