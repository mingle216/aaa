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
 * @title ServiceGrantConfigRes
 * @Author: d
 * @Date: 2020/12/23
 */
public class ServiceGrantConfigRes extends ResultData {
    private List<ServiceGrant> data;
    @Override
    public List<ServiceGrant> getData () {
        return data;
    }
    public void setData (List<ServiceGrant> data) {
        this.data = data;
    }
}
