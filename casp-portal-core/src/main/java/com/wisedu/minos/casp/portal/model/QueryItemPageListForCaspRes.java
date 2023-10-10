package com.wisedu.minos.casp.portal.model;

import io.swagger.annotations.ApiModel;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title QueryItemPageListForCaspRes
 * @Author: 01120034
 * @Date: 2021/7/23
 */
@ApiModel(description = "热门服务事项的resp对象")
@Validated
public class QueryItemPageListForCaspRes extends ModelApiResponse {

    private List<QueryItemPageListForCasp> data;

    public List<QueryItemPageListForCasp> getData () {
        return data;
    }

    public void setData (List<QueryItemPageListForCasp> data) {
        this.data = data;
    }
}
