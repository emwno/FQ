package com.emwno.fq.ui.transition;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.util.FloatProperty;
import android.util.Property;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.transition.Transition;
import androidx.transition.TransitionValues;


/**
 * Created on 15 July 2018.
 */
public class TextViewSize extends Transition {

    /**
     * Property is like a helper that contain setter and getter in one place
     */
    private static final Property<TextView, Float> PROGRESS_PROPERTY = new FloatProperty<TextView>("textSize") {

        @Override
        public void setValue(TextView textView, float value) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, value);
        }

        @Override
        public Float get(TextView textView) {
            return textView.getTextSize();
        }
    };

    /**
     * Internal name of property. Like a bundles for intent
     */
    private static final String PROPNAME_PROGRESS = "TextViewSize:size";

    @Override
    public void captureStartValues(@NonNull TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override
    public void captureEndValues(@NonNull TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    private void captureValues(TransitionValues transitionValues) {
        if (transitionValues.view instanceof TextView) {
            // save current progress in the values map
            TextView textView = ((TextView) transitionValues.view);
            transitionValues.values.put(PROPNAME_PROGRESS, textView.getTextSize());
        }
    }

    @Override
    public Animator createAnimator(@NonNull ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        if (startValues != null && endValues != null && endValues.view instanceof TextView) {
            TextView textView = (TextView) endValues.view;
            float start = (Float) startValues.values.get(PROPNAME_PROGRESS);
            float end = (Float) endValues.values.get(PROPNAME_PROGRESS);
            if (start != end) {
                // first of all we need to return the start value, because right now
                // the view is have the end value
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, start);
                // create animator with our progressBar, property and end value
                return ObjectAnimator.ofFloat(textView, PROGRESS_PROPERTY, end);
            }
        }
        return null;
    }
}