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
    
    public void toPage(Class<?> clz) {
        toPage(clz, null, -1);
    }

    public void toPage(Class<?> clz, int flags) {
        toPage(clz, null, flags);
    }

    public void toPage(Class<?> clz, Bundle bundle) {
        toPage(clz, bundle, -1);
    }

    /**
     * 跳转页面
     *
     * @param clz    跳转对象页面
     * @param bundle intent 内容
     * @param flags  启动模式<>默认-1 为 FLAG_ACTIVITY_CLEAR_TOP</>
     */
    public void toPage(Class<?> clz, Bundle bundle, int flags) {
        Intent intent = new Intent(this, clz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        if (flags == -1) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        } else {
            intent.setFlags(flags);
        }
        startActivity(intent);
    }

    public void toPageForResult(Class<?> clz, int requestCode) {
        toPageForResult(clz, null, requestCode, -1);
    }

    public void toPageForResult(Class<?> clz, int requestCode, int flags) {
        toPageForResult(clz, null, requestCode, flags);
    }

    public void toPageForResult(Class<?> clz, Bundle extras, int requestCode) {
        toPageForResult(clz, extras, requestCode, -1);
    }

    public void toPageForResult(Class<?> clz, Bundle extras, int requestCode, int flags) {
        Intent intent = new Intent(this, clz);
        if (extras != null) {
            intent.putExtras(extras);
        }
        if (flags == -1) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        } else {
            intent.setFlags(flags);
        }
        startActivityForResult(intent, requestCode);
    }
    
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