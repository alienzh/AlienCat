package com.zhangw.aliencat.ui.welcome;

import android.os.Bundle;
import android.widget.TextView;

import com.zhangw.aliencat.R;
import com.zhangw.aliencat.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author zhangw
 * @date 2018/3/23.
 */
public class WelcomeActivity extends BaseActivity {
    @BindView(R.id.sample_text)
    TextView mSampleText;

    @Override
    public int getLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_welcome;
    }

    @Override
    public void initEnv() {

    }

    @OnClick(R.id.sample_text)
    public void onViewClicked() {
        toPage(KpActivity.class);
    }
}
