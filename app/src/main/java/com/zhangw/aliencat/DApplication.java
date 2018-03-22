package com.zhangw.aliencat;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.Utils;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;

/**
 * @author zhangw
 * @date 2018/3/22.
 */
public class DApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        String versionName;
//        try {
//            // getPackageName()是你当前类的包名，0代表是获取版本信息
//            PackageManager packageManager = base.getPackageManager();
//            PackageInfo packInfo = packageManager.getPackageInfo(base.getPackageName(), 0);
//            versionName = packInfo.versionName;
//        } catch (Exception e) {
//            versionName = "";
//        }
        
        //aliyun hotfix 初始化
        SophixManager.getInstance().setContext(this)
                .setAppVersion("1.0.0")
                .setAesKey(null)
                .setEnableDebug(true)
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        // 补丁加载回调通知
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            // 表明补丁加载成功
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                            // 建议: 用户可以监听进入后台事件, 然后调用killProcessSafely自杀，以此加快应用补丁，详见1.3.2.3
                        } else {
                            // 其它错误信息, 查看PatchStatus类说明
                        }
                    }
                }).initialize();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // queryAndLoadNewPatch不可放在attachBaseContext 中，否则无网络权限，建议放在后面任意时刻，如onCreate中
        SophixManager.getInstance().queryAndLoadNewPatch();
        Utils.init(this);
    }
}
