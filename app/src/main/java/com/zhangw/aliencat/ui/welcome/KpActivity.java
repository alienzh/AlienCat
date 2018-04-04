package com.zhangw.aliencat.ui.welcome;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zhangw.aliencat.R;
import com.zhangw.aliencat.base.BaseActivity;
import com.zhangw.aliencat.ui.main.MainActivity;
import com.zhangw.aliencat.widgets.RoundTextProgressbar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author zhangw
 * @date 2018/3/23.
 * 开屏页
 */
public class KpActivity extends BaseActivity {

    /**
     * 跳转
     */
    @BindView(R.id.roundProgressbar)
    RoundTextProgressbar mRoundProgressbar;
    @BindView(R.id.restart)
    Button restart;

    @Override
    public int getLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_kp;
    }

    @Override
    public void initEnv() {
        showHead(false, false);
        mRoundProgressbar.setTimeMills(3000);
        mRoundProgressbar.setOnCountProgressListener(100, new RoundTextProgressbar.OnCountProgressListener() {
            @Override
            public void onProgress(int what, int progress) {
                if (what == 100 && progress >= 100) {
                    goNextPage();
                }
            }
        });
        mRoundProgressbar.start();
    }

    private void goNextPage() {
        ActivityUtils.startActivity(this, MainActivity.class);
    }

    @OnClick({R.id.roundProgressbar, R.id.restart})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.roundProgressbar:
                mRoundProgressbar.stop();
                goNextPage();
                break;
            case R.id.restart:
                mRoundProgressbar.restart();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mRoundProgressbar) {
            mRoundProgressbar.stop();
        }
    }
}
