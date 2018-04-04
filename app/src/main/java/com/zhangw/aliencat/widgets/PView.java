package com.zhangw.aliencat.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author zhangw
 * @date 2018/4/4.
 */
public class PView extends View {

    public final static int POINT = 0x01;
    public final static int POINTS = 0x02;
    public final static int LINE = 0x03;
    public final static int LINES = 0x04;
    public final static int DRAWRECT = 0x05;
    public final static int DRAWOVAL = 0x06;
    public final static int DRAWROUNDRECT = 0x07;
    public final static int DRAWCIRCLE = 0x08;
    public final static int DRAWARC = 0x09;

    @IntDef({POINT, POINTS, LINE, LINES, DRAWRECT, DRAWOVAL, DRAWROUNDRECT, DRAWCIRCLE, DRAWARC})
    @Retention(RetentionPolicy.SOURCE)
    @interface Mode {
    }

    @Mode
    int drawMode = POINT;

    private Paint mPaint = new Paint();
    private Rect mRect;
    private RectF mRectF;

    public PView(Context context) {
        this(context, null);
    }

    public PView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);  //设置画笔模式为填充
        mPaint.setStrokeWidth(10f);
        mPaint.setAntiAlias(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLUE);
        switch (drawMode) {
            case POINT:
                canvas.drawPoint(0, 0, mPaint);
                break;
            case POINTS:
                canvas.drawPoints(new float[]{0, 0, 100, 100}, mPaint);
                break;
            case LINE:
                canvas.drawLine(100, 100, 200, 200, mPaint);
                break;
            case LINES:
                canvas.drawLines(new float[]{200, 200, 400, 400, 300, 300, 500, 500}, mPaint);
                break;
            case DRAWRECT:
                mRect = new Rect();
                mRect.set(50, 50, 350, 350);
                canvas.drawRect(mRect, mPaint);
                break;
            case DRAWOVAL:
                mRectF = new RectF();
                mRectF.set(233, 233, 466, 466);
                canvas.drawOval(mRectF, mPaint);
                break;
            case DRAWROUNDRECT:
                mRectF = new RectF();
                mRectF.set(300, 300, 550, 550);
                canvas.drawRoundRect(mRectF, 40, 40, mPaint);
                break;
            case DRAWCIRCLE:
                canvas.drawCircle(500, 500, 40, mPaint);
                break;
            case DRAWARC:
                mRectF = new RectF();
                mRectF.set(300, 300, 550, 550);
                canvas.drawArc(mRectF, 150, 60, userCenter, mPaint);
                userCenter = !userCenter;
                break;
            default:
                canvas.drawPoint(0, 0, mPaint);
                break;
        }
    }

    private boolean userCenter = true;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void drawView(@Mode int mode) {
        this.drawMode = mode;
        invalidate();
    }

    public void clear() {

    }
}
