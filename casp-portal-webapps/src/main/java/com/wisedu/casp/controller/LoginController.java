package com.wisedu.casp.controller;

import com.google.common.collect.Lists;
import com.wisedu.minos.casp.portal.CasProperties;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.result.ResultData;
import com.wisedu.minos.casp.portal.common.utils.CommonUtil;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.common.utils.UrlUtil;
import com.wisedu.minos.casp.portal.dao.entity.SystemConfigEntity;
import com.wisedu.minos.casp.portal.service.impl.SystemConfigService;
import com.wisedu.minos.casp.portal.service.v311beta2.MultiSitesAdapter;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import com.wisedu.minos.gateway.client.license.MinosLicenseManager;
import com.wisedu.minos.queue.service.PubSubService;
import com.wisedu.minos.util.MinosException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author lwl
 * @date 2020/8/14 9:25
 */
@Controller
@Slf4j
public class LoginController {

    @Autowired
    private CasProperties casProperties;
    @Autowired
    private UserUtil userUtil;
    @Autowired
    private PubSubService pubSubService;

    @Autowired
    MultiSitesAdapter multiSitesAdapter;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private SystemConfigService systemConfigService;

    @GetMapping(value = {"/login"})
    public ModelAndView login(HttpServletRequest request,@RequestParam(value = "service", required = false,defaultValue = "") String service) {
        ModelAndView modelAndView = new ModelAndView();
        if(StringUtils.isNotBlank(service)){
            if(!service.startsWith("http")){
                throw new MinosException("service参数应为http或者https开头可跳转的地址");
            }
            //域名校验
            String serviceHost = extractHost(service);
            if(!MinosLicenseManager.isLicenseDomainValid(serviceHost,"/casp-portal-webapps")) {
                LOGGER.warn("License校验失败，service is {}", service);
                throw new MinosException("License校验失败，不匹配学校根域名");
            }
            modelAndView.setView(new RedirectView(service, true, false));
        }else{
            //有配置登陆后跳转地址，优先跳转该地址
            List<String> keys = Lists.newArrayList(Global.IS_CUSTOM_JUMP,Global.CUSTOM_JUMP_URL);
            List<SystemConfigEntity> systemConfigs = systemConfigService.getSystemConfigs(keys);
            AtomicReference<String> isCustomJump= new AtomicReference<>("0");
            AtomicReference<String> customJumpUrl= new AtomicReference<>("");
            if( CollectionUtils.isNotEmpty(systemConfigs) ){
                systemConfigs.forEach(k->{
                    String value = org.apache.commons.lang.StringUtils.isNotBlank(k.getConfigValue())?k.getConfigValue():k.getDefaultValue();
                    if(Global.IS_CUSTOM_JUMP.equals(k.getConfigKey())){
                        isCustomJump.set(value);
                    }else if(Global.CUSTOM_JUMP_URL.equals(k.getConfigKey())){
                        customJumpUrl.set(value);
                    }
                });
                if("1".equals(isCustomJump.get())&&StringUtils.isNotBlank(customJumpUrl.get())){
                    modelAndView.setView(new RedirectView(customJumpUrl.get(), true, false));
                    return modelAndView;
                }
            }
            String localHref = "";
            if (null != request.getSession().getAttribute("localLoginHref")) {
                localHref = request.getSession().getAttribute("localLoginHref").toString();
                request.getSession().removeAttribute("localLoginHref");
            }
            //bugFixed 站点选择界面登陆后应该跳转首页而不是之前的站点选择界面
            if ("/authSites".equals(localHref)) {
                localHref = "";
            }
            //节点前段url #后面的
            localHref = UrlUtil.getUrlAnchorString(localHref);
            String siteRoute = multiSitesAdapter.getRouteByRequest(request);
            if (StringUtil.isNotEmpty(localHref)) {
//            localHref = localHref.replaceAll("&amp;", "&");

                modelAndView.setView(new RedirectView(siteRoute + Global.INDEX_URI + "#" + localHref, true, false));
            } else {
                modelAndView.setView(new RedirectView(siteRoute + Global.INDEX_URI, true, false));
            }
        }
        return modelAndView;
    }

    @GetMapping(value = {"/getLoginHref"})
    @ResponseBody
    public ResultData getLoginHref(HttpServletRequest request, @RequestParam(value = "localHref", required = false) String localHref){
        String defaultHref ="/login";
        String loginUrl = null;
        String loginHref = systemConfigService.getSystemConfigValue(Global.GET_LOGIN_HREF_KEY);
        defaultHref=StringUtils.isBlank(loginHref)?defaultHref:loginHref;
        try {
            loginUrl = URLEncoder.encode(ApplicationContextUtil.get(CasProperties.class).getModule().getDomain() + defaultHref, "utf-8");
            if (StringUtil.isNotEmpty(localHref)) {
                request.getSession().setAttribute("localLoginHref", localHref);
                request.getSession().setAttribute("isNoLoginException", true);
            }
        } catch ( Exception e ) {
            LOGGER.error("====getLoginHref发生异常:",e);
        }
        return ResultData.success(casProperties.getCas().getServerUrl() + "login?service=" + loginUrl);
    }


    @RequestMapping(value = {"/logout"})
    @ResponseBody
    public ResultData logout( HttpServletRequest request,@RequestParam(value = "localHref", required = false) String localHref) {
        try {
            // 反向代理方式集成
            if (StringUtils.equals("proxy", casProperties.getCas().getIntegrated())) {
                String serviceUrl = request.getParameter("service");
                return ResultData.success(casProperties.getCas().getServerUrl() + "logout?service=" + URLEncoder.encode(serviceUrl, "UTF-8"));
            } else {
                request.getSession().invalidate();
                request.getSession().setAttribute("isNoLoginException", true);
                String casLogoutUrl = casProperties.getModule().getDomain();
                String siteRoute = multiSitesAdapter.getRouteByRequest(request);
                if (StringUtil.isNotEmpty(localHref)) {
                    localHref = localHref.replace("/", "");
                    return ResultData.success(casProperties.getCas().getServerUrl() + "logout?service=" + casLogoutUrl + siteRoute + Global.INDEX_URI + "#/" + localHref);
                } else {
                    return ResultData.success(casProperties.getCas().getServerUrl() + "logout?service=" + casLogoutUrl + request.getContextPath() + siteRoute);
                }
            }
        } catch (IOException e) {
            LOGGER.error("", e);
        }
        return ResultData.success();
    }


    @GetMapping(value = {"/getLoginOrLogoutUrl"})
    @ResponseBody
    public ResultData getLoginOrLogoutUrl(@RequestParam(value = "localHref", required = false) String localHref, HttpServletRequest request){
        String loginHref = systemConfigService.getSystemConfigValue(Global.CHECK_LOGIN_OR_LOGOUT);
        if(StringUtils.isNotBlank(loginHref)){
            if("1".equals(loginHref)){
                return getLoginHref(request,localHref);
            }
        }
        return logout(request,localHref);
    }

    private static String extractHost(String service){
        if(StringUtils.isBlank(service)){
            return "";
        }
        // 进行一次URL解码
        try {
            service = URLDecoder.decode(service,"utf-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("提取host时url解码失败");
        }
        String afterProtocal = service.split("://")[1];
        if(StringUtils.isBlank(afterProtocal)){
            return "";
        }
        String host = afterProtocal.split("/")[0];
        if(StringUtils.isBlank(host)){
            return  "";
        }
        return host;
    }

}
