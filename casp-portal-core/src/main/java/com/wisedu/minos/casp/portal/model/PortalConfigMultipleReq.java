package com.wisedu.minos.casp.portal.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PortalConfigMultipleReq {

    private List<PortalConfigReq> portalConfigList;
}
