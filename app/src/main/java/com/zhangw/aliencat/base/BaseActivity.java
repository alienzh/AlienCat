package com.zhangw.aliencat.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.blankj.utilcode.util.Utils;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author xiemy
 * @date 2017/7/28.
 */
public abstract class BaseActivity extends BaseToolbarActivity {

    public String TAG = this.getClass().getSimpleName();
    protected Context mContext;
    protected Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId(savedInstanceState));
        unbinder = ButterKnife.bind(this);
        mContext = this;

        initEnv();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        getExtraData();
    }

    public Intent getExtraData() {
        return getIntent() != null ? getIntent() : new Intent();
    }

    /**
     * 获取页面布局
     *
     * @param savedInstanceState savedInstanceState
     * @return 页面布局资源
     */
    public abstract
    @LayoutRes
    int getLayoutId(Bundle savedInstanceState);

    /**
     * 初始化数据
     */
    public abstract void initEnv();

    /**
     * 打开软键盘
     */
    public void openSoftInput(View v) {
        WeakReference<View> weakReference = new WeakReference<>(v);
        v.requestFocus();
        v.setFocusable(true);
        v.setFocusableInTouchMode(true);
        InputMethodManager inputManager = (InputMethodManager) Utils.getApp().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.showSoftInput(weakReference.get(), InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            WeakReference<View> weakReference = new WeakReference<>(view);
            InputMethodManager inputMethodManager = ((InputMethodManager) Utils.getApp().getSystemService(Context.INPUT_METHOD_SERVICE));
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(weakReference.get().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}