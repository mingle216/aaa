package com.wisedu.minos.casp.portal.model.site;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 *
 * //todo 添加描述
 * @date 2022/8/24 15:10
 * @author jszhang@wisedu
 * @version 1.0
 **/
@Data
@Accessors(chain = true)
public class SiteRule {
    private String code;
    private String value;
}
