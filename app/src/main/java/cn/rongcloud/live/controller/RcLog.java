package cn.rongcloud.live.controller;

import android.util.Log;

public class RcLog {

    private static final String TAG = "LiveAppLog";

    public static final int VERBOSE = 0;
    public static final int DEBUG = 1;
    public static final int INFO = 2;
    public static final int WARN = 3;
    public static final int ERROR = 4;

    private static int level;

    public static void setLevel(int level) {
        RcLog.level = level;
    }

    public static void v(String tag, String msg) {
        if (level > VERBOSE) return;
        Log.v(TAG, "[" + tag + "] " + msg);
    }

    public static void d(String tag, String msg) {
        if (level > DEBUG) return;
        Log.d(TAG, "[" + tag + "] " + msg);
    }

    public static void i(String tag, String msg) {
        if (level > INFO) return;
        Log.i(TAG, "[" + tag + "] " + msg);
    }

    public static void w(String tag, String msg) {
        if (level > WARN) return;
        Log.w(TAG, "[" + tag + "] " + msg);
    }

    public static void e(String tag, String msg) {
        if (level > ERROR) return;
        Log.e(TAG, "[" + tag + "] " + msg);
    }
}
