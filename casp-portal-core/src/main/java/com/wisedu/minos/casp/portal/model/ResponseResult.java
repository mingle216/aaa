package com.wisedu.minos.casp.portal.model;
import java.io.Serializable;
import java.util.List;

/**
 * @ClassName ResponseResult
 * @Description 返回的结果集
 * @Date 2020/7/3 17:03
 * @Version 1.0
 **/
public class ResponseResult implements Serializable {

    private static final long serialVersionUID = 1L;
    /***
     * 返回给前台的状态码 成功"0000"
     */
    private String code;
    /***
     * 失败消息
     */
    private String msg;
    /***
     * data
     */
    private List<MessageInfo> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<MessageInfo> getData() {
        return data;
    }

    public void setData(List<MessageInfo> data) {
        this.data = data;
    }
}
