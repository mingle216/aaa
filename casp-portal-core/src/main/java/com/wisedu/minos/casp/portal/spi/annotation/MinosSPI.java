package com.wisedu.minos.casp.portal.spi.annotation;

import java.lang.annotation.*;

/**
 * Minos可动态扩展插件的注解定义类
 * 使用方式：
 * 1、在需要使用动态插件的接口定义中增加@MinosSPI的注解
 * 2、在插件类的META-INF/minos/目录下增加对应插件类为类名的文本文件，内容为{name}={class}对应接口实现类
 * 3、
 * @author zhangjian
 * @version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface MinosSPI {
    String value () default "";
}
