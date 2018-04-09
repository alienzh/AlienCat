package com.zhangw.aliencat.ui.test;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import android.widget.ImageView;

import com.blankj.utilcode.util.ToastUtils;
import com.zhangw.aliencat.R;
import com.zhangw.aliencat.base.BaseActivity;
import com.zhangw.aliencat.subutil.ScreenRotateUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author zhangw
 * @date 2018/4/9.
 * 屏幕旋转 测试
 */
public class ScreenRotationActivity extends BaseActivity {

    @BindView(R.id.image)
    ImageView image;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    @Override
    public int getLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_screenrotation;
    }

    @Override
    public void initEnv() {

    }

    @OnClick(R.id.image)
    public void onViewClicked() {
        ScreenRotateUtil.getInstance(this).toggleRotate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (ScreenRotateUtil.getInstance(this).isLandscape()) {
            ToastUtils.showLong("当前为横屏");
        } else {
            ToastUtils.showLong("当前为竖屏");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ScreenRotateUtil.getInstance(this).stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScreenRotateUtil.getInstance(this).start(this);
    }
}
