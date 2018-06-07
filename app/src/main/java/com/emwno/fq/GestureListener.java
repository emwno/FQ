package com.emwno.fq;

import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created on 08 Jun 2018.
 */
public interface GestureListener extends GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    default boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    default boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    default boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    default boolean onDown(MotionEvent e) {
        return false;
    }

    default void onShowPress(MotionEvent e) {

    }

    default boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    default boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    default void onLongPress(MotionEvent e) {

    }

    default boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
