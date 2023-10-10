package com.wisedu.minos.casp.portal.common.utils;

/**
 * 功能描述：函数式接口
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title BeanCopyUtilCallBack
 * @Author: jcx
 * @Date: 2020/10/15
 */
@FunctionalInterface
public interface BeanCopyUtilCallBack <S, T> {
    /**
     * 定义默认回调方法
     * @param t
     * @param s
     */
    void callBack(S t, T s);
}
