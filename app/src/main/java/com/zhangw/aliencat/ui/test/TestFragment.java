package com.zhangw.aliencat.ui.test;

import android.os.Bundle;
import android.widget.TextView;

import com.zhangw.aliencat.R;
import com.zhangw.aliencat.base.BaseFragment;

import butterknife.BindView;

/**
 * @user zhangw
 * @date 2018/4/8.
 * 描述
 */
public class TestFragment extends BaseFragment {
    @BindView(R.id.text)
    TextView text;

    public static TestFragment newInstance() {
        
        Bundle args = new Bundle();
        
        TestFragment fragment = new TestFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void initEnv() {

    }

    @Override
    public int getLayoutId(Bundle savedInstanceState) {
        return R.layout.fragment_test;
    }
}