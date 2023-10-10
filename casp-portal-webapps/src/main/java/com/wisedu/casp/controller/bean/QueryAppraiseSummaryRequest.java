package com.wisedu.casp.controller.bean;

import lombok.Data;

import java.util.List;

@Data
public class QueryAppraiseSummaryRequest{

    private String keyword;

    private int pageNum = 1;

    private int pageSize = 1000000;

    private List<String> domainWidList;

}
