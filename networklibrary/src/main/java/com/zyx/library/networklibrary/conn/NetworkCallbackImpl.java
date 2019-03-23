package com.zyx.library.networklibrary.conn;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import com.zyx.library.networklibrary.utils.Constancs;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NetworkCallbackImpl extends ConnectivityManager.NetworkCallback {

    @Override
    public void onAvailable(Network network) {
        super.onAvailable(network);
        Log.e(Constancs.NET_LOG, "网络连接成功");

    }

    @Override
    public void onLost(Network network) {
        super.onLost(network);
        Log.e(Constancs.NET_LOG, "网络已经断开");

    }

    @Override
    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);
        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.e(Constancs.NET_LOG, "网络发生改变: wifi");

            } else {
                Log.e(Constancs.NET_LOG, "网络发生改变: 其他");

            }
        }
    }
}
