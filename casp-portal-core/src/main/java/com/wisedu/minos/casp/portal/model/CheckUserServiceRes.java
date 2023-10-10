package com.wisedu.minos.casp.portal.model;

import com.wisedu.minos.casp.portal.common.result.ResultData;

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
public class CheckUserServiceRes extends ResultData {
    private boolean data;

    public boolean isData () {
        return data;
    }

    public void setData (boolean data) {
        this.data = data;
    }
}
