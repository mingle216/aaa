package com.wisedu.minos.casp.portal.model;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResponse<T> {

    private String errcode;
    private String errmsg;
    private String traceId;
    private T data;
    private Page page;
}
