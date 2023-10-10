package com.wisedu.amp.card.cus.cucservicebus.model;

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
 * @title ClassifyServiceListRes
 * @Author: u
 * @Date: 2020/10/13
 */
public class ClassifyServiceListRes extends AmpBaseResponse {
    private List<ServiceBiz> data;

    public List<ServiceBiz> getData () {
        return data;
    }

    public void setData (List<ServiceBiz> data) {
        this.data = data;
    }
}
