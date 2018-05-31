package com.emwno.fq;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.fotoapparat.Fotoapparat;
import io.fotoapparat.selector.LensPositionSelectorsKt;
import io.fotoapparat.selector.ResolutionSelectorsKt;
import io.fotoapparat.view.CameraView;

/**
 * Created on 22 May 2018.
 */
public class CameraFragment extends Fragment {

    private Fotoapparat mCamera;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUserVisibleHint(false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_camera, container, false);

        CameraView cameraView = rootView.findViewById(R.id.camera_view);
        mCamera = Fotoapparat.with(getContext())
                .lensPosition(LensPositionSelectorsKt.front())
                .photoResolution(ResolutionSelectorsKt.highestResolution())
                .into(cameraView).build();

        return rootView;
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
