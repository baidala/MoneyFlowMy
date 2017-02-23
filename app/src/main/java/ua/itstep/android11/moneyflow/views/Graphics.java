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
    private Paint paintI;
    private Paint paintE;
    float incomes = 0l;
    float expenses = 0l;
    float topI = 0l;
    float topE = 0l;
    float bottom = 0l;


    public Graphics(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d(Prefs.LOG_TAG , "Graphics CONSTRUCT ");



    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(Prefs.LOG_TAG , "Graphics onDraw ");
        //setBackgroundColor(Color.LTGRAY);



        float left = canvas.getWidth()/10;;
        float right = canvas.getWidth()/3;
        float width = right - left;
        bottom = canvas.getHeight();

        Log.d(Prefs.LOG_TAG , "Graphics onDraw before topI: " +topI);
        Log.d(Prefs.LOG_TAG , "Graphics onDraw before topE: " +topE);

        Log.d(Prefs.LOG_TAG , "Graphics onDraw before bottom: " +bottom);
        Log.d(Prefs.LOG_TAG , "Graphics onDraw before incomes: " +incomes);
        Log.d(Prefs.LOG_TAG , "Graphics onDraw before expenses: " +expenses);


        if ( bottom != 0 ) {
            float bottomTemp = bottom;
            if( incomes > expenses ) {
                topE = bottomTemp-(bottom*(expenses/(incomes +1)));
                topI = 0l;

                //Log.d(Prefs.LOG_TAG, "Graphics onDraw incomes div : "+(expenses/(incomes +1)));
                //Log.d(Prefs.LOG_TAG, "Graphics onDraw incomes div : "+ (bottom*(expenses/(incomes +1))) );
                Log.d(Prefs.LOG_TAG, "Graphics onDraw incomes procent : "+topE);

            } else {
                topE = 0l;
                topI = bottomTemp-(bottom*(incomes/(expenses +1)));

                Log.d(Prefs.LOG_TAG, "Graphics onDraw expenses procent : "+topI);

            }
        }

        Log.d(Prefs.LOG_TAG , "Graphics onDraw after right: " + right);
        Log.d(Prefs.LOG_TAG , "Graphics onDraw after bottom: " + bottom);
        Log.d(Prefs.LOG_TAG , "Graphics onDraw after width: " + width);
        Log.d(Prefs.LOG_TAG , "Graphics onDraw after topI: " + topI);
        Log.d(Prefs.LOG_TAG , "Graphics onDraw after topE: " + topE);

        float leftE = right + width;
        float rightE = leftE + width;

        paintI = new Paint();
        paintI.setARGB(255, 30, 207, 30);

        paintE = new Paint();
        paintE.setARGB(255, 53, 6, 171);

        RectF rectFIncomes = new RectF(left, topI, right, bottom);
        RectF rectFExpenses = new RectF(leftE, topE, rightE, bottom);

        canvas.drawRect(rectFIncomes, paintI);
        canvas.drawRect(rectFExpenses, paintE);



    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int myHeight = getMeasuredHeight();
        final int myWidth = getMeasuredWidth();

        setMeasuredDimension(myWidth, myHeight);


        Log.d(Prefs.LOG_TAG , "Graphics onMeasure  widthMeasureSpec: " + widthMeasureSpec +" , heightMeasureSpec: "+ heightMeasureSpec);
    }


    public void setValues(float inc, float expn) {

        this.incomes = inc;
        this.expenses = expn;

        Log.d(Prefs.LOG_TAG, "Graphics setValues: "+incomes +" "+expenses);

        draw(new Canvas());

    }




}
