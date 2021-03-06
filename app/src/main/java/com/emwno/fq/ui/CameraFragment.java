package com.emwno.fq.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.emwno.fq.R;
import com.emwno.fq.ui.listerner.GestureListener;
import com.otaliastudios.cameraview.Audio;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Facing;
import com.otaliastudios.cameraview.PictureResult;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * Created on 22 May 2018.
 */
public class CameraFragment extends Fragment implements GestureListener {

    private float mZoomLevel = 0;
    private float mTouchStartPointY = -1;

    private CameraView mCameraView;

    private OnCapturePictureListener mListener;

    @Override
    public void onAttach(Context activity) {
        mListener = (OnCapturePictureListener) activity;
        super.onAttach(activity);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_camera, container, false);

        mCameraView = rootView.findViewById(R.id.camera_view);

        mCameraView.setAudio(Audio.OFF);

        GestureDetector gestureDetector = new GestureDetector(getContext(), this);
        mCameraView.setOnTouchListener((v, event) -> !gestureDetector.onTouchEvent(event));

        // For testing
        mCameraView.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(@NonNull PictureResult result) {
                super.onPictureTaken(result);
                mListener.onCapturePicture(result.getData(), mCameraView.getFacing() == Facing.FRONT);
            }
        });

        return rootView;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        mCameraView.takePictureSnapshot();
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        if (mCameraView.getFacing() == Facing.BACK) {
            mCameraView.setFacing(Facing.FRONT);
        } else {
            mCameraView.setFacing(Facing.BACK);
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
            mCameraView.setZoom(mZoomLevel);
        } else {
            mZoomLevel -= scale;
            if (mZoomLevel < 0) mZoomLevel = 0;
            Log.e("king", "down  " + mZoomLevel);
            mCameraView.setZoom(mZoomLevel);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mCameraView.open();
    }

    @Override
    public void onStop() {
        super.onStop();
        mCameraView.close();
    }

    public interface OnCapturePictureListener {
        void onCapturePicture(byte[] picture, boolean camFront);
    }

}
