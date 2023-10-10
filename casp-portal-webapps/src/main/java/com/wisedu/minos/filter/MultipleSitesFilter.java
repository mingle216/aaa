package com.wisedu.minos.filter;

import com.wisedu.minos.casp.portal.CasProperties;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.redis.RedisUtil;
import com.wisedu.minos.casp.portal.common.utils.HttpUtil;
import com.wisedu.minos.casp.portal.common.utils.UrlUtil;
import com.wisedu.minos.casp.portal.dao.mapper.SiteSwitchingMapper;
import com.wisedu.minos.casp.portal.model.UserInfo;
import com.wisedu.minos.casp.portal.model.v311beta2.MultiSitesDto;
import com.wisedu.minos.casp.portal.model.v311beta2.SiteDto;
import com.wisedu.minos.casp.portal.service.impl.SystemConfigService;
import com.wisedu.minos.casp.portal.service.v311beta2.MultiSitesAdapter;
import com.wisedu.minos.casp.portal.utils.CasUtil;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Set;

/**
 *
 * 多站点拦截器
 * @date 2021/11/24 10:33
 * @author jszhang@wisedu
 * @version 1.0
 **/
@Component
@Slf4j
public class MultipleSitesFilter implements Filter {
    public static final String USER_MULTI_SITE_CHOICE_KEY = "casp:multisite:choice:";

    @Autowired
    MultiSitesAdapter multiSitesAdapter;
    @Autowired
    UserUtil userUtil;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    SiteSwitchingMapper siteSwitchingMapper;
    @Autowired
    CasProperties casProperties;


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String requestURI = request.getRequestURI().replace("//", "/");

        //非get请求直接放行
        if (!HttpUtil.isGetMethod(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        //前段ajax请求直接放行
        if (HttpUtil.isAjaxRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        //静态资源放行
        if (UrlUtil.isStaticSource(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }
        //如果是简单站点request例如：域名+/casp重定向到域名+/casp/index.html
        UserInfo userInfoFromCas = CasUtil.getUserInfoFromCas(request);
        if(userInfoFromCas!=null){
            if(null!=request.getSession().getAttribute("user")){
                userInfoFromCas= ( UserInfo ) request.getSession().getAttribute("user");
            }else{
                userInfoFromCas=userUtil.getUserByAccount(userInfoFromCas.getUserAccount());
                request.getSession().setAttribute("user", userInfoFromCas);
                request.getSession().setAttribute("minosUserInfo", userInfoFromCas);
            }
        }
        SiteDto siteDto = multiSitesAdapter.getSiteByUserId(userInfoFromCas);
        boolean isSiteRequest = isSiteRequest(request, siteDto.getAllEnableSiteRoutes());
        //不是站点请求跳过
        if (!isSiteRequest) {
            filterChain.doFilter(request, response);
            return;
        }
        //这个请求是多站点授权请求页面
        if (StringUtils.isNotBlank(request.getParameter("isSelect"))) {
            filterChain.doFilter(request, response);
            return;
        }
        boolean isRandomSiteRule = isRandomSiteRule();
        if (isRandomSiteRule) {
            HttpUtil.setKeyValueInCookie(Global.IS_RANDOM, "1", response);
            randomSiteRule(response, request, requestURI, siteDto);
        } else {
            HttpUtil.setKeyValueInCookie(Global.IS_RANDOM, "0", response);
            commonSiteRule(response, request, requestURI, siteDto);
        }
    }

    private void commonSiteRule(HttpServletResponse response, HttpServletRequest request, String requestURI, SiteDto siteDto) throws IOException, ServletException {
        //判断是简单站点还是带index.html的请求
        String wid = getSiteRouteWid(requestURI, siteDto);
        if (StringUtils.isBlank(wid)) {
//            response.sendRedirect("/index.html?isSelect=1#/authSites");
            response.setStatus(302);
            response.setHeader("location", request.getContextPath() + "/index.html?isSelect=1#/authSites");
        } else {
            request.getSession().setAttribute(Global.SITE_WID, wid);
            HttpUtil.setKeyValueInCookie(Global.SITE_WID, wid, response);
            request.getRequestDispatcher("/index.html").forward(request, response);
        }
    }

    private void randomSiteRule(HttpServletResponse response, HttpServletRequest request, String requestURI, SiteDto siteDto) throws IOException, ServletException {
        //判断是简单站点还是带index.html的请求
        String wid = getSiteRouteWid(requestURI, siteDto);
        //没有任何有权限站点 游客跳转登陆 ，用户条跳转无权限
        if (StringUtils.isBlank(wid) && CollectionUtils.isEmpty(siteDto.getUserMultiSite())) {
            UserInfo userInfoFromCas = CasUtil.getUserInfoFromCas(request);
            if (userInfoFromCas == null) {
                redirectLogin(response);
            } else {
                response.setStatus(302);
                response.setHeader("location", request.getContextPath() + "/index.html?isSelect=1#/authSites");
            }
        } else if (StringUtils.isBlank(wid)) {
            //如果为空，如果有主站点权限跳转主站点，如果没有权限随机跳转一个，然后前段再做重定向
            if (siteDto.getIsMasterPermission()) {
                response.setStatus(302);
                response.setHeader("location", request.getContextPath() + "/index.html");
            } else {
                Set<MultiSitesDto> userMultiSite = siteDto.getUserMultiSite();
                MultiSitesDto next = userMultiSite.iterator().next();
                response.setStatus(302);
                response.setHeader("location", request.getContextPath() + "/" + next.getSiteRoute() + "/index.html");
            }
        } else {
            request.getSession().setAttribute(Global.SITE_WID, wid);
            HttpUtil.setKeyValueInCookie(Global.SITE_WID, wid, response);
            request.getRequestDispatcher("/index.html").forward(request, response);
        }
    }

    @SneakyThrows
    public void redirectLogin(HttpServletResponse httpResponse) {
        String loginPage = casProperties.getCas().getServerUrl() + "login?service=" + URLEncoder.encode(casProperties.getModule().getDomain() + Global.LOGIN_URI, "UTF-8");
        httpResponse.setStatus(302);
        httpResponse.setHeader("location", loginPage);

        return;
    }

    public boolean isRandomSiteRule() {
        String value = systemConfigService.getSystemConfigValue("SITE_RULE");
        return "1".equals(value);
    }

    /***
     * 判断这个请求是不是站点请求
     * @param request
     * @param allSiteRoutes 所有的站点
     * @return boolean
     * @author jszhang
     * @date 2021/11/30 17:18
     */
    private boolean isSiteRequest(HttpServletRequest request, Set<String> allSiteRoutes) {
        String uri = request.getRequestURI().replace("//", "/");
        if ("/index.html".equals(uri)) {
            return true;
        }
        return allSiteRoutes.contains(uri.replace("/index.html", "").replace("/", ""));
    }

    private String getSiteRouteWid(String uri, SiteDto siteDto) {

        //主站点
        if (siteDto.getIsMasterPermission() && "/index.html".equals(uri)) {
            MultiSitesDto multiSitesDto = siteDto.getUserMultiSite().stream().filter(e -> e.getIsMaster() == 1).findAny().orElseGet(MultiSitesDto::new);
            return multiSitesDto.getWid();
        }
        //不是主站点
        String siteRoute = uri.replace("/index.html", "").replace("/", "");
        if (siteDto.getUserEnableSiteRoutes().contains(siteRoute)) {
            MultiSitesDto multiSitesDto = siteDto.getUserMultiSite().stream().filter(e -> siteRoute.equals(e.getSiteRoute())).findAny().orElseGet(MultiSitesDto::new);
            return multiSitesDto.getWid();
        } else {
            return null;
        }


    }

    /***
     * 获取用户的路由
     * @param userRoutes
     * @return java.lang.String
     * @author jszhang
     * @date 2021/11/30 19:30
     */
    private String getUserChoiceRouter(Set<String> userRoutes) {
        UserInfo userInfo = userUtil.getCurrentUser();
        if (userInfo != null) {
            String router = (String) redisUtil.get(USER_MULTI_SITE_CHOICE_KEY + userInfo.getUserAccount());
            if (userRoutes.contains(router)) {
                return router;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
