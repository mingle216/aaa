package com.wisedu.minos.casp.portal.model.caluser;

import lombok.Data;

/**
 *
 * //todo 添加描述
 * @date 2022/8/11 13:45
 * @author jszhang@wisedu
 * @version 1.0
 **/
@Data
public class QueryUserForCalPageDto {
    private String orgId;
    private int pageNumber;
    private int pageSize;
    private int isAll=0;
}
