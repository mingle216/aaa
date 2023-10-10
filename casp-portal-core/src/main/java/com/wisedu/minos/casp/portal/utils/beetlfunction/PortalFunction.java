package com.wisedu.minos.casp.portal.utils.beetlfunction;

import org.beetl.core.Function;

/**
 * PortalFunction
 * <p/>
 * 标记beetl函数
 *
 * @author hyluan
 * @date 2020-10-1 23:07
 * Copyright (c) 2018 wisedu
 */
public abstract class PortalFunction implements Function {

    /**
     * functionName
     * <p/>
     * 定义的函数名称
     * @param
     * @throws
     * @return java.lang.String
     * @date 2020-10-1 23:23
     */
    public abstract String functionName();

    /**
     * beforeRegister
     * <p/>
     * groupTemplate注册前方法
     * @param obj
     * @throws
     * @return void
     * @date 2020-10-1 23:24
     */
    public abstract <T> PortalFunction beforeRegister(T obj);
}
