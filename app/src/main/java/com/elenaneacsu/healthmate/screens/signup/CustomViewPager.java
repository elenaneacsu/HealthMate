package com.elenaneacsu.healthmate.screens.signup;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class CustomViewPager extends ViewPager {
    private boolean enableSwipe = false;

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
//        Log.d("test", "onTouchEventIn: "+(enableSwipe && super.onInterceptTouchEvent(event)));
//        return enableSwipe && super.onInterceptTouchEvent(event);
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("test", "onTouchEvent: " + (enableSwipe && super.onTouchEvent(event)));
        return enableSwipe && super.onTouchEvent(event);

    }

    public void setEnableSwipe(boolean enableSwipe) {
        this.enableSwipe = enableSwipe;
        Log.d("test", "onTouchEventSet: " + this.enableSwipe);
    }
}