package com.wisedu.casp.controller;


import com.wisedu.minos.casp.portal.constants.ResultStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kaisir
 */
public abstract class AbstractController {

    public static final String KEY_STATUS = "errcode";
    public static final String KEY_MESSAGE = "errmsg";
    public static final String KEY_DATA = "data";

    public static Map<String, Object> success() {
        return result(ResultStatus.SUCCESS.getStatus(), ResultStatus.SUCCESS.getMessage(), null);
    }

    public static Map<String, Object> success(Object data) {
        return result(ResultStatus.SUCCESS.getStatus(), ResultStatus.SUCCESS.getMessage(), data);
    }

    public static Map<String, Object> error() {
        return result(ResultStatus.ERR_SYS.getStatus(), ResultStatus.ERR_SYS.getMessage(), null);
    }

    public static Map<String, Object> error(Object data) {
        return result(ResultStatus.ERR_SYS.getStatus(), ResultStatus.ERR_SYS.getMessage(), data);
    }

    public static Map<String, Object> error(ResultStatus resultStatus, Object data) {
        return result(resultStatus.getStatus(), resultStatus.getMessage(), data);
    }

    private static Map<String, Object> result(Integer status, String message, Object data) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(KEY_STATUS, status);
        resultMap.put(KEY_MESSAGE, message);
        if (null != data) {
            resultMap.put(KEY_DATA, data);
        }

        return resultMap;
    }
}
