package com.wisedu.minos.casp.portal.model.configcomponent;

import com.wisedu.minos.casp.portal.common.constant.Global;
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
 * @title NewsSubscribeComponent
 * @Author: 01120034
 * @Date: 2022/12/1
 */
@Component
public class NewsSubscribeComponent extends AbstractComponent {
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
    public String getComponent () {
        return  Global.Components.NEWS_SUBSCRIBE.getKey();
    }
}
