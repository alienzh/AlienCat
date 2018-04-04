package com.zhangw.aliencat.ui.main;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zhangw.aliencat.R;
import com.zhangw.aliencat.base.BaseActivity;
import com.zhangw.aliencat.widgets.PView;

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
    @BindView(R.id.view)
    PView mPView;

    @Override
    public int getLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void initEnv() {
        showHead(false, false);
    }

    @OnClick({R.id.point, R.id.points, R.id.line, R.id.lines, R.id.drawRect, R.id.drawOval, R.id.drawRoundRect, R.id.drawCircle, R.id.drawArc, R.id.clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.point:
                mPView.drawView(PView.POINT);
                break;
            case R.id.points:
                mPView.drawView(PView.POINTS);
                break;
            case R.id.line:
                mPView.drawView(PView.LINE);
                break;
            case R.id.lines:
                mPView.drawView(PView.LINES);
                break;
            case R.id.drawRect:
                mPView.drawView(PView.DRAWRECT);
                break;
            case R.id.drawOval:
                mPView.drawView(PView.DRAWOVAL);
                break;
            case R.id.drawRoundRect:
                mPView.drawView(PView.DRAWROUNDRECT);
                break;
            case R.id.drawCircle:
                mPView.drawView(PView.DRAWCIRCLE);
                break;
            case R.id.drawArc:
                mPView.drawView(PView.DRAWARC);
                break;
            case R.id.clear:
                mPView.clear();
                break;
            default:
                break;
        }
    }
}
