package com.wisedu.minos.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * filter 统一管理器
 * @date 2021/11/24 10:53
 * @author jszhang@wisedu
 * @version 1.0
 **/
@Configuration
public class FilterConfiguration {

    @Autowired
    private MultipleSitesFilter multipleSitesFilter;
    @Autowired
    private RedirectRouteToHtmlFilter redirectRouteToHtmlFilter;

    @Autowired
    private LicenseInfoFilter licenseInfoFilter;
    @Bean
    public FilterRegistrationBean<LicenseInfoFilter> registerLicenseInfoFilter(){
        FilterRegistrationBean<LicenseInfoFilter>registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(licenseInfoFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("licenseInfoFilter");
        registrationBean.setOrder(98);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<RedirectRouteToHtmlFilter> registerRedirectRouteToHtmlFilter() {
        FilterRegistrationBean<RedirectRouteToHtmlFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(redirectRouteToHtmlFilter);
        registration.addUrlPatterns("/*");
        registration.setName("redirectRouteToHtmlFilter");
        //值设置大一点在最后执行
        registration.setOrder(99);
        return registration;
    }
    @Bean
    public FilterRegistrationBean<MultipleSitesFilter> registerMultipleSitesFilter() {
        FilterRegistrationBean<MultipleSitesFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(multipleSitesFilter);
        registration.addUrlPatterns("/*");
        registration.setName("multipleSitesFilter");
        //值设置大一点在最后执行
        registration.setOrder(100);
        return registration;
    }
}
