package com.wisedu.amp.card.sys.personaldata.model;

import lombok.Data;

import java.util.List;

/**
 * @描述
 * @创建人 wangrong
 * @创建时间 2022/3/14
 */

@Data
public class PersonalConfigDto {

    //1-显示个人数据  0-不显示个人数据
    private int isDisplay;

    //个人数据wid
    private String dataSource;

    private List<Integer> infoList;
}
