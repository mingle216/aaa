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
 * @title AllClassifyListRes
 * @Author: u
 * @Date: 2020/10/13
 */
public class AllClassifyListRes extends AmpBaseResponse {

    private List<ClassifyBiz> data;

    private List<String> favClassifyWids = null;

    public List<ClassifyBiz> getData () {
        return data;
    }

    public void setData (List<ClassifyBiz> data) {
        this.data = data;
    }

    public List<String> getFavClassifyWids() {
        return favClassifyWids;
    }

    public void setFavClassifyWids(List<String> favClassifyWids) {
        this.favClassifyWids = favClassifyWids;
    }
}
