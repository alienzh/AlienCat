package com.zhangw.aliencat.video.videoplayer;

/**
 * Created by chends on 2018/1/15.
 * 全屏事件回调
 * 屏幕旋转
 * 点击返回都通过这个回调将事件发送出去
 */

public interface FullscreenEventListener {

    /**
     * 1 竖屏
     * 2 横屏
     * 100返回事件
     * @param newConfig
     */
    void onConfigurationChanged(int newConfig);
}
