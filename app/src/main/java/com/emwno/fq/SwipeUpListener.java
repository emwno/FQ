package com.emwno.fq;

import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created on 25 May 2018.
 */
public abstract class SwipeUpListener extends GestureDetector.SimpleOnGestureListener {

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        boolean result = false;
        float yDiff = Math.abs(e1.getY() - e2.getY());

        if (Math.abs(velocityY) > 0 && yDiff > 100) {
            if (e1.getY() > e2.getY()) {
                onSwipeUp();
                result = true;
            }
        }

        return result;
    }

    public abstract void onSwipeUp();

}
