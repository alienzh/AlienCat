package com.zhangw.aliencat.video.videoplayer.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.blankj.utilcode.util.LogUtils;
import com.tencent.rtmp.TXLiveConstants;
import com.zhangw.aliencat.video.videoplayer.IVideoPlayerListener;
import com.zhangw.aliencat.video.videoplayer.PlayState;
import com.zhangw.aliencat.video.videoplayer.VideoPlayerManager;

import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;

/**
 * Created by chends on 2018/3/26.
 */
public class VideoPlayerViewWrapper extends VideoPlayerControlView implements IVideoPlayerListener {

    protected VideoPlayerManager mVideoPlayerManager;
    /**
     * 播放十秒事件
     */
    private boolean playTenSeconds;
    /**
     * 播放一半事件
     */
    private boolean playHalf;
    /**
     * 试看一分钟 视频时长>= 5分钟的，  可以试看一分钟  视频时长<5分钟的， 按视频时长的20%试看
     */
    private boolean playOneMinute;
    /**
     * 播放计时器
     */
    private ScheduledExecutorService mPlayerScheduleExecutorService = new ScheduledThreadPoolExecutor(1);
    private ScheduledFuture mRunningScheduledFuture;

    public VideoPlayerViewWrapper(@NonNull Context context) {
        super(context);
        initView();
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        super.initView();
        mVideoPlayerManager = VideoPlayerManager.getInstance();
        setListVideoView();
    }

    /**
     * 设置view为列表视频播放器视图
     */
    public void setListVideoView() {
        setIsShowBottomProgress(false);
    }

    @Override
    public void toggleMuteSetting(boolean isMute) {
        VideoPlayerManager.getInstance().audioToggle(isMute);
    }

    @Override
    public void seekSetting(int position) {
        mVideoPlayerManager.seekTo(position);
    }

    @Override
    public void stopMobile(int duration) {
        mVideoPlayerManager.stopMobile(duration);
    }

    public VideoPlayerViewWrapper(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public VideoPlayerViewWrapper(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void setDetailVideoView() {
        setIsShowBottomProgress(true);
    }

    @Override
    public void fullscreenSetting(boolean trigger) {

    }

    @Override
    public void startPlay() {
        if (null == mVideoPlayerManager) {
            mVideoPlayerManager = VideoPlayerManager.getInstance();
        }
        mVideoPlayerManager.saveLastVideoPlayPosition();
        mVideoPlayerManager.setIMediaPlayerListener(this);
        mVideoPlayerManager.startPlay(mVideoDetails.getVideo_url(), getTXCloudVideoView(), mVideoDetails.getVid());
    }

    @Override
    public void resumePlay() {
        VideoPlayerManager.getInstance().resumePlay();
    }

    @Override
    public void replayPlay() {
        mVideoPlayerManager.setIMediaPlayerListener(this);
        mVideoPlayerManager.startPlay(mVideoDetails.getVideo_url(), getTXCloudVideoView(), mVideoDetails.getVid());
    }

    @Override
    public void pausePlayer() {
        mVideoPlayerManager.pausePlay();
    }

    @Override
    public boolean isPlaying() {
        return mVideoPlayerManager.isPlaying();
    }

    @Override
    public void startPrepare() {
        setState(PlayState.READY);
    }

    @Override
    public void onPrepared() {

    }

    @Override
    public void onLoading() {
        setState(PlayState.LOADING);
    }

    @Override
    public void onStart() {
        setStartPlayValue(true);
        setCanSeek(true);
        startPlayTimeTimer();
    }

    @Override
    public void onPlaying() {
        setState(PlayState.PLAY);
        setIsShowControlView(true);
        doToggleMuteButton(mVideoPlayerManager.isAudioMute());
    }

    @Override
    public void onPlayingProgress(Bundle bundle) {
        if (isSeekbarDragging) {
            return;
        }
        mCacheDuration = bundle.getInt(TXLiveConstants.EVT_PLAYABLE_DURATION_MS);
        mTotalDuration = bundle.getInt(TXLiveConstants.EVT_PLAY_DURATION_MS);
        mCurrentDuration = bundle.getInt(TXLiveConstants.EVT_PLAY_PROGRESS_MS);
    }

    @Override
    public void onPause() {
        setState(PlayState.PAUSE);
        stopPlayTimeTimer();
    }

    @Override
    public void onStop() {
        setState(PlayState.STOP);
    }

    @Override
    public void onCompleted() {
        setState(PlayState.COMPLETE);
        stopPlayTimeTimer();
        videoPlayComplete();
    }

    @Override
    public void onResume() {
        setState(PlayState.PLAY);
        startPlayTimeTimer();
    }

    @Override
    public void onSeekCompleted() {
        setState(PlayState.PLAY);
    }

    @Override
    public void onNetStatus(int status) {

    }

    /**
     * 取消定时器
     */
    public void stopPlayTimeTimer() {
        if (null != mRunningScheduledFuture) {
            mRunningScheduledFuture.cancel(true);
        }
    }

    /**
     * 定时器，连续播放时长
     */
    public void startPlayTimeTimer() {
        if (null != mRunningScheduledFuture) {
            mRunningScheduledFuture.cancel(true);
        }
        mRunningScheduledFuture = mPlayerScheduleExecutorService.scheduleAtFixedRate(getPlayerProgressTask(), 0, 1000, TimeUnit.MILLISECONDS);
    }

    /**
     * 进度条计时器
     *
     * @return
     */
    private TimerTask getPlayerProgressTask() {
        return new TimerTask() {
            @Override
            public void run() {
                if (mTotalDuration <= 0) {
                    return;
                }
                int position = mCurrentDuration;
                LogUtils.i(TAG, "current position:" + position);
                int duration = mTotalDuration;
                if (pbVideoPlay != null && isShowBottomProgress) {
                    long pos = 1000L * position / duration;
                    pbVideoPlay.setProgress((int) pos);
                    if (!playTenSeconds && duration > 0 && duration < 10000) {
                        //如果视频总时长小于10秒
                        playTenSeconds = true;
                        playHalf = true;
                        //TODO 埋点统计
                        //mVideoEventListener.onPlayTenSecondsVideo();
                    }
                }
                if (!playTenSeconds && position < 11000 && position > 9000) {
                    //统计播放十秒
                    int totalSeconds = position / 1000;
                    if (totalSeconds == 10) {
                        playTenSeconds = true;
                        //TODO 埋点统计
                        //mVideoEventListener.onPlayTenSecondsVideo();
                    }
                }
                if (!playHalf && position < (duration + 1000) / 2
                        && position > (duration - 1000) / 2
                        && position > 10000) { //统计播放一半
                    int totalSeconds = position / 1000;
                    if (totalSeconds > 10) {
                        playHalf = true;
                        //TODO 埋点统计
                        //mVideoEventListener.onPlayHalfVideo();
                    }
                }
                //seekbar 不能滑动说明是试看视频，试看一分钟视频的才回调
                if (!canSeek) {
                    if (duration >= 5 * 60000) {
                        //视频时长>= 5分钟的，  可以试看一分钟
                        if (/*!playOneMinute &&*/ position < 61000 && position > 59000) {
                            int totalSeconds = position / 1000;
                            if (totalSeconds == 60) {
                                playOneMinute = true;
                                //TODO 埋点统计
                                //mVideoEventListener.onVideoLookMinute();
                            }
                        }
                    } else {
                        //视频时长<5分钟的， 按视频时长的20%试看
                        int per20 = duration / 5;
                        LogUtils.i(TAG, "playCharge:  " + "playOneMinute flag:" + playOneMinute);
                        LogUtils.i(TAG, "playCharge:  " + "playposition:" + position + "--per20" + per20);
                        if (/*!playOneMinute &&*/ position < per20 + 1000 && position > per20 - 1000) {
                            playOneMinute = true;
                            //TODO 埋点统计
                            //mVideoEventListener.onVideoLookMinute();
                        }
                    }
                }
                //如果在播放状态
//                if (isPlaying()) {
//                    //TODO replace
//                    MConstants.videoPlayTotalTime++;
//                    MConstants.videoPlayTime++;
//                }
            }
        };
    }


}

