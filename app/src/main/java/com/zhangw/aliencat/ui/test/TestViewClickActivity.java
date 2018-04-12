package com.zhangw.aliencat.ui.test;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.util.ToastUtils;
import com.zhangw.aliencat.R;
import com.zhangw.aliencat.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author zhangw
 * @date 2018/4/11.
 * android事件分发
 */

public class TestViewClickActivity extends BaseActivity {
    @BindView(R.id.btnClick1)
    Button btnClick1;
    @BindView(R.id.btnClick2)
    Button btnClick2;

    @Override
    public int getLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_viewclick;
    }

    @Override
    public void initEnv() {
        showHead(true, true);
        setHeadTitle("事件分发");
        btnClick1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ToastUtils.showShort("onTouch1 action:" + event.getAction());
                return false;
            }
        });
        btnClick2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ToastUtils.showShort("onTouch2 action:" + event.getAction());
                return true;
            }
        });
    }

    @OnClick({R.id.btnClick1, R.id.btnClick2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnClick1:
                ToastUtils.showShort("onClick 1");
                break;
            case R.id.btnClick2:
                ToastUtils.showShort("onClick 2");
                break;
            default:
        }
    }
}
