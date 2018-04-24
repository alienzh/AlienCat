package com.zhangw.aliencat.video.videoplayer.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhangw.aliencat.R;
import com.zhangw.aliencat.video.videoplayer.PlayState;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by chends on 2018/3/26.
 *
 */

public abstract class VideoPlayerStateView extends BaseVideoPlayerView{

    private static final String TAG = "VideoPlayerStateView";

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
     * 学习人数
     */
    @BindView(R.id.tvVideoLearnCount)
    public TextView tvVideoLearnCount;
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

    protected int state = PlayState.STOP;

    /**
     * 非wifi网络提醒
     */
    private boolean netShowFlag;

    protected boolean isFullscreen;

    public VideoPlayerStateView(@NonNull Context context) {
        super(context);
        inflateView();
    }

    public VideoPlayerStateView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflateView();
    }

    public VideoPlayerStateView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflateView();
    }

    private void inflateView() {
        mLayoutInflater.inflate(R.layout.videoplayer_state_view, this);
    }

    /**
     * @param showLoading 是否显示加载动画
     */
    public void setLoadingAnim(boolean showLoading) {

    }

    public void setState(int state) {
        this.state = state;
        setKeepScreenOn(state == PlayState.LOADING || state == PlayState.PLAY);
        String stateStr = "";
        switch (state) {
            case PlayState.READY:
                ibVideoPause.setVisibility(GONE);
                pbVideoLoading.setVisibility(VISIBLE);
                ibVideoPause.setVisibility(GONE);
                break;

            case PlayState.LOADING:
                stateStr = "加载";
                ibVideoPause.setVisibility(GONE);
                pbVideoLoading.setVisibility(VISIBLE);
                showNetPromptDialog(false,false);
                break;

            case PlayState.PLAY:
                stateStr = "播放";
                pbVideoLoading.setVisibility(GONE);
                showNetPromptDialog(false,false);
                getTXCloudVideoView().setVisibility(VISIBLE);
                setLoadingAnim(false);
                ivVideoBackground.setVisibility(GONE);
                break;

            case PlayState.PAUSE:
                stateStr = "暂停";
                ibVideoPause.setVisibility(VISIBLE);
                pbVideoLoading.setVisibility(GONE);
                setLoadingAnim(false);
                break;

            case PlayState.STOP:
                ivVideoBackground.setVisibility(VISIBLE);
                break;

            case PlayState.RELEASE:
                stateStr = "释放";
                break;

            case PlayState.COMPLETE:
                stateStr = "播放完成";
                ibVideoPause.setVisibility(GONE);
                break;

            default:
                break;
        }
//        LogUtil.i(TAG, "视频播放状态 = " + stateStr + ";videoId=");
        updateState(state);
    }

    public boolean isFullscreen() {
        return isFullscreen;
    }

    public int getState() {
        return state;
    }

    public boolean getNetShowFlag() {
        return netShowFlag;
    }

    /**
     * 点击
     *
     * @param view 点击的view
     */
    @OnClick({R.id.ibVideoPause, R.id.ivVideoBackground, R.id.ivVideoBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivVideoBack:
                //返回小屏
                if (isFullscreen()) {
                    fullscreenSetting(false);
                }
                break;
            case R.id.ibVideoPause:
            case R.id.ivVideoBackground:
                if (getState() == PlayState.PAUSE) {
                    resumePlay();
                } else if(getState() == PlayState.PLAY) {
                    pausePlayer();
                } else {
                    startPlay();
                }
                break;
            default:
                break;
        }
    }

    /**
     * Change the icon of the play/pause button to indicate play or pause based on the state of the
     * video player.
     */
    public void updatePlayPauseButton() {
        ibVideoPause.setVisibility(VISIBLE);
        if (isPlaying()) {
            ibVideoPause.setBackgroundResource(R.drawable.video_play_anim_12);
        } else {
            ibVideoPause.setBackgroundResource(R.drawable.video_play_anim_0);
        }
    }

    /**
     * Play or pause the player.
     *
     * @param shouldPlay If true, then the player starts playing. If false, the player pauses.
     */
    public void setPlayPause(boolean shouldPlay) {
        if (shouldPlay) {
            hideTitle();
            resumePlay();
        } else {
            pausePlayer();
        }
        if (state != PlayState.COMPLETE) {
            setState(shouldPlay ? PlayState.READY : PlayState.PAUSE);
        }
    }

    /**
     * 隐藏标题
     */
    public abstract void hideTitle();


    /**
     * 视频分享
     *
     * @param platform 分享类型<>qq QQ,微信 Wechat,朋友圈 WechatMoments</>
     */
    public abstract void shareVideo(String platform);

    /**
     * 提示网络变化
     *
     * @param showFlag true 显示网络提示框
     * @param noneNet  true 无网络  false 手机网络
     */
    public abstract void showNetPromptDialog(boolean showFlag, boolean noneNet);

    /**
     * 更新state
     * 比如需要回调到列表中
     * @param state 状态
     */
    public abstract void updateState(int state);

    /**
     * 设置全屏
     * false取消全屏
     * true全屏播放
     * @param trigger 开启全屏
     */
    public abstract void fullscreenSetting(boolean trigger);

    /**
     * 开始播放
     */
    public abstract void startPlay();

    /**
     *继续播放
     */
    public abstract void resumePlay();

    /**
     *重播
     */
    public abstract void replayPlay();


    /**
     * 暂停播放
     */
    public abstract void pausePlayer();


    /**
     * 是否正在播放
     */
    public abstract boolean isPlaying();

}
