package com.wisedu.minos.casp.portal.model.configcomponent;

import com.wisedu.minos.casp.portal.common.constant.Global;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class LinkComponent extends AbstractComponent {
    /**
     * 显示文字
     */
    private String name;
    /**
     * 是否打开新窗口
     */
    private String target;
    /**
     *  提示文字
     */
    private String tips;


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
        return Global.Components.LINK.getKey();
    }
}
