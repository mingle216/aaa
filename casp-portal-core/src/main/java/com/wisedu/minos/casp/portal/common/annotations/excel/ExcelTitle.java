package com.wisedu.minos.casp.portal.common.annotations.excel;
import java.lang.annotation.*;

/**
 * 功能描述： Excel导出 excel抬头名称注解
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title ExcelTitle
 * @Author: jcx
 * @Date: 2020/7/24
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelTitle {
    String value ();
}
