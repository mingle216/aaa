package com.wisedu.minos.casp.portal.common.result;

/**
 * 功能描述： 统一返回结果集类
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title ErrorResult
 * @Author: jcx
 * @Date: 2020/7/20
 */
public class ErrorResult<T> extends ResultData<T> {

    public ErrorResult(String message) {
        super(DEFAULT_ERROR_CODE, message, null);
    }

    public ErrorResult(String code, String message) {
        super( code, message, null);
    }

    public ErrorResult(String code, String message, T t) {
        super( code, message, t);
    }
}
