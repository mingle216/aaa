package com.wisedu.minos.casp.portal.common.utils;

import com.alibaba.fastjson.JSON;
import com.wisedu.minos.casp.portal.common.exception.BusinessException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.ReflectionUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Supplier;

/**
 * 功能描述：Object操作工具类
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title ObjectUtil
 * @Author: jcx
 * @Date: 2020/7/28
 */
public class ObjectUtil{

    private static final Logger logger = LogManager.getLogger(ObjectUtil.class);

    public ObjectUtil() {

    }

    /**
     * @return byte
     * @Author jcx
     * @Description 序列化对象
     * @Date 16:50 2020/7/28
     * @Param object:
     **/
    public static byte[] serialize(Object object) {
        if (object == null) {
            return new byte[0];
        }
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
            logger.error("序列化对象发生异常：", e);
            throw new BusinessException("序列化对象发生异常");
        }

    }

    /**
     * @return T
     * @Author jcx
     * @Description 反序列化对象
     * @Date 16:54 2020/7/28
     * @Param obj:
     **/
    public static <T> T unserialize(Object obj) {
        if (obj == null)
            return null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);
            return (T) ois.readObject();
        } catch (Exception e) {
            logger.error("反序列化对象发生异常：", e);
            throw new BusinessException("反序列化对象发生异常");
        }
    }

    /**
     * @return boolean
     * @Author jcx
     * @Description 对象是否相等
     * @Date 16:55 2020/7/28
     * @Param value1:
     * @Param value2:
     **/
    public static boolean objEquals(Object value1, Object value2) {
        if ((value1 == null) && (value2 == null))
            return true;
        if ((value1 == null) || (value2 == null)) {
            return false;
        }
        return value1.equals(value2);
    }

    /**
     * @return void
     * @Author jcx
     * @Description 拷贝对象属性
     * @Date 17:15 2020/7/28
     * @Param srcObj: 属性来源对象
     * @Param destObj:目标对象
     **/
    public static  <T> T copyProperties(Object srcObj, T destObj) {
        org.springframework.beans.BeanUtils.copyProperties(srcObj, destObj);
        return destObj;
    }

    /**
     * @return T
     * @Author jcx
     * @Description 拷贝对象属性
     * @Date 17:22 2020/7/28
     * @Param parentObj:
     * @Param childClass:
     **/
    public static <T> T newCloneChildInstance(Object parentObj, Class<T> childClass) {
        if (!parentObj.getClass().isAssignableFrom(childClass))
            throw new IllegalArgumentException();
        try {
            T instance = childClass.newInstance();
            copyProperties(parentObj, instance);
            return instance;
        } catch (InstantiationException e) {
            logger.error("拷贝对象属性发生异常：", e);
            throw new BusinessException("拷贝对象属性发生异常");
        } catch (IllegalAccessException e) {
            logger.error("拷贝对象属性发生异常：", e);
            throw new BusinessException("拷贝对象属性发生异常");
        }
    }

    /**
     * 集合数据的拷贝
     * 属性不同，无法拷贝，就需使用下面带回调函数的方法
     * @param sources: 数据源类
     * @param target: 目标类::new(eg: UserVO::new)
     * @return
     */
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target) {
        return copyListProperties(sources, target, null);
    }


    /**
     * 带回调函数的集合数据的拷贝（可自定义字段拷贝规则）
     * @param sources: 数据源类
     * @param target: 目标类::new(eg: UserVO::new)
     * @param callBack: 回调函数
     * @return
     */
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target, BeanCopyUtilCallBack<S, T> callBack) {
        List<T> list = new ArrayList<>(sources.size());
        for (S source : sources) {
            T t = target.get();
            org.springframework.beans.BeanUtils.copyProperties(source, t);
            list.add(t);
            if (callBack != null) {
                // 回调
                callBack.callBack(source, t);
            }
        }
        return list;
    }

    /**
     * @return void
     * @Author jcx
     * @Description 设置对象属性
     * @Date 17:59 2020/7/28
     * @Param object:
     * @Param property:
     * @Param value:
     **/
    public static void setProperty(Object object, String property, Object value) {
        try {
            BeanUtils.setProperty(object, property, value);
        } catch (IllegalAccessException e) {
            logger.error("设置对象属性发生异常：", e);
            throw new BusinessException("设置对象属性发生异常");
        } catch (InvocationTargetException e) {
            logger.error("设置对象属性发生异常：", e);
            throw new BusinessException("设置对象属性发生异常");
        }
    }

    /***
     * @Author jcx
     * @Description 设置对象多个属性值
     * @Date 18:00 2020/7/28
     * @Param object:
     * @Param properties:
     * @return void
     **/
    public static void setProperties(Object object, Map<String, Object> properties) {
        for (Map.Entry entry : properties.entrySet())
            setProperty(object, String.valueOf(entry.getKey()), entry.getValue());
    }

    /**
     * @return Map<String   ,       T>
     * @Author jcx
     * @Description 根据属性寻找list中包含的数据，转成Map
     * @Date 18:04 2020/7/28
     * @Param list:
     * @Param specifiedPropertyName:
     **/
    public static <T> Map<String, T> listToMapByProperty(final List<T> list, String specifiedPropertyName) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        try {
            Map map = new HashMap();
            for (T element : list) {
                String propertyValue = BeanUtils.getProperty(element, specifiedPropertyName);
                if (StringUtils.isNotEmpty(propertyValue)) {
                    map.put(propertyValue, element);
                }
            }
            return map;
        } catch (IllegalAccessException e) {
            logger.error("根据属性寻找list中包含的数据，转成Map发生异常：", e);
            throw new BusinessException("根据属性寻找list中包含的数据，转成Map发生异常");
        } catch (InvocationTargetException e) {
            logger.error("根据属性寻找list中包含的数据，转成Map发生异常：", e);
            throw new BusinessException("根据属性寻找list中包含的数据，转成Map发生异常");
        } catch (NoSuchMethodException e) {
            logger.error("根据属性寻找list中包含的数据，转成Map发生异常：", e);
            throw new BusinessException("根据属性寻找list中包含的数据，转成Map发生异常");
        }
    }

    /**
     * 从List<A> copy到List<B>
     *
     * @param list  List<B>
     * @param clazz B
     * @return List<B>
     */
    public static <T> List<T> copy(List<?> list, Class<T> clazz) {
        String oldOb = JSON.toJSONString(list);
        return JSON.parseArray(oldOb, clazz);
    }

    /**
     * 从对象A copy到 对象B
     *
     * @param ob    A
     * @param clazz B.class
     * @return B
     */
    public static <T> T copy(Object ob, Class<T> clazz) {
        String oldOb = JSON.toJSONString(ob);
        return JSON.parseObject(oldOb, clazz);
    }

    /***
     * @Author jcx
     * @Description 从一个类中获取某个方法的值
     * @Date 13:23 2021/2/4
     * @Param t:
     * @Param key:
     * @return Object
     **/
    public static Object getObjectFromClass(Object t, String key){
        Method getKeyMethod = ReflectionUtils.findMethod(t.getClass(), key);
        if(getKeyMethod==null){
            throw new BusinessException("无"+key+"方法，请检查！");
        }
        return ReflectionUtils.invokeMethod(getKeyMethod, t);
    }
    /***
     * @Author jcx
     * @Description 从一个类中获取某个方法的值(没有该字段，不抛异常，用作判断)
     * @Date 13:23 2021/2/4
     * @Param t:
     * @Param key:
     * @return Object
     **/
    public static Object getObjectFromClassNoException(Object t, String key){
        Method getKeyMethod = ReflectionUtils.findMethod(t.getClass(), key);
        if(getKeyMethod==null){
            return null;
        }
        return ReflectionUtils.invokeMethod(getKeyMethod, t);
    }
}
