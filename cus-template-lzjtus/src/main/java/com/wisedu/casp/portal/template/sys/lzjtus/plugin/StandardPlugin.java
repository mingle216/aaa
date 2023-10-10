package com.wisedu.casp.portal.template.sys.lzjtus.plugin;

import com.wisedu.minos.casp.portal.model.TemplateAjaxRequest;
import com.wisedu.minos.casp.portal.spi.annotation.MinosSPI;
import com.wisedu.minos.casp.portal.spi.itf.AbstractTemplate;

@MinosSPI
public class StandardPlugin extends AbstractTemplate {

    @Override
    protected Object doExecDispatcher(TemplateAjaxRequest request) {
        return null;
    }

    @Override
    public String getTemplateId() {
        return StandardVariable.TEMPLATE_ID;
    }

    @Override
    public String getTemplateConfig(String platformType) {
        return StandardVariable.DEFAULT_STANDARD_TEMPLATE_CONFIG;
    }

    @Override
    public void destroy() {

    }
}
