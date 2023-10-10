package com.wisedu.amp.card.cus.cuctodoTask.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class ConfigInfo implements Serializable {
    /**
     * 高度
     */
    private ContainerType containerType;
    /**
     * 任务来源列表
     */
    private Integer sourceList;

    /**
     * 任务列表展示信息
     */
    private String[] itemConfigure;

    /**
     * 任务列表展示数量
     */
    private Integer rows;

    private Integer newsRules;

    /**
     * 自动更新频率
     */
    private Long refreshRate;
    /**
     * 收藏功能
     */
    private String showFavorite;
}
