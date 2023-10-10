package com.wisedu.minos.casp.portal.model.configcomponent;

import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * 功能描述：单选框组件
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title RadioComponent
 * @Author: jcx
 * @Date: 2021/1/15
 */
@Component
public class RadioComponent extends BasicComponent{
    /**
     * datas中label的别名
     */
    private String labelAlias;
    /**
     * datas中value的别名
     */
    private String valueAlias;
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
    public String getComponent() {
        return Global.Components.RADIO.getKey();
    }

    public String getLabelAlias () {
        return labelAlias;
    }

    public void setLabelAlias (String labelAlias) {
        if( StringUtil.isEmpty(labelAlias) ){
            this.labelAlias ="label";
        }else{
            this.labelAlias = labelAlias;
        }
    }

    public String getValueAlias () {
        return valueAlias;
    }

    public void setValueAlias (String valueAlias) {
        if( StringUtil.isEmpty(valueAlias) ){
            this.valueAlias ="value";
        }else{
            this.valueAlias = valueAlias;
        }
    }
}
