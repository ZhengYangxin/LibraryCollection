package com.zyx.library.simple;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import com.zyx.library.hotfixlibrary.HotFixUtils;

/**
 * @author pielan
 * @date 7/3/19 下午6:28
 * @dec
 */
public class BaseApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        //加载热修复Dex文件
        HotFixUtils.loadFixDex(this);
        Log.e("hotFIx", "loadFix");
    }
}
