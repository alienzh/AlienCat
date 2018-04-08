package com.zhangw.aliencat.ui.mine;

import android.os.Bundle;
import android.widget.TextView;

import com.zhangw.aliencat.R;
import com.zhangw.aliencat.base.BaseFragment;

import butterknife.BindView;

/**
 * @author  zhangw
 * @date 2018/4/8.
 * 描述
 */
public class MineFragment extends BaseFragment {
    @BindView(R.id.text)
    TextView text;

    public static MineFragment newInstance() {
        
        Bundle args = new Bundle();
        
        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void initEnv() {

    }

    @Override
    public int getLayoutId(Bundle savedInstanceState) {
        return R.layout.fragment_mine;
    }
}
