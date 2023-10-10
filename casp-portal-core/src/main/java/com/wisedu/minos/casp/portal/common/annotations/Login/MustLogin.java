package com.wisedu.minos.casp.portal.common.annotations.Login;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MustLogin {

    /**
     * 限制用户才可访问，多个用户可用逗号隔开
     * @return
     */
    String restrictUser () default "";
}
