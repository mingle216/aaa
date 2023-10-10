package com.wisedu.minos.filter;

import com.alibaba.fastjson.JSONObject;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.constant.GlobalEnum;
import com.wisedu.minos.casp.portal.common.constant.LoginConstant;
import com.wisedu.minos.casp.portal.CasProperties;
import com.wisedu.minos.casp.portal.common.exception.NoLoginException;
import com.wisedu.minos.casp.portal.common.redis.RedisUtil;
import com.wisedu.minos.casp.portal.model.MenuBiz;
import com.wisedu.minos.casp.portal.model.PageContext;
import com.wisedu.minos.casp.portal.model.UserInfo;
import com.wisedu.minos.casp.portal.service.impl.PageInfoService;
import com.wisedu.minos.casp.portal.utils.CasUtil;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.config.ApplicationContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.resource.ResourceUrlProvider;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * IDSLoginFilter
 * <p/>
 * ids登陆过滤器
 *
 * @author hyluan
 * @date 2020/10/15 16:38
 * Copyright (c) 2018 wisedu
 */
public class IDSLoginFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(IDSLoginFilter.class);

    private static UrlPathHelper pathHelper = new UrlPathHelper();

    private CasProperties casProperties;
    private String[] staticUri;
    private Long redisExpireTime;

    public static final List<String> IGNORE_URL_LIST = Lists.newArrayList(
            "/newmobile/client/guestAppList.json"
    );

    public IDSLoginFilter (CasProperties casProperties,Long redisExpireTime) {
        this.casProperties = casProperties;
        this.redisExpireTime=redisExpireTime;
        this.staticUri = casProperties.getCas().getStaticSuffix().split(",");
    }

    @Override
    public void doFilter (ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = ( HttpServletRequest ) request;
        HttpServletResponse httpResponse = ( HttpServletResponse ) response;
        String requestUri = httpRequest.getRequestURI();
        //静态资源跳过，在忽略资源内跳过
        if ( isStaticResource(requestUri) || IGNORE_URL_LIST.contains(requestUri)
                || StringUtils.endsWithAny(requestUri, Global.LOGIN_URI, Global.LOGOUT_URI) ) {
            chain.doFilter(request, response);
            return;
        }
        logger.debug("ids登录拦截器requestURI:" + requestUri);
        //需要登录访问的页面
        UserInfo userInfo = getUser(httpRequest);
        httpRequest.getSession().setAttribute("user", userInfo);
        httpRequest.setAttribute("user", userInfo);
        UserInfo currentUser = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();

        if ( null != userInfo && null == currentUser ) {
            String url = httpRequest.getRequestURL().toString();
            String errorPage = String.format("/error/403?errorMsg=%s&errorUrl=%s", URLEncoder.encode("该帐号存在异常，请检查管控台帐号配置", "UTF-8"), url);
            httpRequest.getSession().invalidate();
            httpResponse.setHeader("REDIRECT","REDIRECT");//告诉ajax要重定向
            httpResponse.setHeader("PATH",casProperties.getCas().getServerUrl() + "logout?service=" + casProperties.getModule().getDomain() + httpRequest.getContextPath() + errorPage);
            httpRequest.getSession().setAttribute("filterError", casProperties.getCas().getServerUrl() + "logout?service=" + casProperties.getModule().getDomain() + httpRequest.getContextPath() + errorPage);
            //这里加 chain.doFilter(request, response);放行 是为了兼容IE(如果不加此代码，在调用getPageView方法的时候，前端直接报错，拿不到httpResponse中的Header)
            chain.doFilter(request, response);
            return;
        }
        PageContext pageContext = null;
        String pageCode = httpRequest.getParameter("pageCode");
        Boolean isNoLoginException = ( Boolean ) httpRequest.getSession().getAttribute("isNoLoginException");
        if (null != isNoLoginException && isNoLoginException) {
            httpRequest.getSession().removeAttribute("isNoLoginException");
        }
        RedisUtil redisUtil = ApplicationContextUtil.get(RedisUtil.class);
        String pageContextCacheKey="";
        if(null==currentUser){
            pageContextCacheKey="pageContext:visitorKey:"+pageCode;
        }else{
            pageContextCacheKey="pageContext:"+currentUser.getUserAccount()+":"+pageCode;
        }
        Object pageContextResult = redisUtil.get(pageContextCacheKey);
        if(!redisUtil.isHasKey(pageContextCacheKey)||(redisUtil.isHasKey(pageContextCacheKey)&&null==pageContextResult)){
            try {
                pageContext = ApplicationContextUtil.get(PageInfoService.class).getPageContext(httpRequest,pageCode);
                logger.info("-----------pageContextCacheKey------"+pageContextCacheKey);
                redisUtil.set(pageContextCacheKey, pageContext, redisExpireTime, TimeUnit.SECONDS);
            } catch ( Exception e ) {
                String url = httpRequest.getRequestURL().toString();
                if ( e instanceof NoLoginException ) {
                    //未登录或者菜单不在该权限下，跳转
                    String loginPage = getLoginPage(currentUser, isNoLoginException, httpRequest, pageCode);
                    logger.error("未登陆,跳转：{}", loginPage, e);
                    httpRequest.getSession().setAttribute("filterError",loginPage);
                    httpResponse.setHeader("REDIRECT","REDIRECT");//告诉ajax要重定向
                    httpResponse.setHeader("PATH",loginPage);
                    //这里加 chain.doFilter(request, response);放行 是为了兼容IE(如果不加此代码，在调用getPageView方法的时候，前端直接报错，拿不到httpResponse中的Header)
                    chain.doFilter(request, response);
                    return;
                } else {
                    String errorPage = String.format("/error/404?errorMsg=%s&errorUrl=%s", URLEncoder.encode("获取页面属性失败", "UTF-8"), url);
                    logger.error("获取页面属性失败,跳转：{}", errorPage, e);
                    httpRequest.getSession().setAttribute("filterError",errorPage);
                    httpResponse.setHeader("REDIRECT","REDIRECT");//告诉ajax要重定向
                    httpResponse.setHeader("PATH",errorPage);
                    //这里加 chain.doFilter(request, response);放行 是为了兼容IE(如果不加此代码，在调用getPageView方法的时候，前端直接报错，拿不到httpResponse中的Header)
                    chain.doFilter(request, response);
                    return;
                }
            }
        }else{
            pageContext= (PageContext) pageContextResult;
        }
        httpRequest.setAttribute("pageContext",pageContext);

        MenuBiz currentMenu = pageContext.getCurrentMenu();
        if ( currentMenu == null ) {
            //无菜单的单独页面 允许访问
            chain.doFilter(request, response);
            return;
        }
        Global.AuthType authType = Global.AuthType.getById(currentMenu.getAuthType());

        boolean noPermission = permission(userInfo, authType);
        if ( noPermission ) {
            dealIsAjax(httpRequest, request, response, httpResponse, chain);
            return;
        }
        chain.doFilter(request, response);
    }

    private boolean permission(UserInfo userInfo, Global.AuthType authType){
        return null == userInfo && (Global.AuthType.AUTH_VISIBLE.equals(authType) || Global.AuthType.LOGIN_VISIBLE.equals(authType));
    }

    private String getLoginPage(UserInfo currentUser, Boolean isNoLoginException, HttpServletRequest httpRequest, String pageCode) throws UnsupportedEncodingException {
        String loginPage ="";
        if ((null == currentUser && null != isNoLoginException && !isNoLoginException) || (null == currentUser && null == isNoLoginException)) {
            httpRequest.getSession().setAttribute("localLoginHref", pageCode);
            loginPage = casProperties.getCas().getServerUrl() + "login?service=" + URLEncoder.encode(casProperties.getModule().getDomain() + Global.LOGIN_URI, "UTF-8");
        }
        return loginPage;
    }

    private void dealIsAjax(HttpServletRequest httpRequest, ServletRequest request, ServletResponse response, HttpServletResponse httpResponse, FilterChain chain) throws IOException, ServletException {
        boolean isAjax = isAjax(httpRequest);
        if ( isAjax ) {
            response.setContentType("text/plain; charset=utf-8");
            JSONObject object = new JSONObject();
            object.put("code", GlobalEnum.LOGIN_TIME_OUT.getCode());
            response.getWriter().write(object.toJSONString());
        } else {
            StringBuilder reqUrl = new StringBuilder(httpRequest.getRequestURL().toString());
            String queryStr = pathHelper.getOriginatingQueryString(httpRequest);
            if ( StringUtils.isNotBlank(queryStr) ) {
                reqUrl.append("?").append(queryStr);
            }
            String redirectUrl = casProperties.getCas().getServerUrl() + "login?service=" + URLEncoder.encode(reqUrl.toString(), "UTF-8");
            logger.info(redirectUrl);
            httpResponse.setHeader("REDIRECT","REDIRECT");//告诉ajax要重定向
            httpResponse.setHeader("PATH",redirectUrl);
            httpRequest.getSession().setAttribute("filterError", redirectUrl);
            //这里加 chain.doFilter(request, response);放行 是为了兼容IE(如果不加此代码，在调用getPageView方法的时候，前端直接报错，拿不到httpResponse中的Header)
            chain.doFilter(request, response);
        }
    }


    private boolean isStaticResource (String requestUri) {
        ResourceUrlProvider resourceUrlProvider = ApplicationContextUtil.get(ResourceUrlProvider.class);
        String staticUri = resourceUrlProvider.getForLookupPath(requestUri);
        if ( staticUri != null ) {
            return true;
        }
        return Arrays.stream(this.staticUri).anyMatch(s -> requestUri.toLowerCase().endsWith(s.toLowerCase()));
    }

    private UserInfo getUser (HttpServletRequest request) {
        // 反向代理方式集成
        if ( StringUtils.equals("proxy", casProperties.getCas().getIntegrated()) ) {
            String userId = request.getHeader(casProperties.getCas().getNginxHeaderUser());
            if ( userId == null ) {
                return null;
            }
            UserInfo userInfo = new UserInfo();
            userInfo.setWid(userId);
            logger.info("访问者wid ：" + userInfo.getWid());
            return userInfo;
        } else {
            // cas方式集成
            UserInfo userInfo = CasUtil.getUserInfoFromCas(request);
            if ( userInfo != null ) {
                logger.info("访问者userName ：" + userInfo.getUserName());
            }
            return userInfo;
        }
    }

    /**
     * 判断请求是否是ajax请求
     *
     * @param request
     * @return
     * @author zhangjing
     * @since 1.0.0
     */
    public static boolean isAjax (HttpServletRequest request) {
        String xRequestedWith = request.getHeader(LoginConstant.X_REQUESTED_WITH);
        return (StringUtils.isNotBlank(xRequestedWith)
                && LoginConstant.XML_HTTP_REQUEST.equals(xRequestedWith));
    }


}
