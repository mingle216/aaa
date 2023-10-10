package com.wisedu.minos.casp.portal.common.utils;

import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.constant.GlobalEnum;
import com.wisedu.minos.casp.portal.common.exception.BusinessException;
import org.springframework.util.ObjectUtils;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title ProgrammeApiAdapterImpl
 * @Author: 韩俊俊
 * @Date: 2020/9/22
 */
public class Assert {

    public static void isTrue(boolean ret, String code, String msg) {
        if (!ret) {
            throw new BusinessException(code, msg);
        }
    }

    public static void isTrue(boolean ret, GlobalEnum status, Object... args) {
        args = checkNullArgs(args);
        isTrue(ret, status.getCode(), String.format(status.getMsg(), args));
    }

    private static Object[] checkNullArgs(Object[] args) {
        if (ObjectUtils.isEmpty(args)) {
            Object[] objects = new Object[1];
            objects[0] = "";
            args = objects;
        }
        return args;
    }


    public static void notNull(Object obj, GlobalEnum status, Object... args) {
        args = checkNullArgs(args);
        notNull(obj, status.getCode(), String.format(status.getMsg(), args));
    }

    public static void notNull(Object obj, String code, String msg) {
        if (ObjectUtils.isEmpty(obj)) {
            throw new BusinessException(code, msg);
        }
    }
}
