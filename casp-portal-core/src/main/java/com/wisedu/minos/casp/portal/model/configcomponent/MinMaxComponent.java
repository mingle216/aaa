package com.wisedu.minos.casp.portal.model.configcomponent;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title MinMaxComponent
 * @Author: jcx
 * @Date: 2021/1/19
 */
@Component
@Getter
@Setter
public abstract class MinMaxComponent extends AbstractComponent {
    /**
     * 至少限制
     * 0为不限制
     */
    private int min;
    /**
     * 最大限制
     * 0为不限制
     */
    private int max;
}
