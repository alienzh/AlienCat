package com.zhangw.aliencat.video.videoplayer;

/**
 * Created by chends on 2018/1/15.
 */

/**
 * In order to imbue the {@link TXCloudVideoViewWrapper} with the ability make the player fullscreen,
 * a {@link FullscreenCallback} must be assigned to it. The
 * {@link FullscreenCallback} implementation is responsible for
 * hiding/showing the other views on the screen when the player enters/leaves fullscreen
 * mode.
 */

public interface FullscreenCallback {


    /**
     * When triggered, the activity should hide any additional views.
     */
    void onGoToFullscreen(int[] originalPos);

    void onGoToFullscreen(int[] originalPos, FullscreenEventListener listener);

    /**
     * When triggered, the activity should show any views that were hidden when the player
     * went to fullscreen.
     */
    void onReturnFromFullscreen();

    /**
     * 全屏按钮点击，全屏则回到小屏，小屏则放大全屏
     */
    void onToggleFullscreen();
}
