package com.wisedu.minos.filter;

import com.wisedu.minos.casp.portal.common.utils.HttpUtil;
import com.wisedu.minos.casp.portal.common.utils.UrlUtil;
import com.wisedu.minos.casp.portal.service.v311beta2.MultiSitesAdapter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * 将路由重定向到html页面
 * @date 2021/12/1 10:44
 * @author jszhang@wisedu
 * @version 1.0
 **/
@Component
@Slf4j
public class RedirectRouteToHtmlFilter implements Filter {

    @Autowired
    MultiSitesAdapter multiSitesAdapter;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String requestURI = request.getRequestURI().replace("//","/");
        if(StringUtils.endsWithAny(requestURI,".html")) {
            response.addHeader("Cache-Control", "private, no-store, no-cache, must-revalidate, proxy-revalidate");
        }
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

        if (UrlUtil.isStaticSource(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }
        if ("/".equals(requestURI)) {

//            response.sendRedirect("/index.html");
            response.setStatus(302);
            response.setHeader("location", request.getContextPath() + "/index.html");
            return;
        }
        if ((!requestURI.contains("/index.html")) && isAllSiteRoute(requestURI)) {
//            response.sendRedirect("/" + requestURI.replace("/", "") + "/index.html");
            response.setStatus(302);
            response.setHeader("location", request.getContextPath() + "/" + requestURI.replace("/", "") + "/index.html");
            return;
        }
        filterChain.doFilter(request, response);
    }



    public boolean isAllSiteRoute(String uri) {
        return multiSitesAdapter.getAllSitesRoutes().contains(uri.replace("/", ""));
    }

}
