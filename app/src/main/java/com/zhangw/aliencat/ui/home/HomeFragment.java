package com.zhangw.aliencat.ui.home;

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
public class HomeFragment extends BaseFragment {
    @BindView(R.id.text)
    TextView text;

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void initEnv() {

    }

    @Override
    public int getLayoutId(Bundle savedInstanceState) {
        return R.layout.fragment_home;
    }
}
