package com.wisedu.minos.casp.portal.model.configcomponent;

import com.wisedu.minos.casp.portal.common.constant.Global.Components;
import org.springframework.stereotype.Component;

/**
 * @ClassName DisplayTypeComponent
 * @Description //TODO
 * @Date 2021/5/24 10:58
 *@Author jszhang@wisedu
 * @Version 1.0
 **/
@Component
public class AllServiceItemsDisplayComponent extends BasicComponent {

    private int showDisplayType;

    public int getShowDisplayType() {
        return showDisplayType;
    }

    public void setShowDisplayType(int showDisplayType) {
        this.showDisplayType = showDisplayType;
    }

    @Override
    public void setDataResult (Object data) {
        this.setDatas(data);
    }

    @Override
    public void setDefaultValueResult (Object defaultValue) {
        this.setDefaultValue(defaultValue);
    }
    @Override
    public void setValueResult (Object data) {
        this.setValue(data);
    }

    @Override
    public String getComponent () {
        return Components.ALL_SERVICE_ITEMS_DISPLAY.getKey();
    }
}
