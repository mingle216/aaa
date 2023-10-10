package com.wisedu.minos.casp.portal.common.exception;

/**
 * 功能描述： 自定义异常类 业务异常，不会引发回滚事务
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title BusinessException
 * @Author: jcx
 * @Date: 2020/7/20
 */
public class BusinessException extends RuntimeException {

    private static String defaultErrorCode="999";

	private static final long serialVersionUID = 1L;

	private String errorCode;

    private String errorDetail;

    public BusinessException(String errorCode, String message) {
        super(message);
        this.errorDetail = message;
        this.errorCode = errorCode;
    }

    public BusinessException(String message) {
        super(message);
        this.errorDetail = message;
        this.errorCode = defaultErrorCode;
    }

    public BusinessException(String message,Exception e) {
        super(e);
        this.errorDetail = message;

    }

    public String getErrorCode () {
        return errorCode;
    }

    public void setErrorCode (String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDetail () {
        return errorDetail;
    }

    public void setErrorDetail (String errorDetail) {
        this.errorDetail = errorDetail;
    }
}
