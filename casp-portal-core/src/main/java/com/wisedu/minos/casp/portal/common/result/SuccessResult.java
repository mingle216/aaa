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
 * @title SuccessResult
 * @Author: jcx
 * @Date: 2020/7/20
 */
public class SuccessResult<T> extends ResultData<T> {


    public SuccessResult() {
        super( DEFAULT_SUCCESS_CODE, DEFAULT_SUCCESS_MESSAGE, null);
    }

    public SuccessResult( T t) {
        super( DEFAULT_SUCCESS_CODE, DEFAULT_SUCCESS_MESSAGE, t);
    }

    public SuccessResult( String code,  String message,  T t) {
        super( code, message, t);
    }

}
