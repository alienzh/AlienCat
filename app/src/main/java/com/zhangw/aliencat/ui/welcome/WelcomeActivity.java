package com.zhangw.aliencat.ui.welcome;

import android.os.Bundle;
import android.widget.TextView;

import com.zhangw.aliencat.R;
import com.zhangw.aliencat.base.BaseActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * @author zhangw
 * @date 2018/3/23.
 */
public class WelcomeActivity extends BaseActivity {
    private final static int GO_NEXT = 0xAA;

    @Override
    public int getLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_welcome;
    }

    @Override
    public void initEnv() {
        showHead(false, false);
        Observable.just(GO_NEXT)
                .delay(3000, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        if (integer == GO_NEXT) {
                            toPage(KpActivity.class);
                        }
                    }
                });
    }
}
