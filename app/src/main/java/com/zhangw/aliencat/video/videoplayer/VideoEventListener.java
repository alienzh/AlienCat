package com.zhangw.aliencat.video.videoplayer;

/**
 * @author chends on 2018/1/10.
 */
public interface VideoEventListener {

    /**
     * 网络断开，播放错误
     */
    int NET_DISCONNECT = -2301;
    /**
     * 点播网络重连
     */
    int NET_RECONNECT = 2103;

    /**
     * 播放视频
     */
    void onPlayVideo();

    /**
     * 播放十秒上报
     */
    void onPlayTenSecondsVideo();

    /**
     * 播放一半上报
     */
    void onPlayHalfVideo();

    /**
     * 视频播放完成
     */
    void onVideoPlayComplete();

    /**
     * 点击左下角播放暂停按钮监听
     */
    void clickPause(boolean shouldPlay);

    /**
     * 视频播放状态
     *
     * @param isShowLoading 是否显示加载
     */
    void onStateChanged(boolean isShowLoading);

    /**
     * 隐藏播放器外部控件（视频详情头部）
     *
     * @param isHide 是否隐藏
     */
    void onHide(boolean isHide);

    /**
     * 试看一分钟
     * 视频时长>= 5分钟的，  可以试看一分钟
     * 视频时长<5分钟的， 按视频时长的20%试看
     */
    void onVideoLookMinute();

    /**
     * 视频加载错误
     *
     * @param e msg
     */
    void onVideoError(Exception e);

    /**
     * 播放其他视频暂停当前视频
     * 点击进入详情设置当前播放页面为暂停状态
     */
    void onPlayOtherVideo();

    /**
     * 网络播放回调
     *
     * @param status 状态
     */
    void onNetStatus(int status);

    /**
     * 视频列表中开始播放
     */
    void onStartPlayVideo();

    /**
     * 接收到第一帧回调
     */
    void onReceiveFirstFrame();
}
