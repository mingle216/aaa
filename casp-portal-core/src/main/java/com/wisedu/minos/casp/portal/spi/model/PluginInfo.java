package com.wisedu.minos.casp.portal.spi.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 *
 * //todo 添加描述
 * @date 2021/8/4 18:08
 * @author jszhang@wisedu
 * @version 1.0
 **/
@Getter
@Setter
@Accessors(chain = true)
public class PluginInfo {
    private String pluginName;
    private String pluginId;
}
