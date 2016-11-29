package ua.itstep.android11.moneyflow.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import ua.itstep.android11.moneyflow.utils.Prefs;

/**
 * Created by Maksim Baydala on 24/11/16.
 */
public class Graphics extends View {
    private Paint paintI;
    private Paint paintE;


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



        float left = 0;
        float top = 0;
        float right = canvas.getWidth()/5; //TODO
        float bottom = canvas.getHeight();
        float width = right - left;

        Log.d(Prefs.LOG_TAG , "Graphics onDraw right: " +right);
        Log.d(Prefs.LOG_TAG , "Graphics onDraw bottom: " +bottom);

        float leftE = right + width;
        float rightE = leftE + width;

        paintI = new Paint();
        paintI.setColor(Color.GREEN);

        paintE = new Paint();
        paintE.setColor(Color.BLUE);

        RectF rectFIncomes = new RectF(left, top, right, bottom);
        RectF rectFExpenses = new RectF(leftE, top, rightE, bottom);

        canvas.drawRect(rectFIncomes, paintI);
        canvas.drawRect(rectFExpenses, paintE);



    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //int myHeight = getMeasuredHeight();
        //int myWidth = getMeasuredWidth();


        Log.d(Prefs.LOG_TAG , "Graphics onMeasure  widthMeasureSpec: " + widthMeasureSpec +" , heightMeasureSpec: "+ heightMeasureSpec);
    }


    public void setValues(int plan, int current) {

        draw(new Canvas());

    }




}
