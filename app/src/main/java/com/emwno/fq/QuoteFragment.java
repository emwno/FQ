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

    private OnAdjustFQListener mListener;
    private GestureDetector mDetector;

    @Override
    public void onAttach(Context activity) {
        mListener = (OnAdjustFQListener) activity;
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
    public boolean onDoubleTap(MotionEvent e) {
        mListener.onAdjustFQStyle();
        return true;
    }

    public interface OnAdjustFQListener {
        void onAdjustFQSize(int direction, float scale);

        void onAdjustFQStyle();
    }

}
