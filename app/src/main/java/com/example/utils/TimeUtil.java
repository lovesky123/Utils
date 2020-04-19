package com.example.utils;

import android.text.TextUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Xuze on 2015/9/4.
 */
public class TimeUtil {
    private static SimpleDateFormat sSimpleDateFormat = new SimpleDateFormat();
    public static final String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_DATE = "yyyy-MM-dd";
    public static final String FORMAT_TIME = "HH:mm";
    public static final String FORMAT_MONTH_DAY_TIME = "MM-dd HH:mm";
    private static final int YEAR = 365 * 24 * 60 * 60;
    private static final int MONTH = 30 * 24 * 60 * 60;
    private static final int DAY = 24 * 60 * 60;
    private static final int HOUR = 60 * 60;
    private static final int MINUTE = 60;
    private static final int PARTION_SECONDS = 30;

    /**
     * @param time ms
     * @return
     */
    public static int getTotalHour(long time) {
        if (time <= 0) {
            return 0;
        }
        return (int) (time / ((long) HOUR * 1000));
    }

    /**
     * 获取天数
     *
     * @param time ms
     * @return
     */
    public static int getDays(long time) {
        return getTotalHour(time) / 24;
    }

    /**
     * 获取小时
     *
     * @param time ms
     * @return
     */
    public static int getHour(long time) {
        return getTotalHour(time) % 24;
    }

    /**
     * 获取分钟
     *
     * @param time ms
     * @return
     */
    public static int getMinute(long time) {
        int hour = getTotalHour(time);
        long other = time - (long) hour * HOUR * 1000;
        int result = (int) (other / ((long) MINUTE * 1000));
        return result;
    }

    /**
     * 获取秒
     *
     * @param time
     * @return
     */
    public static int getSecond(long time) {
        int hour = getTotalHour(time);
        int minute = getMinute(time);
        long other = time - (long) hour * HOUR * 1000 - (long) minute * MINUTE * 1000;
        return (int) (other / 1000);
    }

    /**
     * 获取半点时间
     *
     * @param minute
     * @return 例如：getHalfTime(11, 40)=11:30   getHalfTime(9, 14)=09:00
     */
    public static String getHalfTime(int hour, int minute) {
        return (hour >= 10 ? hour + "" : "0" + hour) + ":" + (minute >= 30 ? "30" : "00");
    }

    /**
     * 获取指定格式的当前日期
     *
     * @param format
     * @return
     */
    public static Date getCurrentDate(String format) {
        Date date = new Date();
        try {
            return sSimpleDateFormat.parse(getCurrentTime(format));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取当前日期的指定格式的字符串
     *
     * @param format 指定的时间格式，为空时返回yyyy-MM-dd HH:mm
     * @return
     */
    public static String getCurrentTime(String format) {
        if (StringUtil.isEmpty(format)) {
            sSimpleDateFormat.applyPattern(FORMAT_DATE_TIME);
        } else {
            sSimpleDateFormat.applyPattern(format);
        }
        return sSimpleDateFormat.format(new Date());
    }

    /**
     * 将日期字符串以指定格式转换为Date
     *
     * @param timeStr
     * @param format  指定的时间格式，为空时返回yyyy-MM-dd HH:mm
     * @return
     */
    public static Date getTimeFromString(String timeStr, String format) {
        if (StringUtil.isEmpty(format)) {
            sSimpleDateFormat.applyPattern(FORMAT_DATE_TIME);
        } else {
            sSimpleDateFormat.applyPattern(format);
        }
        try {
            return sSimpleDateFormat.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    /**
     * 将Date转换为指定格式的字符串
     *
     * @param time
     * @param format 指定的时间格式，为空时返回yyyy-MM-dd HH:mm
     * @return
     */
    public static String getStringFromTime(Date time, String format) {
        if (StringUtil.isEmpty(format)) {
            sSimpleDateFormat.applyPattern(FORMAT_DATE_TIME);
        } else {
            sSimpleDateFormat.applyPattern(format);
        }
        return sSimpleDateFormat.format(time);
    }

    /**
     * 根据时间戳获取指定格式的字符串
     *
     * @param timestamp
     * @param format    时间格式
     * @return 1.format为空时：当年，返回MM-dd HH:mm 不当年，返回yyyy-MM-dd HH:mm 2.format不为空，返回指定格式字符串
     */
    public static String getFormatTimeFromTimestamp(long timestamp, String format) {
        if (StringUtil.isEmpty(format)) {
            sSimpleDateFormat.applyPattern(FORMAT_DATE);
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            int year = Integer.parseInt(sSimpleDateFormat.format(new Date(timestamp)).substring(0, 4));
            if (currentYear == year) {
                sSimpleDateFormat.applyPattern(FORMAT_MONTH_DAY_TIME);
            } else {
                sSimpleDateFormat.applyPattern(FORMAT_DATE_TIME);
            }
        } else {
            sSimpleDateFormat.applyPattern(format);
        }
        Date date = new Date(timestamp);
        return sSimpleDateFormat.format(date);
    }

    /**
     * 判断是否是当月
     *
     * @param timestamp
     * @return
     */
    public static boolean isCurrentMonth(long timestamp) {
        long currentTime = System.currentTimeMillis();
        long timeGap = (currentTime - timestamp) / 1000;
        if (timeGap <= MONTH) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否是当月
     *
     * @param year
     * @param month
     * @return
     */
    public static boolean isCurrentMonth(String year, String month) {
        if (TextUtils.isEmpty(year) || TextUtils.isEmpty(month)) {
            return false;
        }
        int y_s = Integer.parseInt(year);
        int m_s = Integer.parseInt(month);
        long timestamp = System.currentTimeMillis();
        int y = getYear(timestamp);
        int m = getMonth(timestamp);
        if (y_s == y && m_s == m) {
            return true;
        }
        return false;
    }

    /**
     * 根据时间戳返回描述性的时间
     *
     * @param timestamp
     * @return 如：2个月前， 1小时前，45秒前
     */
    public static String getDescriptionTimeFromTimestamp(long timestamp) {
        long currentTime = System.currentTimeMillis();
        long timeGap = (currentTime - timestamp) / 1000;
        String timeStr = "";
        if (timeGap > YEAR) {
            timeStr = timeGap / YEAR + "年前";
        } else if (timeGap > MONTH) {
            timeStr = timeGap / MONTH + "个月前";
        } else if (timeGap > DAY) {
            timeStr = timeGap / DAY + "天前";
        } else if (timeGap > HOUR) {
            timeStr = timeGap / HOUR + "小时前";
        } else if (timeGap > MINUTE) {
            timeStr = timeGap / MINUTE + "分钟前";
        } else if (timeGap > PARTION_SECONDS) {
            timeStr = timeGap + "秒前";
        } else {
            timeStr = "刚刚";
        }
        return timeStr;
    }

    /**
     * 根据时间戳和时间间隔数获取时间字符串
     *
     * @param timestamp
     * @param partionSeconds
     * @param format
     * @return 1.时间间隔 >= partionSeconds，返回指定格式的字符串   2.时间间隔 < partionSeconds, 返回描述性的时间字符串
     */
    public static String getMixTimeFromTimestamp(long timestamp, long partionSeconds, String format) {
        long currentTime = System.currentTimeMillis();
        long timeGap = (currentTime - timestamp) / 1000;
        if (timeGap >= partionSeconds) {
            return getFormatTimeFromTimestamp(timestamp, format);
        } else {
            return getDescriptionTimeFromTimestamp(timestamp);
        }
    }

    /**
     * 判断是否是同一天
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDay(long date1, long date2) {
        long days1 = date1 / (1000 * 60 * 60 * 24);
        long days2 = date2 / (1000 * 60 * 60 * 24);
        return days1 == days2;
    }

    /**
     * 获取增加多少个月后的时间
     *
     * @param addMonth
     * @return
     */
    public static Date getAddMonthDate(int addMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, addMonth);
        return calendar.getTime();
    }

    /**
     * 获取增加多少天后的时间
     *
     * @param addDay
     * @return
     */
    public static Date getAddDayDate(int addDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, addDay);
        return calendar.getTime();
    }

    /**
     * 获取增加多少小时后的时间
     *
     * @param addHour
     * @return
     */
    public static Date getAddHourDate(int addHour) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, addHour);
        return calendar.getTime();
    }


    /**
     * 获取年
     *
     * @param cc_time 毫秒
     * @return
     */
    public static int getYear(long cc_time) {
        String re_StrTime = null;
        SimpleDateFormat year = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = year.format(new Date(lcc_time));
        return Integer.valueOf(re_StrTime.substring(0, 4));
    }

    /**
     * 获取月
     *
     * @param cc_time 毫秒
     * @return
     */
    public static int getMonth(long cc_time) {
        String re_StrTime = null;
        SimpleDateFormat month = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = month.format(new Date(lcc_time));
        return Integer.valueOf(re_StrTime.substring(5, 7));
    }

    /**
     * 获取日
     *
     * @param cc_time 毫秒
     * @return
     */
    public static int getDay(long cc_time) {
        String re_StrTime = null;
        SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = day.format(new Date(lcc_time));
        return Integer.valueOf(re_StrTime.substring(8, 10));
    }

    /**
     * 获取星期
     *
     * @param cc_time 毫秒
     * @return
     */
    //
    public static String getWeek(long cc_time) {
        String re_StrTime = null;
        String[] weeks = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        int week_index = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long lcc_time = Long.valueOf(cc_time);
            re_StrTime = sdf.format(new Date(lcc_time));
            Date date = sdf.parse(re_StrTime);

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
            if (week_index < 0) {
                week_index = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return weeks[week_index];
    }

    /**
     * 根据服务器的时间搓获取当前的小时
     *
     * @param cc_time
     * @return
     */
    public static String getTimeByHourAndMinutes(long cc_time) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        String date = sdf.format(new Date(cc_time * 1000));
        return date;

    }

    public static String getYesrAndMonth(long cc_time) {
        String date;
        date = getMonth(cc_time) + "月" + getDay(cc_time) + "日";
        return date;
    }

    public static String getData(long cc_time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date = sdf.format(new Date(cc_time * 1000));
        return date;
    }


    private static Date getDateByString(String time) {
        Date date = null;
        if (time == null)
            return date;



        String date_format = time.length()==16? "yyyy-MM-dd HH:mm":"yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(date_format);
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();

        }
        return date;
    }


    public static String getShortTime(String time) {

        Date date = getDateByString(time);
        if (date == null) return time;
        String shortstring ="";
        long now = Calendar.getInstance().getTimeInMillis();
        long deltime = (now - date.getTime()) / 1000;
        if (deltime > 365 * 24 * 60 * 60) {
            return   time;
        } else if (deltime > 24 * 60 * 60) {
            shortstring = (int) (deltime / (24 * 60 * 60)) + "天前";
        } else if (deltime > 60 * 60) {
            shortstring = (int) (deltime / (60 * 60)) + "小时前";
        } else if (deltime > 60) {
            shortstring = (int) (deltime / (60)) + "分钟前";
        } else if (deltime > 1) {
            shortstring = deltime + "秒前";
        } else {
            shortstring = "1秒前";
        }
        return shortstring;
    }


    /**
     * 将日期装换成时间戳
     *
     * @param time
     * @return
     */
    public static String getTime(String time) {
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d;
        try {
            d = sdf.parse(time);
            long l = d.getTime();
            re_time = String.valueOf(l);
            re_time = re_time.substring(0, 10);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return re_time;
    }

    /**
     * @param dateline s
     * @return
     */
    public static String timestampToStr(long dateline) {
        Timestamp timestamp = new Timestamp(dateline * 1000);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return df.format(timestamp);
    }

    /**
     * 格式化时间
     *
     * @param dateline
     * @return
     */
    public static String formatMessageTime(long dateline) {
        Timestamp timestamp = new Timestamp(dateline);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(timestamp);
    }

    /**
     * 是否是7天时间之内
     * @param createTime
     * @return
     */
    public static boolean isLastWeekDayInit(long createTime) {
        Date now = new Date();
        Date addtime = new Date();
        addtime.setTime(createTime);

        Calendar calendar = Calendar.getInstance();  //得到日历
        calendar.setTime(now);//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, -7);  //设置为7天前
        Date before7days = calendar.getTime();   //得到7天前的时间
        if (before7days.getTime() < addtime.getTime()) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * @param time 单位：s
     * @return
     */
    public static String getTimeDesYMD(String time) {
        if (StringUtil.isEmpty(time) || "0".equals(time)) {
            return "";
        }
        return getFormatTimeFromTimestamp(Long.parseLong(time) * 1000, "yyyy-MM-dd");
    }


    /**
     * @param time 单位：s
     * @return
     */
    public static String getTimeDesYMDHM(String time) {
        if (StringUtil.isEmpty(time) || "0".equals(time)) {
            return "";
        }
        return getFormatTimeFromTimestamp(Long.parseLong(time) * 1000, "yyyy-MM-dd HH:mm");
    }

    /**
     * @param time 单位：s
     * @return
     */
    public static String getTimeDesYMDHMS(String time) {
        if (StringUtil.isEmpty(time) || "0".equals(time)) {
            return "";
        }
        return getFormatTimeFromTimestamp(Long.parseLong(time) * 1000, "yyyy-MM-dd HH:mm:ss");
    }
}
