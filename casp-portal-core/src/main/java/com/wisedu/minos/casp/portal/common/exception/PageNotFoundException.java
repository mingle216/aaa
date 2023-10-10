package com.wisedu.minos.casp.portal.common.exception;

/**
 * 功能描述： 自定义异常类 页面加载异常，重定向到异常页面
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title PageException
 * @Author: jcx
 * @Date: 2020/7/20
 */
public class PageNotFoundException extends RuntimeException {


    public PageNotFoundException(String message) {
        super(message);
    }

    public PageNotFoundException(String message, Exception e) {
        super(message, e);

    }

}
