package com.wisedu.minos.casp.portal.common.utils;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：数据工具类
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title ArrayUtil
 * @Author: jcx
 * @Date: 2020/7/28
 */
public class ArrayUtil {

    private static final Object[] EMPTY_OBJECT_ARRAY = new Object[ 0 ];
    private static final String[] EMPTY_STRING_ARRAY = new String[ 0 ];

    public ArrayUtil () {

    }

    /**
     * @Author jcx
     * @Description 判断数组是否空
     * @Date 14:50 2020/7/30
     * @Param array:
     * @return boolean
     **/
    public static boolean isEmpty (Object[] array) {
        return (array == null) || (array.length == 0);
    }

    /**
     * @Author jcx
     * @Description 判断数组是否不为空
     * @Date 14:50 2020/7/30
     * @Param array:
     * @return boolean
     **/
    public static boolean isNotEmpty (Object[] array) {
        return ! isEmpty(array);
    }

    /**
     * @Author jcx
     * @Description 数组是否包含对象
     * @Date 16:47 2020/7/28
     * @Param array:
     * @Param value:
     * @return boolean
     **/
    public static boolean contains (Object[] array, Object value) {
        if ( isEmpty(array) ) {
            return false;
        }
        for ( Object element : array ) {
            if ( ObjectUtil.objEquals(element, value) )
                return true;
        }
        return false;
    }

    /***
     * @Author jcx
     * @Description 获得一个空的Object数组
     * @Date 14:51 2020/7/30
     * @return Object
     **/
    public static Object[] emptyObjectArray () {
        return EMPTY_OBJECT_ARRAY;
    }
    /**
     * @Author jcx
     * @Description 获得一个空的String数组
     * @Date 14:51 2020/7/30
     * @return String
     **/
    public static String[] emptyStringArray () {
        return EMPTY_STRING_ARRAY;
    }

    /***
     * @Author jcx
     * @Description 获得String数组
     * @Date 14:52 2020/7/30
     * @Param objects:
     * @return String
     **/
    public static String[] getStringArray (Object... objects) {
        if ( ArrayUtil.isEmpty(objects) ) {
            return EMPTY_STRING_ARRAY;
        }
        List stringList = new ArrayList();
        for ( Object object : objects ) {
            stringList.add(StringUtil.getString(object));
        }
        return ( String[] ) stringList.toArray(new String[ 0 ]);
    }

    /**
     * @Author jcx
     * @Description string List转成以逗号隔开的String
     * @Date 13:53 2020/8/19
     * @Param param:
     * @return String
     **/
    public static String getStringBuilStr(List<String> param){
        StringBuilder result = new StringBuilder("");
        if( CollectionUtils.isNotEmpty(param) ){
            param.forEach(item->
                    result.append(item).append(",")
            );
        }
        return result.toString();
    }
}
