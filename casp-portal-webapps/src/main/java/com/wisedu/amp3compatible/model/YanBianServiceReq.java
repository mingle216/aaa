package com.wisedu.amp3compatible.model;

import java.util.List;

/**
 * @Description:
 * @Author: 01120012
 * @Date: 2020/4/3
 */
public class YanBianServiceReq {
    private List<String> dutyDeptIds;
    private Integer enabled;

    public List<String> getDutyDeptIds() {
        return dutyDeptIds;
    }

    public void setDutyDeptIds(List<String> dutyDeptIds) {
        this.dutyDeptIds = dutyDeptIds;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }
}
