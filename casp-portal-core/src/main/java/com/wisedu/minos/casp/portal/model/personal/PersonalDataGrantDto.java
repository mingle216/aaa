package com.wisedu.minos.casp.portal.model.personal;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 *
 * 个人数据授权
 * @date 2021/7/19 11:03
 * @author jszhang@wisedu
 * @version 1.0
 **/
@Getter
@Setter
public class PersonalDataGrantDto {
    /**
     * 授权对象wid
     */
    private String wid;
    private List<AuthDto> authDtos;

    /**
     * 授权相关类
     *
     * @author jszhang
     * @date 2021/7/19 17:00
     * @version 1.0
     */
    @Setter
    @Getter
    public static class AuthDto{
        /**
         * 授权关联wid
         */
        private String authRelWid;
        /**
         * 授权类型  0:组织机构 1:用户 2:域及用户组
         */
        private String authType;
    }

}
