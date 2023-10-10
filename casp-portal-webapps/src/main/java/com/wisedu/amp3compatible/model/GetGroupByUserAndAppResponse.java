package com.wisedu.amp3compatible.model;

/**
 * Created by glhan on 2016/6/22.
 */
public class GetGroupByUserAndAppResponse extends BaseResponse {
    private UserGroup[] datas;

    public void setDatas(UserGroup[] datas) {
        this.datas = (null == datas) ? null : datas.clone();
    }

    public UserGroup[] getDatas() {
        return (null == datas) ? null : datas.clone();
    }
}
