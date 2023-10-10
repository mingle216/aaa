package com.wisedu.minos.casp.portal.model.configcomponent;

import com.wisedu.minos.casp.portal.common.constant.Global;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * 功能描述：文本框组件
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title InputComponent
 * @Author: jcx
 * @Date: 2021/1/15
 */
@Component
@Getter
@Setter
public class InputComponent extends BasicComponent {
    /**
     * 文本框类型，会根据情况进行校验
     */
    private String type;
    /**
     * 是否支持国际化  0：不支持 1：支持
     */
    private int isInternational;
    /**
     * 提示文字
     */
    private String placeholder;
    /**
     * 最大字数  0为不限制
     */
    private int maxlength;
    /**
     * 是否显示字数限制   0：不显示 1：显示
     */
    private int showLimit;
    /**
     * 行数，type为textarea时有效
     */
    private int rows;
    /**
     * 是否展示预览按钮，type为htmlEditer时有效   0：不显示 1：显示
     */
    private int showPreview;
    /**
     * 是否展示重置按钮，目前仅支持htmlEditer   0：不显示 1：显示
     */
    private int showReset;
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
        return Global.Components.INPUT.getKey();
    }
}
