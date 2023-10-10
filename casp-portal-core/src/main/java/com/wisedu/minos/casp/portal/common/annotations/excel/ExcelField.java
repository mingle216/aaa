package com.wisedu.minos.casp.portal.common.annotations.excel;

import java.lang.annotation.*;


/**
 * 功能描述： Excel导出 列名注解
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title ExcelField
 * @Author: jcx
 * @Date: 2020/7/24
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelField {
    /**
     * 列名
     * @return
     */
    String value () default "未知列名";

    String className () default "";
}