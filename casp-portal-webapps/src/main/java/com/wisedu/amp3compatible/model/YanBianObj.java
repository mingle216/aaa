package com.wisedu.amp3compatible.model;

import lombok.Data;

/**
 * @Description:
 * @Author: 01321064
 * @Date: 2022/8/3
 */
@Data
public class YanBianObj {
    private String wid;

    private String serviceName;

    private String dutyDept;

    private String serviceContent;

    private String serviceNote;

    private Integer onSceneTimes;

    private Integer enabled;

    private String proCondition;

    private String processTime;

    private String serviceBased;

    private Integer listType;

    private Integer dealType;

    private String flowDesc;

    private Integer dealLimitDays;
}
