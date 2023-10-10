package com.wisedu.minos.casp.portal.utils;

import org.apache.commons.lang3.StringUtils;

public class StrUtil {


    /**
     * 校验路由地址是否合法  仅包含A~Z,a~z,-的字符
     * @param s
     * @return
     */
    public static boolean validRoute(String s) {
        if(StringUtils.isEmpty(s)){
            return false;
        }
        for (char c : s.toCharArray()) {
            if(!isLetterLower(c) && !isLetterUpper(c) && c != '-' && !isNumber(c)){
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否为 仅包含A~Z,a~z的字符
     * @param s
     * @return
     */
    public static boolean isLetter(String s) {
        if(StringUtils.isEmpty(s)){
            return false;
        }
        for (char c : s.toCharArray()) {
            if(!isLetterLower(c) && !isLetterUpper(c)){
                return false;
            }
        }
        return true;
    }

    /**
     * A~Z的字母
     * @param ch
     * @return
     */
    public static boolean isLetterUpper(final char ch) {
        return ch >= 'A' && ch <= 'Z';
    }

    /**
     * a~z的字母
     * @param ch
     * @return
     */
    public static boolean isLetterLower(final char ch) {
        return ch >= 'a' && ch <= 'z';
    }


    /**
     * <p>
     * 检查是否为数字字符，数字字符指0~9
     * </p>
     *
     *  @param ch 被检查的字符
     * @return true表示为数字字符，数字字符指0~9
     */
    public static boolean isNumber(char ch) {
        return ch >= '0' && ch <= '9';
    }

}
