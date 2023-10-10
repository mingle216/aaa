package com.wisedu.minos.casp.portal.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProgrammeLocalStyleReq {
    private String wid;
    private String localStyle;
    private boolean isQuery=true;
}
