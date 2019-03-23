package com.zyx.library.simple.method;

import android.content.Context;
import android.widget.Toast;

/**
 * @author pielan
 * @date 8/3/19 下午12:55
 * @dec
 */
public class ParamSort {

    public void math(Context context) {
        int a = 10;
        int b = 1;
        Toast.makeText(context, "result: " + a/b, Toast.LENGTH_LONG).show();
    }
}
