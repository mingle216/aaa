package com.wisedu.minos.casp.portal.vo;

import lombok.Data;

@Data
public class AppAppraiseSummaryVo {
    String serviceWid;
    String serviceName;
    String serviceIcon;
    int total;
    double avgScore;
}
