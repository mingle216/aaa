package com.wisedu.minos.casp.portal.model.configcomponent;

import com.wisedu.minos.casp.portal.common.constant.Global;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 功能描述：模板设置logo 组件
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title LogoComponent
 * @Author: jcx
 * @Date: 2021/1/19
 */
@Component
@Getter
@Setter
public class LogoComponent extends AbstractComponent {
    /***
     * 白色logo提示信息
     *
     * {"width":702,"height":180,"size":500,"templates":["home","search"]}
     * width:宽度，单位px， height:高度，单位px,size:大小限制，单位kb,templates:适配的模板文件列表
     */
    private Object whiteLogoTips;

    /***
     * 彩色logo提示信息
     * {"width":702,"height":180,"size":500,"templates":["home","search"]}
     * width:宽度，单位px， height:高度，单位px,size:大小限制，单位kb,templates:适配的模板文件列表
     */
    private Object logoTips;

    /**
     * label
     */
    private String label;

    /***
     * 需要显示的logo组件
     */
    private List<Integer> showLogos;


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
        return Global.Components.CONFIG_LOGO.getKey();
    }
}
