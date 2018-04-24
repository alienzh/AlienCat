package com.zhangw.aliencat.video.videoplayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.zhangw.aliencat.video.videoplayer.ui.VideoPlayerViewWrapper;

/**
 * @author zhangw
 * @date 2018/4/23.
 */
public class StandardVideoPlayer extends VideoPlayerViewWrapper {
    public StandardVideoPlayer(@NonNull Context context) {
        super(context);
    }

    public StandardVideoPlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StandardVideoPlayer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
