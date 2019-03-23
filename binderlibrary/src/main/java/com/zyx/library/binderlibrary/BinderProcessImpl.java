package com.zyx.library.binderlibrary;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

/**
 * @author pielan
 * @date 11/3/19 下午1:20
 * @dec 进程间通讯实现IprocessInterface的send方法
 */
public class BinderProcessImpl implements IprocessInterface {

    private ProcessInterface processInterface;

    public BinderProcessImpl(Context context) {
        bind(context, null, BinderService.class);
    }

    public BinderProcessImpl(Context context, String packageName) {
        bind(context, packageName, BinderService.class);
    }

    private void bind(Context context, String  packageName, Class<? extends BinderService> service) {
        Intent intent;
        if (TextUtils.isEmpty(packageName)) {
            intent = new Intent(context, service);
        } else {
            intent = new Intent();
            intent.setPackage(packageName);
            intent.setAction(service.getName());
        }
        context.bindService(intent, new ProcessConnection(), Context.BIND_AUTO_CREATE);
    }

    @Override
    public String send(String request) {
        try {
            return processInterface.send(request);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class ProcessConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            processInterface = ProcessInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
}
