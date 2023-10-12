package com.wisedu.amp.card.sys.personaldata.model;

import lombok.Data;

import java.util.Date;

/**
 * @描述
 * @创建人 wangrong
 * @创建时间 2022/3/10
 */

@Data
public class PersonalDataResponse {
    private String wid;
    private String picUrl;
    private String name;
    private String stuNumber;
    private String organizationName;
    private String lastLogTime;
    private String lastLogIp;
}

