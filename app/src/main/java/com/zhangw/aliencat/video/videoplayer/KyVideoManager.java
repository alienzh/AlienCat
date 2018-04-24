package com.zhangw.aliencat.video.videoplayer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.zhangw.aliencat.R;
import com.zhangw.aliencat.video.CommonUtil;
import com.zhangw.aliencat.video.videoplayer.ui.BaseVideoPlayerView;

/**
 * @author zhangw
 * @date 2018/4/24
 * 视频管理，单例
 */
public class KyVideoManager extends BaseVideoManager {

    public static final int FULLSCREEN_ID = R.id.video_full_id;
    private static final String TAG = "VideoPlayerManager";
    private final static int RETRY_COUNT = 3;
    @SuppressLint("StaticFiledLeak")
    private static KyVideoManager videoManager;

    private KyVideoManager() {
        init();
    }

    /**
     * 退出全屏，主要用于返回键
     *
     * @return 返回是否全屏
     */
    @SuppressWarnings("ResourceType")
    public static boolean backFromWindowFull(Context context) {
        boolean backFrom = false;
        ViewGroup vp = (CommonUtil.scanForActivity(context)).findViewById(Window.ID_ANDROID_CONTENT);
        View oldF = vp.findViewById(FULLSCREEN_ID);
        if (oldF != null) {
            backFrom = true;
            CommonUtil.hideNavKey(context);
            if (KyVideoManager.instance().lastListener() != null) {
                KyVideoManager.instance().lastListener().onBackFullscreen();
            }
        }
        return backFrom;
    }

    /**
     * 单例管理器
     */
    public static synchronized KyVideoManager instance() {
        if (null == videoManager) {
            videoManager = new KyVideoManager();
        }
        return videoManager;
    }

    /**
     * 页面销毁了记得调用是否所有的video
     */
    public static void releaseAllVideos() {
        if (KyVideoManager.instance().listener() != null) {
            instance().listener().onCompleted();
        }
        KyVideoManager.instance().releaseMediaPlayer();
    }


    /**
     * 暂停播放
     */
    public static void onPause() {
        if (KyVideoManager.instance().listener() != null) {
            KyVideoManager.instance().listener().onPause();
        }
    }

    /**
     * 恢复播放
     */
    public static void onResume() {
        if (KyVideoManager.instance().listener() != null) {
            KyVideoManager.instance().listener().onResume();
        }
    }

    /**
     * 恢复暂停状态
     *
     * @param seek 是否产生seek动作,直播设置为false
     */
    public static void onResume(boolean seek) {
        if (KyVideoManager.instance().listener() != null) {
            KyVideoManager.instance().listener().onResume(seek);
        }
    }

    /**
     * 当前是否全屏状态
     *
     * @return 当前是否全屏状态， true代表是。
     */
    @SuppressWarnings("ResourceType")
    public static boolean isFullState(Activity activity) {
        ViewGroup vp = (ViewGroup) (CommonUtil.scanForActivity(activity)).findViewById(Window.ID_ANDROID_CONTENT);
        final View full = vp.findViewById(FULLSCREEN_ID);
        BaseVideoPlayerView videoPlayer = null;
        if (full != null) {
            videoPlayer = (BaseVideoPlayerView) full;
        }
        return videoPlayer != null;
    }

}
