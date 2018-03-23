package com.zhangw.aliencat.widgets;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.zhangw.aliencat.R;

/**
 * @author zhangw
 * @date 2018/3/23.
 * 倒计时跳过
 */

public class RoundTextProgressbar extends AppCompatTextView {

    /**
     * 外部轮廓的颜色
     */
    @ColorInt
    private int mOutLineColor = Color.WHITE;
    /**
     * 外部轮廓的宽度
     */
    private int mOutLineWidth = 4;
    /**
     * 内部圆颜色
     */
    private ColorStateList mInCircleColors = ColorStateList.valueOf(Color.TRANSPARENT);
    /**
     * 中心圆颜色
     */
    @ColorInt
    private int mCircleColor;
    /**
     * 进度条颜色
     */
    @ColorInt
    private int mProgressColor = Color.RED;
    /**
     * 进度条宽度
     */
    private int mProgressWidth = 4;
    /**
     * 进度
     */
    private int mProgress;
    /**
     * 进度类型默认顺时针
     */
    private ProgressType mProgressType = ProgressType.COUT_UP;

    /**
     * 画笔
     */
    private Paint mPaint = new Paint();
    private RectF mArcRect = new RectF();
    /**
     * View的显示区域。
     */
    final Rect mBounds = new Rect();
    /**
     * 倒计时毫秒数
     */
    private long mTimeMills;
    /**
     * 进度监听
     */
    private OnCountProgressListener mCountProgressListener;
    private int mWhat;

    /**
     * 进度类型
     */
    enum ProgressType {
        /**
         * 顺时针进度条 0~100
         */
        COUT_UP,
        /**
         * 逆时针进度条 100~0
         */
        COUT_DOWN
    }

    /**
     * 进度监听
     */
    public interface OnCountProgressListener {
        /**
         * 进度监听
         *
         * @param what
         * @param progress 进度
         */
        void onProgress(int what, int progress);
    }

    public void setOnCountProgressListener(int what, OnCountProgressListener onCountProgressListener) {
        this.mWhat = what;
        this.mCountProgressListener = onCountProgressListener;
    }

    public RoundTextProgressbar(Context context) {
        super(context);
    }

    public RoundTextProgressbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundTextProgressbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context, attrs);
    }

    /**
     * 初始化
     *
     * @param context 上下文
     * @param attrs   attrs
     */
    private void initData(Context context, AttributeSet attrs) {
        mPaint.setAntiAlias(true);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleTextProgressbar);
        if (typedArray.hasValue(R.styleable.CircleTextProgressbar_in_circle_color)) {
            mInCircleColors = typedArray.getColorStateList(R.styleable.CircleTextProgressbar_in_circle_color);
        }
        mCircleColor = mInCircleColors.getColorForState(getDrawableState(), Color.TRANSPARENT);
        if (typedArray.hasValue(R.styleable.CircleTextProgressbar_out_circle_color)) {
            mOutLineColor = typedArray.getColor(R.styleable.CircleTextProgressbar_out_circle_color, Color.WHITE);
        }
        if (typedArray.hasValue(R.styleable.CircleTextProgressbar_progress_color)) {
            mProgressColor = typedArray.getColor(R.styleable.CircleTextProgressbar_progress_color, Color.RED);
        }
        if (typedArray.hasValue(R.styleable.CircleTextProgressbar_out_circle_width)) {
            mOutLineWidth = typedArray.getDimensionPixelSize(R.styleable.CircleTextProgressbar_out_circle_width, 4);
        }
        if (typedArray.hasValue(R.styleable.CircleTextProgressbar_progress_width)) {
            mProgressWidth = typedArray.getDimensionPixelSize(R.styleable.CircleTextProgressbar_progress_width, 4);
        }
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //获取view的边界
        getDrawingRect(mBounds);

        int size = mBounds.height() > mBounds.width() ? mBounds.width() : mBounds.height();
        float outerRadius = size / 2;

        //画内部背景
        int circleColor = mInCircleColors.getColorForState(getDrawableState(), 0);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(circleColor);
        canvas.drawCircle(mBounds.centerX(), mBounds.centerY(), outerRadius - mOutLineWidth, mPaint);

        //画边框圆
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mOutLineWidth);
        mPaint.setColor(mOutLineColor);
        canvas.drawCircle(mBounds.centerX(), mBounds.centerY(), outerRadius - mOutLineWidth / 2, mPaint);

        //画字
        Paint paint = getPaint();
        paint.setColor(getCurrentTextColor());
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        float textY = mBounds.centerY() - (paint.descent() + paint.ascent()) / 2;
        canvas.drawText(getText().toString(), mBounds.centerX(), textY, paint);

        //画进度条
        mPaint.setColor(mProgressColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mProgressWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        int deleteWidth = mProgressWidth + mOutLineWidth;
        mArcRect.set(mBounds.left + deleteWidth, mBounds.top + deleteWidth, mBounds.right - deleteWidth, mBounds.bottom - deleteWidth);

        canvas.drawArc(mArcRect, 0, 360 * mProgress / 100, false, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int lineWidth = 2 * (mOutLineWidth + mProgressWidth);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int size = (width > height ? width : height) + lineWidth;
        setMeasuredDimension(size, size);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        updateCircleColor();
    }

    /**
     * 设置外部轮廓颜色
     *
     * @param outLineColor 外圆轮廓颜色
     */
    public void setOutLineColor(@ColorInt int outLineColor) {
        this.mOutLineColor = outLineColor;
        invalidate();
    }

    /**
     * 设置外部轮廓宽度
     *
     * @param outLineWidth 外圆轮廓宽度
     */
    public void setOutLineWidth(int outLineWidth) {
        this.mOutLineWidth = outLineWidth;
    }

    /**
     * 圆形的填充颜色
     *
     * @param inCircleColor 圆形填充颜色
     */
    public void setInCircleColors(@ColorInt int inCircleColor) {
        this.mInCircleColors = ColorStateList.valueOf(inCircleColor);
        invalidate();
    }

    /**
     * 设置进度调颜色
     *
     * @param progressColor 进度条颜色
     */
    public void setProgressColor(@ColorInt int progressColor) {
        this.mProgressColor = progressColor;
        invalidate();
    }

    /**
     * 设置进度条宽度
     *
     * @param progressWidth 进度条宽度
     */
    public void setProgressWidth(int progressWidth) {
        this.mProgressWidth = progressWidth;
        invalidate();
    }

    /**
     * 设置进度
     *
     * @param progress 进度值
     */
    public void setProgress(int progress) {
        this.mProgress = validateProgress(progress);
        invalidate();
    }

    /**
     * 更新圆的颜色
     */
    private void updateCircleColor() {
        int circleColor = mInCircleColors.getColorForState(getDrawableState(), Color.TRANSPARENT);
        if (this.mCircleColor != circleColor) {
            this.mCircleColor = circleColor;
            invalidate();
        }
    }

    /**
     * 验证进度值
     *
     * @param progress 进度值
     * @return 返回真实进度值
     */
    private int validateProgress(int progress) {
        return progress > 100 ? 100 : (progress < 0 ? 0 : progress);
    }

    /**
     * 进度条复原
     */
    private void resetProgress() {
        mProgress = mProgressType == ProgressType.COUT_UP ? 0 : 100;
    }

    /**
     * 设置倒计时毫秒
     *
     * @param timeMills 毫秒
     */
    public void setTimeMills(long timeMills) {
        this.mTimeMills = timeMills;
        invalidate();
    }

    /**
     * 开始
     */
    public void start() {
        stop();
        post(mProgressChangeTask);
    }

    /**
     * 停止
     */
    public void stop() {
        removeCallbacks(mProgressChangeTask);
    }

    public void restart() {
        resetProgress();
        start();
    }

    /**
     * 进度更新task
     */
    private Runnable mProgressChangeTask = new Runnable() {
        @Override
        public void run() {
            removeCallbacks(this);
            if (mProgressType == ProgressType.COUT_UP) {
                mProgress += 1;
            } else {
                mProgress -= 1;
            }
            if (mProgress >= 0 && mProgress <= 100) {
                if (null != mCountProgressListener) {
                    mCountProgressListener.onProgress(mWhat, mProgress);
                }
                invalidate();
                postDelayed(this, mTimeMills / 100);
            } else {
                mProgress = validateProgress(mProgress);
            }
        }
    };
}
