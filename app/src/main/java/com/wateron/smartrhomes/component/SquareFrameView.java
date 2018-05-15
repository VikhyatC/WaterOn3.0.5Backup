package com.wateron.smartrhomes.component;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by paran on 9/21/2016.
 */
public class SquareFrameView extends FrameLayout {
    public SquareFrameView(Context context) {
        super(context);
    }

    public SquareFrameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if(heightMeasureSpec>widthMeasureSpec){
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        }else{
            super.onMeasure(heightMeasureSpec, heightMeasureSpec);
        }


    }

    public SquareFrameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
