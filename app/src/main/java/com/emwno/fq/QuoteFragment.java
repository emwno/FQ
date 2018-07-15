package com.emwno.fq;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created on 22 May 2018.
 */
public class QuoteFragment extends Fragment implements GestureListener {

    private float mTouchStartPointY = -1;

    private OnActionListener mListener;
    private GestureDetector mDetector;

    @Override
    public void onAttach(Context activity) {
        mListener = (OnActionListener) activity;
        super.onAttach(activity);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_quote, container, false);

        mDetector = new GestureDetector(getContext(), this);
        rootView.setOnTouchListener((v, event) -> !mDetector.onTouchEvent(event));

        return rootView;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (mTouchStartPointY == -1) {
            mTouchStartPointY = e1.getRawY();
        }

        if (mTouchStartPointY - e2.getRawY() > 100) {
            mTouchStartPointY = e2.getRawY();
            mListener.onAdjustFQSize(0, Math.abs(distanceY) / 50);
        } else if (e2.getRawY() - mTouchStartPointY > 100) {
            mTouchStartPointY = e2.getRawY();
            mListener.onAdjustFQSize(1, Math.abs(distanceY) / 50);
        }

        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        boolean result = false;
        float diffY = e1.getY() - e2.getY();
        if (Math.abs(diffY) > 100 && Math.abs(velocityY) > 100) {
            if (diffY > 0) {
                mListener.onShowFucks();
            } else {
                mListener.onHideFucks();
            }
            result = true;
        }

        return result;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        mListener.onShowBlanks();
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        mListener.onAdjustFQStyle();
        return true;
    }

    public interface OnActionListener {
        void onShowFucks();

        void onHideFucks();

        void onShowBlanks();

        void onAdjustFQSize(int direction, float scale);

        void onAdjustFQStyle();
    }

}
