package com.wisedu.minos.casp.portal.model.configcomponent;

import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import org.springframework.stereotype.Component;

/**
 * 功能描述：下拉列表 组件
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title SelectComponent
 * @Author: jcx
 * @Date: 2021/1/18
 */
@Component
public class SelectComponent extends BasicComponent  {
    /**
     * 可被勾选的最小数量
     */
    private int min;
    /**
     * 可被勾选的最大数量
     */
    private int max;
    /**
     * 是否支持多选
     */
    private int multiple;
    /**
     * 是否支持搜索
     */
    private int filterable;
    /**
     * datas中label的别名
     */
    private String labelAlias;
    /**
     * datas中value的别名
     */
    private String valueAlias;
    /**
     * 组件宽度
     */
    private int widthType;
    /**
     * 下拉框文字内容
     */
    private String placeholder;


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
        return Global.Components.SELECT.getKey();
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
    public int getMin () {
        return min;
    }

    public void setMin (int min) {
        this.min = min;
    }

    public int getMax () {
        return max;
    }

    public void setMax (int max) {
        this.max = max;
    }

    public int getMultiple () {
        return multiple;
    }

    public void setMultiple (int multiple) {
        this.multiple = multiple;
    }

    public int getFilterable () {
        return filterable;
    }

    public void setFilterable (int filterable) {
        this.filterable = filterable;
    }

    public int getWidthType() {
        return widthType;
    }

    public void setWidthType(int widthType) {
        this.widthType = widthType;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }
}
