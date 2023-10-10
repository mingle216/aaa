package com.wisedu.minos.casp.portal.model.configcomponent;

import com.wisedu.minos.casp.portal.common.constant.Global;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * 功能描述：添加服务、添加服务事项、事项统计配置组件
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title DetailServiceItemBaseInfoComponent
 * @Author: jcx
 * @Date: 2021/1/21
 */
@Component
@Getter
@Setter
public class DetailServiceItemBaseInfoComponent extends BasicComponent {
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
        return Global.Components.DETAIL_SERVICE_ITEM_BASE_INFO.getKey();
    }
}
