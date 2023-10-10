package com.wisedu.minos.casp.portal.common.utils;

import com.wisedu.minos.casp.portal.common.exception.BusinessException;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.util.HtmlUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 功能描述：字符串操作类
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title StringUtil
 * @Author: jcx
 * @Date: 2020/7/28
 */
public class StringUtil extends StringUtils {

    private static final Logger LOGGER = LogManager.getLogger(StringUtil.class);

    private static HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
//    private static final String SPECIAL_CHAR_REG = "[`'\"~!@#＃$%^&*{}<>?|\\/+=¥‘“？]";
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[`'\"~!@#＃$%^&*{}<>?|\\/+=¥‘“？]");
//    private static final String SQL_KEYWORD_REG = "[\\s|(](?:'|and|exec|insert|select|delete|update|(\\|\\|)|&&|count|\\*|%|chr|mid|master|truncate|char|declare|or|\\+|\\-)[\\s|)]|(ltrim)";
    private static final Pattern SQL_KEYWORD_PATTERN = Pattern.compile("[\\s|(](?:'|and|exec|insert|select|delete|update|(\\|\\|)|&&|count|\\*|%|chr|mid|master|truncate|char|declare|or|\\+|\\-)[\\s|)]|(ltrim)");
//    private static final String LOW_SQL_KEYWORD_REG = "[\\s|(](?:'|exec|insert|delete|update|(\\|\\|)|&&|\\*|%|chr|mid|\\+|\\-)[\\s|)]|(ltrim)";
    private static final Pattern LOW_SQL_KEYWORD_PATTERN = Pattern.compile("[\\s|(](?:'|exec|insert|delete|update|(\\|\\|)|&&|\\*|%|chr|mid|\\+|\\-)[\\s|)]|(ltrim)");
//    private static SecureRandom secureRandom = new SecureRandom();
    private static Pattern humpPattern = Pattern.compile("[A-Z]");

    static {
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
    }


    /**
     * humpToMinus
     * <p/>
     * 驼峰转减号   StringUtil -> string-util
     *
     * @param str
     * @return java.lang.String
     * @throws
     * @date 2020/11/6 10:59
     */
    public static String humpToMinus(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "-" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }


    /**
     * @return String
     * @Author jcx
     * @Description 获得对象描述字符串
     * @Date 18:20 2020/7/28
     * @Param obj:
     **/
    public static String getString(Object obj) {
        if (obj != null) {
            return obj.toString();
        }
        return "";
    }

    /**
     * @return boolean
     * @Author jcx
     * @Description 判断字符串有无内容
     * @Date 18:21 2020/7/28
     * @Param str:
     **/
    public static boolean isEmpty(String str) {
        return StringUtils.isBlank(str);
    }

    /**
     * @return boolean
     * @Author jcx
     * @Description 检查给定的charSequence是否有实际文本
     * @Date 18:25 2020/7/28
     * @Param str:
     **/
    public static boolean hasText(CharSequence str) {
        if (!hasLength(str)) {
            return false;
        }
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return boolean
     * @Author jcx
     * @Description 检查给定的charSequence长度
     * @Date 18:25 2020/7/28
     * @Param str:
     **/
    public static boolean hasLength(CharSequence str) {
        return (str != null) && (str.length() > 0);
    }

    /**
     * @return boolean
     * @Author jcx
     * @Description 将object转成boolean类型，如果参数为null,则返回false
     * @Date 18:26 2020/7/28
     * @Param booleanString:
     **/
    public static boolean getBoolean(Object booleanString) {
        if (booleanString == null) {
            return false;
        }
        String booleanStringTemp = booleanString.toString();
        return Boolean.parseBoolean(booleanStringTemp);
    }

    /**
     * @return boolean
     * @Author jcx
     * @Description 判断字符串是否不为空
     * @Date 18:27 2020/7/28
     * @Param str:
     **/
    public static boolean isNotEmpty(String str) {
        return (str != null) && (!"".equals(str));
    }

    /**
     * @return int
     * @Author jcx
     * @Description object转int
     * @Date 18:27 2020/7/28
     * @Param obj:
     **/
    public static int getInt(Object obj) {
        if (obj != null) {
            return Integer.parseInt(obj.toString());
        }
        return 0;
    }

    /**
     * @return Date
     * @Author jcx
     * @Description object转Date
     * @Date 18:28 2020/7/28
     * @Param obj:
     **/
    public static Date getDate(Object obj) {
        if (obj != null) {
            return (Date) (Date) obj;
        }
        return null;
    }

    /**
     * @return boolean
     * @Author jcx
     * @Description 校验两个字符串（忽略大小写）
     * @Date 16:14 2020/7/30
     * @Param valueA:
     * @Param valueB:
     **/
    public static boolean verifySameValueToUpper(String valueA, String valueB) {
        boolean verifyValue = valueA.toUpperCase().equalsIgnoreCase(valueB);
        return verifyValue;
    }

    /**
     * @return String
     * @Author jcx
     * @Description 多维结果字符串截取
     * @Date 18:29 2020/7/28
     * @Param str:
     **/
    public static String getStrFromOlapStr(String str) {
        String[] s = str.split("\\.");
        String appId = s[(s.length - 1)].replace("[", "");
        appId = appId.replace("]", "");
        return appId;
    }

    /**
     * @return String
     * @Author jcx
     * @Description 格式化文件大小 转化文件大小
     * @Date 17:01 2020/7/29
     * @Param fileSize:
     **/
    public static String formatFileSize(long fileSize) {
        if (fileSize <= 0) {
            return "0";
        }
        DecimalFormat format = new DecimalFormat("#.00");
        String fileSizeString = "";
        double fileSizeDoubleValue = (double) fileSize;
        if (fileSize < 1024) {
            fileSizeString = format.format(fileSizeDoubleValue) + "B";
        } else if (fileSize < 1048576) {
            fileSizeString = format.format(fileSizeDoubleValue / 1024) + "K";
        } else if (fileSize < 1073741824) {
            fileSizeString = format.format(fileSizeDoubleValue / 1048576) + "M";
        } else {
            fileSizeString = format.format(fileSizeDoubleValue / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * @return String
     * @Author jcx
     * @Description 编码
     * @Date 17:03 2020/7/29
     * @Param string:
     * @Param codec:
     **/
    public static String encode(String string, String codec) {
        try {
            return URLEncoder.encode(string, codec);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("编码异常", e);
            throw new BusinessException("编码异常");
        }
    }

    /**
     * @return String
     * @Author jcx
     * @Description 包装string对象并返回
     * @Date 17:07 2020/7/29
     * @Param string:
     * @Param prefix:
     * @Param suffix:
     **/
    public static String wrap(String string, String prefix, String suffix) {
        return getString(prefix) + getString(string) + getString(suffix);
    }

    /**
     * @return String
     * @Author jcx
     * @Description 拼接列表中的字符串
     * @Date 17:11 2020/7/29
     * @Param list:
     * @Param separatorChar:
     * @Param ignoreBlack:
     **/
    public static String list2String(List<?> list, String separatorChar, boolean ignoreBlack) {
        if (CollectionUtils.isEmpty(list)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        String firstValue = getString(list.get(0));
        if ((!ignoreBlack) || (isNotEmpty(firstValue))) {
            sb.append(firstValue);
        }
        int pos = 0;
        while (++pos < list.size()) {
            String stringValue = getString(list.get(pos));
            if ((ignoreBlack) && (isEmpty(stringValue))) {
                continue;
            }
            if ((!ignoreBlack) || (sb.length() != 0)) {
                sb.append(separatorChar);
            }
            sb.append(stringValue);
        }
        return sb.toString();
    }

    /**
     * @return String
     * @Author jcx
     * @Description 数组转String
     * @Date 17:11 2020/7/29
     * @Param splitChar:
     * @Param arrays:
     **/
    public static String array2String(String splitChar, String[] arrays) {
        if (ArrayUtil.isEmpty(arrays))
            return "";
        return list2String(Arrays.asList(arrays), splitChar, true);
    }

    /**
     * @return String
     * @Author jcx
     * @Description 生成UUID
     * @Date 17:12 2020/7/29
     **/
    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * @return String
     * @Author jcx
     * @Description 生成UUID
     * @Date 17:12 2020/7/29
     **/
    public static String simpleUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * @return String
     * @Author jcx
     * @Description 拼音小写
     * @Date 17:14 2020/7/29
     * @Param chinese:
     **/
    public static String toPinyinLowCase(String chinese) {
        if (null == chinese) {
            return null;
        }
        StringBuilder pinyinStr = new StringBuilder();
        //将字符串转为数组
        char[] newChar = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        //设置小写
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        try {
            for (int i = 0; i < newChar.length; i++)
                if (newChar[i] > 128) {
                    String[] array = PinyinHelper.toHanyuPinyinStringArray(newChar[i], defaultFormat);
                    if (ArrayUtil.isEmpty(array))
                        return chinese;
                    pinyinStr.append(array[0]);
                } else {
                    pinyinStr.append(newChar[i]);
                }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            LOGGER.error("拼音小写异常", e);
            return chinese;
        }
        return pinyinStr.toString().toLowerCase();
    }

    /**
     * @return String
     * @Author jcx
     * @Description 拼音首字母小写
     * @Date 17:19 2020/7/29
     * @Param chinese:
     **/
    public static String toPinyinInitialLowCase(String chinese) {
        if (null == chinese) {
            return null;
        }
        StringBuilder pinyinStr = new StringBuilder();
        //将字符串转为数组
        char[] newChar = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        //设置小写
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        try {
            for (int i = 0; i < newChar.length; i++)
                if (newChar[i] > 128) {
                    String[] array = PinyinHelper.toHanyuPinyinStringArray(newChar[i], defaultFormat);
                    if (ArrayUtil.isEmpty(array))
                        return chinese;
                    pinyinStr.append(array[0].charAt(0));
                } else {
                    pinyinStr.append(newChar[i]);
                }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            LOGGER.error("拼音首字母小写异常", e);
            return chinese;
        }
        return pinyinStr.toString().toLowerCase();
    }

    /**
     * @return boolean
     * @Author jcx
     * @Description 是否包含特殊字符
     * @Date 17:23 2020/7/29
     * @Param input:
     **/
    public static boolean containSpecialChar(String input) {
        if (isEmpty(input)) {
            return false;
        }
        return SPECIAL_CHAR_PATTERN.matcher(input).find();
    }

    /**
     * @return boolean
     * @Author jcx
     * @Description 校验sql关键字
     * @Date 17:24 2020/7/29
     * @Param input:
     **/
    public static boolean containSqlKeyword(String input) {
        return containSqlKeyword(input, false);
    }

    /**
     * @return boolean
     * @Author jcx
     * @Description 校验sql关键字
     * @Date 17:24 2020/7/29
     * @Param input:
     * @Param isLow:是否使用低版本sql校验关键字
     **/
    public static boolean containSqlKeyword(String input, boolean isLow) {
        if (isEmpty(input)) {
            return false;
        }
        if (isLow) {
            return LOW_SQL_KEYWORD_PATTERN.matcher(input).find();
        }
        return SQL_KEYWORD_PATTERN.matcher(input).find();
    }

    /***
     * @Author jcx
     * @Description url编码
     * @Date 17:26 2020/7/29
     * @Param url:
     * @return String
     **/
    public static String encodeUrl(String url) {
        if (isEmpty(url))
            return "";
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("url编码异常", e);
        }
        return "";
    }

    /**
     * @return String
     * @Author jcx
     * @Description url解码
     * @Date 17:26 2020/7/29
     * @Param url:
     **/
    public static String decodeUrl(String url) {
        if (isEmpty(url))
            return "";
        try {
            return URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("url解码异常", e);
        }
        return "";
    }

    public static String getNotHttpUrl(String url) {
        if (isNotEmpty(url)) {
            boolean flag = url.startsWith("http");
            if (flag) {
                int index = url.indexOf("//");
                return url.substring(index + 2);
            }
            return url;
        }
        return url;
    }

    /**
     * @return void
     * @Author jcx
     * @Description 按照给定的分割符，将字符串转化为List数组
     * @Date 17:30 2020/7/29
     * @Param map:
     * @Param key:
     * @Param value:
     **/
    public static void addItemToListMap(Map<String, List<String>> map, String key, String value) {
        List<String> list = map.get(key);
        if (null == list) {
            list = new ArrayList<String>();
            map.put(key, list);
        }
        list.add(value);
    }

    /**
     * @return String
     * @Author jcx
     * @Description 根据指定的字节长度截取字符串，且去除乱码
     * @Date 17:34 2020/7/29
     **/
    public static String subStringByByteLength(String str, int length) {
        String attrStr = str;
        if (isNotEmpty(attrStr)) {
            byte[] attrBytes = null;
            try {
                attrBytes = str.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                LOGGER.info("UnsupportedEncodingException", e);
                attrBytes = str.getBytes();
            }
            if (attrBytes.length > length) {
                byte[] subAttrBytes = new byte[length];
                for (int i = 0; i < length; i++) {
                    subAttrBytes[i] = attrBytes[i];
                }
                String subStr = null;
                try {
                    subStr = new String(subAttrBytes, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    LOGGER.info("UnsupportedEncodingException", e);
                    subStr = new String(subAttrBytes);
                }
                int subStrLen = subStr.length();
                if (attrStr.substring(0, subStrLen).getBytes().length > length)
                    attrStr = str.substring(0, subStrLen - 1);
                else {
                    attrStr = str.substring(0, subStrLen);
                }
            }
        }
        return attrStr;
    }

    /***
     * @Author jcx
     * @Description 去掉字符串最后一个字符
     * @Date 17:36 2020/7/29
     * @Param input:
     * @return String
     **/
    public static String cutLastChar(String input) {
        String output = "";
        if (StringUtils.isNotEmpty(input)) {
            output = input.substring(0, input.length() - 1);
        }
        return output;
    }

    /**
     * @return boolean
     * @Author jcx
     * @Description string在数组中是否存在
     * @Date 17:37 2020/7/29
     * @Param string:
     * @Param strings:
     **/
    public static boolean contain(String string, String[] strings) {
        if (strings == null)
            return false;
        for (int i = 0; i < strings.length; i++) {
            String s = strings[i];
            if (string.equals(s))
                return true;
        }
        return false;
    }

    /**
     * @return boolean
     * @Author jcx
     * @Description 判断对象或对象数组中每一个对象是否为空: 对象为null，字符序列长度为0，集合类、Map为empty
     * @Date 11:00 2020/7/29
     * @Param obj:
     **/
    public static boolean isNullOrEmpty(Object obj) {
        if (obj == null)
            return true;

        if (obj instanceof CharSequence)
            return ((CharSequence) obj).length() == 0;

        if (obj instanceof Collection)
            return ((Collection) obj).isEmpty();

        if (obj instanceof Map)
            return ((Map) obj).isEmpty();

        if (obj instanceof Object[]) {
            Object[] object = (Object[]) obj;
            if (object.length == 0) {
                return true;
            }
            boolean empty = true;
            for (int i = 0; i < object.length; i++) {
                if (!isNullOrEmpty(object[i])) {
                    empty = false;
                    break;
                }
            }
            return empty;
        }
        return false;
    }

    /**
     * 字符串省略截取
     * 字符串截取到指定长度size+...的形式
     *
     * @param subject 需要处理的字符串
     * @param size    截取的长度
     * @return 处理后的字符串
     */
    public static String subStringOmit(String subject, int size) {
        if (subject != null && subject.length() > size) {
            subject = subject.substring(0, size) + "...";
        }
        return subject;
    }


    /**
     * 截取字符串　超出的字符用symbol代替
     *
     * @param str    需要处理的字符串
     * @param len    字符串长度
     * @param symbol 最后拼接的字符串
     * @return 测试后的字符串
     */
    public static String subStringSymbol(String str, int len, String symbol) {
        String temp = "";
        if (str != null && str.length() > len) {
            temp = str.substring(0, len) + symbol;
        }
        return temp;
    }

    /**
     * 将一组字符才以指定的字符链接起来
     *
     * @param linkStr 链接字符
     * @param strs    需要连接的动态参数
     * @return
     */
    public static String join(String linkStr, String... strs) {
        StringBuffer result = new StringBuffer();
        for (String temp : strs) {
            if (temp != null && temp.trim().length() > 0)
                result.append(temp + linkStr);
        }
        if (result.length() > 1 && CheckUtil.valid(linkStr)) {
            return result.substring(0, result.length() - linkStr.length());
        }
        return result.toString();
    }

    /**
     * 截取字符串左侧的Num位截取到末尾
     *
     * @param str1 处理的字符串
     * @param num  开始位置
     * @return 截取后的字符串
     */
    public static String ltrim(String str1, int num) {
        String tt = "";
        if (!isEmpty(str1) && str1.length() >= num) {
            tt = str1.substring(num, str1.length());
        }
        return tt;

    }

    /**
     * 截取字符串右侧第0位到第Num位
     *
     * @param str 处理的字符串
     * @param num 截取的位置
     * @return 截取后的字符串
     */
    public static String rtrim(String str, int num) {
        if (!isEmpty(str) && str.length() > num) {
            str = str.substring(0, str.length() - num);
        }
        return str;
    }

    /**
     * 页面中去除字符串中的空格、回车、换行符、制表符
     *
     * @param str 需要处理的字符串
     */
    public static String replaceBlank(String str) {
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            str = m.replaceAll("");
        }
        return str;
    }

    /**
     * 获取字符串str在String中出现的次数
     *
     * @param string 处理的字符串
     * @param str    子字符串
     */
    public static int countSubStr(String string, String str) {
        if ((str == null) || (str.length() == 0) || (string == null) || (string.length() == 0)) {
            return 0;
        }
        int count = 0;
        int index = 0;
        while ((index = string.indexOf(str, index)) != -1) {
            count++;
            index += str.length();
        }
        return count;
    }

    /**
     * 替换一个出现的子串
     *
     * @param s    source string
     * @param sub  substring to replace
     * @param with substring to replace with
     */
    public static String replaceFirst(String s, String sub, String with) {
        int i = s.indexOf(sub);
        if (i == -1) {
            return s;
        }
        return s.substring(0, i) + with + s.substring(i + sub.length());
    }


    /**
     * 替换最后一次出现的字串
     * Replaces the very last occurrence of a substring with supplied string.
     *
     * @param s    source string
     * @param sub  substring to replace
     * @param with substring to replace with
     */
    public static String replaceLast(String s, String sub, String with) {
        int i = s.lastIndexOf(sub);
        if (i == -1) {
            return s;
        }
        return s.substring(0, i) + with + s.substring(i + sub.length());
    }

    /**
     * xssEncode
     * <p/>
     * xss
     *
     * @param s
     * @return java.lang.String
     * @throws
     * @date 2020/11/19 19:25
     */
    public static String xssEncode(String s) {
        return HtmlUtils.htmlEscape(s);
    }

    /**
     * xssDecode
     * <p/>
     * xss
     *
     * @param s
     * @return java.lang.String
     * @throws
     * @date 2020/11/19 19:25
     */
    public static String xssDecode(String s) {
        return HtmlUtils.htmlUnescape(s);
    }
}
