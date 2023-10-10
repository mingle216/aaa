package com.wisedu.amp.card.cus.cucmyTask.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

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

    /**
     * 自动更新频率
     */
    private Long refreshRate;
}
