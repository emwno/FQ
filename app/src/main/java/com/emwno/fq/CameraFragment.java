package com.emwno.fq;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import io.fotoapparat.Fotoapparat;
import io.fotoapparat.configuration.CameraConfiguration;
import io.fotoapparat.selector.LensPositionSelectorsKt;
import io.fotoapparat.selector.ResolutionSelectorsKt;
import io.fotoapparat.view.CameraView;

/**
 * Created on 22 May 2018.
 */
public class CameraFragment extends Fragment implements GestureListener {

    private int mLensPosition = 0;
    private float mZoomLevel = 0;
    private float mTouchStartPointY = -1;

    private Fotoapparat mCamera;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_camera, container, false);

        CameraView cameraView = rootView.findViewById(R.id.camera_view);
        mCamera = Fotoapparat.with(getContext())
                .lensPosition(LensPositionSelectorsKt.back())
                .photoResolution(ResolutionSelectorsKt.highestResolution())
                .into(cameraView).build();

        GestureDetector gestureDetector = new GestureDetector(getContext(), this);
        rootView.setOnTouchListener((v, event) -> !gestureDetector.onTouchEvent(event));

        return rootView;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        if (mLensPosition == 0) {
            mCamera.switchTo(LensPositionSelectorsKt.front(), CameraConfiguration.standard());
            mLensPosition++;
        } else {
            mCamera.switchTo(LensPositionSelectorsKt.back(), CameraConfiguration.standard());
            mLensPosition--;
        }
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (mTouchStartPointY == -1) {
            mTouchStartPointY = e1.getRawY();
        }

        if (mTouchStartPointY - e2.getRawY() > 10) {
            zoom(0, Math.abs(distanceY) / 400);
        } else if (e2.getRawY() - mTouchStartPointY > 10) {
            zoom(1, Math.abs(distanceY) / 400);
        }

        mTouchStartPointY = e2.getRawY();

        return true;
    }

    public void zoom(int direction, float scale) {
        if (direction == 0) {
            mZoomLevel += scale;
            if (mZoomLevel > 1) mZoomLevel = 1;
            Log.e("king", "up  " + mZoomLevel);
            mCamera.setZoom(mZoomLevel);
        } else {
            mZoomLevel -= scale;
            if (mZoomLevel < 0) mZoomLevel = 0;
            Log.e("king", "down  " + mZoomLevel);
            mCamera.setZoom(mZoomLevel);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mCamera.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        mCamera.stop();
    }

}
