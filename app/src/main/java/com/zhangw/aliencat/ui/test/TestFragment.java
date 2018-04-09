package com.zhangw.aliencat.ui.test;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhangw.aliencat.R;
import com.zhangw.aliencat.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author zhangw
 * @date 2018/4/8.
 * 描述
 */
public class TestFragment extends BaseFragment {

    @BindView(R.id.rvTest)
    RecyclerView rvTest;

    private BaseQuickAdapter<String, BaseViewHolder> mQuickAdapter;

    public static TestFragment newInstance() {

        Bundle args = new Bundle();

        TestFragment fragment = new TestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initEnv() {
        List<String> data = new ArrayList<>();
        data.add("view");
        data.add("volley");
        data.add("network state");
        data.add("...");

        rvTest.setLayoutManager(new LinearLayoutManager(_mActivity));
        mQuickAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.support_simple_spinner_dropdown_item, data) {

            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(android.R.id.text1, item);
                helper.setTag(android.R.id.text1, item);
                helper.addOnClickListener(android.R.id.text1);
            }
        };
        rvTest.setAdapter(mQuickAdapter);
        mQuickAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ToastUtils.showLong((String) view.getTag());
                String str = (String) view.getTag();
                if (StringUtils.equals(str, "view")) {
                    ActivityUtils.startActivity(TestViewActivity.class);
                } else if (StringUtils.equals(str, "network state")) {
                    ActivityUtils.startActivity(NetworkStateActivity.class);
                }
            }
        });
    }

    @Override
    public int getLayoutId(Bundle savedInstanceState) {
        return R.layout.fragment_test;
    }
}
