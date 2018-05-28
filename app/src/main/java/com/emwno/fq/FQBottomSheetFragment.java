package com.emwno.fq;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.emwno.fq.network.Fuck;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 25 May 2018.
 */
public class FQBottomSheetFragment extends BottomSheetDialogFragment {

    private OnBlanksFilledListener mListener;
    private List<EditText> mEditTextList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
        Fuck fuck = getArguments().getParcelable("fuck");

        mEditTextList = new ArrayList<>();

        for (int i = 0; i < fuck.getFields().size(); i++) {
            int textId = getResources().getIdentifier("text" + i, "id", "com.emwno.fq");
            int editTextId = getResources().getIdentifier("editText" + i, "id", "com.emwno.fq");

            TextView textView = rootView.findViewById(textId);
            EditText editText = rootView.findViewById(editTextId);

            textView.setVisibility(View.VISIBLE);
            editText.setVisibility(View.VISIBLE);

            textView.setText(fuck.getFields().get(i).getName());
            editText.setHint(fuck.getFields().get(i).getField());

            mEditTextList.add(editText);

            if (i == fuck.getFields().size() - 1) {
                editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
                editText.setOnEditorActionListener((v, actionId, event) -> {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        dismiss();
                        return true;
                    }
                    return false;
                });
            }

        }

        return rootView;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        List<String> list = new ArrayList<>();
        for (EditText editText : mEditTextList) {
            list.add(editText.getText().toString().trim());
        }
        mListener.onBlanksFilled(list);
    }

    @Override
    public void onAttach(Context activity) {
        mListener = (OnBlanksFilledListener) activity;
        super.onAttach(activity);
    }

    public interface OnBlanksFilledListener {
        void onBlanksFilled(List<String> fuckBlanks);
    }

}
