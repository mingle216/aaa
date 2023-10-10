package com.wisedu.minos.casp.portal.common.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * 功能描述：数字操作工具类
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title NumUtil
 * @Author: jcx
 * @Date: 2020/8/5
 */
@Slf4j
public class NumUtil {
    /**
     * 将(十进制,八进制,十六进制) 字符串格式数字,转换为int
     *
     * @param str
     * @param defaultValue
     * @return
     */
    public static Integer decode2int(String str, Integer defaultValue) {
        if (str == null) {
            return defaultValue;
        }

        try {
            //xxx 用Long转,可以避免 0xffee0011 这种解析的逸出 Integer.decode 只能转整数,负数会报错
            return Long.decode(str).intValue();
        } catch (Exception e) {
            LOGGER.info("context",e);
            return defaultValue;
        }
    }

    /**
     * @Author jcx
     * @Description 转换16进制
     * @Date 11:03 2020/8/13
     * @Param hex:
     * @return String
     **/
    public static String toHex(int hex) {
        String str = Integer.toHexString(hex);
        if (str.length() == 1) {
            return "0" + str;
        }

        return str;
    }
}
