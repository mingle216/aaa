/**
 *
 */
package com.wisedu.amp3compatible.util;

import com.alibaba.fastjson.JSONObject;
import com.wisedu.minos.casp.portal.common.utils.ArrayUtil;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.HtmlUtils;

import java.util.regex.Pattern;

/**
 * 参数校验工具类
 * <p>应用管理平台</p>
 * <p>江苏金智教育信息技术有限公司</p>
 * <p>功能说明：</p>	
 * @author 丁窍
 * @version 1.0    创建时间：2016年4月25日上午10:17:58	丁窍	发布
 */
public abstract class ParameterCheckUtil {
    private static final Pattern VERIFY_NUMBER = Pattern.compile("^-?\\d+$");

    private ParameterCheckUtil () {
    }

    /**
     * 方法名：校验输入
     * <p>功能说明：非空、特殊字符、SQL关键字、html等</p>
     * @param args
     */
    public static void verifyInput (String... args) {
        verifyInput(false, args);
    }

    /**
     * 搜索框的验证
     * %,',/特殊处理
     * @param arg
     */
    public static String verifySearchInput (String arg) {
        if ( org.apache.commons.lang3.StringUtils.isBlank(arg) ) {
            return arg;
        }
        if ( arg.contains("\\") ) {
            arg = arg.replace("\\", "\\\\");
        }
        if ( arg.contains("%") ) {
            arg = arg.replace("%", "\\%");
        }
        if ( arg.contains("'") ) {
            arg = arg.replace("'", "''");
        }
        return arg.trim();
    }

    /**
     * 方法名：校验输入
     * <p>功能说明：</p>
     * @param ignoreNullOrEmpty    是否忽略空值校验
     * @param args
     */
    public static void verifyInput (boolean ignoreNullOrEmpty, String... args) {
        if ( ArrayUtil.isEmpty(args) ) {
            return;
        }
        for ( String arg : args ) {
            verifyInput(arg, ! ignoreNullOrEmpty, true, true, true);
        }
    }

    public static void verifyInput (boolean ignoreNullOrEmpty, Integer checkLength, String... args) {
        if ( ArrayUtil.isEmpty(args) ) {
            return;
        }
        for ( String arg : args ) {
            verifyInput(arg, ! ignoreNullOrEmpty, true, true, true, checkLength);
        }
    }

    public static void verifyInputLowSql (boolean ignoreNullOrEmpty, String... args) {
        if ( ArrayUtil.isEmpty(args) ) {
            return;
        }
        for ( String arg : args ) {
            verifyInput(arg, ! ignoreNullOrEmpty, true, true, true, true, 0);
        }
    }

    /**
     * 方法名：校验输入
     * <p>功能说明：</p>
     *
     * @param ignoreNullOrEmpty    是否忽略空值校验
     * @param ignoreSpecialChar    是否忽略特殊字符校验
     * @param args
     */
    public static void verifyInput (boolean ignoreNullOrEmpty, boolean ignoreSpecialChar, String... args) {
        if ( ArrayUtil.isEmpty(args) ) {
            return;
        }
        for ( String arg : args ) {
            verifyInput(arg, ! ignoreNullOrEmpty, ! ignoreSpecialChar, true, true, 0);
        }
    }

    /**
     * 方法名：检验URL
     * <p>功能说明：</p>
     * @param ignoreNullOrEmpty
     * @param args
     */
    public static void verifyUrl (boolean ignoreNullOrEmpty, String... args) {
        if ( ArrayUtil.isEmpty(args) ) {
            return;
        }
        for ( String arg : args ) {
            verifyInput(arg, ! ignoreNullOrEmpty, false, true, false);
        }
    }


    public static void verifyLength (Integer checkLength, String... args) {
        if ( ArrayUtil.isEmpty(args) ) {
            return;
        }
        for ( String arg : args ) {
            verifyInput(arg, false, false, false, false, checkLength);
        }
    }

    /**
     * 方法名：检验Json
     * <p>功能说明：</p>
     * @param ignoreNullOrEmpty
     * @param args
     */
    public static void verifyJson (boolean ignoreNullOrEmpty, String... args) {
        if ( ArrayUtil.isEmpty(args) ) {
            return;
        }
        for ( String arg : args ) {
            JSONObject.parse(arg);
            verifyInput(arg, ! ignoreNullOrEmpty, false, true, false);
        }
    }

    public static void verifyJson (boolean ignoreNullOrEmpty, Integer checkLength, String... args) {
        if ( ArrayUtil.isEmpty(args) ) {
            return;
        }
        for ( String arg : args ) {
            JSONObject.parse(arg);
            verifyInput(arg, ! ignoreNullOrEmpty, false, true, false, checkLength);
        }
    }

    public static void verifyInput (boolean ignoreNullOrEmpty, boolean ignoreSpecialChar, boolean ignoreContainHtml, Integer checkLength, String... args) {
        if ( ArrayUtil.isEmpty(args) ) {
            return;
        }
        for ( String arg : args ) {
            verifyInput(arg, ! ignoreNullOrEmpty, ! ignoreSpecialChar, true, ! ignoreContainHtml, checkLength);
        }
    }

    /**
     * 方法名：校验参数
     * <p>功能说明：</p>
     * @param arg    参数值
     * @param checkNotNull    非空校验
     * @param checkSpecialChar    特殊字符校验
     * @param checkSqlKeyword    SQL关键字校验
     * @param checkContainHtml    包含HTML校验
     */
    public static void verifyInput (Object arg, boolean checkNotNull, boolean checkSpecialChar,
                                    boolean checkSqlKeyword, boolean checkContainHtml) {
        verifyInput(arg, checkNotNull, checkSpecialChar, checkSqlKeyword, checkContainHtml, false, 0);
    }

    public static void verifyInput (Object arg, boolean checkNotNull, boolean checkSpecialChar,
                                    boolean checkSqlKeyword, boolean checkContainHtml, Integer checkLength) {
        verifyInput(arg, checkNotNull, checkSpecialChar, checkSqlKeyword, checkContainHtml, false, checkLength);
    }

    public static void verifyInput (Object arg, boolean checkNotNull, boolean checkSpecialChar,
                                    boolean checkSqlKeyword, boolean checkContainHtml, boolean useLowSqlPatten, Integer checkLength) {
        String inputString = StringUtil.getString(arg);
        if ( checkNotNull ) {
            Assert.state(StringUtil.isNotEmpty(inputString), "输入不能为空");
        }
        if ( StringUtil.isEmpty(inputString) ) {
            return;
        }
        if ( checkSpecialChar ) {
            Assert.state(! StringUtil.containSpecialChar(inputString), "包含特殊字符");
        }
        if ( checkSqlKeyword ) {
            Assert.state(! StringUtil.containSqlKeyword(inputString, useLowSqlPatten), "包含Sql关键字");
        }
        if ( checkContainHtml ) {
            Assert.state(HtmlUtils.htmlEscape(inputString).equals(inputString), "包含Html特殊字符");
            Assert.state(! inputString.toUpperCase().contains("javascript".toUpperCase()), "包含Html特殊字符");
            Assert.state(! inputString.toUpperCase().contains("alert".toUpperCase()), "包含Html特殊字符");
        }
        if ( checkLength > 0 ) {
            Assert.state(length(inputString) <= checkLength, "输入参数超出固定长度");
        }
    }

    private static int length (String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为3，否则为1 */
        for ( int i = 0 ; i < value.length() ; i++ ) {
            /* 获取一个字符 */
            String temp = value.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if ( temp.matches(chinese) ) {
                /* 中文字符长度为3 */
                valueLength += 3;
            } else {
                /* 其他字符长度为1 */
                valueLength += 1;
            }
        }
        return valueLength;
    }


    /**
     * 方法名：检验是否全为数字
     * <p>功能说明：</p>
     *
     *
     * @param arg
     */
    public static void verifyNumber (String arg) {
        if ( StringUtil.isEmpty(arg) ) {
            return;
        }
        Assert.state(VERIFY_NUMBER.matcher(arg).matches(), "参数非法，不是数字类型");
    }

    public static void verifyNumber (String... args) {
        for ( String arg : args ) {
            verifyNumber(arg);
        }
    }

    /**
     * 方法名：检验是否全为数字
     * <p>功能说明：</p>
     *
     * @param
     * @param args
     */
    public static void verifyNumbers (String... args) {
        if ( null == args || ArrayUtil.isEmpty(args) ) {
            return;
        }
        for ( String arg : args ) {
            if ( StringUtil.isNotEmpty(arg) ) {
                if ( ! StringUtils.isNumeric(arg) ) {
                    throw new AmpBizException("参数非法，不是数字类型");
                }
            }
        }
    }
    public static String filter(String value) {
        if (value == null)
            return null;
        StringBuilder result2 = new StringBuilder(value.length());
        String temp = "";
        for (int i = 0; i < value.length(); ++i) {
            temp = "";
            switch (value.charAt(i)) {
                case '<':
                    temp = "&lt;";
                    break;
                case '>':
                    temp = "&gt;";
                    break;
                case '"':
                    temp = "&quot;";
                    break;
                case '\'':
                    temp = "&#39;";
                    break;
                case '%':
                    temp = "&#37;";
                    break;
                case ';':
                    temp = "&#59;";
                    break;
                case '(':
                    temp = "#40;";
                    break;
                case ')':
                    temp = "&#41;";
                    break;
                case '&':
                    temp = "&amp;";
                    break;
                case '+':
                    temp = "&#43;";
                    break;
                default:
                    result2.append(value.charAt(i));
            }
            result2.append(temp);
        }
        return result2.toString();
    }
}
