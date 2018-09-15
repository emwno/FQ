package com.emwno.fq.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.emwno.fq.R;
import com.emwno.fq.ui.listerner.GestureListener;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

        if (Math.abs(distanceY) > Math.abs(distanceX)) {
            if (mTouchStartPointY - e2.getRawY() > 100) {
                mTouchStartPointY = e2.getRawY();
                mListener.onAdjustFQSize(0, Math.abs(distanceY) / 100);
            } else if (e2.getRawY() - mTouchStartPointY > 100) {
                mTouchStartPointY = e2.getRawY();
                mListener.onAdjustFQSize(1, Math.abs(distanceY) / 100);
            }
        }

        return true;
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
        void onShowBlanks();

        void onAdjustFQSize(int direction, float scale);

        void onAdjustFQStyle();
    }

}
