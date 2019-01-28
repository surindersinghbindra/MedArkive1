package com.medarkive.Main;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomViewPagerForEbook extends ViewPager {
	public boolean scroll = true;

	public CustomViewPagerForEbook(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public CustomViewPagerForEbook(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


	@Override
    public boolean onTouchEvent(MotionEvent event) {
        return scroll && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return scroll && super.onInterceptTouchEvent(event);
    }

    public void setScroll(boolean scroll) {
        this.scroll = scroll;
    }

}
