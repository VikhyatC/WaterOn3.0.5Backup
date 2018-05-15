package com.wateron.smartrhomes.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.wateron.smartrhomes.R;

/**
 * Created by paran on 11/26/2016.
 */

public class TouchBar extends View {

    public TouchBar(Context context) {
        super(context);
        init();
    }

    public TouchBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TouchBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    TouchBarCommunicator touchBarCommunicator;
    Paint topbg;
    Paint botbg;
    Paint paint1;    Bitmap[] bitmap1=new Bitmap[2];
    int slotSelected[]=new int[]{0,0};
    int prevSlotSelected[]=new int[]{0,0};
    boolean touched=false;
    float xPointMax;
    float xPointMin;
    int styleSelected=0;
    int prevStyleSelected=0;
    boolean calculated=false;
    float viewHeight;
    float viewWidth;
    float slotWidth;
    float slotHeight;
    float imageHeight;
    float imageWidth;
    float topMargin;
    float bottomMargin;
    float leftMargin;
    float rightMargin;
    boolean recalibrate=true;
    float xPoint;
    private float width;
    private  float height;

    int maxSlotAvailable[]=new int[]{10,5};

    public void setMaxSlotAvailable(int[] maxSlotAvailable) {
        this.maxSlotAvailable = maxSlotAvailable;
        this.slotSelected=maxSlotAvailable;
        Log.d("DATA"+String.valueOf(maxSlotAvailable[0]),String.valueOf(maxSlotAvailable[1]));
        calculated=false;
        invalidate();
    }

    public void setTouchBarCommunicator(TouchBarCommunicator touchBarCommunicator) {
        this.touchBarCommunicator = touchBarCommunicator;
    }

    private  void init(){
        bitmap1[0]= BitmapFactory.decodeResource(getResources(), R.drawable.time_range_slider);
        bitmap1[1]= BitmapFactory.decodeResource(getResources(), R.drawable.date_range_slider);
        topbg=new Paint();
        paint1=new Paint();
        topbg.setColor(getResources().getColor(R.color.topbg));

        botbg=new Paint();
        botbg.setColor(getResources().getColor(R.color.botbg));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);

        if(!calculated | styleSelected!=prevStyleSelected){
            calculateMesurments();
        }

        if(!touched){
            drawStyle(canvas);
        }else {
            drawFreeStyle(canvas);
        }

    }

    private void drawFreeStyle(Canvas canvas) {

        int left= (int) (xPoint-(imageWidth/2));
        int right= (int) ((imageWidth/2)+xPoint);
        int top= (int) topMargin;
        int bottom= (int) bottomMargin;
        Rect rect=new Rect(left,top,right,bottom);
//        Log.d("left",String.valueOf(left));
//        Log.d("right",String.valueOf(right));
//        Log.d("top",String.valueOf(top));
//        Log.d("bottom",String.valueOf(bottom));


        canvas.drawBitmap(bitmap1[styleSelected],null,rect,paint1);
    }

    private void drawStyle(Canvas canvas) {
        int left= (int) (leftMargin+(slotSelected[styleSelected]*getWidth())/partOptions[styleSelected]);
        int right= (int) (left+imageWidth);

        Rect rect=new Rect(left,(int)topMargin,right,(int)bottomMargin);
//        Log.d("left",String.valueOf(left));
//        Log.d("right",String.valueOf(right));
        canvas.drawBitmap(bitmap1[styleSelected],null,rect,paint1);
    }

    int imageWidthRatio[]=new int[]{85,95};
    int imageHeightRatio[]=new int[]{180,201};
    int partOptions[]=new int[]{12,7};

    private void calculateMesurments() {
        viewWidth=getWidth();
        viewHeight=getHeight();
        imageWidth=getWidth()/partOptions[styleSelected];
        slotWidth=imageWidth;
        imageHeight=getHeight()*0.9f;
        slotHeight=getHeight();
        if(imageWidth<(imageWidthRatio[styleSelected]*imageHeight)/imageHeightRatio[styleSelected]){
            imageHeight=(imageHeightRatio[styleSelected]*imageWidth)/imageWidthRatio[styleSelected];
            imageWidth=(imageWidthRatio[styleSelected]*imageHeight)/imageHeightRatio[styleSelected];
        }else {
            imageWidth=(imageWidthRatio[styleSelected]*imageHeight)/imageHeightRatio[styleSelected];
            imageHeight=(imageHeightRatio[styleSelected]*imageWidth)/imageWidthRatio[styleSelected];
        }
        topMargin=(viewHeight-imageHeight)/2;
        bottomMargin=topMargin+imageHeight;
        leftMargin=(slotWidth-imageWidth)/2;
        rightMargin=leftMargin+imageWidth;
        xPointMin=slotWidth/2;
        xPointMax=(slotWidth*(maxSlotAvailable[styleSelected]+1))-xPointMin;
        calculated=true;
        prevStyleSelected=styleSelected;

        Log.d("xPointMin",String.valueOf(xPointMin));
        Log.d("xPointMax",String.valueOf(xPointMax));

        Log.d("xpoint",String.valueOf(xPoint));
    }

    private void drawBackground(Canvas canvas) {
        Rect r=new Rect(0,0,getWidth(),getHeight()/2);
        Rect r2=new Rect(0,getHeight()/2,getWidth(),getHeight());
        canvas.drawRect(r,topbg);
        canvas.drawRect(r2,botbg);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_MOVE){
            xPoint=event.getX();
//            Log.d("xpoint",String.valueOf(xPoint));
            if(xPoint<xPointMin){
                xPoint=xPointMin;
            }
            if(xPoint>xPointMax){
                xPoint=xPointMax;
            }
            Log.d("xpoint",String.valueOf(xPoint));
            changeSlot(xPoint);
            if(slotSelected[styleSelected]!=prevSlotSelected[styleSelected]){
                if(touchBarCommunicator!=null){
                    touchBarCommunicator.barEntered(styleSelected,slotSelected[styleSelected]);
                }
            }
            Log.d("ACTION_MOVE",String.valueOf(event.getX()));
            invalidate();
            return true;
        }else if(event.getAction()==MotionEvent.ACTION_UP){
//            changeSlot(event.getX());

            xPoint=event.getX();
//            Log.d("xpoint",String.valueOf(xPoint));
            if(xPoint<xPointMin){
                xPoint=xPointMin;
            }
            if(xPoint>xPointMax){
                xPoint=xPointMax;
            } Log.d("ACTION_UP",String.valueOf(xPoint));
//            Log.d("xpoint",String.valueOf(xPoint));
            changeSlot(xPoint);if(touchBarCommunicator!=null){
                touchBarCommunicator.barExcitedd(styleSelected,slotSelected[styleSelected]);
            }
            if(slotSelected[styleSelected]!=prevSlotSelected[styleSelected]){

            }
            touched=false;
//            changeSlot(event.getX());
            invalidate();
            return true;
        }else if(event.getAction()==MotionEvent.ACTION_DOWN){
            xPoint=event.getX();
//            Log.d("xpoint",String.valueOf(xPoint));
            if(xPoint<xPointMin){
                xPoint=xPointMin;
            }
            if(xPoint>xPointMax){
                xPoint=xPointMax;
            }
//            Log.d("xpoint",String.valueOf(xPoint));
            changeSlot(xPoint);if(touchBarCommunicator!=null){
                touchBarCommunicator.barTouched(styleSelected,slotSelected[styleSelected]);
            }
            if(slotSelected[styleSelected]!=prevSlotSelected[styleSelected]){

            }
            touched=true;
            Log.d("ACTION_DOWN",String.valueOf(event.getX()));
            invalidate();
            return true;
        }else{

            touched=false;if(touchBarCommunicator!=null){
                touchBarCommunicator.barExcitedd(styleSelected,slotSelected[styleSelected]);
            }
//            changeSlot(event.getX());
            if(slotSelected[styleSelected]!=prevSlotSelected[styleSelected]){

            }
            invalidate();
//            Log.d("TAG",String.valueOf(event.getAction()==MotionEvent.ACTION_CANCEL));
            Log.d("ACTION_CANCEL",String.valueOf(event.getX()));
            return true;
        }
    }

    private void changeSlot(float x) {
        float f=getWidth()/partOptions[styleSelected];
        prevSlotSelected[styleSelected]=slotSelected[styleSelected];
        switch (styleSelected){
            case 0:
                if(0<x && x<f ){
                    slotSelected[styleSelected]=0;
                }
                if(f*1<x && x<f*2 ){
                    slotSelected[styleSelected]=1;
                }
                if(f*2<x && x<f*3 ){
                    slotSelected[styleSelected]=2;
                }
                if(f*3<x && x<f*4 ){
                    slotSelected[styleSelected]=3;
                }
                if(f*4<x && x<f*5 ){
                    slotSelected[styleSelected]=4;
                }
                if(f*5<x && x<f*6 ){
                    slotSelected[styleSelected]=5;
                }
                if(f*6<x && x<f*7 ){
                    slotSelected[styleSelected]=6;
                }
                if(f*7<x && x<f*8 ){
                    slotSelected[styleSelected]=7;
                }
                if(f*8<x && x<f*9 ){
                    slotSelected[styleSelected]=8;
                }
                if(f*9<x && x<f*10 ){
                    slotSelected[styleSelected]=9;
                }
                if(f*10<x && x<f*11 ){
                    slotSelected[styleSelected]=10;
                }
                if(f*11<x && x<f*12 ){
                    slotSelected[styleSelected]=11;
                }


                break;
            case 1:
                if(0<x && x<f ){
                    slotSelected[styleSelected]=0;
                }
                if(f*1<x && x<f*2 ){
                    slotSelected[styleSelected]=1;
                }
                if(f*2<x && x<f*3 ){
                    slotSelected[styleSelected]=2;
                }
                if(f*3<x && x<f*4 ){
                    slotSelected[styleSelected]=3;
                }
                if(f*4<x && x<f*5 ){
                    slotSelected[styleSelected]=4;
                }
                if(f*5<x && x<f*6 ){
                    slotSelected[styleSelected]=5;
                }
                if(f*6<x && x<f*7 ){
                    slotSelected[styleSelected]=6;
                }
                break;

        }
    }

    public void changeStyle(int style){
        prevStyleSelected=styleSelected;
        styleSelected=style;
        invalidate();
    }

}
