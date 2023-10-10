package com.wisedu.minos.casp.portal.model.caluser;

import lombok.Data;

import java.util.List;

/**
 *
 * //todo 添加描述
 * @date 2022/8/11 13:45
 * @author jszhang@wisedu
 * @version 1.0
 **/
@Data
public class QueryUserForCalDto {
    private List<String> orgIds;
    private String keyword;
}
