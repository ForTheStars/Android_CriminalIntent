package cn.jhc.criminalintent.util;

import android.util.Log;

/**
 * Created by Administrator on 2016-12-01.
 */
public class LogUtils {
    public final static String LOG_TAG = "TAG";
    public static boolean enableLog = true;

    public static void e(String tag,String msg) {
        if(enableLog) {
            Log.e(tag,msg);
        }
    }
    public static void w(String tag,String msg) {
        if(enableLog) {
            Log.w(tag,msg);
        }
    }
    public static void i(String tag,String msg) {
        if(enableLog) {
            Log.i(tag,msg);
        }
    }
    public static void d(String tag,String msg) {
        if(enableLog) {
            Log.d(tag, msg);
        }
    }
    public static void v(String tag,String msg) {
        if(enableLog) {
            Log.v(tag, msg);
        }
    }
}
