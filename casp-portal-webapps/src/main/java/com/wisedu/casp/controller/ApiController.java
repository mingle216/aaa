package com.wisedu.casp.controller;

import com.wisedu.minos.casp.portal.dao.entity.TemplateEntity;
import com.wisedu.minos.casp.portal.model.PageContext;
import com.wisedu.minos.casp.portal.service.impl.PageInfoService;
import com.wisedu.minos.casp.portal.service.impl.TemplateService;
import org.apache.ibatis.annotations.Param;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class ApiController  extends AbstractController  {

    @Autowired
    private PageInfoService pageInfoService;
    @Autowired
    private TemplateService templateService;

    @GetMapping("/getPageInfo")
    @ResponseBody
    public Map<String, Object> getPageInfo(HttpServletRequest request, @Param(value = "pageCode") String pageCode) {
        PageContext pageContext = pageInfoService.getPageContext(request,pageCode);
        TemplateEntity templateEntity = templateService.getById(pageContext.getShowProgrammeEntity().getTemplateId());
        pageContext.setTemplateEntity(templateEntity);
        return success(pageContext);
    }
}
