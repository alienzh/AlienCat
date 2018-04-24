package com.zhangw.aliencat.video.videoplayer;

import android.content.Context;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.Message;

import com.blankj.utilcode.util.LogUtils;
import com.tencent.rtmp.ITXVodPlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXVodPlayConfig;
import com.tencent.rtmp.TXVodPlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.lang.ref.WeakReference;

import static com.tencent.rtmp.TXLiveConstants.PAUSE_FLAG_PAUSE_VIDEO;
import static com.tencent.rtmp.TXLiveConstants.PLAY_ERR_FILE_NOT_FOUND;
import static com.tencent.rtmp.TXLiveConstants.PLAY_ERR_NET_DISCONNECT;
import static com.tencent.rtmp.TXLiveConstants.PLAY_EVT_CHANGE_RESOLUTION;
import static com.tencent.rtmp.TXLiveConstants.PLAY_EVT_PLAY_BEGIN;
import static com.tencent.rtmp.TXLiveConstants.PLAY_EVT_PLAY_END;
import static com.tencent.rtmp.TXLiveConstants.PLAY_EVT_PLAY_LOADING;
import static com.tencent.rtmp.TXLiveConstants.PLAY_EVT_PLAY_PROGRESS;
import static com.tencent.rtmp.TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME;
import static com.tencent.rtmp.TXLiveConstants.PLAY_WARNING_RECONNECT;

/**
 * @author zhangw
 * @date 2018/4/24.
 * 基类管理器
 */
public abstract class BaseVideoManager implements ITXVodPlayListener,VideoViewBridge {

    public static String TAG = "VideoBaseManager";

    private final static int RETRY_COUNT = 3;
    protected WeakReference<IVideoPlayerListener> listener;
    protected WeakReference<IVideoPlayerListener> lastListener;
    /**
     * 当前视频的最后状态
     */
    protected int lastState;
    /**
     * 播放的tag，防止错位置，因为普通的url也可能重复
     */
    protected String playTag = "";
    /**
     * 播放的tag，防止错位置，因为普通的url也可能重复
     */
    protected int playPosition = -22;
    /**
     * 是否需要静音
     */
    protected boolean needMute = false;
    /**
     * 腾讯云播放器
     */
    private TXVodPlayer mTXVodPlayer;

    @Override
    public IVideoPlayerListener listener() {
        if (listener == null) {
            return null;
        }
        return listener.get();
    }

    @Override
    public IVideoPlayerListener lastListener() {
        if (lastListener == null) {
            return null;
        }
        return lastListener.get();
    }

    @Override
    public void setListener(IVideoPlayerListener listener) {
        if (listener == null) {
            this.listener = null;
        } else {
            this.listener = new WeakReference<>(listener);
        }
    }

    @Override
    public void setLastListener(IVideoPlayerListener lastListener) {
        if (lastListener == null) {
            this.lastListener = null;
        } else {
            this.lastListener = new WeakReference<>(lastListener);
        }
    }

    @Override
    public TXVodPlayer getMediaPlayer() {
        return mTXVodPlayer;
    }

    @Override
    public int getLastState() {
        return lastState;
    }

    @Override
    public void setLastState(int lastState) {
        this.lastState = lastState;
    }

    public boolean isNeedMute() {
        return needMute;
    }

    @Override
    public void setNeedMute(boolean needMute) {
        if (mTXVodPlayer != null) {
            mTXVodPlayer.setMute(needMute);
        }
    }

    @Override
    public String getPlayTag() {
        return playTag;
    }

    @Override
    public void setPlayTag(String playTag) {
        this.playTag = playTag;
    }

    @Override
    public int getPlayPosition() {
        return playPosition;
    }

    @Override
    public void setPlayPosition(int playPosition) {
        this.playPosition = playPosition;
    }

    @Override
    public void releaseMediaPlayer() {
        if (mTXVodPlayer != null) {
            mTXVodPlayer.stopPlay(true);
            playTag = "";
            playPosition = -22;
        }
    }

    protected void init() {
        HandlerThread mediaHandlerThread = new HandlerThread(TAG);
        mediaHandlerThread.start();
    }


    @Override
    public void initVideoPlayer(Context context, Message msg,TXCloudVideoView txCloudVideoView) {
        mTXVodPlayer = new TXVodPlayer(context);
        //设置平铺模式
        mTXVodPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);

        mTXVodPlayer.setConfig(getTxVodPlayConfig(""));
        mTXVodPlayer.setPlayerView(txCloudVideoView);
        mTXVodPlayer.setVodListener(this);
    }

    protected TXVodPlayConfig getTxVodPlayConfig(String url) {
        TXVodPlayConfig config = new TXVodPlayConfig();
//        config.setCacheFolderPath(videoLocalPath);
        config.setMaxCacheItems(10);
        config.setConnectRetryCount(RETRY_COUNT);
        return config;
    }

    /**
     * @param player
     * @param event
     * @param param
     */
    @Override
    public void onPlayEvent(TXVodPlayer player, int event, Bundle param) {
        //String playEventLog = "receive event: " + event + ", " + param.toString();
        //LogUtil.i(TAG, playEventLog);
        //LogUtil.saveLog(playEventLog.toString(), LogUtil.DETAIL_LOG_LEVEL);
        switch (event) {
            case PLAY_EVT_CHANGE_RESOLUTION:
                break;
            case PLAY_EVT_PLAY_LOADING:
                break;
            //开始播放
            case PLAY_EVT_PLAY_BEGIN:
                break;
            //第一帧视频播放
            case PLAY_EVT_RCV_FIRST_I_FRAME:
                break;
            case PLAY_EVT_PLAY_PROGRESS:
                break;
            case PAUSE_FLAG_PAUSE_VIDEO:
                break;
            case PLAY_EVT_PLAY_END:
                break;
            case PLAY_ERR_FILE_NOT_FOUND:
                break;
            //点播网络重连
            case PLAY_WARNING_RECONNECT:
                break;
            //网络断开，播放错误
            case PLAY_ERR_NET_DISCONNECT:
                break;
            default:
                break;

        }
    }

    @Override
    public void onNetStatus(TXVodPlayer txVodPlayer, Bundle bundle) {
        String playEventLog = "receive net status:  " + bundle.toString();
        LogUtils.i(TAG, playEventLog);
    }
}