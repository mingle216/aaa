package com.wisedu.minos.casp.portal.common.result;

import org.apache.poi.ss.formula.functions.T;

/**
 * 功能描述：AMP接口返回数据格式类
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title ResultData
 * @Author: jcx
 * @Date: 2020/8/18
 */
public class ResultData<T> {

    public static final String DEFAULT_SUCCESS_MESSAGE = "请求成功";
    public static final String DEFAULT_ERROR_MESSAGE = "系统内部错误，请联系管理员！";
    public static final String DEFAULT_SUCCESS_CODE="0";
    public static final String DEFAULT_ERROR_CODE="999";

    /**
     * 验证码错误
     */
    public static final String VERIFY_ERROR_CODE="402";
    /**
     * errcode : 0
     * errmsg : 查询成功
     * data : {"rows":[{"serviceName":"ss","wid":"070eac4e-733c-4f7a-8879-c225eee4286b","dutyDeptName":"机关党委12","targetStr":null,"themeStr":null,"appList":null,"forStudent":0,"forTeacher":0,"forGuest":0,"serviceContent":null,"directlyJump":0,"dealBtnName":null},{"serviceName":"测试在线办理按钮","wid":"ad777f50-b1af-4d63-9d14-30b8518c7891","dutyDeptName":"新农村发展研究院办公室、江苏农村发展学院","targetStr":null,"themeStr":null,"appList":null,"forStudent":0,"forTeacher":0,"forGuest":0,"serviceContent":null,"directlyJump":0,"dealBtnName":null},{"serviceName":"XML部门领导","wid":"7c75ee8b-34dc-4f22-b826-39ecb2e0f9a2","dutyDeptName":"经济管理学院MBA教育中心","targetStr":null,"themeStr":null,"appList":null,"forStudent":0,"forTeacher":0,"forGuest":0,"serviceContent":null,"directlyJump":0,"dealBtnName":null},{"serviceName":"测试测试推送","wid":"db37fb11-2d6b-4db2-94f0-7ebf74dab944","dutyDeptName":"新农村发展研究院办公室、江苏农村发展学院","targetStr":null,"themeStr":null,"appList":null,"forStudent":0,"forTeacher":0,"forGuest":0,"serviceContent":null,"directlyJump":0,"dealBtnName":null},{"serviceName":"行政印章使用","wid":"5cb913b0-3753-40fc-b636-aa404677f812","dutyDeptName":"人文社科管理办公室","targetStr":null,"themeStr":null,"appList":null,"forStudent":0,"forTeacher":0,"forGuest":0,"serviceContent":null,"directlyJump":1,"dealBtnName":null},{"serviceName":"测试（分页）11","wid":"bc83666c-d146-45e8-8593-238e29b38f62","dutyDeptName":"经济管理学院MBA教育中心","targetStr":null,"themeStr":null,"appList":null,"forStudent":0,"forTeacher":0,"forGuest":0,"serviceContent":null,"directlyJump":0,"dealBtnName":null},{"serviceName":"测试流程-czj-004","wid":"b0184e80-dbd3-4a5b-9f89-693945a61978","dutyDeptName":"新农村发展研究院办公室、江苏农村发展学院","targetStr":null,"themeStr":null,"appList":null,"forStudent":0,"forTeacher":0,"forGuest":0,"serviceContent":null,"directlyJump":0,"dealBtnName":null},{"serviceName":"待处理事项","wid":"0ea08894-0a13-4814-b4c8-747fe90d95f0","dutyDeptName":"新农村发展研究院办公室、江苏农村发展学院","targetStr":null,"themeStr":null,"appList":null,"forStudent":0,"forTeacher":0,"forGuest":0,"serviceContent":null,"directlyJump":0,"dealBtnName":null},{"serviceName":"本科生（测试分页14","wid":"cf4ed097-74d0-469e-bb03-6a6751730f57","dutyDeptName":"经济管理学院MBA教育中心","targetStr":null,"themeStr":null,"appList":null,"forStudent":0,"forTeacher":0,"forGuest":0,"serviceContent":null,"directlyJump":0,"dealBtnName":null},{"serviceName":"(测测流程图吧","wid":"d5568a04-4ae6-4136-a5c0-51701fbc9a81","dutyDeptName":"人文社科管理办公室","targetStr":null,"themeStr":null,"appList":null,"forStudent":0,"forTeacher":0,"forGuest":0,"serviceContent":null,"directlyJump":1,"dealBtnName":null}],"total":12,"filterTotal":0,"empty":false}
     */

    private String errcode;
    private String errmsg;
    private T data;

    public ResultData() {
    }

    public ResultData(String code,  String message) {
        this.errcode = code;
        this.errmsg = message;
    }

    public ResultData(String code,  String message,  T data) {
        this.errcode = code;
        this.errmsg = message;
        this.data = data;
    }

    public static <T> SuccessResult<T> success() {
        return new SuccessResult<T>();
    }

    public static <T> SuccessResult<T> success(T t) {
        return new SuccessResult<T>(t);
    }

    public static <T> SuccessResult<T> success(String code, String message) {
        return new SuccessResult<T>(code, message, null);
    }

    public static <T> SuccessResult<T> success(String code, String message, T t) {
        return new SuccessResult<T>(code, message, t);
    }

    public static <T> ErrorResult<T> error(String message) {
        return new ErrorResult<T>(message);
    }

    public static <T> ErrorResult<T> error(String code, String message) {
        return new ErrorResult<T>(code, message);
    }

    public static <T> ErrorResult<T> error(String code, String message, T t) {
        return new ErrorResult<T>(code, message, t);
    }

    public String getErrcode () {
        return errcode;
    }

    public void setErrcode (String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg () {
        return errmsg;
    }

    public void setErrmsg (String errmsg) {
        this.errmsg = errmsg;
    }

    public T getData () {
        return data;
    }

    public void setData (T data) {
        this.data = data;
    }
}
