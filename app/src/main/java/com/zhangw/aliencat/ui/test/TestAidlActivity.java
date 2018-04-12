package com.zhangw.aliencat.ui.test;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.util.ToastUtils;
import com.zhangw.aliencat.IMyAidlInterface;
import com.zhangw.aliencat.R;
import com.zhangw.aliencat.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author zhangw
 * @date 2018/4/12.
 * 描述
 */

public class TestAidlActivity extends BaseActivity {
    @BindView(R.id.bindService)
    Button bindService;
    @BindView(R.id.unbindService)
    Button unbindService;
    @BindView(R.id.addInvoked)
    Button addInvoked;
    @BindView(R.id.minInvoked)
    Button minInvoked;

    private IMyAidlInterface myAidlInterface;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myAidlInterface = IMyAidlInterface.Stub.asInterface(service);
            ToastUtils.showShort("onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myAidlInterface = null;
            ToastUtils.showShort("onServiceDisconnected");
        }
    };

    @Override
    public int getLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_aidl;
    }

    @Override
    public void initEnv() {

    }

    @OnClick({R.id.bindService, R.id.unbindService, R.id.addInvoked, R.id.minInvoked})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bindService:
                Intent intent = new Intent();
                intent.setAction("com.zhangw.alient.aidl.calc");
                bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
                break;
            case R.id.unbindService:
                unbindService(serviceConnection);
                break;
            case R.id.addInvoked:
                if (myAidlInterface != null) {
                    int addRes = 0;
                    try {
                        addRes = myAidlInterface.add(12, 12);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    ToastUtils.showShort(addRes + "");
                } else {
                    ToastUtils.showShort("服务器被异常杀死，请重新绑定服务端");
                }
                break;
            case R.id.minInvoked:
                if (myAidlInterface != null) {
                    int addRes = 0;
                    try {
                        addRes = myAidlInterface.min(58, 12);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    ToastUtils.showShort(addRes + "");
                } else {
                    ToastUtils.showShort("服务器被异常杀死，请重新绑定服务端");
                }
                break;
            default:
                break;
        }
    }
}
