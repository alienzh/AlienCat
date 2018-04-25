package com.zhangw.aliencat.video.videoplayer

/**
 * 视频播放状态
 * Created by xiemy on 2017/6/7.
 */
object PlayState {
    //正常
    const val STATE_NORMAL = 0xF0
    //预备中
    const val STATE_PREPAREING = 0xF1
    //播放中
    const val STATE_PLAYING = 0xF2
    //开始缓存
    const val STATE_BUFFERING_START=0xF3
    //暂停
    const val STATE_PAUSE = 0XF4
    //自动播放结束
    const val STATE_AUTO_COMPLETE = 0xF5
    //错误状态
    const val STATE_ERROR = 0XF6
}
