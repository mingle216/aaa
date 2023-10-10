package com.wisedu.minos.casp.portal.model.configcomponent;

import com.wisedu.minos.casp.portal.common.constant.Global;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class MyAppSelectComponent extends AbstractComponent{

    private String tips;
    private String label;

    @Override
    public void setDataResult(Object data) {
        this.setDatas(data);
    }

    @Override
    public void setDefaultValueResult(Object defaultValue) {
        this.setDefaultValue(defaultValue);
    }

    @Override
    public void setValueResult(Object data) {
        this.setValue(data);
    }

    @Override
    public String getComponent() {
        return Global.Components.MYAPP_SELECT.getKey();
    }
}
