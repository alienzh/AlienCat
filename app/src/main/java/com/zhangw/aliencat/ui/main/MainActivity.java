package com.zhangw.aliencat.ui.main;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zhangw.aliencat.R;
import com.zhangw.aliencat.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author zhangw
 * @date 2018/3/23.
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.point)
    Button mPoint;
    @BindView(R.id.points)
    Button mPoints;
    @BindView(R.id.line)
    Button mLine;
    @BindView(R.id.lines)
    Button mLines;
    @BindView(R.id.drawRect)
    Button mDrawRect;
    @BindView(R.id.drawOval)
    Button mDrawOval;
    @BindView(R.id.drawRoundRect)
    Button mDrawRoundRect;
    @BindView(R.id.drawCircle)
    Button mDrawCircle;
    @BindView(R.id.drawArc)
    Button mDrawArc;

    @Override
    public int getLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void initEnv() {

    }

    private Canvas mCanvas = new Canvas();
    private Paint mPaint = new Paint();
    private Rect mRect;
    private RectF mRectF;

    @OnClick({R.id.point, R.id.points, R.id.line, R.id.lines, R.id.drawRect, R.id.drawOval, R.id.drawRoundRect, R.id.drawCircle, R.id.drawArc, R.id.clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.point:
                break;
            case R.id.points:
                break;
            case R.id.line:
                break;
            case R.id.lines:
                break;
            case R.id.drawRect:
                break;
            case R.id.drawOval:
                break;
            case R.id.drawRoundRect:
                break;
            case R.id.drawCircle:
                break;
            case R.id.drawArc:
                break;
            case R.id.clear:
                mCanvas.restore();
                break;
            default:
                break;
        }
    }
}
