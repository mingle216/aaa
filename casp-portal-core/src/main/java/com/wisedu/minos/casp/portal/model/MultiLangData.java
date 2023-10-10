package com.wisedu.minos.casp.portal.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 多语言相关类
 *
 * @author jszhang
 * @date 2021/7/14 16:27
 * @version 1.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class MultiLangData {
    private String wid = null;

    private String sourceType = null;

    private String langKey = null;

    private String langValue = null;

    private String langCountry = null;


}
