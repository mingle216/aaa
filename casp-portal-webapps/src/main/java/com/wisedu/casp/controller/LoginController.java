package com.wisedu.casp.controller;

import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.CasProperties;
import com.wisedu.minos.casp.portal.common.result.ResultData;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    UserUtil userUtil;

    @GetMapping(value = {"/login"})
    public String login(HttpServletRequest request) {
        String localHref="";
        if(null!=request.getSession().getAttribute("localLoginHref")){
            localHref=request.getSession().getAttribute("localLoginHref").toString();
            request.getSession().removeAttribute("localLoginHref");
        }
        if(StringUtil.isNotEmpty(localHref)){
            localHref= localHref.replaceAll("&amp;","&");
            return "redirect:" +Global.INDEX_URI+"#"+ localHref;
        }else{
            return "redirect:" + Global.INDEX_URI;
        }
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
                if(StringUtil.isNotEmpty(localHref)){
                    localHref=localHref.replace("/","");
                    return ResultData.success(casProperties.getCas().getServerUrl() + "logout?service=" + casLogoutUrl +Global.INDEX_URI+"#/"+ localHref);
                }else{
                    return ResultData.success(casProperties.getCas().getServerUrl() + "logout?service=" + casLogoutUrl+request.getContextPath());
                }
            }
        } catch (IOException e) {
            LOGGER.error("", e);
        }
        return  ResultData.success();
    }

}
