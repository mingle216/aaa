package com.wisedu.minos.casp.portal.model.personal;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName PersonalDataQueryConfigDto
 * @Description 查询配置dto
 * @Date 2021/7/14 10:45
 *@Author jszhang@wisedu
 * @Version 1.0
 **/
@Getter
@Setter
public class PersonalDataQueryConfigDto {
    private Integer sourceType;
    private Integer enabled;
    private String title;
}
