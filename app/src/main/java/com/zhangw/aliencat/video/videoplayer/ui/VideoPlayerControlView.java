package com.zhangw.aliencat.video.videoplayer.ui;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.format.Formatter;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.zhangw.aliencat.R;
import com.zhangw.aliencat.subutil.ViewUtil;
import com.zhangw.aliencat.video.VideoDetails;
import com.zhangw.aliencat.video.videoplayer.FullscreenCallback;
import com.zhangw.aliencat.video.videoplayer.PlayState;
import com.zhangw.aliencat.video.videoplayer.VideoEventListener;
import com.zhangw.aliencat.video.videoplayer.VideoListEventListener;

import java.lang.ref.WeakReference;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by chends on 2018/3/26.
 */

public abstract class VideoPlayerControlView extends VideoPlayerStateView {

    public static final String TAG = "VideoPlayerControlView";
    /**
     * When the playback controls are shown, hide them after DEFAULT_TIMEOUT_MS milliseconds.
     */
    private static final int DEFAULT_TIMEOUT_MS = 5000;
    /**
     * 刚开始播放全屏按钮显示时间
     */
    private static final int FULLSCREEN_BUTTON_TIMEOUT_MS = 3000;
    /**
     * When the controls are hidden, they fade out in FADE_OUT_DURATION_MS milliseconds.
     */
    private static final int FADE_OUT_DURATION_MS = 200;
    private static final int FADE_OUT = 1;
    private static final int SHOW_PROGRESS = 2;
    private static final int HIDE_TITLE = 3;
    /**
     * 显示全屏按钮
     */
    private static final int HIDE_FULLSCREEN = 4;
    /**
     * 消失时长
     */
    private final int DISAPPEAR_TIME = 5000;
    /**
     * Displays the elapsed time into video.
     */
    @BindView(R.id.time_current)
    public TextView currentTime;
    /**
     * Displays the duration of the video.
     */
    @BindView(R.id.time_duration)
    public TextView endTime;
    /**
     * Makes player  enter or leave fullscreen. This button is not displayed unless there is a
     * associated with this object.
     */
    @BindView(R.id.fullscreen)
    public ImageButton fullscreenButton;
    /**
     * 静音按钮
     */
    @BindView(R.id.videoMute)
    public ImageButton videoMuteButton;
    /**
     * 暂停背景遮罩
     */
    @BindView(R.id.flPauseBg)
    public FrameLayout allSectionBg;
    /**
     * Displays a track and a thumb which can be used to seek to different time points in the video.
     */
    @BindView(R.id.mediacontroller_progress)
    public SeekBar seekBar;
    /**
     * The view created by this
     */
    @BindView(R.id.flPlaybackControl)
    public FrameLayout flPlaybackControl;
    /**
     * This is the root view which contains all other views that make up the playback control layer.
     * It can be tinted by setting the chrome color.
     */
    @BindView(R.id.bottom_chrome)
    public LinearLayout playbackControlRootView;
    /**
     * 底部进度，静音等
     */
    @BindView(R.id.bottom_option)
    public LinearLayout bottomOption;
    /**
     * Video title displayed in the left of the top chrome.
     */
    @BindView(R.id.video_title)
    public TextView videoTitleView;
    /**
     * 视频播放进度
     */
    @BindView(R.id.pbVideoPlay)
    public ProgressBar pbVideoPlay;
    /**
     * 播放完成黑色背景
     */
    @BindView(R.id.ivBlackBg)
    public ImageView ivBlackBg;
    public int playVid;
    /**
     * Whether the seekbar is currently being dragged.
     */
    protected boolean isSeekbarDragging;
    /**
     * This is the layout of the FrameLayoutContainer before fullscreen mode has been entered.
     * When we leave fullscreen mode, we restore the layout of the FrameLayoutContainer to this layout.
     */
    protected ViewGroup.LayoutParams originalContainerLayoutParams;
    /**
     * Whether the user can drag the seek bar thumb to seek.
     */
    protected boolean canSeek;
    /**
     * 当前视频缓存的时长，单位毫秒
     */
    protected int mCacheDuration;
    /**
     * 当前视频总时长，单位毫秒
     */
    protected int mTotalDuration;
    /**
     * 当前视频播放的时长，单位毫秒
     */
    protected int mCurrentDuration;
    /**
     * 是否显示视频最底部播放进度
     * true显示
     * false不显示
     */
    protected boolean isShowBottomProgress;
    protected VideoDetails mVideoDetails;
    /**
     * This callback is triggered when going to fullscreen and returning from fullscreen.
     */
    private FullscreenCallback fullscreenCallback;
    /**
     * The message handler which deals with displaying progress and fading out the media controls
     * We use it so that we can make the view fade out after a timeout (by sending a delayed message).
     */
    private Handler handler = new MessageHandler(this);
    private GestureDetector mGestureDetector;
    /**
     * 是否静音 默认不静音
     */
    private boolean isVideoMute;
    /**
     * Whether the play button has been pressed and the video should be playing.
     * We include this variable because the video may pause when buffering must occur. Although
     * the video will usually resume automatically when the buffering is complete, there are instances
     * (i.e. ad playback), where it will not resume automatically. So, if we detect that the video is
     * paused after buffering and should be playing, we can resume it programmatically.
     */
    private boolean shouldBePlaying;
    /**
     * Encodes the HH:MM:SS or MM:SS time format.
     */
    private StringBuilder timeFormat;
    /**
     * Formats times to HH:MM:SS or MM:SS form.
     */
    private java.util.Formatter timeFormatter;
    /**
     * The title displayed in the
     */
    private String videoTitle;
    /**
     * Saved orientation for coming back from fullscreen.
     */
    private int savedOrientation;
    /**
     * 视频播放器高
     */
    private int containerHeight;
    private AnimationDrawable pauseAnim;
    /**
     * Whether the playback control layer is visible.
     */
    private boolean isVisible;
    /**
     * Whether the playback control layer is currently in the process of fading out.
     */
    private boolean isFadingOut;
    private int lastState;
    //开始播放
    private boolean startPlay;
    private VideoEventListener mVideoEventListener;
    /**
     * 是否显示视频播放控制界面（播放、暂停按钮，进度拖动等）
     * true显示
     * false不显示
     */
    private boolean isShowControlView;
    /**
     * States are idle, prepared, buffering, ready, or ended. This is an integer (instead of an enum)
     * because the Exoplayer library uses integers.
     */
    private int lastReportedPlaybackState;
    private Activity mActivity;
    private int ownerUid;
    private int videoHeight;
    private String pos;
    /**
     * 当前视频在列表中的位置
     */
    private int mPosition;

    /**
     * 开始加载视频
     */
    private long startTime;


    private VideoListEventListener mVideoListEventListener;


    public VideoPlayerControlView(@NonNull Context context) {
        super(context);
        inflateView();
    }

    private void inflateView() {
        mLayoutInflater.inflate(R.layout.videoplayer_control_view, this);
    }

    public VideoPlayerControlView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflateView();
    }

    public VideoPlayerControlView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflateView();
    }

    @Override
    public void initView() {
        initGestureEvent();
        initPlayerControlView();
    }

    private void initGestureEvent() {
        mGestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                //不支持显示视频控制界面;没有回调开始播放,非wifi网络禁止双击
                if (!isShowControlView || !startPlay) {
                    return true;
                }
                show(true);
                return false;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                //不支持显示视频控制界面
                if (!isShowControlView) {
                    return false;
                }
                if (!isVisible) {
                    show();
                } else {
                    hide();
                }
                return false;
            }
        });
        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    private void initPlayerControlView() {
        fullscreenButton.setClickable(true);
        if (fullscreenCallback == null) {
            playbackControlRootView.setPadding(0, 0, ConvertUtils.dp2px(10), 0);
            fullscreenButton.setVisibility(View.GONE);
        } else {
            playbackControlRootView.setPadding(0, 0, 0, 0);
        }
        seekBar.setMax(1000);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromuser) {
                if (!fromuser) {
                    // Ignore programmatic changes to seek bar position.
                    // Ignore changes to seek bar position is seeking is not enabled.
                    return;
                }
                long newPosition = (mTotalDuration * progress) / 1000L / 1000L;
                if (!canSeek) {
                    //大于5分钟只能播放一分钟，小于五分钟可以播放20%
                    long canPlayPosition = mTotalDuration >= 5 * 60 * 1000L ? 60 : (long) (mTotalDuration * 0.2) / 1000L;
                    newPosition = newPosition >= canPlayPosition ? canPlayPosition : newPosition;
                }
                seekSetting((int) newPosition);
                //手动拖动进度条时重置当前进度
                mCurrentDuration = (int) (newPosition * 1000);
                if (currentTime != null) {
                    currentTime.setText(stringForTime((int) newPosition * 1000));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                show(0);
                isSeekbarDragging = true;
                handler.removeMessages(SHOW_PROGRESS);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isSeekbarDragging = false;
                updateProgress();
                updatePlayPauseButton();
                show(DEFAULT_TIMEOUT_MS);

                handler.sendEmptyMessage(SHOW_PROGRESS);
            }
        });
        videoTitleView.setText(videoTitle);
        pbVideoPlay.setVisibility(GONE);
        timeFormat = new StringBuilder();
        timeFormatter = new java.util.Formatter(timeFormat, Locale.getDefault());
        if (isFullscreen()) {
            fullscreenButton.setImageResource(R.drawable.video_full_screen_exit);
        } else {
            fullscreenButton.setImageResource(R.drawable.video_full_screen);
        }
        allSectionBg.setVisibility(isShowControlView ? View.INVISIBLE : View.GONE);
    }

    /**
     * Fragment中调用
     *
     * @param fragment
     * @param video
     */
    public void setVideoDetail(@NonNull Fragment fragment, @NonNull VideoDetails video) {
        this.setVideoDetail(fragment.getActivity(), video);
    }

    /**
     * Activity中调用
     *
     * @param activity
     * @param video
     */
    public void setVideoDetail(@NonNull Activity activity, @NonNull VideoDetails video) {
        mActivity = activity;
        mVideoDetails = video;
        playVid = video.getVid();
        ownerUid = (video == null ? 0 : video.getUid());
        videoHeight = ViewUtil.getVideoHeight(video.getWidth(), video.getHeight());
        logHeight(videoHeight, video.getDes());
        setVideoContainerHeight(videoHeight);
        //视频背景图
//        ImageLoadUtils.displayImage(activity, video.getImg_url(), ivVideoBackground, DefaultValue.IMAGE_LARGE_BG,
//                Screen.getInstance().widthPixels, videoHeight);
        getLayoutParams().height = videoHeight;
    }

    private void logHeight(int vh, String dec) {
        if (StringUtils.isEmpty(dec)) {
            LogUtils.d(TAG, "视频描述 = " + "空" + "; 视频高度 = " + vh);
        } else {
            LogUtils.d(TAG, "视频描述 = " + dec.substring(0, dec.length() >= 5 ? 5 : dec.length() - 1) + "; 视频高度 = " + vh);
        }
    }

    public void setVideoContainerHeight(int containerHeight) {
        getLayoutParams().height = containerHeight;
        ((ViewGroup) getParent()).getLayoutParams().width = FrameLayout.LayoutParams.MATCH_PARENT;
        ((ViewGroup) getParent()).getLayoutParams().height = containerHeight;
    }

    public void initTopView() {
        clTopView.setBackgroundResource(R.drawable.item_img_top_mask);
        clTopView.setVisibility(VISIBLE);
        tvVideoDes.setVisibility(VISIBLE);
        ivVideoBack.setVisibility(GONE);
        tvVideoLearnCount.setVisibility(VISIBLE);
    }


    /**
     * 网络提醒时停止播放视频
     *
     * @param duration
     */
    public abstract void stopMobile(int duration);

    /**
     * 在播放器无法返回进度时（出现网络异常）
     * 返回当前播放进度
     *
     * @return
     */
    public int getCurrentDuration() {
        return mCurrentDuration / 1000;
    }

    /**
     * 获取缓存百
     */
    public int getVideoBufferedPer() {
        if (mTotalDuration > 0) {
            return (int) (mCacheDuration * 100F / mTotalDuration);
        } else {
            return 0;
        }
    }

    public void hideTopView(boolean isHide) {
        clTopView.setBackgroundResource(R.color.transparent);
        if (isPlaying()) {
            clTopView.setVisibility(isHide ? INVISIBLE : VISIBLE);
            if (isFullscreen()) {
                ivVideoBack.setVisibility(VISIBLE);
                tvVideoDes.setVisibility(VISIBLE);
                tvVideoLearnCount.setVisibility(INVISIBLE);
            } else {
                ivVideoBack.setVisibility(GONE);
                tvVideoDes.setVisibility(VISIBLE);
                tvVideoLearnCount.setVisibility(VISIBLE);
            }
        } else {
            if (isFullscreen()) {
                clTopView.setVisibility(VISIBLE);
                ivVideoBack.setVisibility(VISIBLE);
                tvVideoDes.setVisibility(INVISIBLE);
                tvVideoLearnCount.setVisibility(INVISIBLE);
            } else {
                ivVideoBack.setVisibility(GONE);
                clTopView.setVisibility(isHide ? INVISIBLE : VISIBLE);
            }
        }
    }

    private void replayUpdateView() {
        pbVideoLoading.setVisibility(GONE);
        ibVideoPause.setVisibility(GONE);
    }

    @OnClick({R.id.fullscreen, R.id.videoMute})
    @Override
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        switch (view.getId()) {
            case R.id.fullscreen:
                //doToggleFullscreen();
                show(DEFAULT_TIMEOUT_MS);
                break;
            case R.id.videoMute:
                isVideoMute = !isVideoMute;
                toggleMuteSetting(isVideoMute);
                doToggleMuteButton(isVideoMute);
                break;
            default:
                break;
        }
    }

    /**
     * 隐藏头部标题
     */
    @Override
    public void hideTitle() {
        if (videoTitle != null) {
            hideTitle(DISAPPEAR_TIME);
        } else {
            videoTitleView.setVisibility(View.GONE);
        }
    }

    @Override
    public void shareVideo(String platform) {

    }

    /**
     * 提示网络变化
     *
     * @param showFlag true 显示网络提示框
     * @param noneNet  true 无网络  false 手机网络
     */
    @Override
    public void showNetPromptDialog(boolean showFlag, boolean noneNet) {
        // TODO: 2018/4/23
    }

    @Override
    public void updateState(int state) {
        if (null != mVideoListEventListener) {
            mVideoListEventListener.onPlayStateChange(state, mPosition);
        }
    }

    public void show(int timeout) {
        show(timeout, false);
    }

    /**
     * 设置静音
     */
    public abstract void toggleMuteSetting(boolean isMute);

    /**
     * 设置静音
     *
     * @param isMute
     */
    public void doToggleMuteButton(boolean isMute) {
        this.isVideoMute = isMute;
        if (isVideoMute) {
            videoMuteButton.setImageResource(R.drawable.video_mute);
        } else {
            videoMuteButton.setImageResource(R.drawable.video_voice_open);
        }
    }

    /**
     * Add the playback control layer back to the mFrameLayoutContainer.
     * The playback controls disappear after timeout milliseconds.
     *
     * @param timeout  Hide the view after timeout milliseconds. If timeout == 0, then the playback
     *                 controls will not disappear unless their mFrameLayoutContainer is tapped again.
     * @param animFlag 是否显示暂停播放动画
     */
    public void show(int timeout, boolean animFlag) {
        if (!isVisible) {
            playbackControlRootView.setAlpha(1.0f);
            allSectionBg.setVisibility(View.VISIBLE);
            bottomOption.setVisibility(VISIBLE);
            fullscreenButton.setVisibility(VISIBLE);
            if (mVideoEventListener != null && isPlaying()) {
                mVideoEventListener.onHide(false);
            }
            pbVideoPlay.setVisibility(View.INVISIBLE);
            updateProgress();
            // Add the view to the mFrameLayoutContainer again.
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER
            );
            removeView(flPlaybackControl);
            addView(flPlaybackControl, layoutParams);
            isVisible = true;
        }

        updatePlayPauseButton();
        handler.sendEmptyMessage(SHOW_PROGRESS);
        Message msg = handler.obtainMessage(FADE_OUT);
        handler.removeMessages(FADE_OUT);
        if (timeout > 0) {
            handler.sendMessageDelayed(msg, timeout);
        }
    }

    /**
     * Adjust the position of the action bar to reflect the progress of the video.
     */
    public int updateProgress() {
        if (isSeekbarDragging) {
            return 0;
        }
        if (seekBar != null && mTotalDuration > 0) {
            long pos = 1000L * mCurrentDuration / mTotalDuration;
            LogUtils.i(TAG, "current position:" + pos);
            seekBar.setProgress((int) pos);
            if (0 < mTotalDuration) {
                long secondaryProgress = 1000L * mCacheDuration / mTotalDuration;
                seekBar.setSecondaryProgress((int) secondaryProgress);
            }
        }
        if (endTime != null) {
            endTime.setText(stringForTime(mTotalDuration));
        }
        if (currentTime != null) {
            currentTime.setText(stringForTime(mCurrentDuration));
        }
        return mCurrentDuration;
    }

    /**
     * Format the milliseconds to HH:MM:SS or MM:SS format.
     */
    public String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        timeFormat.setLength(0);
        if (hours > 0) {
            return timeFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return timeFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    /**
     * 设置回调播放状态给视频列表
     *
     * @param listEventListener
     */
    public void setVideoListEventListener(VideoListEventListener listEventListener) {
        mVideoListEventListener = listEventListener;
    }

    public void setVideoEventListener(VideoEventListener listener) {
        mVideoEventListener = listener;
    }

    public void setIsShowBottomProgress(boolean showBottomProgress) {
        isShowBottomProgress = showBottomProgress;
        pbVideoPlay.setVisibility(isShowBottomProgress ? VISIBLE : GONE);
    }

    public void setCanSeek(boolean canSeek) {
        this.canSeek = canSeek;
    }

    public void setStartPlayValue(boolean startPlay) {
        this.startPlay = startPlay;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    /**
     * 显示全屏按钮
     */
    public void showFullButton() {
        allSectionBg.setVisibility(VISIBLE);
        ViewUtil.setBackground(getContext(), allSectionBg, R.color.transparent);
        playbackControlRootView.setAlpha(1.0f);
        bottomOption.setVisibility(INVISIBLE);
        fullscreenButton.setVisibility(VISIBLE);

        Message msg = handler.obtainMessage(HIDE_FULLSCREEN);
        handler.removeMessages(HIDE_FULLSCREEN);
        handler.sendMessageDelayed(msg, FULLSCREEN_BUTTON_TIMEOUT_MS);
    }

    /**
     * 隐藏播放按钮
     */
    private void hideFullscreen() {
        allSectionBg.setVisibility(GONE);
        ViewUtil.setBackground(getContext(), allSectionBg, R.drawable.video_play_pause_bg);
        bottomOption.setVisibility(VISIBLE);
        if (mVideoEventListener != null && isPlaying()) {
            mVideoEventListener.onHide(true);
        }
    }

    /**
     * Add the playback control layer back to the mFrameLayoutContainer. It will disappear when the user taps
     * the screen.
     */
    public void show() {
        show(DEFAULT_TIMEOUT_MS);
    }

    /**
     * 是否显示暂停播放动画
     *
     * @param animFlag
     */
    public void show(boolean animFlag) {
        show(DEFAULT_TIMEOUT_MS, animFlag);
    }

    protected void videoPlayComplete() {
        setPbVideoLoading(false);
        //隐藏控制层
        hide();
        pbVideoPlay.setProgress(0);
        setIsShowControlView(false);
        seekBar.setProgress(0);
        clearPlayerDuration();
    }

    protected void setPbVideoLoading(boolean isShowLoading) {
        if (state != PlayState.COMPLETE) {
            if (isShowLoading) {
                pbVideoLoading.setVisibility(VISIBLE);
            } else {
                pbVideoLoading.setVisibility(GONE);
                ivVideoBackground.setVisibility(GONE);
            }
        }
    }

    /**
     * Fades the playback control layer out and then removes it from the {@link }'s
     * mFrameLayoutContainer.
     */
    public void hide() {
        hideTitle();
        if (isFadingOut || !isVisible) {
            return;
        }
        isFadingOut = true;
        playbackControlRootView.animate()
                .alpha(0.0f)
                .setDuration(FADE_OUT_DURATION_MS)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (playbackControlRootView == null) {
                            return;
                        }
                        isFadingOut = false;
                        if (mVideoEventListener != null && isPlaying()) {
                            mVideoEventListener.onHide(true);
                        }
                        allSectionBg.setVisibility(View.INVISIBLE);
                        if (isShowBottomProgress) {
                            pbVideoPlay.setVisibility(View.VISIBLE);
                        }
                        boolean visiable = pbVideoPlay.getVisibility() == VISIBLE;
                        //增加非空判断
                        if (isFullscreen && mActivity != null) {
                            mActivity.getWindow().getDecorView().setSystemUiVisibility(
                                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN |
                                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                        }
                        handler.removeMessages(SHOW_PROGRESS);
                        isVisible = false;
                        if (pauseAnim != null) {
                            pauseAnim.stop();
                            pauseAnim = null;
                        }
                        ibVideoPause.setVisibility(GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
    }

    public void setIsShowControlView(boolean showControlView) {
        isShowControlView = showControlView;
    }

    private void clearPlayerDuration() {
        mCurrentDuration = 0;
        mCacheDuration = 0;
        mTotalDuration = 0;
    }

    /**
     * 隐藏头部标题
     *
     * @param disappearTime 时长
     */
    private void hideTitle(long disappearTime) {
        AlphaAnimation mAlphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        mAlphaAnimation.setDuration(disappearTime);
        mAlphaAnimation.setInterpolator(new AccelerateInterpolator(3));
        mAlphaAnimation.setFillAfter(true);
        videoTitleView.startAnimation(mAlphaAnimation);
    }

    /**
     * 播放进度拖动设置
     *
     * @param position
     */
    public abstract void seekSetting(int position);

    /**
     * Message handler which allows us to send delayed messages to the {
     * This is useful for fading out the view after a certain time.
     */
    private static class MessageHandler extends Handler {
        /**
         * A reference to the {@link VideoPlayerControlView} that we are handling messages for.
         */
        private final WeakReference<VideoPlayerControlView> playbackControlLayer;

        /**
         * @param videoPlayerControlView The {@link VideoPlayerControlView} we should handle messages for.
         */
        private MessageHandler(VideoPlayerControlView videoPlayerControlView) {
            this.playbackControlLayer = new WeakReference<>(videoPlayerControlView);
        }

        /**
         * Receives either a {@link VideoPlayerControlView#FADE_OUT} message (which hides the playback
         * control layer) or a {@link VideoPlayerControlView#SHOW_PROGRESS} message (which updates the
         * seek bar to reflect the progress in the video).
         *
         * @param msg Either a {@link VideoPlayerControlView#FADE_OUT} or
         *            {@link VideoPlayerControlView#SHOW_PROGRESS} message.
         */
        @Override
        public void handleMessage(Message msg) {
            VideoPlayerControlView layer = playbackControlLayer.get();
            if (layer == null) {
                return;
            }
            int pos;
            switch (msg.what) {
                case FADE_OUT:
                    layer.hide();
                    break;
                case SHOW_PROGRESS:
                    pos = layer.updateProgress();
                    if (!layer.isSeekbarDragging
                            && layer.isVisible
                            && layer.isPlaying()) {
                        msg = obtainMessage(SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                    }
                    break;
                case HIDE_TITLE:
                    layer.hideTitle();
                    break;
                case HIDE_FULLSCREEN:
                    layer.hideFullscreen();
                    break;
                default:
                    break;
            }
        }
    }

}
