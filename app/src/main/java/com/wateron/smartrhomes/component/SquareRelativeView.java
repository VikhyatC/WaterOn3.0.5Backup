package com.wateron.smartrhomes.component;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by paran on 9/21/2016.
 */
public class SquareRelativeView extends RelativeLayout {
    public SquareRelativeView(Context context) {
        super(context);
    }

    public SquareRelativeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareRelativeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, heightMeasureSpec);
    }
}
