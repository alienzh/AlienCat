package com.zhangw.aliencat.video.videoplayer;

import android.os.Bundle;

/**
 * Created by chends on 2018/3/23.
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
     * 正在播放
     */
    void onPlaying();

    /**
     * 播放进度
     */
    void onPlayingProgress(Bundle bundle);

    /**
     * 播放暂停
     */
    void onPause();

    /**
     * 播放停止
     */
    void onStop();

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
     * @param seek 是否产生seek动作
     */
    void onResume(boolean seek);

    /**
     * 拖动加载完成
     */
    void onSeekCompleted();

    /**
     * 网络错误播放停止
     */
    void onNetStatus(int status);

    /**
     * 退出全屏
     */
    void onBackFullscreen();
}
