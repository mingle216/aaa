package com.wisedu.minos.casp.portal.model.caluser;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 *
 * //todo 添加描述
 * @date 2022/8/11 15:22
 * @author jszhang@wisedu
 * @version 1.0
 **/
@Data
@Accessors(chain = true)
public class UserInfoForCalVo {
    private String userAccount;
    private String wid;
    private String userName;
    private String deptName;
}
