package com.emwno.fq;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

/**
 * Created on 25 May 2018.
 */
public class FQBottomSheetFragment extends BottomSheetDialogFragment {

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                handleDismissal();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        handleDismissal();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setContentView(R.layout.fragment_bottom_sheet);

        FrameLayout bottomSheet = dialog.findViewById(android.support.design.R.id.design_bottom_sheet);

        BottomSheetBehavior behaviour = BottomSheetBehavior.from(bottomSheet);
        behaviour.setBottomSheetCallback(mBottomSheetBehaviorCallback);

        return dialog;
    }

    private void handleDismissal() {
        Toast.makeText(getContext(), "doyouwanttobuildasnowman", Toast.LENGTH_SHORT).show();
    }

}
