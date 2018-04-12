package com.zhangw.aliencat.ui.test;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhangw.aliencat.R;
import com.zhangw.aliencat.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author zhangw
 * @date 2018/4/12.
 * 描述
 */
public class TestPermissionActivity extends BaseActivity {
    private final static String TAG = "TestPermissionActivity";
    @BindView(R.id.rvPermission)
    RecyclerView rvPermission;
    private BaseQuickAdapter<String, BaseViewHolder> mQuickAdapter;

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private TestSensorListener mSensorListener;

    @Override
    public int getLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_permission;
    }

    @Override
    public void initEnv() {
        // 初始化传感器
        mSensorListener = new TestSensorListener();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        showHead(true, true);
        setHeadTitle("权限管理");
        List<String> data = new ArrayList<>();

        data.add("_accemeter");   // 加速度
        data.add("_magfield");   // 磁力
        data.add("_orient");      // 方向
        data.add("_gyros");       // 陀螺仪
        data.add("_light");       // 光线感应
        data.add("_press");       // 压力
        data.add("_tempera");     // 温度
        data.add("_prox");        // 距离感应
        data.add("_grav");        // 重力
        data.add("_lineacce");    // 线性加速度
        data.add("_rota");        // 旋转矢量
        data.add("_gps");         // gps位置

        rvPermission.setLayoutManager(new LinearLayoutManager(this));
        mQuickAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.support_simple_spinner_dropdown_item, data) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(android.R.id.text1, item);
                helper.setTag(android.R.id.text1, item);
                helper.addOnClickListener(android.R.id.text1);
            }
        };
        rvPermission.setAdapter(mQuickAdapter);
        mQuickAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                String str = (String) view.getTag();
                if (StringUtils.isEmpty(str)) {
                    return;
                }
                switch (str) {
                    case "_accemeter":
                        mSensorManager.unregisterListener(mSensorListener);
                        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                        mSensorManager.registerListener(mSensorListener, mSensor, SensorManager.SENSOR_DELAY_UI);
                        break;
                    case "_magfield":
                        mSensorManager.unregisterListener(mSensorListener);
                        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                        mSensorManager.registerListener(mSensorListener, mSensor, SensorManager.SENSOR_DELAY_UI);
                        break;
                    case "_orient":
                        mSensorManager.unregisterListener(mSensorListener);
                        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
                        mSensorManager.registerListener(mSensorListener, mSensor, SensorManager.SENSOR_DELAY_UI);
                        break;
                    case "_gyros":
                        mSensorManager.unregisterListener(mSensorListener);
                        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
                        mSensorManager.registerListener(mSensorListener, mSensor, SensorManager.SENSOR_DELAY_UI);
                        break;
                    case "_light":
                        mSensorManager.unregisterListener(mSensorListener);
                        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
                        mSensorManager.registerListener(mSensorListener, mSensor, SensorManager.SENSOR_DELAY_UI);
                        break;
                    case "_press":
                        mSensorManager.unregisterListener(mSensorListener);
                        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
                        mSensorManager.registerListener(mSensorListener, mSensor, SensorManager.SENSOR_DELAY_UI);
                        break;
                    case "_tempera":
                        mSensorManager.unregisterListener(mSensorListener);
                        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
                        mSensorManager.registerListener(mSensorListener, mSensor, SensorManager.SENSOR_DELAY_UI);
                        break;
                    case "_prox":
                        mSensorManager.unregisterListener(mSensorListener);
                        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
                        mSensorManager.registerListener(mSensorListener, mSensor, SensorManager.SENSOR_DELAY_UI);
                        break;
                    case "_grav":
                        mSensorManager.unregisterListener(mSensorListener);
                        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
                        mSensorManager.registerListener(mSensorListener, mSensor, SensorManager.SENSOR_DELAY_UI);
                        break;
                    case "_lineacce":
                        mSensorManager.unregisterListener(mSensorListener);
                        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
                        mSensorManager.registerListener(mSensorListener, mSensor, SensorManager.SENSOR_DELAY_UI);
                        break;
                    case "_rota":
                        mSensorManager.unregisterListener(mSensorListener);
                        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
                        mSensorManager.registerListener(mSensorListener, mSensor, SensorManager.SENSOR_DELAY_UI);
                        break;
                    case "_gps":
//                        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_G);
//                        mSensorManager.registerListener(mSensorListener, mSensor, SensorManager.SENSOR_DELAY_UI);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 注销监听函数
        mSensorManager.unregisterListener(mSensorListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 注册传感器监听函数
        mSensorManager.registerListener(mSensorListener, mSensor, SensorManager.SENSOR_DELAY_UI);
    }

    class TestSensorListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            // 读取加速度传感器数值，values数组0,1,2分别对应x,y,z轴的加速度
            LogUtils.dTag(TAG, "onSensorChanged: " + event.values[0] + ", " + event.values[1] + ", " + event.values[2]);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            LogUtils.dTag(TAG, "onAccuracyChanged");

        }
    }
}
