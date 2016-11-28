package ua.itstep.android11.moneyflow.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import ua.itstep.android11.moneyflow.utils.Prefs;

/**
 * Created by Maksim Baydala on 24/11/16.
 */
public class Graphics extends View {
    private Paint paint;


    // public Graphics(Context context, AttributeSet attrs, int defStyle) {
    public Graphics(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d(Prefs.LOG_TAG , "Graphics CONSTRUCT ");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(Prefs.LOG_TAG , "Graphics onDraw ");
        setBackgroundColor(676);


        paint = new Paint();
        paint.setColor(Color.BLUE);

        RectF rectF = new RectF(0, 0, 134, 146);


        canvas.drawArc(rectF, 270, 360, true, paint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //int myHeight = getMeasuredHeight();
        //int myWidth = getMeasuredWidth();


        Log.d(Prefs.LOG_TAG , "widthMeasureSpec: " + widthMeasureSpec +" , heightMeasureSpec: "+ heightMeasureSpec);
    }


    public void setValues(int plan, int current) {

        draw(new Canvas());

    }




}
