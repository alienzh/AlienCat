package com.zhangw.aliencat.ui.main;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zhangw.aliencat.R;
import com.zhangw.aliencat.base.BaseActivity;
import com.zhangw.aliencat.ui.home.HomeFragment;
import com.zhangw.aliencat.ui.mine.MineFragment;
import com.zhangw.aliencat.ui.test.TestFragment;
import com.zhangw.aliencat.ui.tool.ToolFragment;

import butterknife.BindView;
import butterknife.OnClick;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * @user ${USER}
 * @date ${DATE}.
 * 描述：首页
 */
public class HomeActivity extends BaseActivity {

    private final static int HOME = 0;
    private final static int TOOL = 1;
    private final static int TEST = 2;
    private final static int MINE = 3;
    @BindView(R.id.flContainer)
    FrameLayout flContainer;
    @BindView(R.id.tvHome)
    TextView tvHome;
    @BindView(R.id.flHome)
    FrameLayout flHome;
    @BindView(R.id.tvTool)
    TextView tvTool;
    @BindView(R.id.flTool)
    FrameLayout flTool;
    @BindView(R.id.tvTest)
    TextView tvTest;
    @BindView(R.id.flTest)
    FrameLayout flTest;
    @BindView(R.id.tvMine)
    TextView tvMine;
    @BindView(R.id.flMine)
    FrameLayout flMine;
    private int showTabIndex = HOME;
    private SupportFragment[] mFragments = new SupportFragment[4];
    private Bundle savedInstanceState;

    @Override
    public int getLayoutId(Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        return R.layout.activity_home;
    }

    @Override
    public void initEnv() {
        showHead(false, false);
        if (null == savedInstanceState) {
            mFragments[HOME] = HomeFragment.newInstance();
            mFragments[TOOL] = ToolFragment.newInstance();
            mFragments[TEST] = TestFragment.newInstance();
            mFragments[MINE] = MineFragment.newInstance();
            tvHome.setSelected(true);
            loadMultipleRootFragment(R.id.flContainer, showTabIndex, mFragments);
        } else {
            mFragments[HOME] = findFragment(HomeFragment.class);
            mFragments[TOOL] = findFragment(ToolFragment.class);
            mFragments[TEST] = findFragment(TestFragment.class);
            mFragments[MINE] = findFragment(MineFragment.class);
        }
    }

    @OnClick({R.id.flHome, R.id.flTool, R.id.flTest, R.id.flMine})
    public void onViewClicked(View view) {
        refreshTabView();
        switch (view.getId()) {
            case R.id.flHome:
                if (showTabIndex != HOME) {
                    tvHome.setSelected(true);
                    showHideFragment(mFragments[HOME], mFragments[showTabIndex]);
                    showTabIndex = HOME;
                }
                break;
            case R.id.flTool:
                if (showTabIndex != TOOL) {
                    tvTool.setSelected(true);
                    showHideFragment(mFragments[TOOL], mFragments[showTabIndex]);
                    showTabIndex = TOOL;
                }
                break;
            case R.id.flTest:
                if (showTabIndex != TEST) {
                    tvTest.setSelected(true);
                    showHideFragment(mFragments[TEST], mFragments[showTabIndex]);
                    showTabIndex = TEST;
                }
                break;
            case R.id.flMine:
                if (showTabIndex != MINE) {
                    tvMine.setSelected(true);
                    showHideFragment(mFragments[MINE], mFragments[showTabIndex]);
                    showTabIndex = MINE;
                }
                break;
            default:
                break;
        }
    }

    private void refreshTabView() {
        tvHome.setSelected(false);
        tvTool.setSelected(false);
        tvTest.setSelected(false);
        tvMine.setSelected(false);
    }
}
