package com.wisedu.minos.casp.portal.model;

import com.wisedu.minos.casp.portal.common.result.ResultData;

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
 * @title CheckUserService
 * @Author: d
 * @Date: 2020/12/23
 */
public class BatchCheckUserServiceRes extends ResultData {
    private List<ServiceGrantModel> data;

    @Override
    public List<ServiceGrantModel> getData() {
        return data;
    }

    public void setData(List<ServiceGrantModel> data) {
        this.data = data;
    }
}
