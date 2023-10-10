package com.wisedu.minos.casp.portal.model;

import lombok.Data;

/**
 * Breadcrumb
 * <p/>
 *  面包屑对象
 * @author hyluan
 * @date 2020-10-4 20:58
 * Copyright (c) 2018 wisedu
 */
@Data
public class Breadcrumb {

    String pageCode;
    String pageName;
    String i18nKey;
    String uri;
    boolean isCurrent;
    int index;
}
