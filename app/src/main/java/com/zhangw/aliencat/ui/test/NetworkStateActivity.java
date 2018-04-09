package com.zhangw.aliencat.ui.test;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.zhangw.aliencat.R;
import com.zhangw.aliencat.base.BaseActivity;
import com.zhangw.aliencat.ui.receiver.NetworkStateReceiver;

/**
 * @author zhangw
 * @date 2018/4/9.
 * 描述
 */
public class NetworkStateActivity extends BaseActivity {

    private NetworkStateReceiver networkStateReceiver;

    @Override
    public int getLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_networkstate;
    }

    @Override
    public void initEnv() {
        showHead(true, true);
        setHeadTitle("网络状态变化");
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkStateReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null == networkStateReceiver) {
            networkStateReceiver = new NetworkStateReceiver();
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkStateReceiver, intentFilter);
    }
}
