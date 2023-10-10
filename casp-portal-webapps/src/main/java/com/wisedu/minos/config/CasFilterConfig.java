package com.wisedu.minos.config;

import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.CasProperties;
import com.wisedu.minos.casp.portal.utils.CasUtil;
import com.wisedu.minos.condition.IDSCondition;
import com.wisedu.minos.filter.IDSLoginFilter;
import com.wisedu.minos.queue.service.PubSubService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas30ProxyReceivingTicketValidationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;

import java.util.List;

/**
 * 功能描述：cas集成核心配置类 修改记录:
 *
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title CasFilterConfig
 * @Author: de
 * @Date: 2020/7/22
 */
@Configuration
@Slf4j
public class CasFilterConfig {

    @Autowired
    CasProperties casProperties;
    @Autowired
    private RedisIndexedSessionRepository redisIndexedSessionRepository;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private PubSubService pubSubService;

    public static final List<String> NEWMOBILE_CLIENT = Lists.newArrayList(
      "/newmobile/client/*",
                "/newMobile/item/*"
    );

    public static final String IGNORE_PATTERN = "/newmobile/client/guestAppList.json";

    @Value("${system.card.redis.expireTime:5}")
    private Long redisExpireTime;

    @Conditional(IDSCondition.class)
    @Bean
    public ServletListenerRegistrationBean servletListenerRegistrationBean() {
        ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> listenerRegistrationBean = new ServletListenerRegistrationBean<>();
        listenerRegistrationBean.setEnabled(true);
        listenerRegistrationBean.setListener(new SingleSignOutHttpSessionListener());
        listenerRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return listenerRegistrationBean;
    }

    /**
     * 单点登录退出
     *
     * @return
     */
    @Conditional(IDSCondition.class)
    @Bean
    public FilterRegistrationBean singleSignOutFilter() {
        FilterRegistrationBean<SingleSignOutFilter> registrationBean = new FilterRegistrationBean<>();
        SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
        singleSignOutFilter.setSessionMappingStorage(
                new MySessionMappingStorage(redisIndexedSessionRepository, stringRedisTemplate));
        registrationBean.setEnabled(true);
        registrationBean.setFilter(new SingleSignOutFilter());
        registrationBean.addUrlPatterns(Global.LOGIN_URI);
        registrationBean.addInitParameter("casServerUrlPrefix", casProperties.getCas().getServerUrl());
        registrationBean.setName("CAS Single Sign Out Filter");
        registrationBean.setOrder(2);
        return registrationBean;
    }

    /**
     * 单点登录认证
     *
     * @return
     */
    @Conditional(IDSCondition.class)
    @Bean
    public FilterRegistrationBean authenticationFilter() {
        FilterRegistrationBean<AuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setEnabled(true);
        registrationBean.setFilter(new AuthenticationFilter());
        registrationBean.addUrlPatterns(Global.LOGIN_URI);
        for (String url : NEWMOBILE_CLIENT) {
            registrationBean.addUrlPatterns(url);
        }
        registrationBean.setName("CAS Filter");
        registrationBean.addInitParameter("casServerLoginUrl", casProperties.getCas().getServerUrl() + "login");
        registrationBean.addInitParameter("serverName", casProperties.getModule().getDomain());
        registrationBean.addInitParameter("ignorePattern", IGNORE_PATTERN);
        registrationBean.setOrder(3);
        return registrationBean;
    }

    /**
     * 单点登录校验
     *
     * @return
     */
    @Conditional(IDSCondition.class)
    @Bean
    public FilterRegistrationBean cas30ProxyReceivingTicketValidationFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setEnabled(true);
        registrationBean.setFilter(new Cas30ProxyReceivingTicketValidationFilter());
        registrationBean.addUrlPatterns(Global.LOGIN_URI);
        for (String url : NEWMOBILE_CLIENT) {
            registrationBean.addUrlPatterns(url);
        }
        registrationBean.setName("CAS Validation Filter");
        registrationBean.addInitParameter("casServerUrlPrefix", casProperties.getCas().getServerUrl());
        registrationBean.addInitParameter("serverName", casProperties.getModule().getDomain());
        registrationBean.setOrder(4);
        return registrationBean;
    }

    /**
     * 单点登录请求包装
     *
     * @return
     */
    @Conditional(IDSCondition.class)
    @Bean
    public FilterRegistrationBean httpServletRequestWrapperFilter() {
        FilterRegistrationBean<HttpServletRequestWrapperFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setEnabled(true);
        registrationBean.setFilter(new HttpServletRequestWrapperFilter());
        registrationBean.addUrlPatterns(Global.LOGIN_URI);
        for (String url : NEWMOBILE_CLIENT) {
            registrationBean.addUrlPatterns(url);
        }
        registrationBean.setName("CAS HttpServletRequest Wrapper Filter");
        registrationBean.setOrder(5);
        return registrationBean;
    }

    /**
     * idsLoginFilter
     * <p/>
     *
     * @param
     * @return org.springframework.boot.web.servlet.FilterRegistrationBean
     * @throws
     * @date 2020/10/10 12:52
     */
    @Bean
    public FilterRegistrationBean idsLoginFilter() {
        FilterRegistrationBean<IDSLoginFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setEnabled(true);
        registrationBean.setFilter(new IDSLoginFilter(casProperties,redisExpireTime,pubSubService));
        registrationBean.addUrlPatterns(Global.LOGIN_URI, Global.LOGOUT_URI,Global.GET_PAGEVIEW_URI,Global.GET_PAGEVIEW_URI+"/*");
        for (String url : NEWMOBILE_CLIENT) {
            registrationBean.addUrlPatterns(url);
        }
        registrationBean.setName("idsLoginFilter");
        registrationBean.setOrder(6);
        CasUtil.setCasSessionKey(casProperties.getCas().getCasSessionKey());
        return registrationBean;
    }

}
