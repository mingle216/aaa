package com.wisedu.casp.controller;

import com.wisedu.minos.casp.portal.CasProperties;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.result.ResultData;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.service.v311beta2.MultiSitesAdapter;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import com.wisedu.minos.queue.service.PubSubService;
import lombok.extern.slf4j.Slf4j;
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
import java.net.URLEncoder;

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
    @GetMapping(value = {"/login"})
    public ModelAndView login(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        String localHref="";
        if(null!=request.getSession().getAttribute("localLoginHref")){
            localHref=request.getSession().getAttribute("localLoginHref").toString();
            request.getSession().removeAttribute("localLoginHref");
        }
        //bugFixed 站点选择界面登陆后应该跳转首页而不是之前的站点选择界面
       if( "/authSites".equals(localHref)){
           localHref="";
       }
        String siteRoute = multiSitesAdapter.getRouteByRequest(request);
        if(StringUtil.isNotEmpty(localHref)){
            localHref= localHref.replaceAll("&amp;","&");
            modelAndView.setView(new RedirectView(siteRoute+Global.INDEX_URI+"#"+ localHref,true,false));
        }else{
            modelAndView.setView(new RedirectView(siteRoute+Global.INDEX_URI,true,false));
        }
        return modelAndView;
    }


    @GetMapping(value = {"/getLoginHref"})
    @ResponseBody
    public ResultData getLoginHref(HttpServletRequest request, @RequestParam(value = "localHref",required = false)String localHref) throws UnsupportedEncodingException {
        String loginUrl = URLEncoder.encode(ApplicationContextUtil.get(CasProperties.class).getModule().getDomain()+"/login","utf-8");
        if( StringUtil.isNotEmpty(localHref)){
            request.getSession().setAttribute("localLoginHref",localHref);
            request.getSession().setAttribute("isNoLoginException",true);
        }
        return ResultData.success(casProperties.getCas().getServerUrl()+"login?service="+loginUrl);
    }


    @RequestMapping(value = {"/logout"})
    @ResponseBody
    public ResultData logout(@RequestParam(value = "localHref",required = false)String localHref,HttpServletRequest request, HttpServletResponse response) {
        try {
            // 反向代理方式集成
            if (StringUtils.equals("proxy", casProperties.getCas().getIntegrated())) {
                String serviceUrl = request.getParameter("service");
                return ResultData.success(casProperties.getCas().getServerUrl() + "logout?service=" + URLEncoder.encode(serviceUrl, "UTF-8"));
            } else {
                request.getSession().invalidate();
                request.getSession().setAttribute("isNoLoginException",true);
                String casLogoutUrl = casProperties.getModule().getDomain();
                String siteRoute = multiSitesAdapter.getRouteByRequest(request);
                if(StringUtil.isNotEmpty(localHref)){
                    localHref=localHref.replace("/","");
                    return ResultData.success(casProperties.getCas().getServerUrl() + "logout?service=" + casLogoutUrl +siteRoute+Global.INDEX_URI+"#/"+ localHref);
                }else{
                    return ResultData.success(casProperties.getCas().getServerUrl() + "logout?service=" + casLogoutUrl+request.getContextPath()+siteRoute);
                }
            }
        } catch (IOException e) {
            LOGGER.error("", e);
        }
        return  ResultData.success();
    }

}
