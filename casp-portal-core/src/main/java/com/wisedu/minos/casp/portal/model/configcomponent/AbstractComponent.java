package com.wisedu.minos.casp.portal.model.configcomponent;

import lombok.Getter;
import lombok.Setter;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title AbstractComponent
 * @Author: jcx
 * @Date: 2021/1/18
 */
@Getter
@Setter
public abstract class AbstractComponent {
    /**
     * 唯一键
     */
    private String key;
    /**
     * 组件名称
     */
    private String component;
    /**
     * 默认值，用于重置
     */
    private Object defaultValue;
    /**
     * 绑定当前值
     */
    private Object value;
    /**
     * 外部动态datas数据
     */
    private Object datas;
    /**
     * 设置 参数 datas 的值
     */
    public abstract void setDataResult (Object data);
    /**
     * 设置 参数 defaultValue 的值
     */
    public abstract void setDefaultValueResult (Object data);
    /**
     * 设置 参数 value 的值
     */
    public abstract void setValueResult (Object data);
    /**
     * 获取组件名称
     */
    public abstract String getComponent();

}
