package com.wisedu.minos.filter;


import com.wisedu.minos.casp.portal.common.utils.CommonUtil;
import com.wisedu.minos.gateway.client.license.MinosLicenseManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class LicenseInfoFilter implements Filter {

    @Autowired
    private CommonUtil commonUtil;

    @Value("${minos.manager.gateway.addresses}")
    private String gatewayAddress ;
    /**
     * 初始化
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.debug("---------------LicenseInfoFilter Init---------------");
    }

    /**
     * 拦截逻辑
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse)servletResponse;
        String requestURI = httpRequest.getRequestURI().replace("//","/");
        if(requestURI.toLowerCase().endsWith("html")){
            // 仅针对页面资源域名校验
            String host = httpRequest.getHeader("Host");
            if(!MinosLicenseManager.isLicenseDomainValid(host,"/casp-portal-webapps")) {
                // 放过健康检查
//                if(StringUtil.endsWith(httpRequest.getRequestURL().toString(),"/checkServiceHealth")){
//                    filterChain.doFilter(httpRequest, httpResponse);
//                    return ;
//                }
                String validErrorPage = "/validLicenseError.html";
                LOGGER.warn("License校验失败，url is {}，host is {}", httpRequest.getRequestURL(),host);
                httpRequest.setAttribute("message", "License校验失败，请联系管理员！");
                httpRequest.getRequestDispatcher(validErrorPage).forward(httpRequest, servletResponse);
                return;
            }
        }

        filterChain.doFilter(httpRequest, httpResponse);
        return;
    }


    /**
     * 销毁
     */
    @Override
    public void destroy() {
        LOGGER.debug("---------------LicenseInfoFilter Destory---------------");
    }


}