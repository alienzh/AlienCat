package com.zhangw.aliencat.video.videoplayer;

/**
 * 视频播放状态
 * Created by xiemy on 2017/6/7.
 */
public class PlayState {
    //预备
    public final static int READY = 0xF1;
    //加载
    public final static int LOADING = 0xF2;
    //播放
    public final static int PLAY = 0xF3;
    //暂停
    public final static int PAUSE = 0XF4;
    //停止
    public final static int STOP = 0xF5;
    //播放完成
    public final static int COMPLETE = 0Xf6;
    //释放
    public final static int RELEASE = 0xF7;
}
