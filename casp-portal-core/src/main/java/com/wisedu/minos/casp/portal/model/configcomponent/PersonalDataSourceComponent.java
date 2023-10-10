package com.wisedu.minos.casp.portal.model.configcomponent;

import com.wisedu.minos.casp.portal.common.constant.Global;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * @描述
 * @创建人 wangrong
 * @创建时间 2022/3/11
 */
@Component
@Getter
@Setter
public class PersonalDataSourceComponent extends BasicComponent{
    @Override
    public void setDataResult(Object data) {
        this.setDatas(data);
    }

    @Override
    public void setDefaultValueResult(Object data) {
        this.setDefaultValue(data);
    }

    @Override
    public void setValueResult(Object data) {
        this.setValue(data);
    }

    @Override
    public String getComponent() {
        return Global.Components.PERSONAL_DATA_SOURCE.getKey();
    }
}
