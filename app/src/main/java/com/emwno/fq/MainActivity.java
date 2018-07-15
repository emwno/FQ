package com.emwno.fq;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.emwno.fq.network.FQ;
import com.emwno.fq.network.FQFactory;
import com.emwno.fq.network.FQService;
import com.emwno.fq.network.Field;
import com.emwno.fq.network.Fuck;
import com.emwno.fq.ui.transition.TextViewSize;
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.ChangeText;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements
        GestureListener, QuoteFragment.OnActionListener, CameraFragment.OnCapturePictureListener, FQBottomSheetFragment.OnBlanksFilledListener {

    private State CURRENT_STATE = State.EXPANDED;

    private int mLayoutCompressedHeight;
    private int mLayoutCompressedMargin;

    private Fuck mFuck;
    private int mQuoteStyle = 0;
    private float mCurrentTitleSize = 25f;

    private FQService mService;
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

    private ViewPagerAdapter mPagerAdapter;
    private FuckAdapter mQuoteListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContainerLayout = findViewById(R.id.containerLayout);
        mPager = findViewById(R.id.viewPager);
        mQuoteList = findViewById(R.id.fqList);
        mQuoteLayout = findViewById(R.id.fqLayout);
        mQuoteTitle = findViewById(R.id.fqTitle);
        mQuoteSubtitle = findViewById(R.id.fqSubtitle);
        mQuoteIcon = findViewById(R.id.fqIcon);

        mService = FQFactory.getRetrofitInstance().create(FQService.class);

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
        mQuoteList.addOnItemTouchListener(new RecyclerItemClickListener(this,
                (view, position) -> onFuckSelected(mQuoteListAdapter.getList().get(position))));

        mLayoutCompressedHeight = (int) (150 * getResources().getDisplayMetrics().density);
        mLayoutCompressedMargin = (int) (8 * getResources().getDisplayMetrics().density);

        mQuoteGradient = (AnimationDrawable) findViewById(R.id.fqBackground).getBackground();
        mQuoteGradient.setEnterFadeDuration(3000);
        mQuoteGradient.setExitFadeDuration(3000);
        mQuoteGradient.start();

        fetchData();
    }

    public void handleLoad() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
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
        }, 2000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mQuoteGradient != null && !mQuoteGradient.isRunning())
            mQuoteGradient.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mQuoteGradient != null && mQuoteGradient.isRunning())
            mQuoteGradient.stop();
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) super.onBackPressed();
        else mPager.setCurrentItem(0);
    }

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
        onHideFucks();
    }

    private void fetchData() {
        FQService mService = FQFactory.getRetrofitInstance().create(FQService.class);
        Call<List<Fuck>> call = mService.getOperations();

        call.enqueue(new Callback<List<Fuck>>() {
            @Override
            public void onResponse(Call<List<Fuck>> call, Response<List<Fuck>> response) {
                mQuoteListAdapter = new FuckAdapter(MainActivity.this, response.body());
                mQuoteList.setAdapter(mQuoteListAdapter);
                handleLoad();
            }

            @Override
            public void onFailure(Call<List<Fuck>> call, Throwable t) {
                handleError();
            }
        });
    }

    private void handleError() {
        TransitionManager.beginDelayedTransition(mContainerLayout);
        TextView tv = findViewById(R.id.fqMessage);
        tv.setText("Oh Noes!\nNo Internet Connection Available\nTap to retry");
        tv.setVisibility(View.VISIBLE);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(mContainerLayout);
                tv.setVisibility(View.GONE);
                fetchData();
            }
        });
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
                title = title.replace("~|", " _______");

                String subtitle = response.body().getSubtitle();
                subtitle = subtitle.replace("~|", " _______");

                TransitionManager.beginDelayedTransition(mContainerLayout,
                        new ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN));
                mQuoteTitle.setText(title.trim());
                mQuoteSubtitle.setText(subtitle);
            }

            @Override
            public void onFailure(Call<FQ> call, Throwable t) {

            }
        });
    }

    @Override
    public void onShowFucks() {
        CURRENT_STATE = State.COMPRESSED;
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

    @Override
    public void onHideFucks() {
        CURRENT_STATE = State.EXPANDED;
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
        if (mBottomSheet != null && CURRENT_STATE == State.EXPANDED) {
            TransitionManager.beginDelayedTransition(mContainerLayout, mTransitionSet);
            animateQuoteLayout(RelativeLayout.LayoutParams.MATCH_PARENT, mLayoutCompressedMargin);
            mBottomSheet.show(getSupportFragmentManager(), "BottomSheetFragment");
        }
    }

    @Override
    public void onBlanksFilled(List<String> fuckBlanks) {
        TransitionManager.beginDelayedTransition(mContainerLayout, mTransitionSet);
        animateQuoteLayout(RelativeLayout.LayoutParams.MATCH_PARENT, 0);

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
        /* Can't differentiate b/w scroll and fling,
         *  gotta find a better way to adjust size.

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

        mQuoteTitle.setTextSize(mCurrentTitleSize);*/
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

    private enum State {
        EXPANDED,
        COMPRESSED
    }
}