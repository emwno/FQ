package com.emwno.fq;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created on 23 May 2018.
 */
public class ViewPagerTransformer implements ViewPager.PageTransformer {

    // Georgi Eftimov's BackgroundToForegroundTransformer
    // modified to meet this project's need.
    // https://github.com/geftimov/android-viewpager-transformers

    @Override
    public void transformPage(View page, float position) {
        if (page.getId() == R.id.fragment_quote) {
            if (position <= 0) {
                // Swipe left
                page.setScaleX(1f);
                page.setScaleY(1f);
                page.setPivotX(page.getWidth() * 0.5f);
                page.setPivotY(page.getHeight() * 0.5f);
                page.setTranslationX(-page.getWidth() * position * 0.25f);
            } else if (position > 0) {
                // Swipe right
                float scale = Math.abs(1f - position);

                page.setScaleX(scale);
                page.setScaleY(scale);
                page.setPivotX(page.getWidth() * 0.5f);
                page.setPivotY(page.getHeight() * 0.5f);
                page.setTranslationX(-page.getWidth() * position * 0.25f);

            }
        }

    }

}
