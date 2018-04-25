package com.zhangw.aliencat.video.videoplayer.listener;

/**
 * 回调播放状态给视频列表
 *
 * @author chends
 * @date 2018/1/14.
 */

public interface VideoListEventListener {

    /**
     * 视频列表页面点击播放回调
     *
     * @param position 视频列表下标
     */
    void onClickPlayVideo(int position);

    /**
     * 视频播放状态改变
     *
     * @param state 当前视频状态
     * @param o 附加字段/用于扩展
     */
    void onPlayStateChange(int state, Object o);

    /**
     * 恢复初始状态
     */
    void restoreView();
}
