package com.wisedu.minos.casp.portal.model.configcomponent;

import com.wisedu.minos.casp.portal.common.constant.Global;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * 功能描述：卡片高度 业务组件
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title CardHeightComponent
 * @Author: jcx
 * @Date: 2021/1/19
 */
@Component
@Getter
@Setter
public class CardHeightComponent extends AbstractComponent{
    /**
     * 最小高度
     */
    private int min;
    /**
     * 最大高度
     */
    private int max;
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
        return Global.Components.CARD_HEIGHT.getKey();
    }
}
