package com.wisedu.minos.casp.portal.model.configcomponent;

import com.wisedu.minos.casp.portal.common.constant.Global;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 *
 * 添加日历组件
 * @date 2021/8/3 16:32
 * @author jszhang@wisedu
 * @version 1.0
 **/
@Getter
@Setter
@Component
public class AddCalendarComponent extends BasicComponent{
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
         return Global.Components.ADD_CALENDAR.getKey();
    }


}
