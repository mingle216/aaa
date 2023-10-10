package com.wisedu.minos.casp.portal.model.v311beta2;

import lombok.Data;

import java.util.Set;

/**
 *
 * //todo 添加描述
 * @date 2021/11/30 19:37
 * @author jszhang@wisedu
 * @version 1.0
 **/
@Data
public class UserSiteVo {
    private String errcode = "0";

    private String errmsg = "success";

    private Set<MultiSitesDto> data;
}
