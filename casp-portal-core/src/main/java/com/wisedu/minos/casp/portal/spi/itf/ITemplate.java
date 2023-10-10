package com.wisedu.minos.casp.portal.spi.itf;

import com.wisedu.minos.casp.portal.model.TemplateAjaxRequest;
import com.wisedu.minos.casp.portal.model.TemplateConfigRequest;
import com.wisedu.minos.casp.portal.model.configcomponent.AbstractComponent;
import com.wisedu.minos.casp.portal.model.configcomponent.BasicComponent;
import com.wisedu.minos.casp.portal.model.configcomponent.ComponentContainer;
import com.wisedu.minos.casp.portal.spi.annotation.MinosSPI;
import com.wisedu.minos.casp.portal.spi.model.TemplateContext;

import java.util.List;
import java.util.Map;

/**
 * @author kaisir
 */
@MinosSPI
public interface ITemplate {

    /**
     * 返回模板的唯一ID
     * SYS_TEMPLATE_OFFICIAL
     *
     * @return
     */
    String getTemplateId();

    String getTemplateConfig(String platformType);


    /***
     * @Author jcx
     * @Description 获取模板配置
     * @Date 13:57 2021/1/15
     * @Param templateConfigRequest:
     * @return List<Map<String,Object>>
     **/
    ComponentContainer getConfig(TemplateConfigRequest templateConfigRequest);

    void initialize(Map<String, Object> param);

    Object execDispatcherData(TemplateAjaxRequest request);

    /**
     * The destroy store template
     */
    void destroy();
}
