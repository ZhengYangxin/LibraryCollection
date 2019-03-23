package com.zyx.library.eventbuslibrary;

import android.util.Log;

import java.util.logging.Level;

/**
 * @author pielan
 * @date 21/3/19 下午2:03
 * @dec
 */
public class AndroidLogger implements Logger{

    private static final boolean ANDROID_LOG_AVAILABLE;

    static {
        boolean android = false;
        try {
            android = Class.forName("android.util.Log") != null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        ANDROID_LOG_AVAILABLE = android;
    }

    private final String tag;

    public AndroidLogger(String tag) {
        this.tag = tag;
    }

    public static boolean isAndroidLogAvailable() {
        return ANDROID_LOG_AVAILABLE;
    }

    @Override
    public void log(Level level, String msg, Throwable cause) {
        if (level != Level.OFF) {
            // That's how Log does it internally
            Log.println(mapLevel(level), tag, msg + "\n" + Log.getStackTraceString(cause));
        }
    }


    @Override
    public void log(Level level, String msg) {
        if (level != level.OFF) {
            Log.println(mapLevel(level), tag, msg);
        }
    }


    private int mapLevel(Level level) {
        int value = level.intValue();
        if (value < 800) {
            if (value < 500) {
                return Log.VERBOSE;
            } else {
                return Log.DEBUG;
            }
        } else if (value < 900) {
            return Log.INFO;
        } else if (value < 1000) {
            return Log.WARN;
        } else {
            return Log.ERROR;
        }
    }
}
