package com.wisedu.minos.casp.portal.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title ServiceGrantReq
 * @Author: jcx
 * @Date: 2021/1/25
 */
@Getter
@Setter
public class ServiceGrantReq {
    private String userId;
    private String serviceWid;
    public ServiceGrantReq(String userId,String serviceWid){
        this.userId=userId;
        this.serviceWid=serviceWid;
    }
}
