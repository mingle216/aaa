package com.wisedu.minos.casp.portal.utils;

import com.wisedu.minos.casp.portal.common.exception.BusinessException;
import com.wisedu.minos.casp.portal.spi.MinosExtensionLoader;
import com.wisedu.minos.casp.portal.spi.itf.ITemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TemplateUtil {

    public ITemplate getTemplate(String templateId) {
        Map<String, ITemplate> templateMap = getTemplateMap();
        ITemplate template = templateMap.get(templateId);
        if (null == template) {
            throw new BusinessException(String.format("系统异常，该模板%s未安装", templateId));
        }
        return template;
    }

    public Map<String, ITemplate> getTemplateMap(){
        return MinosExtensionLoader.getExtensionLoader(ITemplate.class).getSupportedExtensionInstances();
    }
}
