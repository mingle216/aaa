package com.wisedu.minos.filter;

import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.jasig.cas.client.util.IOUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;

/**
 * XXEFilter
 * <p/>
 * 跨站点脚本编制拦截器
 *
 * @author hyluan
 * @date 2020/11/18 17:58
 * Copyright (c) 2018 wisedu
 */
@Slf4j
@WebFilter(filterName = "xxeFilter", urlPatterns = "/*", asyncSupported = true)
public class XXEFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        if (isXXE(httpServletRequest)) {
            String errorPage = String.format("/error/405?errorMsg=%s&errorUrl=%s", URLEncoder.encode("xee过滤", "UTF-8"), httpServletRequest.getRequestURI());
            httpServletResponse.sendRedirect(errorPage);
            return;
        }
        chain.doFilter(request, response);
    }

    /**
     * isXXE
     * <p/>
     * 检查当前请求是否存在XXE攻击，如果存在，则不允许访问
     *
     * @param request
     * @return boolean
     * @throws
     * @date 2020/11/18 18:16
     */
    private boolean isXXE(HttpServletRequest request) {
        // 由于在安全攻击时学校没有使用Content-Type头部传，因此需要修改为检查所有传入的内容
        // 为了安全，会牺牲一部分性能
        // modify by zhangjian 2020-05-29
        String contentType = request.getHeader("Content-Type");
        if (StringUtil.isNullOrEmpty(contentType) || contentType.toLowerCase().contains("xml")) {
            // 存在传入的不是完全正确的XML格式，但又带有DocType的场景，也需要将这个场景去掉
            if (hasDocType(request)) {
                LOGGER.error("存在XXE攻击的XML");
                return true;
            }
        }
        return false;
    }

    /**
     * 存在传入的XML不是完全正确的XML，但又带有DOCTYPE,也需要去掉
     *
     * @param request
     * @return
     */
    private boolean hasDocType(HttpServletRequest request) {
        try {
            String sb = IOUtils.readString(request.getInputStream());
            if (!StringUtil.isEmpty(sb)) {
                return sb.toUpperCase().contains("!DOCTYPE");
            }
        } catch (Exception e) {
            LOGGER.error("", e);
        }
        return false;
    }

}
