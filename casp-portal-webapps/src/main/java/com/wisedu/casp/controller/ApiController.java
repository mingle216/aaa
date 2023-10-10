package com.wisedu.casp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.result.ResultData;
import com.wisedu.minos.casp.portal.common.utils.ObjectUtil;
import com.wisedu.minos.casp.portal.dao.entity.InternationalizationEntity;
import com.wisedu.minos.casp.portal.dao.entity.SystemConfigEntity;
import com.wisedu.minos.casp.portal.dao.entity.TemplateEntity;
import com.wisedu.minos.casp.portal.model.PageContext;
import com.wisedu.minos.casp.portal.model.v354Beta2.CommonConfig;
import com.wisedu.minos.casp.portal.model.v354Beta2.CommonConfigRes;
import com.wisedu.minos.casp.portal.service.impl.*;
import com.wisedu.minos.gateway.client.annotation.ManagerGatewayApi;
import com.wisedu.minos.util.MinosCommonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Controller
public class ApiController  extends AbstractController  {

    @Autowired
    private PageInfoService pageInfoService;
    @Autowired
    private TemplateService templateService;
    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private InternationalizationService langService;
    @Autowired
    private ApiService apiService;

    @GetMapping("/getPageInfo")
    @ResponseBody
    public Map<String, Object> getPageInfo(HttpServletRequest request, @Param(value = "pageCode") String pageCode) {
        PageContext pageContext = pageInfoService.getPageContext(request,pageCode,"-100");
        TemplateEntity templateEntity = templateService.getById(pageContext.getShowProgrammeEntity().getTemplateId());
        pageContext.setTemplateEntity(templateEntity);
        return success(pageContext);
    }

    /***
     * @Author jcx
     * @Description 获取门户公共配置
     * @Date 20:08 2022/11/14
     * @return CommonConfigRes
     **/
    @GetMapping("/base/getCommonConfig")
    @ResponseBody
    @ManagerGatewayApi(name = "获取门户公共配置", realPath = "/base/getCommonConfig")
    public CommonConfigRes getCommonConfig(){
        CommonConfigRes commonConfigRes = new CommonConfigRes();
        commonConfigRes.setData(apiService.getCommonConfig());
        return commonConfigRes;
    }

    /***
     * @Author jcx
     * @Description 保存门户公共配置
     * @Date 20:09 2022/11/14
     * @Param req:
     * @return ResultData
     **/
    @PostMapping("/base/saveCommonConfig")
    @ResponseBody
    @ManagerGatewayApi(name = "保存门户公共配置", realPath = "/base/saveCommonConfig")
    public ResultData saveCommonConfig(@Valid @RequestBody CommonConfig req){
        List<String> keys = Lists.newArrayList(Global.LOCAL_BLACKANDWHITE_MODE, Global.LOCAL_MOBILE_BLACKANDWHITE_MODE, Global.SITE_RULE,
                Global.IS_BROWSER_COMPATIBLE,Global.COMPATIBLE_BROWSER,Global.COMPATIBLE_BROWSER_INFO,Global.BROWSER_INFO_IS_CHANGED,
                Global.IS_CUSTOM_JUMP,Global.CUSTOM_JUMP_URL);
        List<SystemConfigEntity> systemConfigs = systemConfigService.getSystemConfigs(keys);
        if( CollectionUtils.isNotEmpty(systemConfigs) ){
            systemConfigs.forEach(config->{
                if(Global.LOCAL_BLACKANDWHITE_MODE.equals(config.getConfigKey())){
                    config.setConfigValue(req.getIsPcBlackAndWhite());
                }else if(Global.LOCAL_MOBILE_BLACKANDWHITE_MODE.equals(config.getConfigKey())){
                    config.setConfigValue(req.getIsH5BlackAndWhite());
                }else if(Global.SITE_RULE.equals(config.getConfigKey())){
                    config.setConfigValue(req.getSiteSwitchRule());
                }else if(Global.IS_BROWSER_COMPATIBLE.equals(config.getConfigKey())){
                    config.setConfigValue(req.getIsBrowserCompatible());
                }else if(Global.COMPATIBLE_BROWSER.equals(config.getConfigKey())){
                    config.setConfigValue(req.getCompatibleBrowser());
                }else if(Global.BROWSER_INFO_IS_CHANGED.equals(config.getConfigKey())){
                    config.setConfigValue(req.getIsChanged());
                }else if(Global.COMPATIBLE_BROWSER_INFO.equals(config.getConfigKey())){
                    if(CollectionUtils.isNotEmpty(req.getCompatibleInfo())){
                        String langKey = MinosCommonUtil.getWid();
                        AtomicReference<String> cName= new AtomicReference<>("");
                        List<InternationalizationEntity> data = ObjectUtil.copyListProperties(req.getCompatibleInfo(), InternationalizationEntity::new, (info, entity) -> {
                            entity.setLangKey(langKey);
                            entity.setWid(MinosCommonUtil.getWid());
                            entity.setIsDeleted(Global.NO);
                            if(Global.DEFAULT_LANGUAGE.equals(info.getLangCountry())){
                                cName.set(info.getLangValue());
                            }
                        });
                        config.setConfigValue(cName.get());

                        langService.saveBatch(data);
                        if( StringUtils.isNotBlank(config.getValueLangKey())) {
                            langService.remove(new QueryWrapper<InternationalizationEntity>().lambda().eq(InternationalizationEntity::getLangKey, config.getValueLangKey()));
                        }
                        config.setValueLangKey(langKey);
                        }
                }else if(Global.IS_CUSTOM_JUMP.equals(config.getConfigKey())){
                    config.setConfigValue(req.getIsCustomJump());
                }else if(Global.CUSTOM_JUMP_URL.equals(config.getConfigKey())){
                    config.setConfigValue(req.getCustomJumpUrl());
                }
             });
            systemConfigService.updateBatchById(systemConfigs);
        }
        return ResultData.success();
    }

}
