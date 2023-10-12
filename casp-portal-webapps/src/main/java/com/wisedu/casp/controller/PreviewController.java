package com.wisedu.casp.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wisedu.minos.casp.portal.base.BaseController;
import com.wisedu.minos.casp.portal.common.exception.PageException;
import com.wisedu.minos.casp.portal.dao.entity.ShowProgrammeEntity;
import com.wisedu.minos.casp.portal.dao.entity.TemplateEntity;
import com.wisedu.minos.casp.portal.i18n.I18nService;
import com.wisedu.minos.casp.portal.model.TemplateAjaxRequest;
import com.wisedu.minos.casp.portal.service.impl.ShowProgrammeService;
import com.wisedu.minos.casp.portal.service.impl.TemplateService;
import com.wisedu.minos.casp.portal.spi.itf.ITemplate;
import com.wisedu.minos.casp.portal.utils.TemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title DemoController
 * @Author: d
 * @Date: 2020/9/11
 */
@Controller
public class PreviewController extends BaseController {

    @Autowired
    ShowProgrammeService showProgrammeService;

    @Autowired
    TemplateService templateService;

    @Autowired
    I18nService i18nService;

    @Autowired
    ServerProperties serverProperties;

    @Autowired
    TemplateUtil templateUtil;

    /**
     * getPageFoot
     * <p/>
     * 预览footer
     *
     * @param request
     * @param model
     * @param programmeId
     * @return java.lang.String
     * @throws
     * @date 2020-10-2 23:05
     */
    @GetMapping(value = {"/common/preview-footer/{programmeId}"})
    public String previewFooter(HttpServletRequest request, Model model, @PathVariable(required = false) String programmeId) {
        ITemplate template;
        try {
            ShowProgrammeEntity programme = showProgrammeService.getById(programmeId);
            TemplateEntity templateEntity = templateService.getById(programme.getTemplateId());
            JSONObject templateBasicPage = JSON.parseObject(programme.getTemplateConfig());
            template = templateUtil.getTemplate(templateEntity.getTemplateId());
            model.addAttribute("footerHtml", templateBasicPage == null ? "" : templateBasicPage.get("footer"));
            model.addAttribute("ctxPath", serverProperties.getServlet().getContextPath());
        } catch (Exception e) {
            throw new PageException("页面加载失败", e);
        }
        return template.getTemplateId() + "/preview/preview-footer";
    }

    /**
     * previewProgrammeBasicConfig
     * <p/>
     * 预览展示方案模板基本信息页面
     *
     * @param request
     * @param model
     * @param programmeId
     * @return java.lang.String
     * @throws
     * @date 2020-10-2 22:47
     */
    @GetMapping(value = {"/common/previewProgrammeBasicConfig/{programmeId}"})
    public String previewProgrammeBasicConfig(HttpServletRequest request, Model model, @PathVariable(required = false) String programmeId) {
        ITemplate template;
        try {
            ShowProgrammeEntity programme = showProgrammeService.getById(programmeId);
            TemplateEntity templateEntity = templateService.getById(programme.getTemplateId());

            template = templateUtil.getTemplate(templateEntity.getTemplateId());
            TemplateAjaxRequest templateAjaxRequest = new TemplateAjaxRequest();
            templateAjaxRequest.setMethod("getTemplateBasicPage");
            templateAjaxRequest.setLang(i18nService.getCurrentLanguage().toString());
            Map<String, String> map = new HashMap<>();
            map.put("wid", programmeId);
            templateAjaxRequest.setParam(map);
            templateAjaxRequest.setPlatformType(programme.getPlatformType());
            model.addAttribute("programmeConfigContent", template.execDispatcherData(templateAjaxRequest));
            model.addAttribute("ctxPath", serverProperties.getServlet().getContextPath());
        } catch (Exception e) {
            throw new PageException("预览展示方案模板基本信息页面失败", e);
        }
        return template.getTemplateId() + "/preview/previewProgrammeConfig";
    }

}
