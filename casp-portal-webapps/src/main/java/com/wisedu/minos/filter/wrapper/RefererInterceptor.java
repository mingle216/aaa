package com.wisedu.minos.filter.wrapper;

import com.wisedu.minos.util.MinosCommonUtil;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;

/**
 * Referer拦截器，防止CSRF攻击
 * @author zhangjian 0116211
 * @create 2019-11-18
 **/

public class RefererInterceptor implements HandlerInterceptor {

    private String[] legalDomain;

    public RefererInterceptor(String[] legalDomain){
        this.legalDomain = legalDomain;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 没有配置，则放行
        if (MinosCommonUtil.isEmpty(legalDomain)){
            return true;
        }
        String referer = request.getHeader("referer");
        String host = request.getServerName();

        if (Strings.isEmpty(referer)){
            return true;
        }

        java.net.URL url = null;
        try {
            url = new java.net.URL(referer);
        } catch (MalformedURLException e) {
            // URL解析异常，也置为404
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return false;
        }
        // 首先判断请求域名和referer域名是否相同
        if (!host.equals(url.getHost())) {
            // 如果不等，判断是否在白名单中
            for (String s : legalDomain) {
                if (s.equals(url.getHost())) {
                    return checkRefererToken(request);
                }
            }
            return false;
        }
        return true;
    }

    // 是否需要校验请求头中的动态Token值
    private boolean checkRefererToken(HttpServletRequest request){
        return true;
    }
}
