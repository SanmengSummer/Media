package com.landmark.mediasessionlib.utils;

import android.util.Log;

/**
 * Author: chenhuaxia
 * Description: Log utils.
 * Date: 2021.06.08
 **/
public class LogUtils {

    private static final String TAG = "LogUtil";
    private static boolean debuggable = true;

    /**
     * Warn level log.
     *
     * @param msg String
     */
    public static void warn(String msg) {
        Log.w(TAG, msg);
    }

    /**
     * Warn level log.
     *
     * @param msg       String
     * @param exception Exception trace
     */
    public static void warn(String msg, Exception exception) {
        Log.w(TAG, msg, exception);
    }

    /**
     * Debug level log.
     *
     * @param msg String
     */
    public static void debug(String msg) {
        if (debuggable) {
            Log.d(TAG, msg);
        }
    }

    /**
     * Error level log.
     *
     * @param msg String
     */
    public static void error(String msg) {
        Log.e(TAG, "error:" + msg);
    }

    /**
     * What a Terrible Failure.
     *
     * @param exception Exception
     */
    public static void wtf(Exception exception) {
        Log.wtf(TAG, exception);
    }

}