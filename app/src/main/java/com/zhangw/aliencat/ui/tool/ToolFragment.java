package com.zhangw.aliencat.ui.tool;

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
public class ToolFragment extends BaseFragment {
    @BindView(R.id.text)
    TextView text;

    public static ToolFragment newInstance() {

        Bundle args = new Bundle();

        ToolFragment fragment = new ToolFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initEnv() {

    }

    @Override
    public int getLayoutId(Bundle savedInstanceState) {
        return R.layout.fragment_tool;
    }
}
