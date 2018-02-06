package com.skj.wheel.util;

import android.annotation.SuppressLint;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by 孙科技 on 2017/4/26.
 */

public class TimeUtil {


    /**
     * 取得当前系统时间戳
     */
    public static Long getCurrentTime() {
        return System.currentTimeMillis();
    }

    /**
     * 检查时间间隔
     *
     * @param last    需要检查的时间，毫秒
     * @param timeVal 时间间隔,毫秒
     * @return true-超过了timeVal时间间隔;false-没超过timeVal时间间隔
     */
    public static boolean checkTimeVal(long last, long timeVal) {
        long current = System.currentTimeMillis();
        if ((current - last) > timeVal) {
            return true;
        }
        return false;
    }

    /**
     * @param time
     * @return true-为当天;false-不为当天
     * @Title: checkTimeIsCurrentDay
     * @Description: TODO
     * @return: boolean
     */
    public static boolean checkTimeIsCurrentDay(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        int lastDay = cal.get(Calendar.DAY_OF_YEAR);
        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        if (lastDay == currentDay) {
            return true;
        }
        return false;
    }


    private static String[] str = new String[]{"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd", "MM月dd日 HH:mm",
            "yyyy年MM月", "yyyy.M.d HH:mm", "HH:mm:ss", "yyyy-MM-dd HH:mm"};

    /**
     * 毫秒数转String
     */
    public static String MillisToStr(Long timeMillis) {
        if (timeMillis == null)
            return "";
        Date date = new Timestamp(timeMillis);
        SimpleDateFormat sf = new SimpleDateFormat(str[0]);
        return sf.format(date);
    }

    public static String MillisToStr(Long timeMillis, int type) {
        if (timeMillis == null)
            return "";
        Date date = new Timestamp(timeMillis);
        SimpleDateFormat sf = new SimpleDateFormat(str[type]);
        return sf.format(date);
    }

    /**
     * String 转毫秒数
     *
     * @param timeStr
     * @return
     */
    public static Long StrToMillis(String timeStr) {
        if (TextUtil.isEmpty(timeStr))
            return System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(str[0]);//24小时制
        Long millis = null;
        try {
            millis = simpleDateFormat.parse(timeStr).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return millis;
    }

    /**
     * 根据日期比较当前日期计算时间差
     *
     * @param timeMillis
     * @return
     */
    public static String toTime(Long timeMillis) {
        if (timeMillis == null)
            return "";
        StringBuffer result = new StringBuffer();
        long diff = System.currentTimeMillis() - timeMillis;
        Calendar oldTimeCalendar = Calendar.getInstance();
        oldTimeCalendar.setTimeInMillis(timeMillis);
        int oldYear = oldTimeCalendar.get(Calendar.YEAR);
        int oldMonth = oldTimeCalendar.get(Calendar.MONTH) + 1;
        int oldDay = oldTimeCalendar.get(Calendar.DAY_OF_MONTH);
        if (diff < 0) {
            return result.append(oldYear).append("-").append(oldMonth).append("-").append(oldDay).toString();
        }
        int days = (int) (diff / (24 * 60 * 60 * 1000));
        int minutes = (int) (diff / (60 * 1000));
        if (days < 1) {
            if (minutes >= 60) {
                result.append(minutes / 60).append("小时前");
            } else {
                if (minutes == 0) {
                    result.append("1分钟前");
                } else {
                    result.append(minutes).append("分钟前");
                }
            }
        } else if (days == 1) {
            result.append("昨天");
        } else if (days == 2) {
            result.append("前天");
        } else if (days == 3) {
            result.append("三天前");
        } else {
            result.append(oldYear).append("-").append(oldMonth).append("-").append(oldDay);
        }
        return result.toString();
    }


    public static boolean getTimeDiff(long timeMillis) {
        StringBuffer result = new StringBuffer();
        long diff = System.currentTimeMillis() - timeMillis;
        Calendar oldTimeCalendar = Calendar.getInstance();
        oldTimeCalendar.setTimeInMillis(timeMillis);
        if (diff < 0) {
            return true;
        }
        int days = (int) (diff / (24 * 60 * 60 * 1000));
        if (days < 1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 时间格式转化 String转Date
     *
     * @param dateString
     * @param type
     * @return
     */
    public static Date getDate(String dateString, int type) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(str[type]);
            Date date = sdf.parse(dateString);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date getDate(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(str[1]);
            Date date = sdf.parse(dateString);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 得到格式化后的日期，string 转不同string
     *
     * @param dateString
     * @param type
     * @return
     */
    public static String getFormatDate(String dateString, int type) {
        String date = getFormatDate(getDate(dateString), str[type]);
        return date.replace("-", ".");
    }

    public static String getFormatDate(Date currDate, String format) {
        if (currDate == null) {
            return "";
        }
        SimpleDateFormat dtFormatdB = null;
        try {
            dtFormatdB = new SimpleDateFormat(format);
            return dtFormatdB.format(currDate);
        } catch (Exception e) {
            dtFormatdB = new SimpleDateFormat(str[0]);
            try {
                return dtFormatdB.format(currDate);
            } catch (Exception ex) {
            }
        }
        return null;
    }


    /**
     * 时间格式转化 date转Long
     *
     * @param date
     * @return
     */
    public static Long getMillis(Date date) {
        return date.getTime() / 1000;
    }

    public static Long getMillis(String dateString) {
        Date date = getDate(dateString);
        return date.getTime();
    }

    /**
     * 获取当天的零点时间
     *
     * @return
     */
    public static long getTimesmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis());
    }

    /**
     * 获取昨天的开始时间
     *
     * @return
     */
    public static Long getYesterdayBegin(int day) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, day);
        String yesterday = new SimpleDateFormat("yyyy-MM-dd ").format(cal.getTime()) + "00:00:00";
        Date date = TimeUtil.getDate(yesterday, 1);
        return date.getTime();
    }

    public static Long getYesterdayBegin() {
        return getYesterdayBegin(-1);
    }

    /**
     * 获取昨天的结束时间
     *
     * @return
     */
    public static Long getYesterdayEnd(int day) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, day);
        String yesterday = new SimpleDateFormat("yyyy-MM-dd ").format(cal.getTime()) + "23:59:59";
        Date date = TimeUtil.getDate(yesterday, 1);
        return date.getTime();
    }

    public static Long getYesterdayEnd() {
        return getYesterdayEnd(-1);
    }

    /**
     * 取某一天的日期
     *
     * @return
     */
    public static String getDayStr(int day) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, day);
        String yesterday = new SimpleDateFormat("yyyy-MM-dd ").format(cal.getTime());
        return yesterday;
    }

    /**
     * 获得本月第一天0点时间
     *
     * @return
     */
    @SuppressLint("WrongConstant")
    public static int getTimesMonthmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return (int) (cal.getTimeInMillis());
    }

    /**
     * 计算当前一年以内的所有月份
     *
     * @return
     */
    public static List<String> findDates() {
        List<String> dateList = new ArrayList();
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.YEAR, -1);//当前时间减去一年，即一年前的时间

            String start = TimeUtil.getFormatDate(cal.getTime(), str[4]);
            String end = TimeUtil.MillisToStr(TimeUtil.getCurrentTime(), 4);
            SimpleDateFormat sdf = new SimpleDateFormat(str[4]);
            Date dBegin = sdf.parse(start);
            Date dEnd = sdf.parse(end);

            dateList.add(start);
            Calendar calBegin = Calendar.getInstance();
            calBegin.setTime(dBegin);
            Calendar calEnd = Calendar.getInstance();
            calEnd.setTime(dEnd);
            while (dEnd.after(calBegin.getTime())) {
                // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
                calBegin.add(Calendar.MONTH, 1);
                dateList.add(sdf.format(calBegin.getTime()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.reverse(dateList);
        return dateList;
    }
}
