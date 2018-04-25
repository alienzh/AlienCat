package com.zhangw.aliencat.video.videoplayer.ui;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.zhangw.aliencat.R;
import com.zhangw.aliencat.video.CommonUtil;
import com.zhangw.aliencat.video.NetInfoModule;
import com.zhangw.aliencat.video.videoplayer.listener.IVideoPlayerListener;
import com.zhangw.aliencat.video.videoplayer.listener.VideoAllCallBack;
import com.zhangw.aliencat.video.videoplayer.VideoViewBridge;

import butterknife.BindView;

/**
 * @author chends
 * @date 2018/3/26.
 * 视频回调与状态处理等相关层
 */
public abstract class VideoPlayerStateView extends BaseVideoPlayerView implements IVideoPlayerListener {

    //正常
    public static final int STATE_NORMAL = 0xF0;
    //预备中
    public static final int STATE_PREPAREING = 0xF1;
    //播放中
    public static final int STATE_PLAYING = 0xF2;
    //开始缓存
    public static final int STATE_BUFFERING_START = 0xF3;
    //暂停
    public static final int STATE_PAUSE = 0XF4;
    //自动播放结束
    public static final int STATE_AUTO_COMPLETE = 0xF5;
    //错误状态
    public static final int STATE_ERROR = 0XF6;

    /**
     * 避免切换时频繁
     */
    public static final int CHANGE_DELAY_TIME = 2000;
    /**
     * 头部遮罩
     */
    @BindView(R.id.fl_operation)
    public ConstraintLayout clTopView;
    /**
     * 返回键
     */
    @BindView(R.id.ivVideoBack)
    public ImageView ivVideoBack;
    /**
     * 视频描述
     */
    @BindView(R.id.tvVideoDes)
    public TextView tvVideoDes;
    /**
     * 视频背景
     */
    @BindView(R.id.ivVideoBackground)
    public ImageView ivVideoBackground;
    /**
     * 暂停
     */
    @BindView(R.id.ibVideoPause)
    public ImageButton ibVideoPause;
    @BindView(R.id.pbVideoLoading)
    public ProgressBar pbVideoLoading;
    /**
     * 当前的播放状态
     */
    protected int mCurrentState = -1;
    /**
     * 播放的tag，防止错误，因为普通的url也可能重复
     */
    protected int mPlayPosition = -22;
    /**
     * 缓存进度
     */
    protected int mBuffterPoint;
    /**
     * 是否发送了网络改变
     */
    protected boolean mNetChanged = false;
    /**
     * 从哪个开始播放
     */
    protected long mSeekOnStart = -1;
    /**
     * 保存切换时的时间，避免频繁契合
     */
    protected long mSaveChangeViewTIme = 0;
    /**
     * 当前的播放位置
     */
    protected long mCurrentPosition;

    /**
     * 是否播放过
     */
    protected boolean mHadPlay = false;
    //是否播放器当失去音频焦点
    protected boolean mReleaseWhenLossAudio = true;

    //音频焦点的监听
    protected AudioManager mAudioManager;
    /**
     * 当前是否全屏
     */
    protected boolean mIfCurrentIsFullscreen = false;
    /**
     * 是否准备完成前调用了暂停
     */
    protected boolean mPauseBeforePrepared = false;
    /**
     * Prepared之后是否自动开始播放
     */
    protected boolean mStartAfterPrepared = true;
    /**
     * Prepared
     */
    protected boolean mHadPrepared = false;
    /**
     * 上下文
     */
    protected Context mContext;
    /**
     * 原来的url
     */
    protected String mOriginUrl;
    /**
     * 转化后的URL
     */
    protected String mUrl;
    /**
     * 标题
     */
    protected String mTitle;
    /**
     * 网络状态
     */
    protected String mNetSate = "NORMAL";
    //屏幕宽度
    protected int mScreenWidth;

    //屏幕高度
    protected int mScreenHeight;

    /**
     * 视频回调
     */
    protected VideoAllCallBack mVideoAllCallBack;
    /**
     * 网络监听
     */
    protected NetInfoModule mNetInfoModule;
    /**
     * 监听是否有外部其他多媒体开始播放
     */
    protected AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    post(new Runnable() {
                        @Override
                        public void run() {
                            if (mReleaseWhenLossAudio) {
                                releaseVideos();
                            } else {
                                onPause();
                            }
                        }
                    });
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    try {
                        onPause();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    break;
                default:
                    break;
            }
        }
    };

    public VideoPlayerStateView(@NonNull Context context) {
        super(context);
        inflateView();
    }

    private void inflateView() {
        mScreenWidth = getActivityContext().getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = getActivityContext().getResources().getDisplayMetrics().heightPixels;
        mAudioManager = (AudioManager) getActivityContext().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

        mLayoutInflater.inflate(R.layout.videoplayer_state_view, this);
    }

    protected Context getActivityContext() {
        return CommonUtil.getActivityContext(getContext());
    }

    public VideoPlayerStateView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflateView();
    }

    public VideoPlayerStateView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflateView();
    }

    /**
     * 隐藏标题
     */
    public abstract void hideTitle();

    /**
     * 提示网络变化
     *
     * @param showFlag true 显示网络提示框
     * @param noneNet  true 无网络  false 手机网络
     */
    public abstract void showNetPromptDialog(boolean showFlag, boolean noneNet);

    /**
     * @param showLoading 是否显示加载动画
     */
    public void setLoadingAnim(boolean showLoading) {

    }

    /**
     * 更新state
     * 比如需要回调到列表中
     *
     * @param state 状态
     */
    public abstract void updateState(int state);

    /**
     * 视频分享
     *
     * @param platform 分享类型<>qq QQ,微信 Wechat,朋友圈 WechatMoments</>
     */
    public abstract void shareVideo(String platform);

    /**
     * 重播
     */
    public abstract void replayPlay();

    /************************* 需要继承处理部分 *************************/

    /**
     * 是否全屏
     */
    public boolean isIfCurrentIsFullscreen() {
        return mIfCurrentIsFullscreen;
    }

    /**
     * 退出全屏
     *
     * @return 是否在全屏界面
     */
    protected abstract boolean backFromFull(Context context);

    /**
     * 当前UI
     */
    public abstract int getLayoutId();

    @Override
    public void onPrepared() {
        if (mCurrentState != STATE_PREPAREING) {
            return;
        }
        mHadPrepared = true;

        if (mVideoAllCallBack != null && isCurrentMediaListener()) {
            LogUtils.d(LOG_TAG, "onPrepared");
            mVideoAllCallBack.onPrepared(mOriginUrl, mTitle, this);
        }

        if (!mStartAfterPrepared) {
            setStateAndUi(STATE_PAUSE);
            return;
        }

        startAfterPrepared();
    }

    protected boolean isCurrentMediaListener() {
        return getVideoManager().listener() != null
                && getVideoManager().listener() == this;
    }

    /**
     * 设置播放显示状态
     *
     * @param state
     */
    protected abstract void setStateAndUi(int state);

    /**
     * prepared成功之后会开始播放
     */
    public void startAfterPrepared() {
        if (!mHadPrepared) {
            prepareVideo();
        }

        try {
            if (getVideoManager().getMediaPlayer() != null) {
                getVideoManager().getMediaPlayer().resume();
            }

            setStateAndUi(STATE_PLAYING);

            if (getVideoManager().getMediaPlayer() != null && mSeekOnStart > 0) {
                getVideoManager().getMediaPlayer().seek(mSeekOnStart);
                mSeekOnStart = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        createNetWorkState();

        listenerNetWorkState();

//        mHadPlay = true;

        if (mTXCloudVideoView != null) {
            mTXCloudVideoView.onResume();
        }

        if (mPauseBeforePrepared) {
            onPause();
            mPauseBeforePrepared = false;
        }
    }

    /**
     * 获取管理器桥接的实现
     */
    public abstract VideoViewBridge getVideoManager();

    /**
     * 开始状态视频播放
     */
    protected void prepareVideo() {
        startPrepare();
    }

    /**
     * 创建网络监听
     */
    protected void createNetWorkState() {
        if (mNetInfoModule == null) {
            mNetInfoModule = new NetInfoModule(getActivityContext().getApplicationContext(), new NetInfoModule.NetChangeListener() {
                @Override
                public void changed(String state) {
                    if (!mNetSate.equals(state)) {
                        LogUtils.d(LOG_TAG, "******* change network state ******* " + state);
                        mNetChanged = true;
                    }
                    mNetSate = state;
                }
            });
            mNetSate = mNetInfoModule.getCurrentConnectionType();
        }
    }

    /**
     * 监听网络状态
     */
    protected void listenerNetWorkState() {
        if (mNetInfoModule != null) {
            mNetInfoModule.onHostResume();
        }
    }

    /**
     * 暂停状态
     */
    @Override
    public void onPause() {
        if (mCurrentState == STATE_PREPAREING) {
            mPauseBeforePrepared = true;
        }
        try {
            if (getVideoManager().getMediaPlayer() != null &&
                    getVideoManager().getMediaPlayer().isPlaying()) {
                setStateAndUi(STATE_PAUSE);
                mCurrentPosition = (long) getVideoManager().getMediaPlayer().getCurrentPlaybackTime();
                if (getVideoManager().getMediaPlayer() != null) {
                    getVideoManager().getMediaPlayer().pause();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCompleted() {
        //make me normal first
        setStateAndUi(STATE_NORMAL);

        mSaveChangeViewTIme = 0;

//        if (mTextureViewContainer.getChildCount() > 0) {
//            mTextureViewContainer.removeAllViews();
//        }

        if (!mIfCurrentIsFullscreen) {
            getVideoManager().setListener(null);
            getVideoManager().setLastListener(null);
        }
//        getVideoManager().setCurrentVideoHeight(0);
//        getVideoManager().setCurrentVideoWidth(0);

        mAudioManager.abandonAudioFocus(onAudioFocusChangeListener);
        ((Activity) getActivityContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        releaseNetWorkState();

    }

    /**
     * 恢复暂停状态
     */
    @Override
    public void onResume() {
        onResume(true);
    }

    /**
     * 恢复暂停状态
     *
     * @param seek 是否产生seek动作
     */
    @Override
    public void onResume(boolean seek) {
        mPauseBeforePrepared = false;
        if (mCurrentState == STATE_PAUSE) {
            try {
                if (mCurrentPosition > 0 && getVideoManager().getMediaPlayer() != null) {
                    if (seek) {
                        getVideoManager().getMediaPlayer().seek(mCurrentPosition);
                    }
                    getVideoManager().getMediaPlayer().resume();
                    setStateAndUi(STATE_PLAYING);
                    if (mAudioManager != null && !mReleaseWhenLossAudio) {
                        mAudioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                    }
                    mCurrentPosition = 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSeekCompleted() {

    }

    @Override
    public void onError(int what, int extra) {

        if (mNetChanged) {
            mNetChanged = false;
            netWorkErrorLogic();
            if (mVideoAllCallBack != null) {
                mVideoAllCallBack.onPlayError(mOriginUrl, mTitle, this);
            }
            return;
        }

        if (what != 38 && what != -38) {
            setStateAndUi(STATE_ERROR);
//            deleteCacheFileWhenError();
            if (mVideoAllCallBack != null) {
                mVideoAllCallBack.onPlayError(mOriginUrl, mTitle, this);
            }
        }
    }

    /**
     * 处理因切换网络而导致的问题
     */
    protected void netWorkErrorLogic() {
        final long currentPosition = getCurrentPositionWhenPlaying();
        LogUtils.d(LOG_TAG, "******* Net State Changed. renew player to connect *******" + currentPosition);
        getVideoManager().releaseMediaPlayer();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                setSeekOnStart(currentPosition);
                startPlayLogic();
            }
        }, 500);
    }

    /**
     * 获取当前播放进度
     */
    public int getCurrentPositionWhenPlaying() {
        int position = 0;
        if (mCurrentState == STATE_PLAYING || mCurrentState == STATE_PAUSE) {
            try {
                position = (int) getVideoManager().getMediaPlayer().getCurrentPlaybackTime();
            } catch (Exception e) {
                e.printStackTrace();
                return position;
            }
        }
        if (position == 0 && mCurrentPosition > 0) {
            return (int) mCurrentPosition;
        }
        return position;
    }

    /**
     * 从哪里开始播放
     * 目前有时候前几秒有跳动问题，毫秒
     * 需要在startPlayLogic之前，即播放开始之前
     */
    public void setSeekOnStart(long seekOnStart) {
        this.mSeekOnStart = seekOnStart;
    }

    /**
     * 开始播放
     */
    public abstract void startPlayLogic();

    @Override
    public void onInfo(int what, int extra) {
//        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
//            mBackUpPlayingBufferState = mCurrentState;
//            //避免在onPrepared之前就进入了buffering，导致一只loading
//            if (mHadPlay && mCurrentState != CURRENT_STATE_PREPAREING && mCurrentState > 0)
//                setStateAndUi(CURRENT_STATE_PLAYING_BUFFERING_START);
//
//        } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
//            if (mBackUpPlayingBufferState != -1) {
//                if (mBackUpPlayingBufferState == CURRENT_STATE_PLAYING_BUFFERING_START) {
//                    mBackUpPlayingBufferState = CURRENT_STATE_PLAYING;
//                }
//                if (mHadPlay && mCurrentState != CURRENT_STATE_PREPAREING && mCurrentState > 0)
//                    setStateAndUi(mBackUpPlayingBufferState);
//
//                mBackUpPlayingBufferState = -1;
//            }
//        } else if (what == IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED) {
//            mRotate = extra;
//            Debuger.printfLog("Video Rotate Info " + extra);
//            if (mTextureView != null)
//                mTXCloudVideoView.setRotation(mRotate);
//        }
    }

//    @Override
//    public void onAutoCompletion() {
//        setStateAndUi(CURRENT_STATE_AUTO_COMPLETE);
//
//        mSaveChangeViewTIme = 0;
//
//        if (mTextureViewContainer.getChildCount() > 0) {
//            mTextureViewContainer.removeAllViews();
//        }
//
//        if (!mIfCurrentIsFullscreen)
//            getGSYVideoManager().setLastListener(null);
//        mAudioManager.abandonAudioFocus(onAudioFocusChangeListener);
//        ((Activity) getActivityContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//
//        releaseNetWorkState();
//
//        if (mVideoAllCallBack != null && isCurrentMediaListener()) {
//            Debuger.printfLog("onAutoComplete");
//            mVideoAllCallBack.onAutoComplete(mOriginUrl, mTitle, this);
//        }
//    }

    /**
     * 释放网络监听
     */
    protected void releaseNetWorkState() {
        if (mNetInfoModule != null) {
            mNetInfoModule.onHostPause();
            mNetInfoModule = null;
        }
    }

    /**
     * 播放错误的时候，删除缓存文件
     */
    protected void deleteCacheFileWhenError() {
//        clearCurrentCache();
//        Debuger.printfError("Link Or mCache Error, Please Try Again " + mOriginUrl);
//        if (mCache) {
//            Debuger.printfError("mCache Link " + mUrl);
//        }
//        mUrl = mOriginUrl;
    }

    /**
     * 取消网络监听
     */
    protected void unListenerNetWorkState() {
        if (mNetInfoModule != null) {
            mNetInfoModule.onHostPause();
        }
    }

    /**
     * 开始播放逻辑
     */
    protected void startButtonLogic() {
        if (mVideoAllCallBack != null && mCurrentState == STATE_NORMAL) {
//            Debuger.printfLog("onClickStartIcon");
            mVideoAllCallBack.onClickStartIcon(mOriginUrl, mTitle, this);
        } else if (mVideoAllCallBack != null) {
//            Debuger.printfLog("onClickStartError");
            mVideoAllCallBack.onClickStartError(mOriginUrl, mTitle, this);
        }
        prepareVideo();
    }

//    @Override
//    public void onVideoSizeChanged() {
//        int mVideoWidth = getGSYVideoManager().getCurrentVideoWidth();
//        int mVideoHeight = getGSYVideoManager().getCurrentVideoHeight();
//        if (mVideoWidth != 0 && mVideoHeight != 0 && mTextureView != null) {
//            mTXCloudVideoView.requestLayout();
//        }
//    }

    /**
     * 获取当前总时长
     */
    public int getDuration() {
        int duration = 0;
        try {
            duration = (int) getVideoManager().getMediaPlayer().getDuration();
        } catch (Exception e) {
            e.printStackTrace();
            return duration;
        }
        return duration;
    }

    /**
     * 释放吧
     */
    public void release() {
        mSaveChangeViewTIme = 0;
        if (isCurrentMediaListener() &&
                (System.currentTimeMillis() - mSaveChangeViewTIme) > CHANGE_DELAY_TIME) {
            releaseVideos();
        }
    }

    /**
     * 释放播放器
     */
    protected abstract void releaseVideos();
}
