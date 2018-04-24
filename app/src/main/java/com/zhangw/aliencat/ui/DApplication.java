package com.zhangw.aliencat.ui;


import com.blankj.utilcode.util.Utils;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

/**
 * @author zhangw
 * @date 2018/3/22.
 */
public class DApplication extends LitePalApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        // queryAndLoadNewPatch不可放在attachBaseContext 中，否则无网络权限，建议放在后面任意时刻，如onCreate中
//        SophixManager.getInstance().queryAndLoadNewPatch();
        Utils.init(this);
    }
}
