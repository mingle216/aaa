package com.wisedu.minos.casp.portal.model.configcomponent;

import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * 功能描述：级联选择器
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title CascaderComponent
 * @Author: jcx
 * @Date: 2021/1/19
 */
@Component
public class CascaderComponent extends BasicComponent {
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
     * datas中childrenAlias的别名
     */
    private String childrenAlias;
    /**
     * 是否隐藏第一级选择框
     */
    private Integer hideFirstChoice;

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
        return Global.Components.CASCADER.getKey();
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

    public String getChildrenAlias () {
        return childrenAlias;
    }

    public void setChildrenAlias (String childrenAlias) {
        this.childrenAlias = childrenAlias;
    }

    public Integer getHideFirstChoice () {
        return hideFirstChoice;
    }

    public void setHideFirstChoice (Integer hideFirstChoice) {
        this.hideFirstChoice = hideFirstChoice;
    }
}
