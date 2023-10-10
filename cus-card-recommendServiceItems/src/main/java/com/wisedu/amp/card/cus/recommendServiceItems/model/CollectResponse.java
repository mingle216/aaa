package com.wisedu.amp.card.cus.recommendServiceItems.model;

import com.wisedu.minos.casp.portal.model.AmpBaseResponse;

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
 * @title CollectResponse
 * @Author: u
 * @Date: 2020/10/20
 */
public class CollectResponse extends AmpBaseResponse {
    private List<CollectServiceItemInfoBiz> data;

    public List<CollectServiceItemInfoBiz> getData () {
        return data;
    }

    public void setData (List<CollectServiceItemInfoBiz> data) {
        this.data = data;
    }
}
