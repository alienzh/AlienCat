package com.zhangw.aliencat.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.blankj.utilcode.util.Utils;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * @user zhangw
 * @date 2018/4/8.
 * 描述
 */
public abstract class BaseFragment extends SupportFragment {

    public static final String ARG_PARAM1 = "param1";
    public static final String ARG_PARAM2 = "param2";
    Unbinder unbinder;

    private String mParam1;
    private String mParam2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(getLayoutId(savedInstanceState), container, false);
        unbinder = ButterKnife.bind(this, view);
        initEnv();
        return view;
    }

    /**
     * 初始化数据
     */
    public abstract void initEnv();

    public abstract
    @LayoutRes
    int getLayoutId(Bundle savedInstanceState);

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
        View view = _mActivity.getCurrentFocus();
        if (view != null) {
            WeakReference<View> weakReference = new WeakReference<>(view);
            InputMethodManager inputMethodManager = ((InputMethodManager) Utils.getApp().getSystemService(Context.INPUT_METHOD_SERVICE));
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(weakReference.get().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
