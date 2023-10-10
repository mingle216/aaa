package com.wisedu.minos.filter;

import com.wisedu.minos.filter.wrapper.XssHttpServletRequestWrapper;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 防止XSS攻击的Filter配置
 * @author zhangjian 0116211
 * @create 2019-11-18
 **/
@WebFilter(filterName = "xssFilter", urlPatterns = "/*", asyncSupported = true)
public class XSSFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        XssHttpServletRequestWrapper xssRequest =
                new XssHttpServletRequestWrapper((HttpServletRequest) request);
        chain.doFilter(xssRequest, response);
    }


}
