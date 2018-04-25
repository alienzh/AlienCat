package com.zhangw.aliencat.video.videoplayer;

import android.os.Bundle;

/**
 * @author chends
 * @date 2018/3/23.
 * 视频回调
 */
public interface IVideoPlayerListener {

    /**
     * 开始准备
     */
    void startPrepare();

    /**
     * 准备完成
     */
    void onPrepared();

    /**
     * 缓冲
     */
    void onLoading();

    /**
     * 开始播放
     */
    void onStart();

    /**
     * 播放进度
     */
    void onPlayingProgress(Bundle bundle);

    /**
     * 播放暂停
     */
    void onPause();

    /**
     * 播放完成
     */
    void onCompleted();

    /**
     * 继续播放
     */
    void onResume();

    /**
     * 恢复暂停状态
     *
     * @param seek 是否产生seek动作
     */
    void onResume(boolean seek);

    /**
     * 拖动加载完成
     */
    void onSeekCompleted();

    /**
     * 退出全屏
     */
    void onBackFullscreen();

    /**
     * 播放错误
     *
     * @param what
     * @param extra
     */
    void onError(int what, int extra);

    /**
     * 播放信息
     *
     * @param what
     * @param extra
     */
    void onInfo(int what, int extra);
}
