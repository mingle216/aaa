package com.wisedu.amp3compatible.model;

import java.util.List;

public class PersonalRemindResponse extends BaseResponse {
    private List<GrsjInfoExtra> datas;

    public List<GrsjInfoExtra> getDatas() {
        return datas;
    }

    public void setDatas(List<GrsjInfoExtra> datas) {
        this.datas = datas;
    }
}
