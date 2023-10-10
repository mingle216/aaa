package com.wisedu.minos.casp.portal.common.exception;

/**
 * 功能描述： 自定义异常类 未登录
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
public class PageCodeNotInShowProgramException extends RuntimeException {


    public PageCodeNotInShowProgramException(String message) {
        super(message);
    }

    public PageCodeNotInShowProgramException() {
        //之所以抛出notLogin信息，是为了做多语言
        super("当前pageCode存在但是不在这个展示方案中");
    }

    public PageCodeNotInShowProgramException(String message, Exception e) {
        super(message, e);

    }

}
