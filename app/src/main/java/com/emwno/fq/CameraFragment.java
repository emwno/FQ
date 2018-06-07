package com.emwno.fq;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
