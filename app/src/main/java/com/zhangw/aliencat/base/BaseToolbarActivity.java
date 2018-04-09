package com.zhangw.aliencat.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhangw.aliencat.R;

import java.lang.reflect.Field;

import me.yokeyword.fragmentation.SupportActivity;

/**
 * @author zhangw
 * @date 2018/3/23.
 * 带toolbar的activity
 */
public abstract class BaseToolbarActivity extends SupportActivity {

    /**
     * 标题
     */
    TextView mTvHeadTitle;
    /**
     * 右侧图片
     */
    ImageView mIvHeadRight;
    /**
     * 右侧文字
     */
    TextView mTvHeadRight;
    /**
     * 右侧容器
     */
    RelativeLayout mRlHeadRight;
    /**
     * 头部toolbar
     */
    Toolbar mToolbar;
    /**
     * 分割线
     */
    View mTbDiver;
    /**
     * 根布局
     */
    LinearLayout mLlRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base_toolbar);
    }

    /**
     * 初始化头部Toolbar控件
     */
    private void initToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        mTvHeadTitle = mToolbar.findViewById(R.id.tvHeadTitle);
        mTvHeadRight = mToolbar.findViewById(R.id.tvHeadRight);
        mIvHeadRight = mToolbar.findViewById(R.id.ivHeadRight);
        mRlHeadRight = mToolbar.findViewById(R.id.rlHeadRight);
        mTbDiver = findViewById(R.id.tbDiver);
        if (mToolbar != null) {
            mToolbar.setTitle("");
            setSupportActionBar(mToolbar);
            mToolbar.setNavigationIcon(R.drawable.back);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressedSupport();
                }
            });

        }
    }

    /**
     * 通过反射获取titleTextView来设置监听
     */
    public TextView getToolbarTitle() {
        try {
            //获取成员变量
            Field f = mToolbar.getClass().getDeclaredField("mTitleTextView");
            //设置可访问
            f.setAccessible(true);
            //获取到mSubtitleTextView的实例
            //这里使用final是为了方便下面在匿名内部类里使用
            //传入的是toolbar实例
            final TextView tv = (TextView) f.get(mToolbar);
            return tv;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 显示头部
     *
     * @param tbFlag    是否顯头部标题
     * @param diverFlag 是否顯示顶部线
     */
    public void showHead(boolean tbFlag, boolean diverFlag) {
        mToolbar.setVisibility(tbFlag ? View.VISIBLE : View.GONE);
        mTbDiver.setVisibility(diverFlag ? View.VISIBLE : View.GONE);
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public TextView getTvHeadRight() {
        mRlHeadRight.setVisibility(View.VISIBLE);
        mTvHeadRight.setVisibility(View.VISIBLE);
        return mTvHeadRight;
    }

    public ImageView getIvHeadRight() {
        mRlHeadRight.setVisibility(View.VISIBLE);
        mIvHeadRight.setVisibility(View.VISIBLE);
        return mIvHeadRight;
    }

    public RelativeLayout getLayoutRight() {
        mRlHeadRight.setVisibility(View.VISIBLE);
        return mRlHeadRight;
    }

    public void setHeadTitle(@StringRes int titleStr) {
        setHeadTitle(0, getString(titleStr));
    }

    public void setHeadTitle(String titleStr) {
        setHeadTitle(0, titleStr);
    }

    /**
     * 设置头部背景颜色,头部标题
     *
     * @param headColor 背景颜色
     * @param titleStr  头部标题
     */
    public void setHeadTitle(@ColorRes int headColor, String titleStr) {
        if (headColor > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mToolbar.setBackgroundColor(getResources().getColor(headColor, null));
            } else {
                mToolbar.setBackgroundColor(getResources().getColor(headColor));
            }
        }
        mTvHeadTitle.setText(titleStr);
    }

    /**
     * 获取手机屏幕状态栏高度
     *
     * @return 状态栏高度
     */
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 关闭页面
     */
    public void backPressed() {
        //finish();
        super.onBackPressedSupport();
    }

    @Override
    public void setContentView(int layoutId) {
        setContentView(View.inflate(this, layoutId, null));
    }

    @Override
    public void setContentView(View view) {
        LinearLayout llRootView = findViewById(R.id.llRootView);
        if (llRootView == null) {
            return;
        }
        llRootView.addView(view,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        initToolbar();
    }
}
