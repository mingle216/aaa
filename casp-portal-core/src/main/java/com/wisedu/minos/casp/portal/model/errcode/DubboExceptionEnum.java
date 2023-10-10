package com.wisedu.minos.casp.portal.model.errcode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * //todo 添加描述
 * @date 2022/6/14 9:27
 * @author jszhang@wisedu
 * @version 1.0
 **/
@AllArgsConstructor
@Getter
public enum DubboExceptionEnum {
    GLOBAL_DINGDING_NOT_ENABLE("1111","全局钉钉同步开关尚未开启"),
    CANT_PULL_DINGDING_AGAIN("1112","在3min内不能重复拉取数据"),
    SYNCHRONIZE_JOB_ALREADY_EXIST("1112","已经有同步任务在执行请稍后再试"),
    ;
    private String code;
    private String desc;

    public String getCode(){
        return "35"+code;
    }
}
