/**********************************************
 * Filename：
 * Author:   chen.qiang@landmark-phb.cn
 * Description：
 * Date：
 * Version:
 * History:
 *------------------------------------------------------
 * Version  date      author   description
 * V0.xx  2021/6/9  chenqiang   1
 ***********************************************/

package com.landmark.mediasessionlib.model.utils;


import android.util.Log;

/**
 * Settings LogUtil util
 * Utilities and constants for Log.
 */

public final class LogUtils {

    /**
     * APP aggregateLog TAG.
     */
    private static final String APP_TAG = "Media";

    /**
     * mAggregateLog flag.
     */
    private static boolean mAggregateLog = true;

    /**
     * Verbose mode log.
     *
     * @param tag
     *            - The Category of logging purpose.
     * @param msg
     *            - Log statement for detailed info.
     */
    public static void verbose(String tag, String msg) {
        if (mAggregateLog) {
            Log.v(APP_TAG, tag + " " + msg);
        } else {
            Log.v(tag, msg);
        }
    }

    /**
     * Debug mode log.
     *
     * @param tag
     *            - The Category of logging purpose.
     * @param msg
     *            - Log statement for detailed info.
     */
    public static void debug(String tag, String msg) {
        if (mAggregateLog) {
            Log.d(APP_TAG, tag + " " + msg);
        } else {
            Log.d(tag, msg);
        }
    }

    /**
     * Info mode log.
     *
     * @param tag
     *            - The Category of logging purpose.
     * @param msg
     *            - Log statement for detailed info.
     * @param throwable
     *            - Any Throwable instance.
     */
    public static void info(String tag, String msg, Throwable throwable) {
        if (mAggregateLog) {
            Log.i(APP_TAG, tag + " " + msg, throwable);
        } else {
            Log.i(tag, msg, throwable);
        }
    }

    /**
     * Info mode log.
     *
     * @param tag
     *            - The Category of logging purpose.
     * @param msg
     *            - Log statement for detailed info.
     */
    public static void info(String tag, String msg) {
        if (mAggregateLog) {
            Log.i(APP_TAG, tag + " " + msg);
        } else {
            Log.i(tag, msg);
        }
    }

    /**
     * Warning mode log.
     *
     * @param tag
     *            - The Category of logging purpose.
     * @param msg
     *            - Log statement for detailed info.
     */
    public static void warning(String tag, String msg) {
        if (mAggregateLog) {
            Log.w(APP_TAG, tag + " " + msg);
        } else {
            Log.w(tag, msg);
        }
    }

    /**
     * Warning mode log.
     *
     * @param tag
     *            - The Category of logging purpose.
     * @param msg
     *            - Log statement for detailed info.
     * @param throwable
     *            - Any Throwable instance.
     */
    public static void warning(String tag, String msg, Throwable throwable) {
        if (mAggregateLog) {
            Log.w(APP_TAG, tag + " " + msg, throwable);
        } else {
            Log.w(tag, msg, throwable);
        }
    }

    /**
     * Error mode log.
     *
     * @param tag
     *            - The Category of logging purpose.
     * @param msg
     *            - Log statement for detailed info.
     * @param throwable
     *            - Any Throwable instance.
     */
    public static void error(String tag, String msg, Throwable throwable) {
        if (mAggregateLog) {
            Log.e(APP_TAG, tag + " " + msg, throwable);
        } else {
            Log.e(tag, msg, throwable);
        }
    }

    /**
     * Error mode log.
     *
     * @param tag
     *            - The Category of logging purpose.
     * @param msg
     *            - Log statement for detailed info.
     */
    public static void error(String tag, String msg) {
        if (mAggregateLog) {
            Log.e(APP_TAG, tag + " " + msg);
        } else {
            Log.e(tag, msg);
        }
    }

}

