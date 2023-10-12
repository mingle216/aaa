package com.wisedu.minos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
public class RedisSessionConfig {

    /**
     * SessionId
     */
    public static final String CASP_SESSION_ID = "WISCPSID";

    @Bean
    public DefaultCookieSerializer getDefaultCookieSerializer() {
        DefaultCookieSerializer defaultCookieSerializer = new DefaultCookieSerializer();
        defaultCookieSerializer.setCookieName(CASP_SESSION_ID);
        defaultCookieSerializer.setUseBase64Encoding(false);
        return defaultCookieSerializer;
    }
}
