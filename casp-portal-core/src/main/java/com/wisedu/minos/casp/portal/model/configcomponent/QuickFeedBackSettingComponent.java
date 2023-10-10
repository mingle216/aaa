package com.wisedu.minos.casp.portal.model.configcomponent;

import com.wisedu.minos.casp.portal.common.constant.Global.Components;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * @ClassName NewsContentComponent
 * @Description 新闻卡片树配置
 * @Date 2021/4/22 16:06
 *@Author jszhang@wisedu
 * @Version 1.0
 **/
@Component
@Getter
@Setter
public class QuickFeedBackSettingComponent extends AbstractComponent {

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
        return Components.QUICK_FEEDBACK_SETTING.getKey();
    }

}
