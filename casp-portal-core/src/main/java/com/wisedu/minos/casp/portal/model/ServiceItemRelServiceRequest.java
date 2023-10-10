package com.wisedu.minos.casp.portal.model;

import lombok.Data;


@Data
public class ServiceItemRelServiceRequest {
    /**
     * 服务事项WID
     */
    private String serviceItemWid;
    /**
     * 语言
     */
    private String langCountry;
}
