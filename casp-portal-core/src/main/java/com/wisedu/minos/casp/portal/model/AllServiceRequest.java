package com.wisedu.minos.casp.portal.model;

import lombok.Data;

import java.util.List;

@Data
public class AllServiceRequest {
    int pageNumber = 1;
    int pageSize = 10;
    String userId ;
    String searchKey;
    boolean permission;
    boolean console;
    private String serviceStation;
    private List<String> classifyList;
    private String labels;
    private String lang;
    private String source;
    private List<String> domainWidList;
}
