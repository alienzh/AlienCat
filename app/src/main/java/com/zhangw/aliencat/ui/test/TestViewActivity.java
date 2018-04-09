package com.zhangw.aliencat.ui.test;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.zhangw.aliencat.R;
import com.zhangw.aliencat.base.BaseActivity;
import com.zhangw.aliencat.widgets.PView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author zhangw
 * @date 2018/3/23.
 */
public class TestViewActivity extends BaseActivity {

    @BindView(R.id.flViewContainer)
    FrameLayout mFlViewContainer;

    @Override
    public int getLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_testview;
    }

    @Override
    public void initEnv() {
        showHead(true, true);
        setHeadTitle("自定义view");
        loadRootFragment(R.id.flViewContainer,TestViewFragment.newInstance());
    }
}
