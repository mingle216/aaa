package com.wisedu.minos.casp.portal.model;

import lombok.Data;

/**
 * LangResoure
 * <p/>
 * 自定义国际化映射
 *
 * @author hyluan
 * @date 2020-10-1 22:45
 * Copyright (c) 2018 wisedu
 */
@Data
public class LangResoure {
    /**
     *  语言key 例如zh_CN
     */
    String key;

    /**
     * 语言value  例如 我的大学
     */
    String value;
    /**
     * 语言描述 例如中文
     */
    String name;
}
