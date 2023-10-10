package com.wisedu.amp.card.cus.recommendapp.model;

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
    private List<CollectServiceInfoBiz> data;

    public List<CollectServiceInfoBiz> getData () {
        return data;
    }

    public void setData (List<CollectServiceInfoBiz> data) {
        this.data = data;
    }
}
