package com.wisedu.minos.casp.portal.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 功能描述：门户用提供给运营中心服务查看入参
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title ServiceForCaspReq
 * @Author: 01120034
 * @Date: 2021/7/27
 */
@Getter
@Setter
public class ServiceForCaspReq {

    private String serviceWid;

    private Long pageSize;

    public ServiceForCaspReq(String serviceWid,Long pageSize){
        this.pageSize=pageSize;
        this.serviceWid=serviceWid;
    }

    public ServiceForCaspReq(){
    }

}
