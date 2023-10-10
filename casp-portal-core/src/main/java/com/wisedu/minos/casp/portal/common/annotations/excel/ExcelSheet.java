package com.wisedu.minos.casp.portal.common.annotations.excel;

import java.lang.annotation.*;

/**
 * 功能描述： Excel导出 sheet页名称注解
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title ExcelSheet
 * @Author: jcx
 * @Date: 2020/7/24
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelSheet {
    String value ();
}
