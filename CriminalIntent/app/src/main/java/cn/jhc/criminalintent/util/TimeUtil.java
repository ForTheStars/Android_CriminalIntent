package cn.jhc.criminalintent.util;

import android.text.format.DateFormat;

import java.util.Date;

/**
 * Created by Administrator on 2016-11-27.
 */
public class TimeUtil {
    private static final String DATE_FORMAT = "yyyy年MM月dd日 hh:mm:ss a";
    public static String DateFormat(Date date) {
        return DateFormat.format(DATE_FORMAT,date).toString();
    }
}
