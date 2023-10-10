package com.wisedu.minos.casp.portal.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @ClassName ResponseResult
 * @Description 待办中心resful返回的结果集
 * @Date 2020/9/1 13:03
 * @Author zkpu
 * @Version 1.0
 **/
@Data
@Accessors(chain = true)
public class ResponseTdcResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    /***
     * 返回给前台的状态码 成功"0"
     */
    private String code = "0";
    /***
     * 失败消息999
     */
    private String msg = "success";
    /***
     * data
     */
    private T data;

    public static <T> ResponseTdcResult<T> success() {
        return new ResponseTdcResult<T>().setCode("0")
            .setMsg("success");
    }

    public static <T> ResponseTdcResult<T> fail(String msg) {
        return new ResponseTdcResult<T>().setCode("999").setMsg(msg);
    }
}
