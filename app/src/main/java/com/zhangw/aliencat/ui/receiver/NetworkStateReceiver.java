package com.zhangw.aliencat.ui.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;

/**
 * @author zhangw
 * @date 2018/4/9.
 * 描述 网络状态变化监听
 */
public class NetworkStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == intent || null == intent.getAction()) {
            return;
        }
        //获得ConnectivityManager对象
//        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
//
//            //获取ConnectivityManager对象对应的NetworkInfo对象
//            //获取WIFI连接的信息
//            NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//            //获取移动数据连接的信息
//            NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//            if (wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
//                ToastUtils.showLong("WIFI已连接,移动数据已连接");
//            } else if (wifiNetworkInfo.isConnected() && !dataNetworkInfo.isConnected()) {
//                ToastUtils.showLong("WIFI已连接,移动数据已断开");
//            } else if (!wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
//                ToastUtils.showLong("WIFI已断开,移动数据已连接");
//            } else {
//                ToastUtils.showLong("WIFI已断开,移动数据已断开");
//            }
//        //API大于23时使用下面的方式进行网络监听
//        } else {
//            System.out.println("API level 大于23");
//            //获取所有网络连接的信息
//            Network[] networks = connMgr.getAllNetworks();
//            //用于存放网络连接信息
//            StringBuilder sb = new StringBuilder();
//            //通过循环将网络信息逐个取出来
//            for (int i = 0; i < networks.length; i++) {
//                //获取ConnectivityManager对象对应的NetworkInfo对象
//                NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[i]);
//                sb.append(networkInfo.getTypeName() + " connect is " + networkInfo.isConnected());
//            }
//            ToastUtils.showLong(sb.toString());
//        }
        switch (NetworkUtils.getNetworkType()){
            case NETWORK_WIFI:
                ToastUtils.showLong("正在使用WIFI网络");
                break;
            case NETWORK_4G:
                ToastUtils.showLong("正在使用4G网络");
                break;
            case NETWORK_3G:
                ToastUtils.showLong("正在使用3G网络");
                break;
            case NETWORK_2G:
                ToastUtils.showLong("正在使用2G网络");
                break;
            case NETWORK_UNKNOWN:
                ToastUtils.showLong("网络未识别");
                break;
            case NETWORK_NO:
                ToastUtils.showLong("没有网络");
                break;
            default:
                break;
        }
    }
}
