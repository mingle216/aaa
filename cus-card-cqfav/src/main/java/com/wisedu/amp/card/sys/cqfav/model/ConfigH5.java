package com.wisedu.amp.card.sys.cqfav.model;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
/**
 * ConfigH5
 * h5配置<p/>
 *
 * @author jszhang
 * @date 2021/3/16 17:59
 * Copyright (c) 2018 wisedu
 */
@Getter
@Setter
public class ConfigH5 implements Serializable {

    /**
     * 服务事项列表图标是否展示
     */
    private String iconShow;
    /**
     * 列表展示总数
     */
    private String columns;



}
