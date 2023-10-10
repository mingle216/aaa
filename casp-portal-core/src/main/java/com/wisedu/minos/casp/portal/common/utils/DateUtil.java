package com.wisedu.minos.casp.portal.common.utils;

import com.wisedu.minos.casp.portal.common.constant.Global;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 功能描述： 日期工具类，提供各种常用的日期处理方法
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title DateUtil
 * @Author: jcx
 * @Date: 2020/7/20
 */
public class DateUtil {

    private static final Logger logger = LogManager.getLogger(DateUtil.class);

    // 日志记录器
    public static final String DATE_FORMATE_STRING_A = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMATE_STRING_B = "yyyy-MM-dd";
    public static final String DATE_FORMATE_STRING_C = "yyyy/MM/dd";
    public static final String DATE_FORMATE_STRING_D = "yyyy/MM/dd HH:mm:ss";
    public static final String DATE_FORMATE_STRING_E = "yyyyMMddHHmmss";
    public static final String DATE_FORMATE_STRING_G = "yyyy-MM-dd 00:00:00";
    public static final String DATE_FORMATE_STRING_H = "yyyy-MM-dd 23:59:59";
    public static final String DATE_FORMATE_STRING_K = "yyyy.MM.dd";

    public static final String DATE_FORMATE_STRING_L = "yyyy";

    // 日期格式yyyy-MM-dd
    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    //并发日期转化安全变量
    private static ThreadLocal threadLocalA = new ThreadLocal() {
        protected synchronized SimpleDateFormat initialValue() {
            return new SimpleDateFormat(DATE_FORMATE_STRING_A);
        }
    };
    private static ThreadLocal threadLocalB = new ThreadLocal() {
        protected synchronized Object initialValue() {
            return new SimpleDateFormat(DATE_FORMATE_STRING_B);
        }
    };
    private static ThreadLocal threadLocalC = new ThreadLocal() {
        protected synchronized Object initialValue() {
            return new SimpleDateFormat(DATE_FORMATE_STRING_C);
        }
    };
    private static ThreadLocal threadLocalD = new ThreadLocal() {
        protected synchronized Object initialValue() {
            return new SimpleDateFormat(DATE_FORMATE_STRING_D);
        }
    };
    private static ThreadLocal threadLocalE = new ThreadLocal() {
        protected synchronized Object initialValue() {
            return new SimpleDateFormat(DATE_FORMATE_STRING_E);
        }
    };

    //注意SimpleDateFormat不是线程安全的
    private static ThreadLocal<SimpleDateFormat> ThreadDateTime = new ThreadLocal<SimpleDateFormat>();
    private static ThreadLocal<SimpleDateFormat> ThreadDate = new ThreadLocal<SimpleDateFormat>();
    private static ThreadLocal<SimpleDateFormat> ThreadTime = new ThreadLocal<SimpleDateFormat>();
    private static SimpleDateFormat dateTimeInstance() {
        SimpleDateFormat df = ThreadDateTime.get();
        if (df == null) {
            df = new SimpleDateFormat(Global.DATETIME_FORMAT);
            ThreadDateTime.set(df);
        }
        return df;
    }
    private static SimpleDateFormat dateInstance() {
        SimpleDateFormat df = ThreadDate.get();
        if (df == null) {
            df = new SimpleDateFormat(Global.DATE_FORMAT);
            ThreadDate.set(df);
        }
        return df;
    }

    private static SimpleDateFormat timeInstance() {
        SimpleDateFormat df = ThreadTime.get();
        if (df == null) {
            df = new SimpleDateFormat(Global.TIME_FORMAT);
            ThreadTime.set(df);
        }
        return df;
    }

    /**
     * 将字符串按照指定格式转换成日期类型，如果没有指定格式，则使用默认格式yyyy-MM-dd
     *
     * @param dateStr 源字符串
     * @param format  日期格式
     * @return 日期类型数据
     */
    public static Date getDateFromStr(String dateStr, String format) {

        if (dateStr == null || dateStr.trim().length() == 0) {
            return null;
        }
        SimpleDateFormat sdf = null;
        try {
            if (format == null || format.trim().length() == 0) {
                // 默认格式
                sdf = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD);
            } else {
                // 指定格式
                sdf = new SimpleDateFormat(format);
            }

            return sdf.parse(dateStr);
        } catch (ParseException e) {
            logger.error("getDateFromStr发生异常："+ e);
            return null;
        }
    }

    /**
     * 将日期类型数据按照指定格式转换成字符串，如果没有指定格式，则使用默认格式yyyy-MM-dd
     *
     * @param date   源日期类型数据
     * @param format 日期格式
     * @return 日期字符串
     */
    public static String getStrFromDate(Date date, String format) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = null;
        if (format == null || format.trim().length() == 0) {
            // 默认格式
            sdf = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD);
        } else {
            // 指定格式
            sdf = new SimpleDateFormat(format);
        }
        return sdf.format(date);
    }

    /**
     * 获取当天的开始时间，即前一天晚上00:00:00
     *
     * @return
     */
    public static Date getStartOfDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

    /**
     * 获取指定日期前几天的日期
     *
     * @param d
     * @param day
     * @return
     */
    public static Date getDateBefore(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return now.getTime();
    }

    /**
     * 获取当天的结束时间，即当天晚上23:59:59
     *
     * @return
     */
    public static Date getEndOfDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        return calendar.getTime();
    }

    /**
     * 查询某年某月的最后一天,注：月份是从0开始的
     *
     * @param year
     * @param month
     * @return
     */
    public static Date getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DATE));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    /**
     * 查询某年某月的第一天,注：月份是从0开始的
     *
     * @param year
     * @param month
     * @return
     */
    public static Date getFirstDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, cal.getMinimum(Calendar.DATE));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取当前的年月, 格式yyyyMM
     *
     * @return 当前的年月
     */
    public static String getCurrentYearMonth() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);

        return year + "" + (month + 1);
    }


    /**
     * 获取当前的年月日, 格式yyyyMMdd
     *
     * @return 当前的年月日
     */
    public static String getCurrentYearMonthDay() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        String result = year + "";
        if (month < 10) {
            result = result + "0" + month;
        } else {
            result = result + month;
        }

        if (day < 10) {
            result = result + "0" + day;
        } else {
            result = result + day;
        }

        return result;
    }

    /**
     * 获取当前的年月日, 格式yyyyMMddHHmmss
     *
     * @return 当前的年月日
     */
    public static String getCurrentYearMonthDayTime() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        String result = year + "";
        if (month < 10) {
            result = result + "0" + month;
        } else {
            result = result + month;
        }

        if (day < 10) {
            result = result + "0" + day;
        } else {
            result = result + day;
        }

        if (hour < 10) {
            result = result + "0" + hour;
        } else {
            result = result + hour;
        }

        if (minute < 10) {
            result = result + "0" + minute;
        } else {
            result = result + minute;
        }

        if (second < 10) {
            result = result + "0" + second;
        } else {
            result = result + second;
        }

        return result;
    }

    /**
     * 获取当前的年月日, 格式yyyy-MM-dd
     *
     * @return 当前的年月日
     */
    public static String getCurrentYearMonthDayOther() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        String result = year + "-";
        if (month < 10) {
            result = result + "0" + month;
        } else {
            result = result + month;
        }

        if (day < 10) {
            result = result + "-0" + day;
        } else {
            result = result + "-" + day;
        }

        return result;
    }

    /**
     * 获取第一个时间参数与第二个时间参数的时间差, 以天为单位
     *
     * @param firstDay   第一个时间
     * @param secondDay  第二个时间
     * @param dateFormat 时间格式, 默认为yyyy-MM-dd HH:mm:ss
     * @return 时间差, 以天为单位
     */
    public static int getTimeDifference(String firstDay, String secondDay, String dateFormat) {
        if (dateFormat == null || dateFormat.trim().length() == 0) {
            dateFormat = "yyyy-MM-dd HH:mm:ss";
        }

        DateFormat df = new SimpleDateFormat(dateFormat);

        try {
            Date d1 = df.parse(firstDay);
            Date d2 = df.parse(secondDay);
            long diff = d1.getTime() - d2.getTime();
            long days = diff / (1000 * 60 * 60 * 24);

            return (int) days;
        } catch (Exception e) {
            logger.error("getTimeDifference发生异常：", e);
            return 0;
        }
    }


    /**
     * 获取第一个时间参数与第二个时间参数的时间差, 以天为单位
     *
     * @param firstDay   第一个时间
     * @param secondDay  第二个时间
     * @param dateFormat 时间格式, 默认为yyyy-MM-dd HH:mm:ss
     * @return 时间差, 以天为单位
     * @throws ParseException
     */
    public static int getTimeDifferenceOther(String firstDay, String secondDay, String dateFormat) throws
            ParseException {
        if (dateFormat == null || dateFormat.trim().length() == 0) {
            dateFormat = "yyyy-MM-dd HH:mm:ss";
        }
        DateFormat df = new SimpleDateFormat(dateFormat);
        Date d1 = df.parse(firstDay);
        Date d2 = df.parse(secondDay);
        long diff = d1.getTime() - d2.getTime();
        long days = diff / (1000 * 60 * 60 * 24);
        return (int) days;
    }

    /**
     * 自动适配两种格式的日期字符串转换为date对象
     * A格式	:	yyyy-MM-dd HH:mm:ss
     * B格式	:	yyyy-MM-dd
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date getDateFromStringAdaptTwoPattern(String date) throws ParseException {
        try {
            return getDateFromString(date, DATE_FORMATE_STRING_A);
        } catch (ParseException e) {
            try {
                return getDateFromString(date, DATE_FORMATE_STRING_B);
            } catch (ParseException e1) {
                return getDateFromString(date, DATE_FORMATE_STRING_C);
            }
        }
    }

    /**
     * 将格式将日期字符串转换为Date对象
     *
     * @param date    字符串
     * @param pattern 格式如下：
     *                yyyyMMddHHmmss
     *                yyyy-MM-dd HH:mm:ss
     *                yyyy-MM-dd
     *                MM/dd/yyyy HH:mm:ss a
     *                yyyy-MM-dd HH:mm:ss a
     *                yyyy-MM-dd'T'HH:mm:ss'Z'
     *                yyyy-MM-dd'T'HH:mm:ssZ
     *                yyyy-MM-dd'T'HH:mm:ssz
     * @return 日期Date对象
     * @throws ParseException
     * @see Date
     */
    public static Date getDateFromString(String date, String pattern) throws ParseException {
        SimpleDateFormat sDateFormat = getDateFormat(pattern);
        return sDateFormat.parse(date);
    }

    /**
     * 根据pattern取得的date formate
     *
     * @param pattern
     * @return
     */
    public static SimpleDateFormat getDateFormat(String pattern) {
        if (DATE_FORMATE_STRING_A.equals(pattern)) {
            return (SimpleDateFormat) threadLocalA.get();
        } else if (DATE_FORMATE_STRING_B.equals(pattern)) {
            return (SimpleDateFormat) threadLocalB.get();
        } else if (DATE_FORMATE_STRING_C.equals(pattern)) {
            return (SimpleDateFormat) threadLocalC.get();
        } else if (DATE_FORMATE_STRING_E.equals(pattern)) {
            return (SimpleDateFormat) threadLocalE.get();
        } else {
            return (SimpleDateFormat) threadLocalD.get();
        }
    }


    /**
     * 获取当前周的周一，每周的第一天为周日，第七天为周六
     *
     * @return
     */
    public static Date getMonday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return cal.getTime();
    }

    /**
     * 获取当前周的开始时间，周一的00:00:00
     *
     * @return
     */
    public static Date getWeekStart() {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取当前周的周日，每周的第一天为周日，第七天为周六
     *
     * @return
     */
    public static Date getSunday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return cal.getTime();
    }

    /**
     * 获取当前周的结束时间，周日的23:59:59
     *
     * @return
     */
    public static Date getWeekEnd() {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }



    /**
     * 获取指定时间格式的一天的开始时间
     * @param dt
     * @param format
     * @return
     * @throws ParseException
     */
    public static Date getDateStartTime(String dt, String format) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = sdf.parse(dt);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();

    }

    /**
     * 获取指定时间格式的一天的结束时间
     * @param dt
     * @param format
     * @return
     * @throws ParseException
     */
    public static Date getDateEndTime(String dt, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = sdf.parse(dt);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * 把毫秒转化成日期
     * @param format(日期格式，例如：yyyy-MM-dd)
     * @param millSec(毫秒数)
     * @return
     */
        public static String transferLongToDate(String format,Long millSec){
            SimpleDateFormat sdf =null;
            if (format == null || format.trim().length() == 0){

                sdf = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD);
            }else {
                sdf =  new SimpleDateFormat(format);
            }
        Date date= new Date(millSec);
        return sdf.format(date);
    }


    public static List<String> getLastDaysString(int days){
        List<String> list = new ArrayList<String>();
        Date now = new Date();
        for(int i=days-1;i>=0;i--){
           Date before = DateUtil.getDateBefore(now,i);
            String dt = getStrFromDate(before,"yyyy-MM-dd");
            list.add(dt);
        }
        return list;
    }


    /**
     * 日期加几月
     */
    public static String dateSubMonth(int months)  {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Calendar rightNow = Calendar.getInstance();
        rightNow.add(Calendar.MONTH, -1*months);
        Date dt1 = rightNow.getTime();
        String reStr = sdf.format(dt1);
        return reStr;
    }

    /**
     * 获取当前日期的到期日期
     *
     * @param days  天数
     * @param startTime 开始日期
     * @return
     */
    public static String dateExpire (String startTime,int days){

        // 时间表示格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date date ;
        if (StringUtil.isNotEmpty(startTime)){
            date = new Date();
        }else {
            // 将字符串的日期转为Date类型，ParsePosition(0)表示从第一个字符开始解析
            date = sdf.parse(startTime, new ParsePosition(0));
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // add方法中的第二个参数n中，正数表示该日期后n天，负数表示该日期的前n天
        calendar.add(Calendar.DATE, days - 1);
        Date dateExpire = calendar.getTime();

        return sdf.format(dateExpire);

    }

    /**
     * 获取当前日期的到期日期
     *
     * @param days  天数
     * @param startTime 开始日期
     * @return
     */
    public static String dateExpire2 (String startTime,int days){

        // 时间表示格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date ;
        if (StringUtil.isNotEmpty(startTime)){
            date = new Date();
        }else {
            // 将字符串的日期转为Date类型，ParsePosition(0)表示从第一个字符开始解析
            date = sdf.parse(startTime, new ParsePosition(0));
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // add方法中的第二个参数n中，正数表示该日期后n天，负数表示该日期的前n天
        calendar.add(Calendar.DATE, days - 1);
        Date dateExpire = calendar.getTime();

        return sdf.format(dateExpire);

    }


    /**
     * 返回两个时间相差的毫秒（）
     * @param time1
     * @param time2
     * @return time1 - time2
     */
    public static Long differenceMilliseconds(Date time1, Date time2) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time1);
        Long mm1 = calendar.getTimeInMillis();
        calendar.setTime(time2);
        Long mm2 = calendar.getTimeInMillis();
        Long dm = mm1 - mm2;
        return dm;
    }

    /***
     * 获取当前时间到第二天凌晨零点的秒数
     */
    public static Long getSecondsNextEarlyMorning(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
    }
    /**
     * 在当前时间的基础上加或减去year年
     *
     * @param year
     * @return
     */
    public static Date year(int year) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.YEAR, year);
        return cal.getTime();
    }

    /**
     * 在指定的时间上加或减去几年
     *
     * @param date
     * @param year
     * @return
     */
    public static Date year(Date date, int year) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, year);
        return cal.getTime();
    }

    /**
     * 在当前时间的基础上加或减去几月
     *
     * @param month
     * @return
     */
    public static Date month(int month) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, month);
        return cal.getTime();
    }

    /**
     * 在指定的时间上加或减去几月
     *
     * @param date
     * @param month
     * @return
     */
    public static Date month(Date date, int month) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, month);
        return cal.getTime();
    }

    /**
     * 在当前时间的基础上加或减去几天
     *
     * @param day
     * @return
     */
    public static Date day(int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, day);
        return cal.getTime();
    }

    /**
     * 在指定的时间上加或减去几天
     *
     * @param date
     * @param day
     * @return
     */
    public static Date day(Date date, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, day);
        return cal.getTime();
    }

    /**
     * 在当前时间的基础上加或减去几小时-支持浮点数
     *
     * @param hour
     * @return
     */
    public static Date hour(float hour) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, (int) (hour * 60));
        return cal.getTime();
    }

    /**
     * 在制定的时间上加或减去几小时-支持浮点数
     *
     * @param date
     * @param hour
     * @return
     */
    public static Date hour(Date date, float hour) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, (int) (hour * 60));
        return cal.getTime();
    }

    /**
     * 在当前时间的基础上加或减去几分钟
     *
     * @param minute
     * @return
     */
    public static Date minute(int minute) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, minute);
        return cal.getTime();
    }

    /**
     * 在制定的时间上加或减去几分钟
     *
     * @param date
     * @param minute
     * @return
     */
    public static Date minute(Date date, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minute);
        return cal.getTime();
    }


    /**
     * 判断字符串是否为日期字符串
     *
     * @param date 日期字符串
     * @return true or false
     */
    public static boolean isDate(String date) {
        try {
            dateTimeInstance().parse(date);
            return true;
        } catch (ParseException e) {
            logger.info("context",e);
        }
        return false;
    }


    /**
     * 时间date1和date2的时间差-单位秒
     *
     * @param date1
     * @param date2
     * @return 秒
     */
    public static long subtract(Date date1, Date date2) {
        long cha = (date2.getTime() - date1.getTime()) / 1000;
        return cha;
    }

    /**
     * 时间date1和date2的时间差-单位秒
     *
     * @param date1
     * @param date2
     * @return 秒
     */
    public static long subtract(String date1, String date2) {
        long rs = 0;
        try {
            Date start = dateTimeInstance().parse(date1);
            Date end = dateTimeInstance().parse(date2);
            long cha = (end.getTime() - start.getTime()) / 1000;
            rs = cha;
        } catch (ParseException e) {
            logger.info("context",e);
        }
        return rs;
    }


    /**
     * 时间date1和date2的时间差 -单位分钟
     *
     * @param date1
     * @param date2
     * @return 分钟
     */
    public static int subtractMinute(String date1, String date2) {
        int rs = 0;
        try {
            Date start = dateTimeInstance().parse(date1);
            Date end = dateTimeInstance().parse(date2);
            long cha = (end.getTime() - start.getTime()) / 1000;
            rs = (int) cha / (60);
        } catch (ParseException e) {
            logger.info("context",e);
        }
        return rs;
    }

    /**
     * 时间date1和date2的时间差-单位分钟
     *
     * @param date1
     * @param date2
     * @return 分钟
     */
    public static int subtractMinute(Date date1, Date date2) {
        long cha = date2.getTime() - date1.getTime();
        return (int) cha / (1000 * 60);
    }

    /**
     * 时间date1和date2的时间差-单位小时
     *
     * @param date1
     * @param date2
     * @return 小时
     */
    public static int subtractHour(Date date1, Date date2) {
        long cha = (date2.getTime() - date1.getTime()) / 1000;
        return (int) cha / (60 * 60);
    }

    /**
     * 时间date1和date2的时间差-单位小时
     *
     * @param date1
     * @param date2
     * @return 小时
     */
    public static int subtractHour(String date1, String date2) {
        int rs = 0;
        try {
            Date start = dateTimeInstance().parse(date1);
            Date end = dateTimeInstance().parse(date2);
            long cha = (end.getTime() - start.getTime()) / 1000;
            rs = (int) cha / (60 * 60);
        } catch (ParseException e) {
            logger.info("context",e);
        }
        return rs;
    }


    /**
     * 时间date1和date2的时间差-单位天
     *
     * @param date1
     * @param date2
     * @return 天
     */
    public static int subtractDay(String date1, String date2) {
        int rs = 0;
        try {
            Date start = dateTimeInstance().parse(date1);
            Date end = dateTimeInstance().parse(date2);
            long sss = (end.getTime() - start.getTime()) / 1000;
            rs = (int) sss / (60 * 60 * 24);
        } catch (ParseException e) {
            logger.info("context",e);
        }
        return rs;
    }

    /**
     * 时间date1和date2的时间差-单位天
     *
     * @param date1
     * @param date2
     * @return 天
     */
    public static int subtractDay(Date date1, Date date2) {
        long cha = date2.getTime() - date1.getTime();
        return (int) cha / (1000 * 60 * 60 * 24);
    }

    /**
     * 时间date1和date2的时间差-单位月
     *
     * @param date1
     * @param date2
     * @return 月
     */
    public static int subtractMonth(String date1, String date2) {
        int result;
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c1.setTime(dateInstance().parse(date1));
            c2.setTime(dateInstance().parse(date2));
            int year1 = c1.get(Calendar.YEAR);
            int month1 = c1.get(Calendar.MONTH);
            int year2 = c2.get(Calendar.YEAR);
            int month2 = c2.get(Calendar.MONTH);
            if (year1 == year2) {
                result = month2 - month1;
            } else {
                result = 12 * (year2 - year1) + month2 - month1;
            }
        } catch (ParseException e) {
            logger.info("context",e);
            result = -1;
        }
        return result;
    }

    /**
     * 时间date1和date2的时间差-单位月
     *
     * @param date1
     * @param date2
     * @return 月
     */
    public static int subtractMonth(Date date1, Date date2) {
        int result;
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(date1);
        c2.setTime(date2);
        int year1 = c1.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH);
        int year2 = c2.get(Calendar.YEAR);
        int month2 = c2.get(Calendar.MONTH);
        if (year1 == year2) {
            result = month2 - month1;
        } else {
            result = 12 * (year2 - year1) + month2 - month1;
        }
        return result;
    }

    /**
     * 时间date1和date2的时间差-单位年
     *
     * @param date1
     * @param date2
     * @return 年
     */
    public static int subtractYear(String date1, String date2) {
        int result;
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c1.setTime(dateInstance().parse(date1));
            c2.setTime(dateInstance().parse(date2));
            int year1 = c1.get(Calendar.YEAR);
            int year2 = c2.get(Calendar.YEAR);
            result = year2 - year1;
        } catch (ParseException e) {
            logger.info("context",e);
            result = -1;
        }
        return result;
    }

    /**
     * 时间date1和date2的时间差-单位年
     *
     * @param date1
     * @param date2
     * @return 年
     */
    public static int subtractYear(Date date1, Date date2) {
        int result;
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(date1);
        c2.setTime(date2);
        int year1 = c1.get(Calendar.YEAR);
        int year2 = c2.get(Calendar.YEAR);
        result = year2 - year1;
        return result;
    }

    /**
     * 获取俩个时间的查结果用时秒表示
     *
     * @param date1
     * @param date2
     * @return 几小时:几分钟:几秒钟
     * @Summary:此处可以讲计算结果包装成一个结构体返回便于格式化
     */
    public static String subtractTime(String date1, String date2) {
        String result = "";
        try {
            Date start = dateTimeInstance().parse(date1);
            Date end = dateTimeInstance().parse(date2);
            long sss = (end.getTime() - start.getTime()) / 1000;
            int hh = (int) sss / (60 * 60);
            int mm = (int) (sss - hh * 60 * 60) / (60);
            int ss = (int) (sss - hh * 60 * 60 - mm * 60);
            result = hh + ":" + mm + ":" + ss;
        } catch (ParseException e) {
            logger.info("context",e);
        }
        return result;
    }

    /**
     * 获取俩个时间的查结果用时秒表示
     *
     * @param date1
     * @param date2
     * @return 几天-几小时:几分钟:几秒钟
     * @Summary:此处可以讲计算结果包装成一个结构体返回便于格式化
     */
    public static String subtractDate(String date1, String date2) {
        String result = "";
        try {
            Date start = dateTimeInstance().parse(date1);
            Date end = dateTimeInstance().parse(date2);
            long sss = (end.getTime() - start.getTime()) / 1000;
            int dd = (int) sss / (60 * 60 * 24);
            int hh = (int) (sss - dd * 60 * 60 * 24) / (60 * 60);
            int mm = (int) (sss - dd * 60 * 60 * 24 - hh * 60 * 60) / (60);
            int ss = (int) (sss - dd * 60 * 60 * 24 - hh * 60 * 60 - mm * 60);
            result = dd + "-" + hh + ":" + mm + ":" + ss;
        } catch (ParseException e) {
            logger.info("context",e);
        }
        return result;
    }

    /**
     * 获取俩个时间之前的相隔的天数
     *
     * @param startTime
     * @param endTime
     * @return
     * @throws ParseException
     */
    public static int subDay(Date startTime, Date endTime) {
        int days = 0;
        Calendar can1 = Calendar.getInstance();
        can1.setTime(startTime);
        Calendar can2 = Calendar.getInstance();
        can2.setTime(endTime);
        int year1 = can1.get(Calendar.YEAR);
        int year2 = can2.get(Calendar.YEAR);

        Calendar can = null;
        if (can1.before(can2)) {
            days -= can1.get(Calendar.DAY_OF_YEAR);
            days += can2.get(Calendar.DAY_OF_YEAR);
            can = can1;
        } else {
            days -= can2.get(Calendar.DAY_OF_YEAR);
            days += can1.get(Calendar.DAY_OF_YEAR);
            can = can2;
        }
        for (int i = 0; i < Math.abs(year2 - year1); i++) {
            days += can.getActualMaximum(Calendar.DAY_OF_YEAR);
            can.add(Calendar.YEAR, 1);
        }

        return days;
    }

    /**
     * 获取俩个时间之前的相隔的天数
     *
     * @param startTime
     * @param endTime
     * @return
     * @throws ParseException
     */
    public static int subDay(String startTime, String endTime) {
        int days = 0;
        try {
            Date date1 = dateInstance().parse(dateInstance().format(dateTimeInstance().parse(startTime)));
            Date date2 = dateInstance().parse(dateInstance().format(dateTimeInstance().parse(endTime)));
            Calendar can1 = Calendar.getInstance();
            can1.setTime(date1);
            Calendar can2 = Calendar.getInstance();
            can2.setTime(date2);
            int year1 = can1.get(Calendar.YEAR);
            int year2 = can2.get(Calendar.YEAR);

            Calendar can = null;
            if (can1.before(can2)) {
                days -= can1.get(Calendar.DAY_OF_YEAR);
                days += can2.get(Calendar.DAY_OF_YEAR);
                can = can1;
            } else {
                days -= can2.get(Calendar.DAY_OF_YEAR);
                days += can1.get(Calendar.DAY_OF_YEAR);
                can = can2;
            }
            for (int i = 0; i < Math.abs(year2 - year1); i++) {
                days += can.getActualMaximum(Calendar.DAY_OF_YEAR);
                can.add(Calendar.YEAR, 1);
            }

        } catch (ParseException e) {
            logger.info("context",e);
        }
        return days;
    }

    /**
     * 返回俩个时间在时间段(例如每天的08:00-18:00)的时长-单位秒
     *
     * @param startDate
     * @param endDate
     * @param timeBurst 只就按该时间段内的08:00-18:00时长
     * @return 计算后的秒数
     * @throws ParseException
     * @summary 格式错误返回0
     */
    public static long subtimeBurst(String startDate, String endDate, String timeBurst)
            throws ParseException {
        Date start = dateTimeInstance().parse(startDate);
        Date end = dateTimeInstance().parse(endDate);
        return subtimeBurst(start, end, timeBurst);
    }

    /**
     * 返回俩个时间在时间段(例如每天的08:00-18:00)的时长-单位秒
     *
     * @param startDate
     * @param endDate
     * @param timeBurst 只就按该时间段内的08:00-18:00时长
     * @return 计算后的秒数
     * @throws ParseException
     */
    public static long subtimeBurst(Date startDate, Date endDate, String timeBurst)
            throws ParseException {
        long second = 0;
        Pattern p = Pattern.compile("^\\d{2}:\\d{2}-\\d{2}:\\d{2}");
        Matcher m = p.matcher(timeBurst);
        boolean falg = false;
        if (startDate.after(endDate)) {
            Date temp = startDate;
            startDate = endDate;
            endDate = temp;
            falg = true;
        }
        if (m.matches()) {
            String[] a = timeBurst.split("-");
            int day = subDay(startDate, endDate);
            second = getSecond(startDate, endDate, second, a, day);
        } else {
            second = (endDate.getTime() - startDate.getTime()) / 1000;
        }
        if (falg) {
            second = Long.parseLong("-" + second);
        }
        return second;
    }

    private static long getSecond(Date startDate, Date endDate, long second, String[] a, int day)
        throws ParseException {
        if (day > 0) {
            long firstMintues = 0;
            long lastMintues = 0;
            long daySecond = 0;
            String strDayStart = dateInstance().format(startDate) + " " + a[0] + ":00";
            String strDayEnd = dateInstance().format(startDate) + " " + a[1] + ":00";
            Date dayStart = dateTimeInstance().parse(strDayStart);
            Date dayEnd = dateTimeInstance().parse(strDayEnd);
            daySecond = subtract(dayStart, dayEnd);
            if ((startDate.after(dayStart) || startDate.equals(dayStart))
                    && startDate.before(dayEnd)) {
                firstMintues = (dayEnd.getTime() - startDate.getTime()) / 1000;
            } else if (startDate.before(dayStart)) {
                firstMintues = (dayEnd.getTime() - dayStart.getTime()) / 1000;
            }
            dayStart = dateTimeInstance().parse(dateInstance().format(endDate) + " " + a[0] + ":00");
            dayEnd = dateTimeInstance().parse(dateInstance().format(endDate) + " " + a[1] + ":00");
            if (endDate.after(dayStart) && (endDate.before(dayEnd) || endDate.equals(dayEnd))) {
                lastMintues = (endDate.getTime() - dayStart.getTime()) / 1000;
            } else if (endDate.after(dayEnd)) {
                lastMintues = (dayEnd.getTime() - dayStart.getTime()) / 1000;
            }
            //第一天的秒数 + 最好一天的秒数 + 天数*全天的秒数
            second = firstMintues + lastMintues;
            second += (day - 1) * daySecond;
        } else {
            String strDayStart = dateInstance().format(startDate) + " " + a[0] + ":00";
            String strDayEnd = dateInstance().format(startDate) + " " + a[1] + ":00";
            Date dayStart = dateTimeInstance().parse(strDayStart);
            Date dayEnd = dateTimeInstance().parse(strDayEnd);
            second = getSecond(startDate, endDate, second, dayStart, dayEnd);
            if ((startDate.before(dayStart) && endDate.before(dayStart))
                    || startDate.after(dayEnd) && endDate.after(dayEnd)) {
                second = 0;
            }
        }
        return second;
    }

    private static long getSecond(Date startDate, Date endDate, long second, Date dayStart,
        Date dayEnd) {
        if ((startDate.after(dayStart) || startDate.equals(dayStart))
                && startDate.before(dayEnd) && endDate.after(dayStart)
                && (endDate.before(dayEnd) || endDate.equals(dayEnd))) {
            second = (endDate.getTime() - startDate.getTime()) / 1000;
        } else {
            if (startDate.before(dayStart)) {
                if (endDate.before(dayEnd)) {
                    second = (endDate.getTime() - dayStart.getTime()) / 1000;
                } else {
                    second = (dayEnd.getTime() - dayStart.getTime()) / 1000;
                }
            }
            if (startDate.after(dayStart)) {
                if (endDate.before(dayEnd)) {
                    second = (endDate.getTime() - startDate.getTime()) / 1000;
                } else {
                    second = (dayEnd.getTime() - startDate.getTime()) / 1000;
                }
            }
        }
        return second;
    }

    /**
     * 时间Date在时间段(例如每天的08:00-18:00)上增加或减去second秒
     *
     * @param date
     * @param second
     * @param timeBurst
     * @return 计算后的时间
     * @Suumary 指定的格式错误后返回原数据
     */
    public static Date calculate(String date, int second, String timeBurst) {
        Date start = null;
        try {
            start = dateTimeInstance().parse(date);
            return calculate(start, second, timeBurst);
        } catch (ParseException e) {
            logger.info("context",e);
        }
        return new Date();
    }

    /**
     * 时间Date在时间段(例如每天的08:00-18:00)上增加或减去second秒
     *
     * @param date
     * @param second
     * @param timeBurst
     * @return 计算后的时间
     * @Suumary 指定的格式错误后返回原数据
     */
    public static Date calculate(Date date, int second, String timeBurst) {
        Pattern p = Pattern.compile("^\\d{2}:\\d{2}-\\d{2}:\\d{2}");
        Matcher m = p.matcher(timeBurst);
        Calendar cal = Calendar.getInstance();
        if (m.matches()) {
            String[] a = timeBurst.split("-");
            try {
                Date dayStart = dateTimeInstance().parse(dateInstance().format(date) + " " + a[0] + ":00");
                Date dayEnd = dateTimeInstance().parse(dateInstance().format(date) + " " + a[1] + ":00");
                int daySecond = (int) subtract(dayStart, dayEnd);
                int toDaySecond = (int) subtract(dayStart, dayEnd);
                extracted(date, second, cal, dayStart, dayEnd, daySecond, toDaySecond);
            } catch (ParseException e) {
                logger.info("context",e);
            }
        } else {
            cal.setTime(date);
        }
        return cal.getTime();
    }

    private static void extracted(Date date, int second, Calendar cal, Date dayStart, Date dayEnd,
        int daySecond, int toDaySecond) {
        if (second >= 0) {
            if ((date.after(dayStart) || date.equals(dayStart))
                    && (date.before(dayEnd) || date.equals(dayEnd))) {
                cal.setTime(date);
                toDaySecond = (int) subtract(date, dayEnd);
            }
            if (date.before(dayStart)) {
                cal.setTime(dayStart);
                toDaySecond = (int) subtract(dayStart, dayEnd);
            }
            if (date.after(dayEnd)) {
                cal.setTime(day(dayStart, 1));
                toDaySecond = 0;
            }

            if (second > toDaySecond) {
                int day = (second - toDaySecond) / daySecond;
                int remainder = (second - toDaySecond) % daySecond;
                cal.setTime(day(dayStart, 1));
                cal.add(Calendar.DAY_OF_YEAR, day);
                cal.add(Calendar.SECOND, remainder);
            } else {
                cal.add(Calendar.SECOND, second);
            }

        } else {
            extractedChild(date, second, cal, dayStart, dayEnd, daySecond, toDaySecond);
        }
    }

    private static void extractedChild(Date date, int second, Calendar cal, Date dayStart, Date dayEnd,
        int daySecond, int toDaySecond) {
        if ((date.after(dayStart) || date.equals(dayStart))
                && (date.before(dayEnd) || date.equals(dayEnd))) {
            cal.setTime(date);
            toDaySecond = (int) subtract(date, dayStart);
        }
        if (date.before(dayStart)) {
            cal.setTime(day(dayEnd, -1));
            toDaySecond = 0;
        }
        if (date.after(dayEnd)) {
            cal.setTime(dayEnd);
            toDaySecond = (int) subtract(dayStart, dayEnd);
        }
        if (Math.abs(second) > Math.abs(toDaySecond)) {
            int day = (Math.abs(second) - Math.abs(toDaySecond)) / daySecond;
            int remainder = (Math.abs(second) - Math.abs(toDaySecond)) % daySecond;
            cal.setTime(day(dayEnd, -1));
            cal.add(Calendar.DAY_OF_YEAR, Integer.valueOf("-" + day));
            cal.add(Calendar.SECOND, Integer.valueOf("-" + remainder));
        } else {
            cal.add(Calendar.SECOND, second);
        }
    }

    /**
     * 将指定的字符串解析为时间类型
     *
     * @param datestr
     * @return
     * @throws ParseException
     */
    public static Date dateTime(String datestr) throws ParseException {
        return dateTimeInstance().parse(datestr);
    }

    /**
     * 判断是否在某个时间段内
     *
     * @param startTime
     * @param endTime
     * @param date
     * @return
     * @throws ParseException
     */
    public static boolean between(String startTime, String endTime, Date date)
            throws ParseException {
        return between(dateTime(startTime), dateTime(endTime), date);
    }

    /**
     * 判断在某个时间内
     *
     * @param startTime
     * @param endTime
     * @param date
     * @return
     */
    public static boolean between(Date startTime, Date endTime, Date date) {
        return date.after(startTime) && date.before(endTime);
    }


    private DateUtil(){

    }
    /**
     * @Author jcx
     * @Description LocalDate转date
     * @Date 11:13 2021/7/5
     * @Param localDate:
     * @return Date
     **/
    public static Date asDate(LocalDate localDate) {
        if(null==localDate){
            return null;
        }
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }
    /**
     * @Author jcx
     * @Description LocalDateTime转date
     * @Date 11:13 2021/7/5
     * @Param localDateTime:
     * @return Date
     **/
    public static Date asDate(LocalDateTime localDateTime) {
        if(null==localDateTime){
            return null;
        }
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    /**
     * @Author jcx
     * @Description Date转LocalDate
     * @Date 11:13 2021/7/5
     * @Param date:
     * @return LocalDate
     **/
    public static LocalDate asLocalDate(Date date) {
        if(null==date){
            return null;
        }
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }
    /**
     * @Author jcx
     * @Description Date转LocalDateTime
     * @Date 11:13 2021/7/5
     * @Param date:
     * @return LocalDateTime
     **/
    public static LocalDateTime asLocalDateTime(Date date) {
        if(null==date){
            return null;
        }
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
