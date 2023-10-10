package com.wisedu.minos.casp.portal.model.personal;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 *
 * 个人数据授权
 * @date 2021/7/19 16:34
 * @author jszhang@wisedu
 * @version 1.0
 **/
@Getter
@Setter
@Accessors(chain = true)
public class PersonalDataGrantVo {
    /**
     * 授权关联wid
     */
    private String authRelWid;
    /**
     * 关联名字
     */
    private String authRelName;
    /**
     * 工号
     */
    private String userId;
    /**
     * 授权类型  0:组织机构 1:用户 2:域及用户组
     */
    private String authType;
}
