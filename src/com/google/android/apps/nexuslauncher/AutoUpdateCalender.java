package com.google.android.apps.nexuslauncher;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.android.launcher3.FastBitmapDrawable;

import java.util.Calendar;

public class AutoUpdateCalender extends FastBitmapDrawable {

    protected float mDensity;

    public AutoUpdateCalender(Bitmap b,float density) {
        super(b);
        mDensity=density;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        Rect bounds = getBounds();

        String dayString=String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        int week=Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        String weekStr="";
        if(week==1){
            weekStr="星期日";
        }else if(week==2){
            weekStr="星期一";
        }else if(week==3){
            weekStr="星期二";
        }else if(week==4){
            weekStr="星期三";
        }else if(week==5){
            weekStr="星期四";
        }else if(week==6){
            weekStr="星期五";
        }else if(week==7){
            weekStr="星期六";
        }

        Paint datePaint = new Paint();
        datePaint.setTypeface(Typeface.DEFAULT_BOLD);
        datePaint.setTextSize((int)38F * mDensity);
        datePaint.setColor(0xff727272);
        datePaint.setAntiAlias(true);

        Rect rect = new Rect();
        datePaint.getTextBounds(dayString,0,dayString.length(),rect);
        int hoffset = 26;
        int width1 = rect.right - rect.left;
        int height1 = rect.bottom - rect.top;
        int width2 = bounds.width();
        int height2 = bounds.height() + hoffset;

        canvas.drawText(dayString,(width2 - width1)/2 - rect.left,(height2 - height1)/2 - rect.top,datePaint);

        Paint weekPaint = new Paint();
        weekPaint.setTypeface(Typeface.DEFAULT);
        weekPaint.setTextSize((int)10F * mDensity);
        weekPaint.setColor(0xffffffff);
        weekPaint.setAntiAlias(true);

        weekPaint.getTextBounds(weekStr,0,weekStr.length(),rect);
        int hoffset2 = 4;
        width1 = rect.right - rect.left;
        height1 = rect.bottom - rect.top;
        width2 = bounds.width();
        height2 = bounds.height() + hoffset2;

        canvas.drawText(weekStr,(width2 - width1)/2 - rect.left,rect.bottom-rect.top+hoffset2,weekPaint);


    }
}
