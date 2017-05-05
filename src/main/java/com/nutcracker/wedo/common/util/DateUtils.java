package com.nutcracker.wedo.common.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by huh on 2017/2/21.
 */
public class DateUtils {

    public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public DateUtils() {
    }

    public static Date getNextDay(Date dt) {
        return getNextDay(dt, Long.valueOf(1L));
    }

    public static Date getNextDay(Date dt, Long n) {
        Date date = org.apache.commons.lang.time.DateUtils.addDays(dt, n.intValue());
        date = org.apache.commons.lang.time.DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
        return date;
    }

    public static Date getLastNDay(Date dt, Long n) {
        return getNextDay(dt, Long.valueOf(-n.longValue()));
    }

    public static int compareDateByDay(Date date1, Date date2) {
        return compareDate(date1, date2, 14);
    }

    public static int compareDate(Date date1, Date date2, int precision) {
        Date d1 = org.apache.commons.lang.time.DateUtils.truncate(date1, precision);
        Date d2 = org.apache.commons.lang.time.DateUtils.truncate(date2, precision);
        return d1.compareTo(d2);
    }

    public static Date parseDate(String str, String pattern) {
        if(StringUtils.isBlank(str)) {
            return null;
        } else {
            if(StringUtils.isBlank(pattern)) {
                pattern = "yyyy-MM-dd HH:mm:ss";
            }

            SimpleDateFormat df = new SimpleDateFormat(pattern);
            df.setLenient(true);

            try {
                return df.parse(str);
            } catch (ParseException var4) {
                throw new RuntimeException("日期转化错误:str=" + str + "pattern=" + pattern);
            }
        }
    }

    public static String formatDate(Date date, String pattern) {
        if(date == null) {
            return "";
        } else {
            if(StringUtils.isBlank(pattern)) {
                pattern = "yyyy-MM-dd HH:mm:ss";
            }

            return DateFormatUtils.format(date, pattern);
        }
    }

    public static Date createDate() {
        return new Date();
    }

    public static Date getMaxTime(Date date) {
        if(date == null) {
            return date;
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.set(11, 23);
            c.set(12, 59);
            c.set(13, 59);
            c.set(14, 999);
            return c.getTime();
        }
    }

    public static Date getMinTime(Date date) {
        if(date == null) {
            return date;
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.set(11, 0);
            c.set(12, 0);
            c.set(13, 0);
            c.set(14, 0);
            return c.getTime();
        }
    }

}
