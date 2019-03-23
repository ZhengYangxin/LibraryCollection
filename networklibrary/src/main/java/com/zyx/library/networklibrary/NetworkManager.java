package com.zyx.library.networklibrary;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.os.Build;
import com.zyx.library.networklibrary.conn.NetworkCallbackImpl;

public class NetworkManager {

    private static volatile NetworkManager instance;
    private Application application;
    private NetworkBroadcast broadcast;

    private NetworkManager() {
        broadcast = new NetworkBroadcast();
    }

    public static NetworkManager getInstance() {
        if (instance == null) {
            synchronized (NetworkBroadcast.class) {
                instance = new NetworkManager();
            }
        }
        return instance;
    }

    public Application getApplication() {
        if (application == null) {
            throw new RuntimeException("Application is null");
        }

        return application;
    }

    @SuppressLint("MissingPermission")
    public void init(Application application) {
        this.application = application;
        // 注册广播
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(Constancs.CONNECTIVITY_ACTION);
//
//        application.registerReceiver(broadcast, intentFilter);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ConnectivityManager.NetworkCallback networkCallback = new NetworkCallbackImpl();
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            NetworkRequest request = builder.build();
            ConnectivityManager connectivityManager = (ConnectivityManager) NetworkManager.getInstance().getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                connectivityManager.registerNetworkCallback(request, networkCallback);
            }
        }
    }

//    public void setListener(NetChangeObserver listener) {
//        broadcast.setListener(listener);
//    }

    public void removeObserver(Object register) {
        broadcast.removeObserver(register);

    }

    public void registerObserver(Object register) {
        broadcast.registerObserver(register);
    }

    public void removeAllObserver() {
        broadcast.removeAllObserver();

    }
}
