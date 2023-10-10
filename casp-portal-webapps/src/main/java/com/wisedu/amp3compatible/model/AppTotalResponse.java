package com.wisedu.amp3compatible.model;

import com.wisedu.minos.api.model.amp.AmpAppEntity;

import java.util.List;

/**
 * Created by glhan on 2016/6/22.
 */
public class AppTotalResponse extends BaseTotalResponse {
    private List<AmpAppEntity> datas;

    public List<AmpAppEntity> getDatas () {
        return datas;
    }

    public void setDatas (List<AmpAppEntity> datas) {
        this.datas = datas;
    }
}
