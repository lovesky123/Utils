package com.example.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@SuppressLint("SimpleDateFormat")
public class TimeTool {

    public static String sdf1 = "HH:mm";
    public static String sdf2 = "yyyy-MM-dd";
    public static String sdf3 = "yyyy-MM-dd HH:mm";
    public static String sdf4 = "yyyy-MM-dd HH:mm:ss";
    public static String sdf5 = "MM/dd HH:mm";
    public static String sdf6 = "MM/dd HH:mm:ss";
    public static String sdf7 = "MM-dd HH:mm";
    public static String sdf8 = "yyyy/MM/dd HH:mm:ss";
    public static String sdf9 = "yyyy/MM/dd HH:mm";


    /**
     * 描述：格式化为时 分
     *
     * @param timeMill
     * @return
     * @return_type：String
     */
    public static String format1(long timeMill) {
        Date date = new Date(timeMill);
        return new SimpleDateFormat(sdf1).format(date);
    }

    /**
     * 描述：格式化为年月日
     *
     * @param timeMill
     * @return
     * @return_type：String
     */
    public static String format2(long timeMill) {
        Date date = new Date(timeMill);
        return new SimpleDateFormat(sdf2).format(date);
    }

    /**
     * 描述：格式化为年月日时分
     *
     * @param timeMill
     * @return
     * @return_type：String
     */
    public static String format3(long timeMill) {
        Date date = new Date(timeMill);
        return new SimpleDateFormat(sdf3).format(date);
    }

    /**
     * 描述：格式化为年月日时分秒
     *
     * @param timeMill
     * @return
     * @return_type：String
     */
    public static String format4(long timeMill) {
        Date date = new Date(timeMill);
        return new SimpleDateFormat(sdf4).format(date);
    }

    public static String format5(long timeMill) {
        Date date = new Date(timeMill);
        return new SimpleDateFormat(sdf5).format(date);
    }

    public static String format6(long timeMill) {
        Date date = new Date(timeMill);
        return new SimpleDateFormat(sdf6).format(date);
    }

    public static String format7(long timeMill) {
        Date date = new Date(timeMill);
        return new SimpleDateFormat(sdf7).format(date);
    }

    public static String format8(long timeMill) {
        Date date = new Date(timeMill);
        return new SimpleDateFormat(sdf8).format(date);
    }

    public static String format9(long timeMill) {
        Date date = new Date(timeMill);
        return new SimpleDateFormat(sdf9).format(date);
    }

    @SuppressWarnings("deprecation")
    public static String format4DBDate(String timeStr) {
        if (TextUtils.isEmpty(timeStr)) {
            return "";
        }
        Date date = new Date(timeStr);
        return new SimpleDateFormat(sdf4).format(date);
    }

    public static boolean isBeforeToday(long timeMill) {
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(sdf2);
        String todayString = sdf.format(today);
        try {
            today = sdf.parse(todayString);
            Date date = new Date(timeMill);
            return date.before(today);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static long dateToTime(String date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        long time = 0;
        try {
            time = dateFormat.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * sdf4
     *
     * @param date
     * @return
     */
    public static long dateToMill(String date) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        format.setTimeZone(TimeZone.getDefault());
        long time = 0;
        try {
            time = format.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }


    /**
     * 根据参数回滚时间
     *
     * @param timeMill 时间参数
     * @param days     回滚天数
     * @return
     */
    public static String rollDateByDay(long timeMill, int days) {
        long backMill = (long) days * 24 * 60 * 60 * 1000;
        return new SimpleDateFormat(sdf4).format(timeMill - backMill);
    }

    /**
     * 根据当前时间回滚月份
     *
     * @param count 月的个数
     * @return
     */
    public static String rollDateByMonth(int count) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(sdf4);
        if (count == 0) {
            return sdf.format(c.getTime());
        }
        int currentMonth = c.get(Calendar.MONTH) + 1;
        if (count < 0) {
            if (Math.abs(count) >= currentMonth) {
                c.roll(Calendar.YEAR, -(1 + (Math.abs(count) - currentMonth) / 12));
            }
        } else {
            if (count > 12 - currentMonth) {
                c.roll(Calendar.YEAR, (count + currentMonth) / 12);
            }
        }

        c.roll(Calendar.MONTH, count);
        return sdf.format(c.getTime());
    }

    /**
     * 判断是否为符合sdf4的字符串
     *
     * @param str
     * @return
     */
    public static boolean isValidFormat(String str) {
        try {
            new SimpleDateFormat(sdf4).parse(str);
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String format4DBDate(String timeStr, SimpleDateFormat sdf) {
        if (TextUtils.isEmpty(timeStr)) {
            return "";
        }
        Date date = new Date(timeStr);
        if(sdf == null) {
            return new SimpleDateFormat(sdf4).format(date);
        }
        return sdf.format(date);
    }
}
