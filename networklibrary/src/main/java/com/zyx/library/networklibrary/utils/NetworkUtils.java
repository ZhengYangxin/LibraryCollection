package com.zyx.library.networklibrary.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.zyx.library.networklibrary.NetworkManager;
import com.zyx.library.networklibrary.type.NetType;

public class NetworkUtils {


    @SuppressLint("MissingPermission")
    public static boolean isNetworkAvalible() {
        ConnectivityManager connectivityManager = (ConnectivityManager) NetworkManager.getInstance().getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null)
            return false;

        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
        if (networkInfos != null) {
            for (NetworkInfo info : networkInfos) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }

        return false;
    }

    @SuppressLint("MissingPermission")
    public static NetType getNetType() {
        ConnectivityManager connectivityManager = (ConnectivityManager) NetworkManager.getInstance().getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return NetType.NONE;
        }

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null)
            return  NetType.NONE;

        Log.e(Constancs.NET_LOG, "网络信息： " + networkInfo.toString());

        int type = networkInfo.getType();
        if (type == ConnectivityManager.TYPE_MOBILE) {
            if (networkInfo.getExtraInfo().equalsIgnoreCase("cmnet"))
                return NetType.CMNET;
            else
                return NetType.CMWAP;

        } else if (type == ConnectivityManager.TYPE_WIFI) {
            return NetType.WIFI;
        }

        return NetType.NONE;
    }
}
