package com.wisedu.amp.card.cus.calendar.enums;

import lombok.Getter;

/**
 * 日历查询枚举
 * @date 2021/8/4 9:30
 * @author jszhang@wisedu
 * @version 1.0
 **/
@Getter
public enum QueryModelEnum {

   USER_PERMISSION(0,"卡片正常访问，获取用户有权限的日历(带屏蔽标识)"),
   USER_NOT_LOGIN(1,"未登录时访问卡片，获取所有允许登录前可见的日历列表"),
   CONSOLE(2,"管控台调用，获取所有的日历列表"),

    ;
    private int code;
    private String desc;

    QueryModelEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
