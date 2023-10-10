package com.wisedu.minos.casp.portal.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 功能描述：门户获取或切换黑白模式入参
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title BlackAndWhiteModeReq
 * @Author: 01120034
 * @Date: 2021/7/20
 */
@Getter
@Setter
public class BlackAndWhiteModeReq {
    /**
     * 是否黑暗模式  1：是  0：否         2：表示不是设置黑白模式，是查询当前门户开启的哪一种模式
     */
    private String isBlackMode;

    public BlackAndWhiteModeReq(String isBlackMode){
        this.isBlackMode=isBlackMode;
    }

    public BlackAndWhiteModeReq(){

    }
}
