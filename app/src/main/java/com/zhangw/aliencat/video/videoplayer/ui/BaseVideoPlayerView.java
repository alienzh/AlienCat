package com.zhangw.aliencat.video.videoplayer.ui;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.tencent.rtmp.ui.TXCloudVideoView;

/**
 * BaseVideoPlayerView class
 *
 * @author chends
 * @date 2018/4/8
 */

public abstract class BaseVideoPlayerView extends FrameLayout {

    protected static final String LOG_TAG = "KyVideoPlayer";
    protected TXCloudVideoView mTXCloudVideoView;
    protected LayoutParams mLayoutParams;
    protected Context mContext;
    protected LayoutInflater mLayoutInflater;

    public BaseVideoPlayerView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public BaseVideoPlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public BaseVideoPlayerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.setBackgroundColor(Color.BLACK);
        mLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addTXCloudVideoView();
    }

    /**
     * 初始化view
     */
    public abstract void initView();

    public TXCloudVideoView getTXCloudVideoView() {
        return mTXCloudVideoView;
    }

    /**
     * 插入视频渲染视图
     */
    public void addTXCloudVideoView() {
        if (null != mTXCloudVideoView) {
            this.removeView(mTXCloudVideoView);
        } else {
            mTXCloudVideoView = new TXCloudVideoView(mContext);
        }
        this.addView(mTXCloudVideoView, 0, mLayoutParams);
    }

    public void removeTXCloudVideoView() {
        if (null != mTXCloudVideoView) {
            this.removeView(mTXCloudVideoView);
        }
    }
}
