package com.wisedu.minos.casp.portal.common.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Map;

/**
 * 功能描述：提供一些对象有效性校验的方法
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title CheckUtil
 * @Author: jcx
 * @Date: 2020/7/29
 */
public class CheckUtil {

    private static final Logger logger = LogManager.getLogger(CheckUtil.class);

    /**
     * 判断字符串是否是符合指定格式的时间
     *
     * @param date   时间字符串
     * @param format 时间格式
     * @return 是否符合
     */
    public static final boolean isDate (String date, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            sdf.parse(date);
            return true;
        } catch ( ParseException e ) {
            logger.error("判断字符串是否是符合指定格式的时间发生了异常",e);
        }
        return false;
    }

    /**
     * 判断字符串有效性
     */
    public static final boolean valid (String src) {
        return ! (src == null || "".equals(src.trim()));
    }

    /**
     * 判断一组字符串是否有效
     *
     * @param src
     * @return
     */
    public static final boolean valid (String[] src) {
        for ( String s : src ) {
            if ( ! valid(s) ) {
                return false;
            }
        }
        return true;
    }


    /**
     * 判断一个对象是否为空
     */
    public static final boolean valid (Object obj) {
        return ! (null == obj);
    }

    /**
     * 判断一组对象是否有效
     *
     * @param objs
     * @return
     */
    public static final boolean valid (Object[] objs) {
        return objs != null && objs.length != 0;
    }

    /**
     * 判断集合的有效性
     */
    public static final boolean valid (Collection col) {
        return ! (col == null || col.isEmpty());
    }

    /**
     * 判断一组集合是否有效
     *
     * @param cols
     * @return
     */
    public static final boolean valid (Collection... cols) {
        for ( Collection c : cols ) {
            if ( ! valid(c) ) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断map是否有效
     *
     * @param map
     * @return
     */
    public static final boolean valid (Map map) {
        return ! (map == null || map.isEmpty());
    }

    /**
     * 判断一组map是否有效
     *
     * @param maps 需要判断map
     * @return 是否全部有效
     */
    public static final boolean valid (Map... maps) {
        for ( Map m : maps ) {
            if ( ! valid(m) ) {
                return false;
            }
        }
        return true;
    }
}
