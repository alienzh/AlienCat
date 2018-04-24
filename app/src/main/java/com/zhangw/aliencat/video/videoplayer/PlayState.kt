package com.zhangw.aliencat.video.videoplayer

/**
 * 视频播放状态
 * Created by xiemy on 2017/6/7.
 */
object PlayState {
    //预备
    const val READY = 0xF1
    //加载
    const val LOADING = 0xF2
    //播放
    const val PLAY = 0xF3
    //暂停
    const val PAUSE = 0XF4
    //停止
    const val STOP = 0xF5
    //播放完成
    const val COMPLETE = 0Xf6
    //释放
    const val RELEASE = 0xF7
}
