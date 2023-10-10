package com.wisedu.minos.casp.portal.model.configcomponent;

import com.wisedu.minos.casp.portal.common.constant.Global;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * @ClassName slideSettingComponent
 * @Description
 * @Date 2021/2/1 0001 14:55
 * @Author zkpu
 * @Version 1.0
 **/
@Component
@Getter
@Setter
public class SlideSettingComponent extends AbstractComponent{

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
        return Global.Components.SLIDE_SETTING.getKey();
    }
}
