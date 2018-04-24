package com.zhangw.aliencat.video.videoplayer;

import android.os.Bundle;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.tencent.rtmp.ITXVodPlayListener;
import com.tencent.rtmp.TXVodPlayConfig;
import com.tencent.rtmp.TXVodPlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.zhangw.aliencat.video.AudioMngHelper;

import java.io.File;

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
 * Created by chends on 2018/3/23.
 */

public class VideoPlayerManager implements ITXVodPlayListener {

    private static final String TAG = "VideoPlayerManager";
    private static TXVodPlayer mTXVodPlayer;
    private final static int RETRY_COUNT = 3;
    private IVideoPlayerListener mIVideoPlayerListener;
    private int mVideoId;
    private AudioMngHelper mAudioMngHelper;

    private VideoPlayerManager() {
        mTXVodPlayer = new TXVodPlayer(Utils.getApp());
        mTXVodPlayer.setVodListener(this);
        mAudioMngHelper = new AudioMngHelper(Utils.getApp());
    }

    public static VideoPlayerManager getInstance() {
        return MediaPlayerManagerHolder.MEDIA_PLAYER_MANAGER;
    }

    public  void startPlay(String url, TXCloudVideoView txCloudVideoView, int videoId){
        if (null != mTXVodPlayer) {
            mTXVodPlayer.setPlayerView(txCloudVideoView);
            mTXVodPlayer.startPlay(url);
            mTXVodPlayer.setConfig(getTxVodPlayConfig(url));
            mVideoId = videoId;
        }
        if (null != mIVideoPlayerListener) {
            mIVideoPlayerListener.startPrepare();
        }
    }

    /**
     * 暂停播放
     */
    public  void pausePlay() {
        if (null != mTXVodPlayer) {
            mTXVodPlayer.pause();
        }
        if (null != mIVideoPlayerListener) {
            mIVideoPlayerListener.onPause();
        }
    }

    /**
     * 继续播放
     */
    public  void resumePlay() {
        if (null != mTXVodPlayer) {
            mTXVodPlayer.resume();
        }
        if (null != mIVideoPlayerListener) {
            mIVideoPlayerListener.onResume();
        }
    }

    /**
     * 停止播放
     * @param complete
     */
    public void stopPlay(boolean complete) {
        if (null != mTXVodPlayer) {
            mTXVodPlayer.stopPlay(complete);
        }
        if (null != mIVideoPlayerListener) {
            mIVideoPlayerListener.onStop();
        }
    }

    public boolean isPlaying() {
        if (null != mTXVodPlayer) {
            return mTXVodPlayer.isPlaying();
        }
        return false;
    }

    /**
     * 播放事件通知
     * @param txVodPlayer
     * @param event 事件id.id类型请参考 播放事件列表.
     * @param bundle 事件相关的参数.(key,value)格式,其中key请参考 事件参数.
     */
    @Override
    public void onPlayEvent(TXVodPlayer txVodPlayer, int event, Bundle bundle) {
        String playEventLog = "receive event: " + event + ", " + bundle.toString();
        LogUtils.i(TAG, playEventLog);
        if (null == mIVideoPlayerListener) {
            return;
        }
        switch (event) {
            case PLAY_EVT_CHANGE_RESOLUTION:
                break;
            case PLAY_EVT_PLAY_LOADING:
                mIVideoPlayerListener.onLoading();
                break;
            //开始播放
            case PLAY_EVT_PLAY_BEGIN:
                //LogUtil.i(TAG, "position:" + "start!!!!");
                mIVideoPlayerListener.onPlaying();
                break;
            //第一帧视频播放
            case PLAY_EVT_RCV_FIRST_I_FRAME:
                mIVideoPlayerListener.onStart();
                break;
            case PLAY_EVT_PLAY_PROGRESS:
                mIVideoPlayerListener.onPlayingProgress(bundle);
                break;
            case PAUSE_FLAG_PAUSE_VIDEO:
                mIVideoPlayerListener.onStop();
                break;
            case PLAY_EVT_PLAY_END:
                mIVideoPlayerListener.onCompleted();
                break;
            case PLAY_ERR_FILE_NOT_FOUND:
                break;
            //点播网络重连
            case PLAY_WARNING_RECONNECT:
                mIVideoPlayerListener.onNetStatus(VideoEventListener.NET_RECONNECT);
                break;
            //网络断开，播放错误
            case PLAY_ERR_NET_DISCONNECT:
                mIVideoPlayerListener.onNetStatus(VideoEventListener.NET_DISCONNECT);
                break;
            default:
                break;

        }
    }

    /**
     * 网络状态通知
     * @param txVodPlayer
     * @param bundle 通知的内容.(key,value)格式,其中key请参考 网络状态通知.
     */
    @Override
    public void onNetStatus(TXVodPlayer txVodPlayer, Bundle bundle) {

    }

    public void setIMediaPlayerListener(IVideoPlayerListener listener) {
        mIVideoPlayerListener = listener;
    }

    private static TXVodPlayConfig getTxVodPlayConfig(String url) {
        return getTxVodPlayConfig(url, RETRY_COUNT);
    }

    private static TXVodPlayConfig getTxVodPlayConfig(String url, int retryCount) {
//        String videoLocalPath = null;
//        File file = new File(FileUtils.(FileUtils.FilePathType.CACHE_VIDEO));
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//        if (!FileUtils.fileIsExists(url)) {
//            videoLocalPath = file.getAbsolutePath();
//        }
        TXVodPlayConfig config = new TXVodPlayConfig();
//        config.setCacheFolderPath(videoLocalPath);
        config.setMaxCacheItems(10);
        config.setConnectRetryCount(retryCount);
        return config;
    }

    public int getVideoId() {
        return mVideoId;
    }

    private static class MediaPlayerManagerHolder {
        private static final VideoPlayerManager MEDIA_PLAYER_MANAGER = new VideoPlayerManager();
    }

    /**
     * 获取视频当前播放时间位置
     *
     * @return 播放时间位置
     */
    public int getCurrentPosition() {
        if (mTXVodPlayer != null) {
            return (int) mTXVodPlayer.getCurrentPlaybackTime();
        }
        return 0;
    }

    /**
     * 保存上一个播放视频的播放位置
     */
    public void saveLastVideoPlayPosition() {
        if (!isPlaying()) {
            return;
        }
//        VideoUtil.saveVideoPosition(VideoPlayControl.getVpControlInstance().getVid(), getCurrentPosition());
    }

    public void seekTo(int position) {
        if (null != mTXVodPlayer) {
            mTXVodPlayer.seek(position);
        }
    }

    public void seekTo(float position) {
        if (null != mTXVodPlayer) {
            mTXVodPlayer.seek(position);
        }
    }

    /**
     * 设置音量
     * ⑤静音按钮的说明：
     * a在小窗模式播放视频时，静音按钮同手机音量大小保持一致，如果手机是静音则为静音状态，如果手机为有声则视频播放有声。
     * b如果手机为静音状态，小窗播放视频时，点击静音按钮，将声音调为默认值（1/3音量）
     * c如果手机为有声状态音量为X，小窗播放视频时，点击静音按钮，将声音设置为0。再一次点击静音按钮，恢复为有声状态的音量X。
     * d通过物理音量键改变手机音量时，静音图标会随着音量自动变化。
     * e全屏模式下没有禁音按钮
     *
     * @param mute
     */
    public void audioToggle(boolean mute) {
        if (mTXVodPlayer != null) {
            mTXVodPlayer.setMute(mute);
        }
        //打开声音
        if (!mute) {
            if (mAudioMngHelper != null) {
                int currentVoice = mAudioMngHelper.get100CurrentVolume();
                if (currentVoice <= 0) {
                    //设置音量1/3
                    mAudioMngHelper.setVoice100(33);
                }
            }
        }
    }

    /**
     * 手机系统音量是否是静音
     *
     * @return true 静音 false 不是静音
     */
    public boolean isAudioMute() {
        if (mAudioMngHelper != null) {
            return mAudioMngHelper.getSystemCurrentVolume() <= 0;
        }
        return false;
    }

    /**
     * 网络提醒时候，停止视频
     */
    public void stopMobile() {
//        VideoUtil.saveVideoPosition(VideoPlayControl.getVpControlInstance().getVid(), getCurrentPosition());
        if (mTXVodPlayer != null) {
            mTXVodPlayer.stopPlay(true);
        }
    }

    /**
     * 网络提醒时候，停止视频
     */
    public void stopMobile(int currentDuration) {
        if (currentDuration <= 0) {
            stopMobile();
            return;
        }
//        VideoUtil.saveVideoPosition(VideoPlayControl.getVpControlInstance().getVid(), currentDuration);
        if (mTXVodPlayer != null) {
            mTXVodPlayer.stopPlay(true);
        }
    }
}
