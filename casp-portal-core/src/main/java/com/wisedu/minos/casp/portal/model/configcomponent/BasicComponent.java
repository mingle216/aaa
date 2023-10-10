package com.wisedu.minos.casp.portal.model.configcomponent;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
/**
 * 功能描述：基础组件抽象
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title BasicComponent
 * @Author: jcx
 * @Date: 2021/1/15
 */
@Component
@Getter
@Setter
public abstract class BasicComponent extends AbstractComponent{
    /**
     * 参数标签文字
     */
    private String label;
    /**
     * 是否必填  0：非必填 1：必填
     */
    private int required;
    /**
     * 提示文字
     */
    private String tips;
}
