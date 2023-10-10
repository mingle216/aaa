package com.wisedu.casp.controller.bean;

import lombok.Data;

@Data
public class DeletedAppraiseReq {
    private String wid;

    private int isDeleted;
}
