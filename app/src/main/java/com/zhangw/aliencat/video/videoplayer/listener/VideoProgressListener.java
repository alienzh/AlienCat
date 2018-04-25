package com.zhangw.aliencat.video.videoplayer.listener;

/**
 * 进度回调
 * @author zhangw
 * @date 2017/12/20.
 */
public interface VideoProgressListener {
    /**
     * 进度回调
     *
     * @param progress        当前播放进度（暂停后再播放可能会有跳动）
     * @param secProgress     当前内存缓冲进度（可能会有0值）
     * @param currentPosition 当前播放位置（暂停后再播放可能会有跳动）
     * @param duration        总时长
     */
    void onProgress(int progress, int secProgress, int currentPosition, int duration);
}
