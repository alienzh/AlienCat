package com.zhangw.aliencat.ui.test;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhangw.aliencat.R;
import com.zhangw.aliencat.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author zhangw
 * @date 2018/4/9.
 * 描述 volley测试
 */
public class TestVolleyActivity extends BaseActivity {
    @BindView(R.id.rvVolley)
    RecyclerView rvVolley;

    private BaseQuickAdapter<String, BaseViewHolder> mQuickAdapter;

    @Override
    public int getLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_volley;
    }

    @Override
    public void initEnv() {
        showHead(true, true);
        setHeadTitle("volley");
        List<String> data = new ArrayList<>();
        data.add("StringRequest");
        data.add("JsonObjectRequest");

        rvVolley.setLayoutManager(new LinearLayoutManager(this));
        mQuickAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.support_simple_spinner_dropdown_item, data) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(android.R.id.text1, item);
                helper.setTag(android.R.id.text1, item);
                helper.addOnClickListener(android.R.id.text1);
            }
        };
        rvVolley.setAdapter(mQuickAdapter);
        mQuickAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                String str = (String) view.getTag();
                if (StringUtils.isEmpty(str)) {
                    return;
                }
                ToastUtils.showLong(str);
                switch (str) {
                    case "StringRequest":
                        break;
                    case "JsonObjectRequest":
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
