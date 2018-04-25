package com.zhangw.aliencat.video.videoplayer;

import android.content.Context;
import android.os.Message;

import com.tencent.rtmp.TXVodPlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.zhangw.aliencat.video.videoplayer.listener.IVideoPlayerListener;

/**
 * @author zhangw
 * @date 2018/4/24.
 * VideoManager 与 View 之间的接口
 */
public interface VideoViewBridge {
    /**
     * 当前播放监听
     */
    IVideoPlayerListener listener();

    /**
     * 上个播放监听
     */
    IVideoPlayerListener lastListener();

    /**
     * 设置监听
     * @param listener
     */
    void setListener(IVideoPlayerListener listener);

    /**
     * 设置监听
     */
    void setLastListener(IVideoPlayerListener lastListener);

    TXVodPlayer getMediaPlayer();

    /**
     * 获取播放状态
     */
    int getLastState();

    /**
     * 设置播放状态
     */
    void setLastState(int lastState);

    /**
     * 设置是否静音
     */
    void setNeedMute(boolean needMute);

    String getPlayTag();

    void setPlayTag(String playTag);

    int getPlayPosition();

    void setPlayPosition(int playPosition);

    /**
     * 释放
     */
    void releaseMediaPlayer();

    void initVideoPlayer(Context context, Message message,TXCloudVideoView initVideoPlayer);

}
