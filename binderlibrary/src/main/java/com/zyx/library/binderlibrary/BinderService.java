package com.zyx.library.binderlibrary;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

/**
 * @author pielan®
 * @date 10/3/19 下午10:29
 * @dec binder处理中心
 */
public class BinderService extends Service {

    private static final ResponseExecute responseExecute = new ResponseExecute();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ProcessInterface.Stub() {
            @Override
            public String send(String request) throws RemoteException {
                return responseExecute.execute(request);
            }
        };
    }
}
