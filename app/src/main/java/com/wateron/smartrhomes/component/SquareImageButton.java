package com.wateron.smartrhomes.component;

import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;

/**
 * Created by paran on 9/23/2016.
 */
public class SquareImageButton extends AppCompatImageButton {

    public SquareImageButton(Context context) {
        super(context);
    }

    public SquareImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if(heightMeasureSpec>widthMeasureSpec){
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        }else{
            super.onMeasure(heightMeasureSpec, heightMeasureSpec);
        }


    }
}

