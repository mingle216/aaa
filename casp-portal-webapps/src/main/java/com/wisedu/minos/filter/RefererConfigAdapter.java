package com.wisedu.minos.filter;

import com.wisedu.minos.filter.wrapper.RefererInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Referer拦截器，防止CSRF攻击
 * @author zhangjian 0116211
 * @create 2019-11-18
 **/

@Configuration
@ConditionalOnProperty(name = {"system.referer.legalDomain","system.referer.excludePaths"},matchIfMissing = false)
public class RefererConfigAdapter implements WebMvcConfigurer {
    @Value("${system.referer.legalDomain}")
    private String[] legalDomain;

    @Value("${system.referer.excludePaths}")
    private String[] excludePaths;

    /**
     * 添加过滤器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RefererInterceptor(legalDomain)).addPathPatterns("/**").excludePathPatterns(excludePaths);
    }

}
