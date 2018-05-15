package com.wateron.smartrhomes.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.wateron.smartrhomes.R;

/**
 * Created by paran on 9/19/2016.
 */
public class Bar extends View {
    public Bar(Context context) {
        super(context);
        initPaint();
    }

    private void initPaint() {
        avgPaint.setColor(getResources().getColor(R.color.white));
        valuePaint.setColor(getResources().getColor(R.color.value));
        touchPaint.setColor(getResources().getColor(R.color.touched));
    }

    public Bar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public Bar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    int max=100;
    int avg=0;
    int value=50;
    boolean touched=false;
    Paint avgPaint=new Paint();
    Paint valuePaint=new Paint();
    Paint touchPaint=new Paint();

    public void init(int max,int avg,int value){
        Log.d("progress_bar","max"+String.valueOf(max)+"avg"+String.valueOf(avg)+"value"+String.valueOf(value));
        if(max==0){
            this.max=100;
        }else{
            this.max=max;
        }

        this.avg=avg;
        this.value=value;
        Log.d("progress_bar","max"+String.valueOf(max)+"avg"+String.valueOf(avg)+"value"+String.valueOf(value));
        this.invalidate();
    }

    public void setTouched(boolean touched) {
        this.touched = touched;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width=getWidth();
        int height=getHeight();

        float valuetodisplay=(height*value)/max;
        float avgtodisplay=(height*avg)/max;
        Log.d("Bar","max value-"+String.valueOf(max)+" max Pixles-"+String.valueOf(getHeight())+" pixels for value-"+String.valueOf(valuetodisplay)
                +" value-"+String.valueOf(value));
        valuetodisplay=height-valuetodisplay;
        avgtodisplay=height-avgtodisplay;
        canvas.drawRect(0,valuetodisplay,getWidth(),getHeight(),valuePaint);
        for(int i = (int) valuetodisplay; i<height; i++){

        }
        if(avgtodisplay<valuetodisplay){
            canvas.drawRect(0,valuetodisplay,getWidth(),getHeight(),avgPaint);
            if(touched){
                canvas.drawRect(0,valuetodisplay,getWidth(),getHeight(),touchPaint);
            }
        }else{
            canvas.drawRect(0,avgtodisplay,getWidth(),getHeight(),avgPaint);
            if(touched){
                canvas.drawRect(0,avgtodisplay,getWidth(),getHeight(),touchPaint);
            }
        }


    }
}
