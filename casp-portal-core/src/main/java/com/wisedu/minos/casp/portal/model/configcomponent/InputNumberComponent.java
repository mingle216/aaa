package com.wisedu.minos.casp.portal.model.configcomponent;

import com.wisedu.minos.casp.portal.common.constant.Global;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 功能描述：计数器组件
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title InputNumberComponent
 * @Author: jcx
 * @Date: 2021/1/19
 */
@Component
@Getter
@Setter
public class InputNumberComponent extends BasicComponent  {
    /**
     * 最小数值
     */
    private int min;
    /**
     * 最大数值
     */
    private int max;
    /**
     * 步长
     */
    private int step;
    /**
     * 是否只能输入step的倍数	0：否 1：是
     */
    private int stepStrictly;
    /**
     * 小数位数
     */
    private int precision;

    private String unit;

    @Override
    public void setDataResult (Object data) {
        this.setDatas(data);
    }

    @Override
    public void setDefaultValueResult (Object data) {
        this.setDefaultValue(data);
    }

    @Override
    public void setValueResult (Object data) {
        this.setValue(data);
    }

    @Override
    public String getComponent() {
        return Global.Components.INPUT_NUMBER.getKey();
    }
}
